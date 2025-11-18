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
    
    /**
     * Calcula a média semanal usando a função FN_CALCULAR_MEDIA_SEMANAL
     * @param idDepto ID do departamento
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Média de estresse calculada pela função
     */
    public Double calcularMediaSemanalViaFunction(Long idDepto, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return procedureService.calcularMediaSemanal(idDepto, dataInicio, dataFim);
    }
    
    /**
     * Valida um nível de estresse usando a função FN_VALIDAR_NIVEL_ESTRESSE
     * @param nivel Nível de estresse a validar
     * @return Mensagem de validação
     */
    public String validarNivelEstresseViaFunction(Integer nivel) {
        return procedureService.validarNivelEstresse(nivel);
    }
    
    /**
     * Conta registros em um período usando a função FN_CONTAR_REGISTROS_PERIODO
     * @param idDepto ID do departamento
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Total de registros no período
     */
    public Long contarRegistrosPeriodoViaFunction(Long idDepto, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return procedureService.contarRegistrosPeriodo(idDepto, dataInicio, dataFim);
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
            throw new EntityNotFoundException("Departamento", avaliacao.getDepartamento().getIdDepto());
        }
        avaliacao.setDepartamento(departamento.get());
        
        // Verificar se nível de estresse existe
        Optional<NivelEstresse> nivelEstresse = nivelEstresseRepository.findById(avaliacao.getNivelEstresse().getIdNivelEstresse());
        if (nivelEstresse.isEmpty()) {
            throw new EntityNotFoundException("Nível de estresse", avaliacao.getNivelEstresse().getIdNivelEstresse());
        }
        avaliacao.setNivelEstresse(nivelEstresse.get());
        
        // Definir data automaticamente se não informada
        if (avaliacao.getDtAvaliacao() == null) {
            avaliacao.setDtAvaliacao(LocalDateTime.now());
        }
        
        return avaliacaoRepository.save(avaliacao);
    }
    
    /**
     * Cria uma avaliação usando a procedure PC_INSERIR_AVALIACAO
     * @param avaliacao Avaliação a ser criada
     * @return Avaliação criada com ID retornado pela procedure
     */
    public Avaliacao criarAvaliacaoViaProcedure(Avaliacao avaliacao) {
        // Validações
        if (avaliacao.getDepartamento() == null || avaliacao.getDepartamento().getIdDepto() == null) {
            throw new IllegalArgumentException("Departamento é obrigatório");
        }
        
        if (avaliacao.getNivelEstresse() == null || avaliacao.getNivelEstresse().getIdNivelEstresse() == null) {
            throw new IllegalArgumentException("Nível de estresse é obrigatório");
        }
        
        // Chamar procedure
        Long idGerado = procedureService.inserirAvaliacao(
                avaliacao.getDepartamento().getIdDepto(),
                avaliacao.getNivelEstresse().getIdNivelEstresse(),
                avaliacao.getComentario()
        );
        
        // Buscar a avaliação criada pela procedure
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
