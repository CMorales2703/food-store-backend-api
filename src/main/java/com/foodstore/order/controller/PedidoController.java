package com.foodstore.order.controller;

import com.foodstore.order.dto.PedidoCreate;
import com.foodstore.order.dto.PedidoDto;
import com.foodstore.order.dto.PedidoEdit;
import com.foodstore.order.service.PedidoService;
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
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoDto> create(@Valid @RequestBody PedidoCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<PedidoDto>> findAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    // IMPORTANTE: /user/{id} va ANTES de /{id} para que Spring no confunda "user" con un ID.
    @GetMapping("/user/{id}")
    public ResponseEntity<List<PedidoDto>> findByUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.findByUsuario(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PedidoEdit dto) {
        return ResponseEntity.ok(pedidoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
