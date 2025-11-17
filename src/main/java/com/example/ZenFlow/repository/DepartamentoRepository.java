package com.example.ZenFlow.repository;

import com.example.ZenFlow.entity.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    
    Optional<Departamento> findByNomeDepto(String nomeDepto);
    
    Page<Departamento> findByNomeDeptoContainingIgnoreCase(String nome, Pageable pageable);
    
    @Query("SELECT d FROM Departamento d WHERE d.descricao LIKE %:descricao%")
    Page<Departamento> findByDescricaoContaining(@Param("descricao") String descricao, Pageable pageable);
}
