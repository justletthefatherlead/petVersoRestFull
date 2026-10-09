# 🐾 petVerso API

API REST do **petVerso**, um aplicativo mobile para **compartilhar o cuidado de pets entre várias pessoas**. O foco não é compartilhar dados do usuário, e sim os dados do pet: tutores vinculados ao mesmo pet enxergam o perfil dele e uma agenda de tarefas em comum, e podem acompanhar o que já foi feito.

Projeto desenvolvido como TCC. O aplicativo Android é desenvolvido separadamente e consome esta API.

> 🚧 **Em desenvolvimento.** A API é construída de forma incremental: Usuário → Pet → Vínculo → Agenda → Vacinas.

---

## ✨ Funcionalidades

**Usuários**
- Cadastro com e-mail, telefone e senha (e-mail e telefone únicos)
- Login por e-mail e senha
- Perfil com apelido e foto de perfil, ambos opcionais

**Pets**
- Cadastro de pet com dados básicos (nome, raça, espécie, porte, peso, sexo, data de nascimento) e foto opcional
- Geração automática de um **código de vínculo** (formato `XXX-XXX`) para compartilhar o pet
- Edição do perfil de sensibilidade (alergias, restrições, sensibilidade emocional) e das personalidades
- Atualização da foto do pet (limite de 5 MB)
- Listagem dos pets do usuário

**Vínculo entre tutores**
- O dono do pet compartilha o código de vínculo
- Outro usuário solicita ser **tutor de suporte** usando o código
- O dono lista, aceita ou recusa as solicitações
- Solicitações recusadas podem ser feitas novamente

**Agenda (tarefas)**
- Criação de tarefas para um pet (somente o dono)
- Listagem das tarefas de um pet
- Listagem das tarefas de um dia, somando todos os pets aos quais o usuário tem vínculo aceito

---

## 🧱 Stack

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Módulos | Spring Web MVC, Spring Data JPA, Spring Security, Bean Validation |
| Banco de dados | MySQL (driver `mysql-connector-j`) |
| Build | Maven (Maven Wrapper incluso) |
| Container | Docker (build multi-stage) |
| Hospedagem | API no Render, MySQL na Aiven |

Pacote base: `com.namassacompany.petVersoRestFull`

---

## 🏗️ Arquitetura

Organização em camadas:

```
src/main/java/com/namassacompany/petVersoRestFull/
├── controller/   # endpoints REST
├── services/     # regras de negócio
├── repository/   # acesso a dados (Spring Data JPA)
├── model/        # entidades JPA e enums
├── dto/          # records de entrada e saída
├── exception/    # exceções de domínio e GlobalExceptionHandler
└── security/     # SecurityConfig e TokenAuthFilter
```

Principais decisões:

- **DTOs em vez de entidades** na entrada e na saída, evitando mass assignment e vazamento de dados.
- **Fotos como Base64 no JSON** (sem multipart) e armazenadas como `MEDIUMBLOB`. O DTO leve (listagens) não leva foto; o DTO de perfil leva.
- **Enums** (`Papel`, `StatusDeVinculo`, `Porte`, `Sexo`) gravados como `String` no banco.
- **Controle de acesso mascarado**: quem não tem vínculo aceito com um pet recebe o mesmo `404` de um pet inexistente, sem revelar que ele existe.

### Modelo de dados (resumo)

```
Usuario ──< VinculoPet >── Pet ──< Tarefa
                │                    │
        papel: DONO | SUPORTE        └── executadoPor → Usuario
        status: PENDENTE | ACEITO | RECUSADO
```

- **Usuario**: nome, e-mail, telefone, senha, apelido, foto
- **Pet**: nome, raça, espécie, porte, peso, sexo, data de nascimento, perfil de sensibilidade, foto, código de vínculo, personalidades
- **VinculoPet**: relação muitos-para-muitos entre usuário e pet, com papel, status e data de criação
- **Sessao**: token de sessão de cada login
- **Tarefa**: título, descrição, data, hora, concluída, quem executou, data de conclusão

---

## 🔐 Autenticação e autorização

Autenticação por **token opaco de sessão salvo no banco** (não é JWT):

1. O cadastro e o login retornam um token.
2. Nas demais requisições, envie `Authorization: Bearer <token>`.
3. O `TokenAuthFilter` busca a sessão pelo token e coloca o usuário no `SecurityContext`.
4. O token **não expira sozinho**: o app é de uso exclusivo no celular e a sessão só termina com logout manual.
5. Um mesmo usuário pode ter várias sessões válidas ao mesmo tempo.

Rotas públicas: `POST /api/usuarios` (cadastro) e `POST /api/login`. Todo o resto exige token; sem ele a resposta é `403`.

**Regras por papel**

| Ação | DONO | SUPORTE (aceito) | Sem vínculo / pendente |
| --- | :---: | :---: | :---: |
| Ver perfil do pet | ✅ | ✅ | ❌ (404) |
| Ver agenda do pet | ✅ | ✅ | ❌ (404) |
| Criar tarefa | ✅ | ❌ (403) | ❌ (404) |
| Editar sensibilidade e personalidades | ✅ | ❌ (403) | ❌ (404) |
| Atualizar foto do pet | ✅ | ❌ | ❌ |
| Ver e processar solicitações de vínculo | ✅ | ❌ (403) | ❌ (404) |

