package com.namassacompany.petVersoRestFull.repository;

import com.namassacompany.petVersoRestFull.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    void deleteByToken(String token);
    Optional<Sessao> findByToken(String token);
}
