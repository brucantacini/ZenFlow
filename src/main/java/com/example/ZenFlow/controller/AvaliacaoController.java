package com.example.ZenFlow.controller;

import com.example.ZenFlow.entity.Avaliacao;
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

    @GetMapping
    public ResponseEntity<Page<Avaliacao>> listar(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(avaliacaoService.listarAvaliacoes(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable Long id) {
        return avaliacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/departamento/{idDepto}")
    public ResponseEntity<Page<Avaliacao>> buscarPorDepartamento(@PathVariable Long idDepto,
                                                                 @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(avaliacaoService.buscarPorDepartamento(idDepto, pageable));
    }

    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<Page<Avaliacao>> buscarPorNivel(@PathVariable Integer nivel,
                                                          @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(avaliacaoService.buscarPorNivelEstresse(nivel, pageable));
    }

    @GetMapping("/periodo")
    public ResponseEntity<Page<Avaliacao>> buscarPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(avaliacaoService.buscarPorPeriodo(inicio, fim, pageable));
    }

    @GetMapping("/departamento/{idDepto}/periodo")
    public ResponseEntity<Page<Avaliacao>> buscarPorDepartamentoEPeriodo(
            @PathVariable Long idDepto,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(avaliacaoService.buscarPorDepartamentoEPeriodo(idDepto, inicio, fim, pageable));
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
    public ResponseEntity<Avaliacao> criar(@RequestBody @Valid Avaliacao avaliacao) {
        Avaliacao criada = avaliacaoService.criarAvaliacao(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(@PathVariable Long id,
                                               @RequestBody @Valid Avaliacao avaliacao) {
        return ResponseEntity.ok(avaliacaoService.atualizarAvaliacao(id, avaliacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
}

