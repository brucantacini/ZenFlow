package com.example.ZenFlow.service;

import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.exception.EntityNotFoundException;
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
    
    @Autowired
    private ProcedureService procedureService;

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
    
    /**
     * Cria um nível de estresse usando a procedure PC_INSERIR_NIVEL_ESTRESSE
     * @param nivelEstresse Nível de estresse a ser criado
     * @return Nível de estresse criado com ID retornado pela procedure
     */
    public NivelEstresse criarViaProcedure(NivelEstresse nivelEstresse) {
        Long idGerado = procedureService.inserirNivelEstresse(
                nivelEstresse.getNivel(),
                nivelEstresse.getDescricao()
        );
        
        // Buscar o nível de estresse criado pela procedure
        return nivelEstresseRepository.findById(idGerado)
                .orElseThrow(() -> new EntityNotFoundException("Nível de estresse", idGerado));
    }

    public NivelEstresse atualizar(Long id, NivelEstresse nivelAtualizado) {
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nível de estresse", id));

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
            throw new EntityNotFoundException("Nível de estresse", id);
        }
        nivelEstresseRepository.deleteById(id);
    }
}

