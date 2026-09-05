package com.namassacompany.petVersoRestFull.service;

import com.namassacompany.petVersoRestFull.dto.PetCadastroDTO;
import com.namassacompany.petVersoRestFull.dto.PetCadastroResponseDTO;
import com.namassacompany.petVersoRestFull.dto.PetPerfilDTO;
import com.namassacompany.petVersoRestFull.exception.PetNaoEncontradoException;
import com.namassacompany.petVersoRestFull.model.*;
import com.namassacompany.petVersoRestFull.repository.PetRepository;
import com.namassacompany.petVersoRestFull.repository.VinculoPetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
                null,
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
        boolean temVinculo = vinculoPetRepository.existsByPetAndUsuario(pet, usuarioAutenticado);
        if(!temVinculo){ throw new PetNaoEncontradoException("Pet nao encontrado");}

        return new PetPerfilDTO(pet);
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
}