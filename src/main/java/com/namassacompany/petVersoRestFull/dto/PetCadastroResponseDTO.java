package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.Pet;
import com.namassacompany.petVersoRestFull.model.Porte;
import com.namassacompany.petVersoRestFull.model.Sexo;

public record PetCadastroResponseDTO(
        Long id,
        String nome,
        String raca,
        String especie,
        Porte porte,
        Double peso,
        Sexo sexo,
        String codigoVinculo
) {
    public PetCadastroResponseDTO(Pet pet){
        this(pet.getIdPet(), pet.getNome(), pet.getRaca(), pet.getEspecie(), pet.getPorte(), pet.getPeso(), pet.getSexo(),
                pet.getCodigoVinculo());
    }
}
