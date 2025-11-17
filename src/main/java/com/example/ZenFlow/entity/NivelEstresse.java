package com.example.ZenFlow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "t_zf_nivel_estresse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelEstresse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nivel_estresse")
    @SequenceGenerator(name = "seq_nivel_estresse", sequenceName = "SEQ_T_ZF_NIVEL_ESTRESSE", allocationSize = 1)
    @Column(name = "id_nivel_estresse")
    private Long idNivelEstresse;

    @NotNull(message = "O nível é obrigatório")
    @Min(value = 1, message = "O nível mínimo é 1")
    @Max(value = 5, message = "O nível máximo é 5")
    @Column(name = "nivel", nullable = false, unique = true)
    private Integer nivel;

    @NotBlank(message = "A descrição do nível é obrigatória")
    @Size(max = 20, message = "A descrição deve ter no máximo 20 caracteres")
    @Column(name = "descricao", nullable = false, length = 20)
    private String descricao;

    @JsonIgnore
    @OneToMany(mappedBy = "nivelEstresse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes;

}

