package com.namassacompany.petVersoRestFull.service;

import com.namassacompany.petVersoRestFull.dto.TarefaCadastroDTO;
import com.namassacompany.petVersoRestFull.dto.TarefaResponseDTO;
import com.namassacompany.petVersoRestFull.exception.AcessoNegadoException;
import com.namassacompany.petVersoRestFull.exception.PetNaoEncontradoException;
import com.namassacompany.petVersoRestFull.model.*;
import com.namassacompany.petVersoRestFull.repository.PetRepository;
import com.namassacompany.petVersoRestFull.repository.TarefaRepository;
import com.namassacompany.petVersoRestFull.repository.VinculoPetRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarefaService {
    private final PetRepository petRepository;
    private final VinculoPetRepository vinculoPetRepository;
    private final TarefaRepository tarefaRepository;

    public TarefaService(PetRepository petRepository, VinculoPetRepository vinculoPetRepository, TarefaRepository tarefaRepository) {
        this.petRepository = petRepository;
        this.vinculoPetRepository = vinculoPetRepository;
        this.tarefaRepository = tarefaRepository;
    }

    @Transactional
    public TarefaResponseDTO criar (Long idPet, TarefaCadastroDTO dto, Usuario usuario){
        Pet pet = petRepository.findById(idPet).orElseThrow(()->new PetNaoEncontradoException("Pet nao encontrado"));
        VinculoPet vinculo = buscarVinculoAceito(pet, usuario);
        if (vinculo.getPapel() != Papel.DONO){
             throw new AcessoNegadoException("voce nao tem permissao para realizar esta acao");
        }
        Tarefa tarefa = new Tarefa(
                pet,
                dto.titulo(),
                dto.descricao(),
                dto.dataTarefa(),
                dto.hora()
        );
        return new TarefaResponseDTO(tarefaRepository.save(tarefa));


    }

    @Transactional(readOnly = true)
    public List<TarefaResponseDTO>listarTarefas( Long idPet, Usuario usuario){
        Pet pet = petRepository.findById(idPet).orElseThrow(()->new PetNaoEncontradoException("Pet nao encontrado"));
         buscarVinculoAceito(pet, usuario);
         List<Tarefa> tarefas = tarefaRepository.findByPetIdPetOrderByDataTarefaAscHoraAsc(idPet);
         return tarefas.stream().map(TarefaResponseDTO::new).toList();
    }
    @Transactional(readOnly = true)
    public List<TarefaResponseDTO> listarPorDia(LocalDate data, Usuario usuario){
        List<Tarefa> tarefas = tarefaRepository.listarDoDia(usuario, StatusDeVinculo.ACEITO, data);
        return tarefas.stream().map(TarefaResponseDTO::new).toList();
    }






    private VinculoPet buscarVinculoAceito(Pet pet, Usuario usuario) {
        VinculoPet vinculo =  vinculoPetRepository.findByPetAndUsuario(pet, usuario).orElseThrow(()-> new PetNaoEncontradoException("Pet nao encontrado"));
        if (vinculo.getStatus()!= StatusDeVinculo.ACEITO){
            throw new PetNaoEncontradoException("Pet nao encontrado");
        }
        return vinculo;
    }
}
