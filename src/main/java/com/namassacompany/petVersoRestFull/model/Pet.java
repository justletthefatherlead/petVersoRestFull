package com.namassacompany.petVersoRestFull.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Pet {
    @Id
    @GeneratedValue
    @Column(name = "id_pet")
    private Long idPet;

    @Column(name = "nome")
    private String nome;

    @Column(name = "raca")
    private String raca;

    @Column(name = "especie")
    private String especie;

    @Column(name = "data_nascimento")
    private LocalDateTime dataDeNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "porte")
    private Porte porte;

    @Column(name = "peso")
    private Double peso;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

    @Column(name = "sensibilidade")
    private String perfilSensibilidade;

    @Lob
    @Column(name = "foto", columnDefinition = "MEDIUMBLOB")
    private byte[] foto;

    @Column(name = "codigoVinculo", unique = true)
    private String codigoVinculo;

    @ElementCollection
    @CollectionTable(name = "pet_personalidades", joinColumns = @JoinColumn(name = "id_pet"))
    @Column(name = "personalidades")
    private List<String> personalidades;


    public LocalDateTime getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDateTime dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(idPet, pet.idPet);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPet);
    }

    public Pet(Long idPet, String nome, String raca, String especie, LocalDateTime dataDeNascimento, Porte porte, Double peso, Sexo sexo, String perfilSensibilidade, byte[] foto, String codigoVinculo, List<String> personalidades) {
        this.idPet = idPet;
        this.nome = nome;
        this.raca = raca;
        this.especie = especie;
        this.dataDeNascimento = dataDeNascimento;
        this.porte = porte;
        this.peso = peso;
        this.sexo = sexo;
        this.perfilSensibilidade = perfilSensibilidade;
        this.foto = foto;
        this.codigoVinculo = codigoVinculo;
        this.personalidades = personalidades;
    }
    public Pet(){}

    public Long getIdPet() {
        return idPet;
    }

    public void setIdPet(Long idPet) {
        this.idPet = idPet;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getPerfilSensibilidade() {
        return perfilSensibilidade;
    }

    public void setPerfilSensibilidade(String perfilSensibilidade) {
        this.perfilSensibilidade = perfilSensibilidade;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getCodigoVinculo() {
        return codigoVinculo;
    }

    public void setCodigoVinculo(String codigoVinculo) {
        this.codigoVinculo = codigoVinculo;
    }

    public List<String> getPersonalidades() {
        return personalidades;
    }

    public void setPersonalidades(List<String> personalidades) {
        this.personalidades = personalidades;
    }
}
