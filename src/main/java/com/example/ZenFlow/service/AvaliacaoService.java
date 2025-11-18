package com.example.ZenFlow.service;

import com.example.ZenFlow.entity.Avaliacao;
import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.exception.EntityNotFoundException;
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
    
    @Autowired
    private ProcedureService procedureService;

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
    
    public Double calcularMediaSemanalViaFunction(Long idDepto, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return procedureService.calcularMediaSemanal(idDepto, dataInicio, dataFim);
    }
    
    public String validarNivelEstresseViaFunction(Integer nivel) {
        return procedureService.validarNivelEstresse(nivel);
    }
    
    public Long contarRegistrosPeriodoViaFunction(Long idDepto, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return procedureService.contarRegistrosPeriodo(idDepto, dataInicio, dataFim);
    }

    public Avaliacao criarAvaliacao(Long idDepartamento, Long idNivelEstresse, String comentario) {
        Departamento departamento = departamentoRepository.findById(idDepartamento)
                .orElseThrow(() -> new EntityNotFoundException("Departamento", idDepartamento));
        
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(idNivelEstresse)
                .orElseThrow(() -> new EntityNotFoundException("Nível de estresse", idNivelEstresse));
        
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setDepartamento(departamento);
        avaliacao.setNivelEstresse(nivelEstresse);
        avaliacao.setComentario(comentario);
        avaliacao.setDtAvaliacao(LocalDateTime.now());
        
        return avaliacaoRepository.save(avaliacao);
    }
    
    public Avaliacao criarAvaliacao(Avaliacao avaliacao) {
        if (avaliacao.getDepartamento() == null || avaliacao.getDepartamento().getIdDepto() == null) {
            throw new IllegalArgumentException("Departamento é obrigatório");
        }
        
        if (avaliacao.getNivelEstresse() == null || avaliacao.getNivelEstresse().getIdNivelEstresse() == null) {
            throw new IllegalArgumentException("Nível de estresse é obrigatório");
        }
        
        Departamento departamento = departamentoRepository.findById(avaliacao.getDepartamento().getIdDepto())
                .orElseThrow(() -> new EntityNotFoundException("Departamento", avaliacao.getDepartamento().getIdDepto()));
        avaliacao.setDepartamento(departamento);
        
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(avaliacao.getNivelEstresse().getIdNivelEstresse())
                .orElseThrow(() -> new EntityNotFoundException("Nível de estresse", avaliacao.getNivelEstresse().getIdNivelEstresse()));
        avaliacao.setNivelEstresse(nivelEstresse);
        
        if (avaliacao.getDtAvaliacao() == null) {
            avaliacao.setDtAvaliacao(LocalDateTime.now());
        }
        
        return avaliacaoRepository.save(avaliacao);
    }
    
    public Avaliacao criarAvaliacaoViaProcedure(Long idDepartamento, Long idNivelEstresse, String comentario) {
        Long idGerado = procedureService.inserirAvaliacao(idDepartamento, idNivelEstresse, comentario);
        return avaliacaoRepository.findById(idGerado)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação", idGerado));
    }
    
    public Avaliacao atualizarAvaliacao(Long id, Avaliacao avaliacaoAtualizada) {
        Optional<Avaliacao> avaliacaoExistente = avaliacaoRepository.findById(id);
        
        if (avaliacaoExistente.isEmpty()) {
            throw new EntityNotFoundException("Avaliação", id);
        }
        
        Avaliacao avaliacao = avaliacaoExistente.get();
        
        // Atualizar apenas campos permitidos
        if (avaliacaoAtualizada.getNivelEstresse() != null) {
            Optional<NivelEstresse> nivelEstresse = nivelEstresseRepository.findById(
                avaliacaoAtualizada.getNivelEstresse().getIdNivelEstresse()
            );
            if (nivelEstresse.isEmpty()) {
                throw new EntityNotFoundException("Nível de estresse", avaliacaoAtualizada.getNivelEstresse().getIdNivelEstresse());
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
            throw new EntityNotFoundException("Avaliação", id);
        }
        avaliacaoRepository.deleteById(id);
    }

}
