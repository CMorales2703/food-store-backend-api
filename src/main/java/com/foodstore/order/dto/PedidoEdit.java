package com.foodstore.order.dto;

import com.foodstore.common.enums.Estado;
import com.foodstore.common.enums.FormaPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para actualizar un Pedido (actualización parcial).
 * Solo se actualizan los campos que llegan no-nulos.
 *
 * HU-021: PUT /api/orders/{id}
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEdit {

    /** Nuevo estado del pedido (opcional) */
    private Estado estado;

    /** Nueva forma de pago (opcional) */
    private FormaPago formaPago;
}
