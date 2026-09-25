package com.namassacompany.petVersoRestFull.service;

import com.namassacompany.petVersoRestFull.dto.*;
import com.namassacompany.petVersoRestFull.exception.PetNaoEncontradoException;
import com.namassacompany.petVersoRestFull.exception.StatusDeVinculoInvalidoException;
import com.namassacompany.petVersoRestFull.model.*;
import com.namassacompany.petVersoRestFull.repository.PetRepository;
import com.namassacompany.petVersoRestFull.repository.VinculoPetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    private static final SecureRandom random = new SecureRandom();
    private static final String caracteres = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private final PetRepository petRepository;
    private final VinculoPetRepository vinculoPetRepository;

    public PetService(PetRepository petRepository, VinculoPetRepository vinculoPetRepository) {
        this.petRepository = petRepository;
        this.vinculoPetRepository = vinculoPetRepository;
    }
    @Transactional
   public PetCadastroResponseDTO cadastrar(PetCadastroDTO petdto, Usuario usuario){
       String codigoVinculo = gerarCodigoVinculo();
        byte[] foto= null;
        if (petdto.fotoBase64() != null){
           foto =(Base64.getDecoder().decode(petdto.fotoBase64()));
        }
        Pet pet = new Pet(
                null,
                petdto.nome(),
                petdto.raca(),
                petdto.especie(),
                petdto.dataDeNascimento(),
                petdto.porte(),
                petdto.peso(),
                petdto.sexo(),
                null,
                foto,
                codigoVinculo,
                new ArrayList<>()


        );
        Pet petSalvo = petRepository.save(pet);

       VinculoPet vinculoPet = new VinculoPet(
               null,
               petSalvo,
               usuario,
               Papel.DONO,
               StatusDeVinculo.ACEITO,
               LocalDateTime.now()

       );
       vinculoPetRepository.save(vinculoPet);


        return new PetCadastroResponseDTO(
                petSalvo
        );
   }

   public PetPerfilDTO visualizarPetPerfil(Long idPet, Usuario usuarioAutenticado){
        Pet pet = petRepository.findById(idPet).
                orElseThrow(() -> new PetNaoEncontradoException("Pet nao encontrado"));
        buscarVinculoAceito(pet, usuarioAutenticado);
        return new PetPerfilDTO(pet);
   }

    public PetPerfilDTO addPerfilSensiAndPersonalit(Long idPet, AtualizarPetDTO pdto, Usuario usuario){
        Pet pet = petRepository.findById(idPet).orElseThrow(()-> new PetNaoEncontradoException("Pet nao encontrado"));
        VinculoPet vinculo = buscarVinculoAceito(pet, usuario);
        if (vinculo.getPapel()!= Papel.DONO){
            throw new PetNaoEncontradoException("Seu papel nao perimite fazer alterações");
        }
        if(pdto.perfilDeSensibilidade()!= null){
            pet.setPerfilSensibilidade(pdto.perfilDeSensibilidade());
        }
        if (pdto.personalidades()!= null){
            pet.setPersonalidades(pdto.personalidades());
        }
        return new PetPerfilDTO(petRepository.save(pet));
    }

    public SolicitarVinculoResponseDTO solicitarVinculo(Usuario usuario, SolicitarVinculoDTO dto){
        String codigo  = dto.codigoVinculo();
        Pet pet = petRepository.findByCodigoVinculo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Código de vínculo inválido"));

        Optional<VinculoPet> vinculoExistente = vinculoPetRepository.findByPetAndUsuario(pet, usuario);
            VinculoPet vinculo;
            if(vinculoExistente.isPresent()){
                vinculo = vinculoExistente.get();
                switch (vinculo.getStatus()){
                    case PENDENTE -> throw new IllegalStateException("Você já possui uma soliciçao para este pet.");
                    case ACEITO -> throw new IllegalStateException("Você já possui vinculo com este pet.");
                    case RECUSADO -> {
                        vinculo.setStatus(StatusDeVinculo.PENDENTE);
                        vinculo.setDataDeCriacao(LocalDateTime.now());
                    }
                }
            }else {
                vinculo = new VinculoPet();
                vinculo.setPet(pet);
                vinculo.setUsuario(usuario);
                vinculo.setPapel(Papel.SUPORTE);
                vinculo.setStatus(StatusDeVinculo.PENDENTE);
                vinculo.setDataDeCriacao(LocalDateTime.now());


            }
            vinculoPetRepository.save(vinculo);
            var dados = new SolicitarVinculoResponseDTO.DadosSolicitacaoDTO(
                    vinculo.getId(),
                    codigo,
                    pet.getNome(),
                    vinculo.getDataDeCriacao()
            );
            return new SolicitarVinculoResponseDTO(
                    true,
                    "Solicitação enviada com sucesso! Aguarde a confirmação do tutor",
                    StatusDeVinculo.PENDENTE,
                    dados
            );
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoPendenteDTO> listarSolicitacoes(Usuario usuario){
        List<VinculoPet> vinculosDono = vinculoPetRepository.findByUsuarioAndPapel(usuario, Papel.DONO);
        List<SolicitacaoPendenteDTO> resultado = new ArrayList<>();

        for (VinculoPet vinculoDono : vinculosDono) {
            if (vinculoDono.getStatus()!= StatusDeVinculo.ACEITO){
                continue;
            }
            Pet pet = vinculoDono.getPet();

            List<VinculoPet> pendentes = vinculoPetRepository.findByPetAndStatus(pet,StatusDeVinculo.PENDENTE);
            for (VinculoPet solicitacao : pendentes){
                resultado.add(new SolicitacaoPendenteDTO(
                        solicitacao.getId(),
                        solicitacao.getUsuario().getNome(),
                        pet.getNome(),
                        solicitacao.getDataDeCriacao()
                ));
            }
        }
        return  resultado;
    }
    @Transactional
    public  SolicitarVinculoResponseDTO processarSolicitacao(Long idSolicitacao, StatusDeVinculo novoStatus, Usuario usuario){
        VinculoPet solicitacao =  vinculoPetRepository.findById(idSolicitacao).orElseThrow(()-> new PetNaoEncontradoException("solicitacao nao existe"));
        VinculoPet meuVinculo = vinculoPetRepository.findByPetAndUsuario(solicitacao.getPet(), usuario).orElseThrow(()-> new PetNaoEncontradoException("solicitacao nao existe"));
        if(meuVinculo.getPapel()!= Papel.DONO || meuVinculo.getStatus() != StatusDeVinculo.ACEITO){
            throw new PetNaoEncontradoException("solicitacao nao existe");
        }
        if (novoStatus != StatusDeVinculo.ACEITO && novoStatus != StatusDeVinculo.RECUSADO){
            throw new StatusDeVinculoInvalidoException("Só pode ser aceito ou recusado");
        }
        if (solicitacao.getStatus()!= StatusDeVinculo.PENDENTE){
            throw new StatusDeVinculoInvalidoException("Esta solicitacao já foi respondida anteriormente");
        }
        solicitacao.setStatus(novoStatus);
        vinculoPetRepository.save(solicitacao);

        return new SolicitarVinculoResponseDTO(
                   true,
                    "concluido",
                    novoStatus,
                new SolicitarVinculoResponseDTO.DadosSolicitacaoDTO(
                        solicitacao.getId(),
                        solicitacao.getPet().getCodigoVinculo(),
                        solicitacao.getPet().getNome(),
                        solicitacao.getDataDeCriacao()
                )

        );


    }

    public List<PetResumoDTO> listarPets(Usuario usuario){
        List<VinculoPet> vinculos = vinculoPetRepository.findByUsuarioAndStatus(usuario, StatusDeVinculo.ACEITO);
        return vinculos.stream().map(v-> new PetResumoDTO(
                v.getPet().getIdPet(),
                v.getPet().getNome(),
                v.getPet().getCodigoVinculo(),
                v.getPapel().name()
        )).toList();
    }


    private String gerarCodigoBruto() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        String codigoVinculo = sb.toString();
        return codigoVinculo.substring(0, 3) + "-" + codigoVinculo.substring(3);

    }
    private String gerarCodigoVinculo(){
        String codigo = gerarCodigoBruto();
        while (petRepository.findByCodigoVinculo(codigo).isPresent()){
            codigo = gerarCodigoBruto();
        }
        return codigo;
    }
   private VinculoPet buscarVinculoAceito(Pet pet, Usuario usuario) {
      VinculoPet vinculo =  vinculoPetRepository.findByPetAndUsuario(pet, usuario).orElseThrow(()-> new PetNaoEncontradoException("Vinculo nao existe"));
      if (vinculo.getStatus()!= StatusDeVinculo.ACEITO){
          throw new PetNaoEncontradoException("Vinculo nao existe");
      }
      return vinculo;
    }
   }