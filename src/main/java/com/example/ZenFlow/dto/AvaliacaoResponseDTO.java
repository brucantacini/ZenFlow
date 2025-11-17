package com.example.ZenFlow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponseDTO {
    
    private Long idAvaliacao;
    private Long idDepartamento;
    private String nomeDepartamento;
    private Long idNivelEstresse;
    private Integer nivelEstresse;
    private String descricaoNivelEstresse;
    private String comentario;
    private LocalDateTime dtAvaliacao;
}

