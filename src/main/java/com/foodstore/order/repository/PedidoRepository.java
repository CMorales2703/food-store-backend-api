package com.foodstore.order.repository;

import com.foodstore.common.repository.BaseRepository;
import com.foodstore.order.model.Pedido;
import java.util.List;

/**
 * Repositorio para Pedido.
 * Hereda soft delete y findByIdOrThrow() de BaseRepository.
 *
 * HU-020: findAllByUsuarioIdAndEliminadoFalse → GET /api/orders/user/{id}
 */
public interface PedidoRepository extends BaseRepository<Pedido, Long> {

    List<Pedido> findAllByUsuarioIdAndEliminadoFalse(Long usuarioId);
}
