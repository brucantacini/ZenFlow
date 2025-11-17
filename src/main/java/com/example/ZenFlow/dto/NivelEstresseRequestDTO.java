package com.example.ZenFlow.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelEstresseRequestDTO {
    
    @NotNull(message = "O nível é obrigatório")
    @Min(value = 1, message = "O nível mínimo é 1")
    @Max(value = 5, message = "O nível máximo é 5")
    private Integer nivel;
    
    @NotBlank(message = "A descrição do nível é obrigatória")
    @Size(max = 20, message = "A descrição deve ter no máximo 20 caracteres")
    private String descricao;
}

