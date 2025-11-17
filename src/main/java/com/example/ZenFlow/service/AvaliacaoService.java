package com.example.ZenFlow.service;

import com.example.ZenFlow.entity.Avaliacao;
import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.repository.AvaliacaoRepository;
import com.example.ZenFlow.repository.DepartamentoRepository;
import com.example.ZenFlow.repository.NivelEstresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    
    @Autowired
    private DepartamentoRepository departamentoRepository;
    
    @Autowired
    private NivelEstresseRepository nivelEstresseRepository;

    public Page<Avaliacao> listarAvaliacoes(Pageable pageable) {
        return avaliacaoRepository.findAll(pageable);
    }
    
    public Optional<Avaliacao> buscarPorId(Long id) {
        return avaliacaoRepository.findById(id);
    }
    
    public Page<Avaliacao> buscarPorDepartamento(Long idDepto, Pageable pageable) {
        return avaliacaoRepository.findByDepartamentoIdDepto(idDepto, pageable);
    }
    
    public Page<Avaliacao> buscarPorNivelEstresse(Integer nivel, Pageable pageable) {
        return avaliacaoRepository.findByNivelEstresseNivel(nivel, pageable);
    }
    
    public Page<Avaliacao> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        return avaliacaoRepository.findByDtAvaliacaoBetween(dataInicio, dataFim, pageable);
    }
    
    public Page<Avaliacao> buscarPorDepartamentoEPeriodo(Long idDepto, LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        return avaliacaoRepository.findByDepartamentoIdDeptoAndDtAvaliacaoBetween(idDepto, dataInicio, dataFim, pageable);
    }
    
    public Double calcularMediaEstressePorDepartamento(Long idDepto) {
        return avaliacaoRepository.calcularMediaEstressePorDepartamento(idDepto);
    }
    
    public Long contarAvaliacoesPorDepartamento(Long idDepto) {
        return avaliacaoRepository.countByDepartamento(idDepto);
    }

    public Avaliacao criarAvaliacao(Avaliacao avaliacao) {
        // Validações
        if (avaliacao.getDepartamento() == null || avaliacao.getDepartamento().getIdDepto() == null) {
            throw new IllegalArgumentException("Departamento é obrigatório");
        }
        
        if (avaliacao.getNivelEstresse() == null || avaliacao.getNivelEstresse().getIdNivelEstresse() == null) {
            throw new IllegalArgumentException("Nível de estresse é obrigatório");
        }
        
        // Verificar se departamento existe
        Optional<Departamento> departamento = departamentoRepository.findById(avaliacao.getDepartamento().getIdDepto());
        if (departamento.isEmpty()) {
            throw new IllegalArgumentException("Departamento não encontrado");
        }
        avaliacao.setDepartamento(departamento.get());
        
        // Verificar se nível de estresse existe
        Optional<NivelEstresse> nivelEstresse = nivelEstresseRepository.findById(avaliacao.getNivelEstresse().getIdNivelEstresse());
        if (nivelEstresse.isEmpty()) {
            throw new IllegalArgumentException("Nível de estresse não encontrado");
        }
        avaliacao.setNivelEstresse(nivelEstresse.get());
        
        // Definir data automaticamente se não informada
        if (avaliacao.getDtAvaliacao() == null) {
            avaliacao.setDtAvaliacao(LocalDateTime.now());
        }
        
        return avaliacaoRepository.save(avaliacao);
    }
    
    public Avaliacao atualizarAvaliacao(Long id, Avaliacao avaliacaoAtualizada) {
        Optional<Avaliacao> avaliacaoExistente = avaliacaoRepository.findById(id);
        
        if (avaliacaoExistente.isEmpty()) {
            throw new IllegalArgumentException("Avaliação não encontrada");
        }
        
        Avaliacao avaliacao = avaliacaoExistente.get();
        
        // Atualizar apenas campos permitidos
        if (avaliacaoAtualizada.getNivelEstresse() != null) {
            Optional<NivelEstresse> nivelEstresse = nivelEstresseRepository.findById(
                avaliacaoAtualizada.getNivelEstresse().getIdNivelEstresse()
            );
            if (nivelEstresse.isEmpty()) {
                throw new IllegalArgumentException("Nível de estresse não encontrado");
            }
            avaliacao.setNivelEstresse(nivelEstresse.get());
        }
        
        if (avaliacaoAtualizada.getComentario() != null) {
            avaliacao.setComentario(avaliacaoAtualizada.getComentario());
        }
        
        return avaliacaoRepository.save(avaliacao);
    }
    
    public void deletarAvaliacao(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new IllegalArgumentException("Avaliação não encontrada");
        }
        avaliacaoRepository.deleteById(id);
    }

}
