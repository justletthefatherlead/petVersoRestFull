package com.namassacompany.petVersoRestFull.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class VinculoPet {
    @Id
    @GeneratedValue
    @Column(name = "id_vinculo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pet", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel")
    private Papel papel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_vinculo")
    private StatusDeVinculo status;

    @Column(name = "data_de_criacao")
    private LocalDateTime dataDeCriacao;

    public VinculoPet(Long id, Pet pet, Usuario usuario, Papel papel, StatusDeVinculo status, LocalDateTime dataDeCriacao) {
        this.id = id;
        this.pet = pet;
        this.usuario = usuario;
        this.papel = papel;
        this.status = status;
        this.dataDeCriacao = dataDeCriacao;
    }

    public VinculoPet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public StatusDeVinculo getStatus() {
        return status;
    }

    public void setStatus(StatusDeVinculo status) {
        this.status = status;
    }

    public LocalDateTime getDataDeCriacao() {
        return dataDeCriacao;
    }

    public void setDataDeCriacao(LocalDateTime dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VinculoPet that = (VinculoPet) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
