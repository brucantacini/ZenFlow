package com.example.ZenFlow.controller;

import com.example.ZenFlow.dto.AvaliacaoRequestDTO;
import com.example.ZenFlow.dto.AvaliacaoResponseDTO;
import com.example.ZenFlow.dto.mapper.AvaliacaoMapper;
import com.example.ZenFlow.entity.Avaliacao;
import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.repository.DepartamentoRepository;
import com.example.ZenFlow.repository.NivelEstresseRepository;
import com.example.ZenFlow.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
@Validated
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    private final AvaliacaoMapper avaliacaoMapper;
    private final DepartamentoRepository departamentoRepository;
    private final NivelEstresseRepository nivelEstresseRepository;

    @GetMapping
    public ResponseEntity<Page<AvaliacaoResponseDTO>> listar(@PageableDefault(size = 10) Pageable pageable) {
        Page<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacoes(pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return avaliacaoService.buscarPorId(id)
                .map(avaliacaoMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/departamento/{idDepto}")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> buscarPorDepartamento(@PathVariable Long idDepto,
                                                                             @PageableDefault(size = 10) Pageable pageable) {
        Page<Avaliacao> avaliacoes = avaliacaoService.buscarPorDepartamento(idDepto, pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> buscarPorNivel(@PathVariable Integer nivel,
                                                                      @PageableDefault(size = 10) Pageable pageable) {
        Page<Avaliacao> avaliacoes = avaliacaoService.buscarPorNivelEstresse(nivel, pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/periodo")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> buscarPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Avaliacao> avaliacoes = avaliacaoService.buscarPorPeriodo(inicio, fim, pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/departamento/{idDepto}/periodo")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> buscarPorDepartamentoEPeriodo(
            @PathVariable Long idDepto,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Avaliacao> avaliacoes = avaliacaoService.buscarPorDepartamentoEPeriodo(idDepto, inicio, fim, pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/departamento/{idDepto}/media")
    public ResponseEntity<Double> calcularMedia(@PathVariable Long idDepto) {
        return ResponseEntity.ok(avaliacaoService.calcularMediaEstressePorDepartamento(idDepto));
    }

    @GetMapping("/departamento/{idDepto}/total")
    public ResponseEntity<Long> contarPorDepartamento(@PathVariable Long idDepto) {
        return ResponseEntity.ok(avaliacaoService.contarAvaliacoesPorDepartamento(idDepto));
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criar(@RequestBody @Valid AvaliacaoRequestDTO dto) {
        Departamento departamento = departamentoRepository.findById(dto.getIdDepartamento())
                .orElseThrow(() -> new IllegalArgumentException("Departamento não encontrado"));
        
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(dto.getIdNivelEstresse())
                .orElseThrow(() -> new IllegalArgumentException("Nível de estresse não encontrado"));
        
        Avaliacao avaliacao = avaliacaoMapper.toEntity(dto, departamento, nivelEstresse);
        Avaliacao criada = avaliacaoService.criarAvaliacao(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoMapper.toResponseDTO(criada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> atualizar(@PathVariable Long id,
                                                          @RequestBody @Valid AvaliacaoRequestDTO dto) {
        Departamento departamento = departamentoRepository.findById(dto.getIdDepartamento())
                .orElseThrow(() -> new IllegalArgumentException("Departamento não encontrado"));
        
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(dto.getIdNivelEstresse())
                .orElseThrow(() -> new IllegalArgumentException("Nível de estresse não encontrado"));
        
        Avaliacao avaliacao = avaliacaoMapper.toEntity(dto, departamento, nivelEstresse);
        Avaliacao atualizada = avaliacaoService.atualizarAvaliacao(id, avaliacao);
        return ResponseEntity.ok(avaliacaoMapper.toResponseDTO(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
}

