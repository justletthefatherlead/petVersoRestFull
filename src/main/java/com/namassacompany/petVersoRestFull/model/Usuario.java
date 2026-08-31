package com.namassacompany.petVersoRestFull.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Usuario {
    @Id
    @GeneratedValue
    @Column(name = "idUsuario")
    private Long idUsuario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "apelido")
    private String apelido;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "senha")
    private String senha;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idUsuario);
    }
    public Usuario(){}




    public Usuario(Long idUsuario, String nome, String apelido, String email, String telefone, byte[] foto, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.apelido = apelido;
        this.email = email;
        this.telefone = telefone;
        this.foto = foto;
        this.senha = senha;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {return apelido;}

    public void setApelido(String apelido) {this.apelido = apelido;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public byte[] getFoto() {return foto;}

    public void setFoto(byte[] foto) {this.foto = foto;}

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
