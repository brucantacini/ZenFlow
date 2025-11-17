package com.example.ZenFlow.controller;

import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.service.DepartamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
@Validated
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<Page<Departamento>> listar(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(departamentoService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> buscarPorId(@PathVariable Long id) {
        return departamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Departamento>> buscarPorFiltro(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @PageableDefault(size = 10) Pageable pageable) {

        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(departamentoService.buscarPorNome(nome, pageable));
        }

        if (descricao != null && !descricao.isBlank()) {
            return ResponseEntity.ok(departamentoService.buscarPorDescricao(descricao, pageable));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<Departamento> criar(@RequestBody @Valid Departamento departamento) {
        Departamento criado = departamentoService.criar(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> atualizar(@PathVariable Long id,
                                                  @RequestBody @Valid Departamento departamento) {
        return ResponseEntity.ok(departamentoService.atualizar(id, departamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        departamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

