package com.example.ZenFlow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "t_zf_departamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_departamento")
    @SequenceGenerator(name = "seq_departamento", sequenceName = "SEQ_T_ZF_DEPARTAMENTO", allocationSize = 1)
    @Column(name = "id_depto")
    private Long idDepto;

    @NotBlank(message = "O nome do departamento é obrigatório")
    @Size(max = 50, message = "O nome do departamento deve ter no máximo 50 caracteres")
    @Column(name = "nome_depto", nullable = false, length = 50)
    private String nomeDepto;

    @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
    @Column(name = "descricao", length = 100)
    private String descricao;

    @JsonIgnore
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes;

}
