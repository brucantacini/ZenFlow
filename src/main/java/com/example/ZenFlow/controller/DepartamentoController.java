package com.example.ZenFlow.controller;

import com.example.ZenFlow.dto.DepartamentoRequestDTO;
import com.example.ZenFlow.dto.DepartamentoResponseDTO;
import com.example.ZenFlow.dto.mapper.DepartamentoMapper;
import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.service.DepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Departamentos", description = "API para gerenciamento de departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;
    private final DepartamentoMapper departamentoMapper;

    @Operation(summary = "Listar departamentos", description = "Retorna uma lista paginada de todos os departamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<DepartamentoResponseDTO>> listar(@PageableDefault(size = 10) Pageable pageable) {
        Page<Departamento> departamentos = departamentoService.listar(pageable);
        Page<DepartamentoResponseDTO> dtos = departamentos.map(departamentoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar departamento por ID", description = "Retorna um departamento específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> buscarPorId(
            @Parameter(description = "ID do departamento") @PathVariable Long id) {
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

    @Operation(summary = "Criar departamento", description = "Cria um novo departamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Departamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
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

