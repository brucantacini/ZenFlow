package com.example.ZenFlow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelEstresseResponseDTO {
    
    private Long idNivelEstresse;
    private Integer nivel;
    private String descricao;
}

