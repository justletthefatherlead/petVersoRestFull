package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.StatusDeVinculo;

import java.time.LocalDateTime;

public record SolicitarVinculoResponseDTO(
        boolean sucesso,
        String mensagem,
        StatusDeVinculo statusDeVinculo,
        DadosSolicitacaoDTO dadosSolicitacaoDTO
) {
    public record DadosSolicitacaoDTO(
            Long idSolicitacao,
            String codigoUtilizado,
            String nomePet,
            LocalDateTime dataSolicitacao

    ){}
}
