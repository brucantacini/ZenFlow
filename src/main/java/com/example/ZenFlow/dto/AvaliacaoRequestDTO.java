package com.example.ZenFlow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoRequestDTO {
    
    @NotNull(message = "ID do departamento é obrigatório")
    private Long idDepartamento;
    
    @NotNull(message = "ID do nível de estresse é obrigatório")
    private Long idNivelEstresse;
    
    @Size(max = 200, message = "O comentário deve ter no máximo 200 caracteres")
    private String comentario;
}

