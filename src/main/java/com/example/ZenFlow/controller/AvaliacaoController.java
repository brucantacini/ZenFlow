package com.example.ZenFlow.controller;

import com.example.ZenFlow.dto.AvaliacaoRequestDTO;
import com.example.ZenFlow.dto.AvaliacaoResponseDTO;
import com.example.ZenFlow.dto.mapper.AvaliacaoMapper;
import com.example.ZenFlow.entity.Avaliacao;
import com.example.ZenFlow.entity.Departamento;
import com.example.ZenFlow.entity.NivelEstresse;
import com.example.ZenFlow.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Avaliações", description = "API para gerenciamento de avaliações de bem-estar")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    private final AvaliacaoMapper avaliacaoMapper;

    @Operation(summary = "Listar avaliações", description = "Retorna uma lista paginada de todas as avaliações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<AvaliacaoResponseDTO>> listar(
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
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

    @Operation(summary = "Buscar avaliações por departamento", description = "Retorna avaliações de um departamento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/departamento/{idDepto}")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> buscarPorDepartamento(
            @Parameter(description = "ID do departamento") @PathVariable Long idDepto,
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Avaliacao> avaliacoes = avaliacaoService.buscarPorDepartamento(idDepto, pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar avaliações por nível de estresse", description = "Retorna avaliações com um nível de estresse específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> buscarPorNivel(
            @Parameter(description = "Nível de estresse (1 a 5)") @PathVariable Integer nivel,
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Avaliacao> avaliacoes = avaliacaoService.buscarPorNivelEstresse(nivel, pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar avaliações por período", description = "Retorna avaliações entre as datas informadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/periodo")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> buscarPorPeriodo(
            @Parameter(description = "Data de início (formato: yyyy-MM-dd)", example = "2025-11-10", required = true)
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-dd)", example = "2025-11-18", required = true)
            @RequestParam("fim") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim,
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);
        Pageable pageable = PageRequest.of(page, size);
        Page<Avaliacao> avaliacoes = avaliacaoService.buscarPorPeriodo(inicioDateTime, fimDateTime, pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar avaliações por departamento e período", description = "Retorna avaliações de um departamento entre as datas informadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/departamento/{idDepto}/periodo")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> buscarPorDepartamentoEPeriodo(
            @Parameter(description = "ID do departamento") @PathVariable Long idDepto,
            @Parameter(description = "Data de início (formato: yyyy-MM-dd)", example = "2025-11-10", required = true)
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-dd)", example = "2025-11-18", required = true)
            @RequestParam("fim") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim,
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);
        Pageable pageable = PageRequest.of(page, size);
        Page<Avaliacao> avaliacoes = avaliacaoService.buscarPorDepartamentoEPeriodo(idDepto, inicioDateTime, fimDateTime, pageable);
        Page<AvaliacaoResponseDTO> dtos = avaliacoes.map(avaliacaoMapper::toResponseDTO);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Calcular média de estresse por departamento", description = "Calcula a média de estresse de um departamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Média calculada com sucesso")
    })
    @GetMapping("/departamento/{idDepto}/media")
    public ResponseEntity<Double> calcularMedia(
            @Parameter(description = "ID do departamento") @PathVariable Long idDepto) {
        return ResponseEntity.ok(avaliacaoService.calcularMediaEstressePorDepartamento(idDepto));
    }

    @Operation(summary = "Contar avaliações por departamento", description = "Retorna o total de avaliações de um departamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contagem realizada com sucesso")
    })
    @GetMapping("/departamento/{idDepto}/total")
    public ResponseEntity<Long> contarPorDepartamento(
            @Parameter(description = "ID do departamento") @PathVariable Long idDepto) {
        return ResponseEntity.ok(avaliacaoService.contarAvaliacoesPorDepartamento(idDepto));
    }
    
    @Operation(summary = "Calcular média semanal de estresse", description = "Calcula a média de estresse de um departamento em um período usando função Oracle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Média calculada com sucesso")
    })
    @GetMapping("/departamento/{idDepto}/media-semanal")
    public ResponseEntity<Double> calcularMediaSemanal(
            @Parameter(description = "ID do departamento") @PathVariable Long idDepto,
            @Parameter(description = "Data de início (formato: yyyy-MM-dd)", example = "2025-11-10", required = true)
            @RequestParam("dataInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-dd)", example = "2025-11-18", required = true)
            @RequestParam("dataFim") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFim) {
        LocalDateTime inicioDateTime = dataInicio.atStartOfDay();
        LocalDateTime fimDateTime = dataFim.atTime(LocalTime.MAX);
        return ResponseEntity.ok(avaliacaoService.calcularMediaSemanalViaFunction(idDepto, inicioDateTime, fimDateTime));
    }
    
    @Operation(summary = "Validar nível de estresse", description = "Valida se um nível de estresse é válido usando função Oracle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validação realizada com sucesso")
    })
    @GetMapping("/validar-nivel/{nivel}")
    public ResponseEntity<String> validarNivelEstresse(
            @Parameter(description = "Nível de estresse a validar (1 a 5)") @PathVariable Integer nivel) {
        return ResponseEntity.ok(avaliacaoService.validarNivelEstresseViaFunction(nivel));
    }
    
    @Operation(summary = "Contar registros por período", description = "Conta avaliações de um departamento em um período usando função Oracle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contagem realizada com sucesso")
    })
    @GetMapping("/departamento/{idDepto}/contar-periodo")
    public ResponseEntity<Long> contarRegistrosPeriodo(
            @Parameter(description = "ID do departamento") @PathVariable Long idDepto,
            @Parameter(description = "Data de início (formato: yyyy-MM-dd)", example = "2025-11-10", required = true)
            @RequestParam("dataInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-dd)", example = "2025-11-18", required = true)
            @RequestParam("dataFim") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFim) {
        LocalDateTime inicioDateTime = dataInicio.atStartOfDay();
        LocalDateTime fimDateTime = dataFim.atTime(LocalTime.MAX);
        return ResponseEntity.ok(avaliacaoService.contarRegistrosPeriodoViaFunction(idDepto, inicioDateTime, fimDateTime));
    }

    @Operation(summary = "Criar avaliação", description = "Cria uma nova avaliação de bem-estar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criar(@RequestBody @Valid AvaliacaoRequestDTO dto) {
        Avaliacao criada = avaliacaoService.criarAvaliacao(
                dto.getIdDepartamento(),
                dto.getIdNivelEstresse(),
                dto.getComentario()
        );
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
        Avaliacao criada = avaliacaoService.criarAvaliacaoViaProcedure(
                dto.getIdDepartamento(),
                dto.getIdNivelEstresse(),
                dto.getComentario()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoMapper.toResponseDTO(criada));
    }

    @Operation(summary = "Atualizar avaliação", description = "Atualiza uma avaliação existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> atualizar(
            @Parameter(description = "ID da avaliação") @PathVariable Long id,
            @RequestBody @Valid AvaliacaoRequestDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        Departamento departamento = new Departamento();
        departamento.setIdDepto(dto.getIdDepartamento());
        avaliacao.setDepartamento(departamento);
        
        NivelEstresse nivelEstresse = new NivelEstresse();
        nivelEstresse.setIdNivelEstresse(dto.getIdNivelEstresse());
        avaliacao.setNivelEstresse(nivelEstresse);
        avaliacao.setComentario(dto.getComentario());
        
        Avaliacao atualizada = avaliacaoService.atualizarAvaliacao(id, avaliacao);
        return ResponseEntity.ok(avaliacaoMapper.toResponseDTO(atualizada));
    }

    @Operation(summary = "Deletar avaliação", description = "Remove uma avaliação do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avaliação deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da avaliação") @PathVariable Long id) {
        avaliacaoService.deletarAvaliacao(id);
        return ResponseEntity.noContent().build();
    }
}

