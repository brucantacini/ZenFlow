package com.example.ZenFlow.dto.mapper;

import com.example.ZenFlow.dto.AvaliacaoRequestDTO;
import com.example.ZenFlow.dto.AvaliacaoResponseDTO;
import com.example.ZenFlow.entity.Avaliacao;
import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.entity.NivelEstresse;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoMapper {
    
    public Avaliacao toEntity(AvaliacaoRequestDTO dto, Departamento departamento, NivelEstresse nivelEstresse) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setDepartamento(departamento);
        avaliacao.setNivelEstresse(nivelEstresse);
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setDtAvaliacao(java.time.LocalDateTime.now());
        return avaliacao;
    }
    
    public AvaliacaoResponseDTO toResponseDTO(Avaliacao avaliacao) {
        AvaliacaoResponseDTO dto = new AvaliacaoResponseDTO();
        dto.setIdAvaliacao(avaliacao.getIdAvaliacao());
        dto.setIdDepartamento(avaliacao.getDepartamento().getIdDepto());
        dto.setNomeDepartamento(avaliacao.getDepartamento().getNomeDepto());
        dto.setIdNivelEstresse(avaliacao.getNivelEstresse().getIdNivelEstresse());
        dto.setNivelEstresse(avaliacao.getNivelEstresse().getNivel());
        dto.setDescricaoNivelEstresse(avaliacao.getNivelEstresse().getDescricao());
        dto.setComentario(avaliacao.getComentario());
        dto.setDtAvaliacao(avaliacao.getDtAvaliacao());
        return dto;
    }
}

