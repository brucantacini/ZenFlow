package com.example.ZenFlow.controller;

import com.example.ZenFlow.dto.DepartamentoRequestDTO;
import com.example.ZenFlow.dto.DepartamentoResponseDTO;
import com.example.ZenFlow.dto.mapper.DepartamentoMapper;
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
    private final DepartamentoMapper departamentoMapper;

    @GetMapping
    public ResponseEntity<Page<DepartamentoResponseDTO>> listar(@PageableDefault(size = 10) Pageable pageable) {
        Page<Departamento> departamentos = departamentoService.listar(pageable);
        Page<DepartamentoResponseDTO> dtos = departamentos.map(departamentoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return departamentoService.buscarPorId(id)
                .map(departamentoMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<DepartamentoResponseDTO>> buscarPorFiltro(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<Departamento> departamentos;
        
        if (nome != null && !nome.isBlank()) {
            departamentos = departamentoService.buscarPorNome(nome, pageable);
        } else if (descricao != null && !descricao.isBlank()) {
            departamentos = departamentoService.buscarPorDescricao(descricao, pageable);
        } else {
            return ResponseEntity.badRequest().build();
        }

        Page<DepartamentoResponseDTO> dtos = departamentos.map(departamentoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponseDTO> criar(@RequestBody @Valid DepartamentoRequestDTO dto) {
        Departamento departamento = departamentoMapper.toEntity(dto);
        Departamento criado = departamentoService.criar(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoMapper.toResponseDTO(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> atualizar(@PathVariable Long id,
                                                              @RequestBody @Valid DepartamentoRequestDTO dto) {
        Departamento departamento = departamentoMapper.toEntity(dto);
        Departamento atualizado = departamentoService.atualizar(id, departamento);
        return ResponseEntity.ok(departamentoMapper.toResponseDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        departamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

