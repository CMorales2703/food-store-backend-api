package com.foodstore.order.dto;

import com.foodstore.common.enums.Estado;
import com.foodstore.common.enums.FormaPago;
import com.foodstore.order.model.Pedido;
import com.foodstore.user.dto.UsuarioDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para Pedido. Usa Java Record (inmutable).
 * Incluye el usuario anidado y la lista completa de detalles.
 */
public record PedidoDto(
        Long id,
        UsuarioDto usuario,
        List<DetallePedidoDto> detalles,
        Estado estado,
        FormaPago formaPago,
        BigDecimal total,
        LocalDateTime createdAt
) {
    public static PedidoDto from(Pedido pedido) {
        return new PedidoDto(
                pedido.getId(),
                UsuarioDto.from(pedido.getUsuario()),
                pedido.getDetalles().stream()
                        .map(DetallePedidoDto::from)
                        .toList(),
                pedido.getEstado(),
                pedido.getFormaPago(),
                pedido.getTotal(),
                pedido.getCreatedAt()
        );
    }
}
