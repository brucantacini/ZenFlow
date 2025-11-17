package com.example.ZenFlow.dto.mapper;

import com.example.ZenFlow.dto.NivelEstresseRequestDTO;
import com.example.ZenFlow.dto.NivelEstresseResponseDTO;
import com.example.ZenFlow.entity.NivelEstresse;
import org.springframework.stereotype.Component;

@Component
public class NivelEstresseMapper {
    
    public NivelEstresse toEntity(NivelEstresseRequestDTO dto) {
        NivelEstresse nivelEstresse = new NivelEstresse();
        nivelEstresse.setNivel(dto.getNivel());
        nivelEstresse.setDescricao(dto.getDescricao());
        return nivelEstresse;
    }
    
    public NivelEstresseResponseDTO toResponseDTO(NivelEstresse nivelEstresse) {
        NivelEstresseResponseDTO dto = new NivelEstresseResponseDTO();
        dto.setIdNivelEstresse(nivelEstresse.getIdNivelEstresse());
        dto.setNivel(nivelEstresse.getNivel());
        dto.setDescricao(nivelEstresse.getDescricao());
        return dto;
    }
}

