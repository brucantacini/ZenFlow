package com.example.ZenFlow.controller;

import com.example.ZenFlow.dto.NivelEstresseRequestDTO;
import com.example.ZenFlow.dto.NivelEstresseResponseDTO;
import com.example.ZenFlow.dto.mapper.NivelEstresseMapper;
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
    private final NivelEstresseMapper nivelEstresseMapper;

    @GetMapping
    public ResponseEntity<Page<NivelEstresseResponseDTO>> listar(@PageableDefault(size = 10) Pageable pageable) {
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
            @PageableDefault(size = 10) Pageable pageable) {
        Page<NivelEstresse> niveis = nivelEstresseService.buscarPorDescricao(descricao, pageable);
        Page<NivelEstresseResponseDTO> dtos = niveis.map(nivelEstresseMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

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

