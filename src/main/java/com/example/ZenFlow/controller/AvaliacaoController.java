package com.example.ZenFlow.controller;

import com.example.ZenFlow.dto.AvaliacaoRequestDTO;
import com.example.ZenFlow.dto.AvaliacaoResponseDTO;
import com.example.ZenFlow.dto.mapper.AvaliacaoMapper;
import com.example.ZenFlow.entity.Avaliacao;
import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.exception.EntityNotFoundException;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Avaliações", description = "API para gerenciamento de avaliações de bem-estar")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    private final AvaliacaoMapper avaliacaoMapper;
    private final DepartamentoRepository departamentoRepository;
    private final NivelEstresseRepository nivelEstresseRepository;

    @Operation(summary = "Listar avaliações", description = "Retorna uma lista paginada de todas as avaliações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<AvaliacaoResponseDTO>> listar(@PageableDefault(size = 10) Pageable pageable) {
        Page<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacoes(pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar avaliação por ID", description = "Retorna uma avaliação específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação encontrada"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarPorId(
            @Parameter(description = "ID da avaliação") @PathVariable Long id) {
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
    
    /**
     * Calcula média semanal usando a função FN_CALCULAR_MEDIA_SEMANAL
     */
    @GetMapping("/departamento/{idDepto}/media-semanal")
    public ResponseEntity<Double> calcularMediaSemanal(
            @PathVariable Long idDepto,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return ResponseEntity.ok(avaliacaoService.calcularMediaSemanalViaFunction(idDepto, dataInicio, dataFim));
    }
    
    /**
     * Valida nível de estresse usando a função FN_VALIDAR_NIVEL_ESTRESSE
     */
    @GetMapping("/validar-nivel/{nivel}")
    public ResponseEntity<String> validarNivelEstresse(@PathVariable Integer nivel) {
        return ResponseEntity.ok(avaliacaoService.validarNivelEstresseViaFunction(nivel));
    }
    
    /**
     * Conta registros em período usando a função FN_CONTAR_REGISTROS_PERIODO
     */
    @GetMapping("/departamento/{idDepto}/contar-periodo")
    public ResponseEntity<Long> contarRegistrosPeriodo(
            @PathVariable Long idDepto,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return ResponseEntity.ok(avaliacaoService.contarRegistrosPeriodoViaFunction(idDepto, dataInicio, dataFim));
    }

    @Operation(summary = "Criar avaliação", description = "Cria uma nova avaliação de bem-estar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso",
                    content = @Content(schema = @Schema(implementation = AvaliacaoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criar(@RequestBody @Valid AvaliacaoRequestDTO dto) {
        Departamento departamento = departamentoRepository.findById(dto.getIdDepartamento())
                .orElseThrow(() -> new EntityNotFoundException("Departamento", dto.getIdDepartamento()));
        
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(dto.getIdNivelEstresse())
                .orElseThrow(() -> new EntityNotFoundException("Nível de estresse", dto.getIdNivelEstresse()));
        
        Avaliacao avaliacao = avaliacaoMapper.toEntity(dto, departamento, nivelEstresse);
        Avaliacao criada = avaliacaoService.criarAvaliacao(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoMapper.toResponseDTO(criada));
    }
    
    @Operation(summary = "Criar avaliação via Procedure", 
               description = "Cria uma nova avaliação usando a procedure Oracle PC_INSERIR_AVALIACAO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso via procedure"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/procedure")
    public ResponseEntity<AvaliacaoResponseDTO> criarViaProcedure(@RequestBody @Valid AvaliacaoRequestDTO dto) {
        Departamento departamento = departamentoRepository.findById(dto.getIdDepartamento())
                .orElseThrow(() -> new EntityNotFoundException("Departamento", dto.getIdDepartamento()));
        
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(dto.getIdNivelEstresse())
                .orElseThrow(() -> new EntityNotFoundException("Nível de estresse", dto.getIdNivelEstresse()));
        
        Avaliacao avaliacao = avaliacaoMapper.toEntity(dto, departamento, nivelEstresse);
        Avaliacao criada = avaliacaoService.criarAvaliacaoViaProcedure(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoMapper.toResponseDTO(criada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> atualizar(@PathVariable Long id,
                                                          @RequestBody @Valid AvaliacaoRequestDTO dto) {
        Departamento departamento = departamentoRepository.findById(dto.getIdDepartamento())
                .orElseThrow(() -> new EntityNotFoundException("Departamento", dto.getIdDepartamento()));
        
        NivelEstresse nivelEstresse = nivelEstresseRepository.findById(dto.getIdNivelEstresse())
                .orElseThrow(() -> new EntityNotFoundException("Nível de estresse", dto.getIdNivelEstresse()));
        
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

