package com.foodstore.category.controller;

import com.foodstore.category.dto.CategoriaCreate;
import com.foodstore.category.dto.CategoriaDto;
import com.foodstore.category.dto.CategoriaEdit;
import com.foodstore.category.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de Categorías.
 *
 * HU-001: POST   /api/categories        → 201 Created
 * HU-002: GET    /api/categories        → 200 OK (lista)
 * HU-003: GET    /api/categories/{id}   → 200 OK / 404
 * HU-004: PUT    /api/categories/{id}   → 200 OK / 404
 * HU-005: DELETE /api/categories/{id}   → 204 No Content / 404
 */
@Tag(name = "Categorías", description = "Gestión de categorías de productos (HU-001 a HU-005)")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría. El nombre debe ser único y tener entre 2 y 100 caracteres. (HU-001)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado")
    })
    @PostMapping
    public ResponseEntity<CategoriaDto> create(@Valid @RequestBody CategoriaCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(dto));
    }

    @Operation(summary = "Listar categorías", description = "Devuelve todas las categorías activas (no eliminadas). (HU-002)")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    @GetMapping
    public ResponseEntity<List<CategoriaDto>> findAll() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @Operation(summary = "Obtener categoría por ID", description = "Busca una categoría por su ID. Retorna 404 si no existe o fue eliminada. (HU-003)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @Operation(summary = "Actualizar categoría", description = "Actualización parcial: solo se modifican los campos enviados (no nulos). (HU-004)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaEdit dto) {
        return ResponseEntity.ok(categoriaService.update(id, dto));
    }

    @Operation(summary = "Eliminar categoría", description = "Soft delete: marca la categoría como eliminada sin borrarla físicamente. (HU-005)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoría eliminada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
