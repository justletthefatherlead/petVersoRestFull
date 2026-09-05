package com.namassacompany.petVersoRestFull.service;

import com.namassacompany.petVersoRestFull.repository.PetRepository;
import com.namassacompany.petVersoRestFull.repository.VinculoPetRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final VinculoPetRepository vinculoPetRepository;

    public PetService(PetRepository petRepository, VinculoPetRepository vinculoPetRepository) {
        this.petRepository = petRepository;
        this.vinculoPetRepository = vinculoPetRepository;
    }

    private static final String caracteres = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";

    private static final SecureRandom random = new SecureRandom();

    private String gerarCodigoBruto() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        String codigoVinculo = sb.toString();
        return codigoVinculo.substring(0, 3) + "-" + codigoVinculo.substring(3);

    }
    public String gerarCodigoVinculo(){
        String codigo = gerarCodigoBruto();
        while (petRepository.findByCodigoVinculo(codigo).isPresent()){
            codigo = gerarCodigoBruto();
        }
        return codigo;
    }
}