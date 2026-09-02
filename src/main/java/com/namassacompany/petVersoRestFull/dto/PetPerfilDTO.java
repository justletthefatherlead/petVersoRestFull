package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.Pet;
import com.namassacompany.petVersoRestFull.model.Porte;
import com.namassacompany.petVersoRestFull.model.Sexo;

import java.util.Base64;

public record PetPerfilDTO(
        Long id,
        String nome,
        String raca,
        String especie,
        Porte porte,
        Double peso,
        Sexo sexo,
        String fotoPetBase64
) {
    public PetPerfilDTO(Pet pet){
        this(pet.getIdPet(), pet.getNome(), pet.getRaca(), pet.getEspecie(), pet.getPorte(), pet.getPeso(), pet.getSexo(),
                (pet.getFoto() != null) ? Base64.getEncoder().encodeToString(pet.getFoto()): null);
    }
}
