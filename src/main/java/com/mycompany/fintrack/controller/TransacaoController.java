package com.mycompany.fintrack.controller;

import com.mycompany.fintrack.dto.SaldoDTO;
import com.mycompany.fintrack.dto.TransacaoDTO;
import com.mycompany.fintrack.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios/{usuarioId}/transacoes")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Transações", description = "Gerenciamento de transações do usuário")
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    // GET com filtros opcionais (data e categoria)
    @GetMapping
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Lista transações com filtros opcionais por período e categoria")
    public List<TransacaoDTO> listar(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Long categoriaId) {

        if (categoriaId != null) {
            return service.listarPorCategoria(usuarioId, categoriaId);
        }
        if (dataInicio != null && dataFim != null) {
            return service.listarPorPeriodo(usuarioId, dataInicio, dataFim);
        }
        return service.listarPorUsuario(usuarioId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Cria nova transação")
    public TransacaoDTO criar(@PathVariable Long usuarioId,
                              @Valid @RequestBody TransacaoDTO dto) {
        return service.criar(usuarioId, dto);
    }

    // NOVO: PUT para atualizar
    @PutMapping("/{transacaoId}")
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Atualiza uma transação existente")
    public TransacaoDTO atualizar(@PathVariable Long usuarioId,
                                  @PathVariable Long transacaoId,
                                  @Valid @RequestBody TransacaoDTO dto) {
        return service.atualizar(usuarioId, transacaoId, dto);
    }

    @DeleteMapping("/{transacaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Remove transação")
    public void deletar(@PathVariable Long usuarioId,
                        @PathVariable Long transacaoId) {
        service.deletar(usuarioId, transacaoId);
    }

    @GetMapping("/saldo")
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Retorna o saldo atual do usuário")
    public SaldoDTO saldo(@PathVariable Long usuarioId) {
        return service.calcularSaldo(usuarioId);
    }
}