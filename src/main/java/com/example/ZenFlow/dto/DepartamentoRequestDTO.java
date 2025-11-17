package com.example.ZenFlow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoRequestDTO {
    
    @NotBlank(message = "O nome do departamento é obrigatório")
    @Size(max = 50, message = "O nome do departamento deve ter no máximo 50 caracteres")
    private String nomeDepto;
    
    @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
    private String descricao;
}

