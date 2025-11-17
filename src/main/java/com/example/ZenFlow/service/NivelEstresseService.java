package com.example.ZenFlow.service;

import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.repository.NivelEstresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class NivelEstresseService {

    @Autowired
    private NivelEstresseRepository nivelEstresseRepository;

    public Page<NivelEstresse> listar(Pageable pageable) {
        return nivelEstresseRepository.findAll(pageable);
    }

    public Optional<NivelEstresse> buscarPorId(Long id) {
        return nivelEstresseRepository.findById(id);
    }

    public Optional<NivelEstresse> buscarPorNivel(Integer nivel) {
        return nivelEstresseRepository.findByNivel(nivel);
    }

    public Page<NivelEstresse> buscarPorDescricao(String descricao, Pageable pageable) {
        return nivelEstresseRepository.findByDescricaoContainingIgnoreCase(descricao, pageable);
    }

    public NivelEstresse criar(NivelEstresse nivelEstresse) {
        return nivelEstresseRepository.save(nivelEstresse);
    }

    public NivelEstresse atualizar(Long id, NivelEstresse nivelAtualizado) {
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nível de estresse não encontrado"));

        if (nivelAtualizado.getNivel() != null) {
            nivelEstresse.setNivel(nivelAtualizado.getNivel());
        }
        if (nivelAtualizado.getDescricao() != null) {
            nivelEstresse.setDescricao(nivelAtualizado.getDescricao());
        }

        return nivelEstresseRepository.save(nivelEstresse);
    }

    public void deletar(Long id) {
        if (!nivelEstresseRepository.existsById(id)) {
            throw new IllegalArgumentException("Nível de estresse não encontrado");
        }
        nivelEstresseRepository.deleteById(id);
    }
}

