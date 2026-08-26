package com.namassacompany.petVersoRestFull.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
@Entity
public class Sessao {
    @Id
    @GeneratedValue
    @Column(name = "idSession")

    private Long idSession;

    @Column(name = "token", unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private LocalDateTime dataCreate;

    public Sessao(Long idSession, String token, Usuario usuario, LocalDateTime dataCreate) {
        this.idSession = idSession;
        this.token = token;
        this.usuario = usuario;
        this.dataCreate = dataCreate;
    }
    public Sessao(){}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sessao sessao = (Sessao) o;
        return Objects.equals(idSession, sessao.idSession);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idSession);
    }

    public Long getIdSession() {
        return idSession;
    }

    public void setIdSession(Long idSession) {
        this.idSession = idSession;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(LocalDateTime dataCreate) {
        this.dataCreate = dataCreate;
    }
}
