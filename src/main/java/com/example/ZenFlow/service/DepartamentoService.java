package com.example.ZenFlow.service;

import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.exception.EntityNotFoundException;
import com.example.ZenFlow.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;
    
    @Autowired
    private ProcedureService procedureService;

    public Page<Departamento> listar(Pageable pageable) {
        return departamentoRepository.findAll(pageable);
    }

    public Optional<Departamento> buscarPorId(Long id) {
        return departamentoRepository.findById(id);
    }

    public Page<Departamento> buscarPorNome(String nome, Pageable pageable) {
        return departamentoRepository.findByNomeDeptoContainingIgnoreCase(nome, pageable);
    }

    public Page<Departamento> buscarPorDescricao(String descricao, Pageable pageable) {
        return departamentoRepository.findByDescricaoContaining(descricao, pageable);
    }

    public Departamento criar(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }
    
    /**
     * Cria um departamento usando a procedure PC_INSERIR_DEPARTAMENTO
     * @param departamento Departamento a ser criado
     * @return Departamento criado com ID retornado pela procedure
     */
    public Departamento criarViaProcedure(Departamento departamento) {
        Long idGerado = procedureService.inserirDepartamento(
                departamento.getNomeDepto(),
                departamento.getDescricao()
        );
        
        // Buscar o departamento criado pela procedure
        return departamentoRepository.findById(idGerado)
                .orElseThrow(() -> new EntityNotFoundException("Departamento", idGerado));
    }

    public Departamento atualizar(Long id, Departamento departamentoAtualizado) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento", id));

        if (departamentoAtualizado.getNomeDepto() != null) {
            departamento.setNomeDepto(departamentoAtualizado.getNomeDepto());
        }
        departamento.setDescricao(departamentoAtualizado.getDescricao());

        return departamentoRepository.save(departamento);
    }

    public void deletar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Departamento", id);
        }
        departamentoRepository.deleteById(id);
    }
}

