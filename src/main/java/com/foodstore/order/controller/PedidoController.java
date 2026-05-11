package com.foodstore.order.controller;

import com.foodstore.order.dto.PedidoCreate;
import com.foodstore.order.dto.PedidoDto;
import com.foodstore.order.dto.PedidoEdit;
import com.foodstore.order.service.PedidoService;
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
 * Controlador REST para la gestión de Pedidos.
 *
 * HU-017: POST   /api/orders              → 201 Created
 * HU-018: GET    /api/orders              → 200 OK
 * HU-019: GET    /api/orders/{id}         → 200 OK / 404
 * HU-020: GET    /api/orders/user/{id}    → 200 OK / 404
 * HU-021: PUT    /api/orders/{id}         → 200 OK / 404
 * HU-022: DELETE /api/orders/{id}         → 204 No Content / 404
 */
@Tag(name = "Pedidos", description = "Gestión de pedidos con detalle de productos y control de stock (HU-017 a HU-022)")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Crear pedido", description = "Crea un pedido con sus detalles de forma transaccional. Valida stock y disponibilidad de cada producto. Reduce el stock automáticamente. Hace rollback si alguna validación falla. (HU-017)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Stock insuficiente o producto no disponible"),
        @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado")
    })
    @PostMapping
    public ResponseEntity<PedidoDto> create(@Valid @RequestBody PedidoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(dto));
    }

    @Operation(summary = "Listar pedidos", description = "Devuelve todos los pedidos activos con usuario y detalles anidados. (HU-018)")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos")
    @GetMapping
    public ResponseEntity<List<PedidoDto>> findAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @Operation(summary = "Listar pedidos por usuario", description = "Filtra pedidos activos de un usuario. Retorna 404 si el usuario no existe. (HU-020)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pedidos del usuario"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<PedidoDto>> findByUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.findByUsuario(id));
    }

    @Operation(summary = "Obtener pedido por ID", description = "Busca un pedido por su ID con todos sus detalles. Retorna 404 si no existe o fue eliminado. (HU-019)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    @Operation(summary = "Actualizar pedido", description = "Actualización parcial del estado y/o forma de pago. No modifica los detalles del pedido. (HU-021)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PedidoEdit dto) {
        return ResponseEntity.ok(pedidoService.update(id, dto));
    }

    @Operation(summary = "Eliminar pedido", description = "Soft delete: marca el pedido como eliminado. Los detalles históricos se conservan. (HU-022)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido eliminado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
