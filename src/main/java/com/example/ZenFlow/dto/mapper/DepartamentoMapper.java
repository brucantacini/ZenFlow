package com.example.ZenFlow.dto.mapper;

import com.example.ZenFlow.dto.DepartamentoRequestDTO;
import com.example.ZenFlow.dto.DepartamentoResponseDTO;
import com.example.ZenFlow.entity.Departamento;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoMapper {
    
    public Departamento toEntity(DepartamentoRequestDTO dto) {
        Departamento departamento = new Departamento();
        departamento.setNomeDepto(dto.getNomeDepto());
        departamento.setDescricao(dto.getDescricao());
        return departamento;
    }
    
    public DepartamentoResponseDTO toResponseDTO(Departamento departamento) {
        DepartamentoResponseDTO dto = new DepartamentoResponseDTO();
        dto.setIdDepto(departamento.getIdDepto());
        dto.setNomeDepto(departamento.getNomeDepto());
        dto.setDescricao(departamento.getDescricao());
        return dto;
    }
}

