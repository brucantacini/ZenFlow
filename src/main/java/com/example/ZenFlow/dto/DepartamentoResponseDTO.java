package com.example.ZenFlow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoResponseDTO {
    
    private Long idDepto;
    private String nomeDepto;
    private String descricao;
}

