package com.example.ZenFlow.controller;

import com.example.ZenFlow.dto.NivelEstresseRequestDTO;
import com.example.ZenFlow.dto.NivelEstresseResponseDTO;
import com.example.ZenFlow.dto.mapper.NivelEstresseMapper;
import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.service.NivelEstresseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/niveis-estresse")
@RequiredArgsConstructor
@Validated
@Tag(name = "Níveis de Estresse", description = "API para gerenciamento de níveis de estresse")
public class NivelEstresseController {

    private final NivelEstresseService nivelEstresseService;
    private final NivelEstresseMapper nivelEstresseMapper;

    @Operation(summary = "Listar níveis de estresse", description = "Retorna uma lista paginada de todos os níveis de estresse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<NivelEstresseResponseDTO>> listar(
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NivelEstresse> niveis = nivelEstresseService.listar(pageable);
        Page<NivelEstresseResponseDTO> dtos = niveis.map(nivelEstresseMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelEstresseResponseDTO> buscarPorId(@PathVariable Long id) {
        return nivelEstresseService.buscarPorId(id)
                .map(nivelEstresseMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<NivelEstresseResponseDTO> buscarPorNivel(@PathVariable Integer nivel) {
        return nivelEstresseService.buscarPorNivel(nivel)
                .map(nivelEstresseMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<NivelEstresseResponseDTO>> buscarPorDescricao(
            @RequestParam String descricao,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NivelEstresse> niveis = nivelEstresseService.buscarPorDescricao(descricao, pageable);
        Page<NivelEstresseResponseDTO> dtos = niveis.map(nivelEstresseMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Criar nível de estresse", description = "Cria um novo nível de estresse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nível de estresse criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<NivelEstresseResponseDTO> criar(@RequestBody @Valid NivelEstresseRequestDTO dto) {
        NivelEstresse nivelEstresse = nivelEstresseMapper.toEntity(dto);
        NivelEstresse criado = nivelEstresseService.criar(nivelEstresse);
        return ResponseEntity.status(HttpStatus.CREATED).body(nivelEstresseMapper.toResponseDTO(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NivelEstresseResponseDTO> atualizar(@PathVariable Long id,
                                                              @RequestBody @Valid NivelEstresseRequestDTO dto) {
        NivelEstresse nivelEstresse = nivelEstresseMapper.toEntity(dto);
        NivelEstresse atualizado = nivelEstresseService.atualizar(id, nivelEstresse);
        return ResponseEntity.ok(nivelEstresseMapper.toResponseDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        nivelEstresseService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

