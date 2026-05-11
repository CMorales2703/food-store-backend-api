package com.foodstore.product.controller;

import com.foodstore.product.dto.ProductoCreate;
import com.foodstore.product.dto.ProductoDto;
import com.foodstore.product.dto.ProductoEdit;
import com.foodstore.product.service.ProductoService;
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
 * Controlador REST para la gestión de Productos.
 *
 * HU-011: POST   /api/products                 → 201 Created
 * HU-012: GET    /api/products                 → 200 OK (lista con categoría anidada)
 * HU-013: GET    /api/products/{id}            → 200 OK / 404
 * HU-014: GET    /api/products/category/{id}   → 200 OK / 404
 * HU-015: PUT    /api/products/{id}            → 200 OK / 404
 * HU-016: DELETE /api/products/{id}            → 204 No Content / 404
 */
@Tag(name = "Productos", description = "Gestión de productos con relación a categorías (HU-011 a HU-016)")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Crear producto", description = "Crea un nuevo producto asociado a una categoría existente. Si 'disponible' no se envía, se asigna true por defecto. (HU-011)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PostMapping
    public ResponseEntity<ProductoDto> create(@Valid @RequestBody ProductoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.save(dto));
    }

    @Operation(summary = "Listar productos", description = "Devuelve todos los productos activos con su categoría anidada. (HU-012)")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping
    public ResponseEntity<List<ProductoDto>> findAll() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @Operation(summary = "Listar productos por categoría", description = "Filtra productos activos por categoría. Retorna 404 si la categoría no existe. (HU-014)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos de la categoría"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductoDto>> findByCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findByCategoria(id));
    }

    @Operation(summary = "Obtener producto por ID", description = "Busca un producto por su ID. Retorna 404 si no existe o fue eliminado. (HU-013)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    @Operation(summary = "Actualizar producto", description = "Actualización parcial: solo se modifican los campos enviados. Se puede cambiar de categoría. (HU-015)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoEdit dto) {
        return ResponseEntity.ok(productoService.update(id, dto));
    }

    @Operation(summary = "Eliminar producto", description = "Soft delete: marca el producto como eliminado. Los pedidos históricos conservan la referencia. (HU-016)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
