package com.namassacompany.petVersoRestFull.repository;

import com.namassacompany.petVersoRestFull.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
