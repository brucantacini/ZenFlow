package com.example.ZenFlow.repository;

import com.example.ZenFlow.entity.NivelEstresse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NivelEstresseRepository extends JpaRepository<NivelEstresse, Long> {
    
    Optional<NivelEstresse> findByNivel(Integer nivel);
    
    Page<NivelEstresse> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
}
