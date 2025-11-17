package com.example.ZenFlow.controller;

import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.service.NivelEstresseService;
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
@RequestMapping("/api/niveis-estresse")
@RequiredArgsConstructor
@Validated
public class NivelEstresseController {

    private final NivelEstresseService nivelEstresseService;

    @GetMapping
    public ResponseEntity<Page<NivelEstresse>> listar(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(nivelEstresseService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelEstresse> buscarPorId(@PathVariable Long id) {
        return nivelEstresseService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<NivelEstresse> buscarPorNivel(@PathVariable Integer nivel) {
        return nivelEstresseService.buscarPorNivel(nivel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<NivelEstresse>> buscarPorDescricao(
            @RequestParam String descricao,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(nivelEstresseService.buscarPorDescricao(descricao, pageable));
    }

    @PostMapping
    public ResponseEntity<NivelEstresse> criar(@RequestBody @Valid NivelEstresse nivelEstresse) {
        NivelEstresse criado = nivelEstresseService.criar(nivelEstresse);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NivelEstresse> atualizar(@PathVariable Long id,
                                                   @RequestBody @Valid NivelEstresse nivelEstresse) {
        return ResponseEntity.ok(nivelEstresseService.atualizar(id, nivelEstresse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        nivelEstresseService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

