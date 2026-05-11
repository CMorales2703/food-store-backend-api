package com.foodstore.order.service;

import com.foodstore.common.exception.BusinessException;
import com.foodstore.order.dto.DetallePedidoCreate;
import com.foodstore.order.dto.PedidoCreate;
import com.foodstore.order.dto.PedidoDto;
import com.foodstore.order.dto.PedidoEdit;
import com.foodstore.order.model.DetallePedido;
import com.foodstore.order.model.Pedido;
import com.foodstore.order.repository.PedidoRepository;
import com.foodstore.product.model.Producto;
import com.foodstore.product.repository.ProductoRepository;
import com.foodstore.user.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para Pedidos.
 *
 * HU-017: Crear Pedido (transaccional: reduce stock, rollback si falla)
 * HU-018: Listar Pedidos activos
 * HU-019: Obtener Pedido por ID
 * HU-020: Listar Pedidos por Usuario
 * HU-021: Actualizar estado/formaPago (parcial)
 * HU-022: Eliminar Pedido (soft delete)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    /**
     * Crea un nuevo pedido con sus detalles.
     *
     * Flujo transaccional (RN-017):
     * 1. Valida que el usuario exista.
     * 2. Por cada detalle: valida producto, disponibilidad y stock suficiente.
     * 3. Reduce stock de cada producto (reducirStock).
     * 4. Calcula el total con calcularTotal() (implementación de Calculable).
     * 5. Persiste el pedido con todos sus detalles (cascade).
     *
     * Si cualquier validación falla → @Transactional hace rollback automático
     * y el stock NO queda modificado (RN-017-02).
     *
     * HU-017 → POST /api/orders → 201 Created
     */
    @Transactional
    public PedidoDto save(PedidoCreate dto) {
        log.info("Creando pedido para usuario id: {}", dto.getIdUsuario());

        var usuario = usuarioRepository.findByIdOrThrow(dto.getIdUsuario());

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .formaPago(dto.getFormaPago())
                .build();

        for (DetallePedidoCreate dc : dto.getDetalles()) {
            Producto producto = productoRepository.findByIdOrThrow(dc.getIdProducto());

            // RN-017-05: el producto debe estar disponible
            if (!Boolean.TRUE.equals(producto.getDisponible())) {
                throw new BusinessException(
                        "El producto '" + producto.getNombre() + "' no está disponible");
            }

            // RN-017-06: debe haber stock suficiente
            if (!producto.tieneStockSuficiente(dc.getCantidad())) {
                throw new BusinessException(
                        "Stock insuficiente para '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock()
                        + ", solicitado: " + dc.getCantidad());
            }

            // Reduce stock (el flush al final del @Transactional lo persiste)
            producto.reducirStock(dc.getCantidad());

            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .cantidad(dc.getCantidad())
                    .precioUnitario(producto.getPrecio()) // snapshot del precio actual
                    .build();

            pedido.getDetalles().add(detalle);
        }

        // Calcula y fija el total antes de persistir (RN-017-04)
        pedido.setTotal(pedido.calcularTotal());

        return PedidoDto.from(pedidoRepository.save(pedido));
    }

    /**
     * Devuelve todos los pedidos activos (no eliminados).
     * HU-018 → GET /api/orders → 200 OK
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> findAll() {
        log.info("Listando todos los pedidos activos");
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoDto::from)
                .toList();
    }

    /**
     * Obtiene un pedido por ID. Lanza 404 si no existe o fue eliminado.
     * HU-019 → GET /api/orders/{id} → 200 OK / 404
     */
    @Transactional(readOnly = true)
    public PedidoDto findById(Long id) {
        log.info("Buscando pedido con id: {}", id);
        return PedidoDto.from(pedidoRepository.findByIdOrThrow(id));
    }

    /**
     * Filtra pedidos activos por usuario.
     * Valida que el usuario exista antes de buscar (RN-020-01).
     * HU-020 → GET /api/orders/user/{id} → 200 OK / 404
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> findByUsuario(Long usuarioId) {
        log.info("Buscando pedidos del usuario id: {}", usuarioId);
        usuarioRepository.findByIdOrThrow(usuarioId); // lanza 404 si no existe
        return pedidoRepository.findAllByUsuarioIdAndEliminadoFalse(usuarioId)
                .stream()
                .map(PedidoDto::from)
                .toList();
    }

    /**
     * Actualización parcial: solo modifica estado y/o formaPago si llegan no-nulos.
     * HU-021 → PUT /api/orders/{id} → 200 OK / 404
     */
    @Transactional
    public PedidoDto update(Long id, PedidoEdit dto) {
        log.info("Actualizando pedido con id: {}", id);
        Pedido pedido = pedidoRepository.findByIdOrThrow(id);

        if (dto.getEstado() != null) {
            pedido.setEstado(dto.getEstado());
        }
        if (dto.getFormaPago() != null) {
            pedido.setFormaPago(dto.getFormaPago());
        }

        return PedidoDto.from(pedidoRepository.save(pedido));
    }

    /**
     * Soft delete: marca el pedido como eliminado.
     * Los detalles históricos se conservan (RN-022-02).
     * HU-022 → DELETE /api/orders/{id} → 204 No Content / 404
     */
    @Transactional
    public void deleteById(Long id) {
        log.info("Eliminando (soft delete) pedido con id: {}", id);
        pedidoRepository.deleteById(id);
    }
}
