package com.foodstore.order.dto;

import com.foodstore.order.model.DetallePedido;
import com.foodstore.product.dto.ProductoDto;
import java.math.BigDecimal;

/**
 * DTO de respuesta para DetallePedido. Incluye el producto anidado y el subtotal calculado.
 */
public record DetallePedidoDto(
        Long id,
        ProductoDto producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
    public static DetallePedidoDto from(DetallePedido detalle) {
        return new DetallePedidoDto(
                detalle.getId(),
                ProductoDto.from(detalle.getProducto()),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
