package com.example.ZenFlow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_zf_avaliacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_avaliacao")
    @SequenceGenerator(name = "seq_avaliacao", sequenceName = "SEQ_T_ZF_AVALIACAO", allocationSize = 1)
    @Column(name = "id_avaliacao")
    private Long idAvaliacao;

    @NotNull(message = "Departamento é obrigatório")
    @JsonIgnoreProperties(value = {"avaliacoes"}, allowSetters = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_depto", nullable = false)
    private Departamento departamento;

    @NotNull(message = "Nível de estresse é obrigatório")
    @JsonIgnoreProperties(value = {"avaliacoes"}, allowSetters = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel_estresse", nullable = false)
    private NivelEstresse nivelEstresse;

    @Size(max = 200, message = "O comentário deve ter no máximo 200 caracteres")
    @Column(name = "comentario", length = 200)
    private String comentario;

    @NotNull(message = "A data da avaliação é obrigatória")
    @Column(name = "dt_avaliacao", nullable = false)
    private LocalDateTime dtAvaliacao;

}
