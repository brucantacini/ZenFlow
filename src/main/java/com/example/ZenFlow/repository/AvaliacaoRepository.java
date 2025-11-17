package com.example.ZenFlow.repository;

import com.example.ZenFlow.entity.Avaliacao;
import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.entity.NivelEstresse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    
    // Filtros por departamento
    Page<Avaliacao> findByDepartamento(Departamento departamento, Pageable pageable);
    
    Page<Avaliacao> findByDepartamentoIdDepto(Long idDepto, Pageable pageable);
    
    // Filtros por nível de estresse
    Page<Avaliacao> findByNivelEstresse(NivelEstresse nivelEstresse, Pageable pageable);
    
    Page<Avaliacao> findByNivelEstresseNivel(Integer nivel, Pageable pageable);
    
    // Filtros por data
    Page<Avaliacao> findByDtAvaliacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);
    
    Page<Avaliacao> findByDtAvaliacaoAfter(LocalDateTime data, Pageable pageable);
    
    Page<Avaliacao> findByDtAvaliacaoBefore(LocalDateTime data, Pageable pageable);
    
    // Filtros combinados
    Page<Avaliacao> findByDepartamentoAndNivelEstresse(
            Departamento departamento, 
            NivelEstresse nivelEstresse, 
            Pageable pageable
    );
    
    Page<Avaliacao> findByDepartamentoIdDeptoAndDtAvaliacaoBetween(
            Long idDepto, 
            LocalDateTime dataInicio, 
            LocalDateTime dataFim, 
            Pageable pageable
    );
    
    // Query customizada para buscar avaliações com comentário
    @Query("SELECT a FROM Avaliacao a WHERE a.comentario IS NOT NULL AND a.comentario != ''")
    Page<Avaliacao> findAvaliacoesComComentario(Pageable pageable);
    
    // Query para contar avaliações por departamento
    @Query("SELECT COUNT(a) FROM Avaliacao a WHERE a.departamento.idDepto = :idDepto")
    Long countByDepartamento(@Param("idDepto") Long idDepto);
    
    // Query para média de nível de estresse por departamento
    @Query("SELECT AVG(n.nivel) FROM Avaliacao a JOIN a.nivelEstresse n WHERE a.departamento.idDepto = :idDepto")
    Double calcularMediaEstressePorDepartamento(@Param("idDepto") Long idDepto);
}