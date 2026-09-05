package com.namassacompany.petVersoRestFull.controller;

import com.namassacompany.petVersoRestFull.dto.PetCadastroDTO;
import com.namassacompany.petVersoRestFull.dto.PetCadastroResponseDTO;
import com.namassacompany.petVersoRestFull.dto.PetPerfilDTO;
import com.namassacompany.petVersoRestFull.model.Usuario;
import com.namassacompany.petVersoRestFull.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<PetCadastroResponseDTO> cadastrar(@RequestBody PetCadastroDTO petDto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PetCadastroResponseDTO petCadastrado = petService.cadastrar(petDto,usuario);
        return ResponseEntity.ok(petCadastrado);
    }

    @GetMapping("/{id}/perfil")
    public ResponseEntity<PetPerfilDTO> visualizarPetPerfil(@PathVariable Long id ){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PetPerfilDTO visualizarPetPerfil = petService.visualizarPetPerfil(id, usuario);
        return ResponseEntity.ok(visualizarPetPerfil);
    }
}