---

## 📡 Endpoints

Todos os endpoints (exceto os públicos) exigem `Authorization: Bearer <token>`.

### Usuários e autenticação

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/usuarios` | Cadastra o usuário e já retorna o token (público) |
| `POST` | `/api/login` | Autentica por e-mail e senha e retorna o token (público) |
| `GET` | `/api/usuarios/perfil` | Perfil do usuário autenticado (com foto) |
| `PUT` | `/api/usuarios/atualizarPerfil` | Atualiza apelido e foto (Base64) |

### Pets

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/pets/cadastrar` | Cadastra um pet; o criador vira DONO e a resposta traz o código de vínculo |
| `GET` | `/api/pets/{id}/perfil` | Perfil completo do pet |
| `PUT` | `/api/pets/{id}/atualizarPetPerfil` | Atualiza perfil de sensibilidade e personalidades (somente DONO) |
| `PUT` | `/api/pets/{id}/foto` | Atualiza a foto do pet (somente DONO, máx. 5 MB) |
| `POST` | `/api/pets/solicitarVinculo` | Solicita vínculo como suporte usando o código do pet |

Há ainda endpoints de listagem dos pets do usuário e de listagem e processamento (aceitar/recusar) de solicitações pendentes, no `PetController`.

### Agenda

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/pets/{id}/tarefas` | Cria uma tarefa para o pet (somente DONO) |
| `GET` | `/api/pets/{id}/tarefas` | Lista as tarefas de um pet |
| `GET` | `/api/tarefas?data=yyyy-MM-dd` | Lista as tarefas do dia de todos os pets vinculados |

### Exemplo

```bash
# Cadastro (retorna o token)
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria","email":"maria@email.com","telefone":"11999999999","senha":"********"}'

# Criar uma tarefa
curl -X POST http://localhost:8080/api/pets/1/tarefas \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Passeio","descricao":"30 minutos","dataTarefa":"2026-10-10","hora":"08:00"}'
```

### Erros

Tratados de forma central no `GlobalExceptionHandler`:

| Status | Quando |
| --- | --- |
| `400` | Validação de campos, foto inválida ou grande demais |
| `403` | Sem token ou sem permissão para a ação |
| `404` | Pet ou solicitação não encontrada (ou sem acesso a ele) |
| `409` | Conflito, como e-mail ou telefone já cadastrado, ou solicitação de vínculo duplicada |

---

## 🚀 Como rodar localmente

### Pré-requisitos

- Java 21
- MySQL em execução (local ou remoto)
- Docker (opcional)

### Variáveis de ambiente

| Variável | Descrição |
| --- | --- |
| `DB_HOST` | Host do MySQL |
| `DB_PORT` | Porta do MySQL |
| `DB_NAME` | Nome do schema |
| `DB_USER` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |

### Com Maven

```bash
git clone https://github.com/justletthefatherlead/petVerso-API.git
cd petVerso-API

export DB_HOST=localhost DB_PORT=3306 DB_NAME=petverso DB_USER=root DB_PASSWORD=sua_senha
./mvnw spring-boot:run
```

### Com Docker

```bash
docker build -t petverso-api .

docker run -p 8080:8080 \
  -e DB_HOST=... -e DB_PORT=... -e DB_NAME=... \
  -e DB_USER=... -e DB_PASSWORD=... \
  petverso-api
```

O `Dockerfile` é multi-stage: compila com `eclipse-temurin:21-jdk` + Maven Wrapper e executa o `.jar` em `eclipse-temurin:21-jre`.

---

## ☁️ Deploy

- **API**: Render, a partir do `Dockerfile`.
- **Banco**: MySQL na Aiven.
- A configuração do banco vem 100% de variáveis de ambiente; nenhum segredo fica no repositório.

---

## 📝 Notas de desenvolvimento

- `spring.jpa.hibernate.ddl-auto=update` é usado enquanto as entidades ainda estão sendo criadas. A migração para Flyway ou Liquibase está prevista para quando o projeto se aproximar de produção.
- `spring.jpa.open-in-view=false`: relacionamentos `LAZY` são acessados dentro de `@Transactional` e copiados para os DTOs (`List.copyOf`).
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` em todas as entidades (necessário com MySQL no Hibernate 7).
- Coleções `@ElementCollection` (como `pet_personalidades`) podem não ter a tabela criada pelo Hibernate no `ddl-auto=update`; confira com `SHOW TABLES` após deploys que criem tabelas novas. A Aiven exige chave primária em todas as tabelas.

---

## 🗺️ Roadmap

- [x] Cadastro, login e sessão por token
- [x] Perfil do usuário (apelido e foto)
- [x] Cadastro e perfil do pet
- [x] Vínculo por código (dono e tutor de suporte)
- [x] Agenda: criar e listar tarefas
- [ ] Concluir tarefa (registrar quem executou e quando)
- [ ] Módulo de vacinas (data de aplicação e próxima dose)
- [ ] Eventos na agenda (totalmente customizáveis)
- [ ] Verificação de e-mail no cadastro
- [ ] Revisão de atomicidade (`@Transactional`) no cadastro de usuário
- [ ] Migrações de banco com Flyway ou Liquibase

---

## 👤 Autor

Desenvolvido por [@justletthefatherlead](https://github.com/justletthefatherlead).
