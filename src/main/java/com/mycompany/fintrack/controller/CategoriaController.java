package com.mycompany.fintrack.controller;

import com.mycompany.fintrack.dto.CategoriaDTO;
import com.mycompany.fintrack.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios/{usuarioId}/categorias")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Categorias", description = "Gerenciamento de categorias do usuário")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Lista categorias do usuário")
    public List<CategoriaDTO> listar(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Cria nova categoria")
    public CategoriaDTO criar(@PathVariable Long usuarioId,
                              @Valid @RequestBody CategoriaDTO dto) {
        return service.criar(usuarioId, dto);
    }

    @PutMapping("/{categoriaId}")
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Atualiza uma categoria existente")
    public CategoriaDTO atualizar(@PathVariable Long usuarioId,
                                  @PathVariable Long categoriaId,
                                  @Valid @RequestBody CategoriaDTO dto) {
        return service.atualizar(usuarioId, categoriaId, dto);
    }

    @DeleteMapping("/{categoriaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#usuarioId == authentication.principal.id")
    @Operation(summary = "Remove categoria")
    public void deletar(@PathVariable Long usuarioId,
                        @PathVariable Long categoriaId) {
        service.deletar(usuarioId, categoriaId);
    }
}