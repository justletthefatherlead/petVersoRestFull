package com.namassacompany.petVersoRestFull.repository;

import com.namassacompany.petVersoRestFull.model.Pet;
import com.namassacompany.petVersoRestFull.model.StatusDeVinculo;
import com.namassacompany.petVersoRestFull.model.Usuario;
import com.namassacompany.petVersoRestFull.model.VinculoPet;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.namassacompany.petVersoRestFull.model.StatusDeVinculo.PENDENTE;

public interface VinculoPetRepository extends JpaRepository<VinculoPet, Long> {

        boolean existsByPetAndUsuario(Pet pet, Usuario usuario);

    Optional<VinculoPet> findByPetAndUsuario(Pet pet, Usuario usuario);

    List<VinculoPet> findByUsuario(Usuario usuario);
    List<VinculoPet> findByPetAndStatus(Pet pet, StatusDeVinculo status);
}
