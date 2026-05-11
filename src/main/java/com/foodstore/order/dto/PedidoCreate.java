package com.foodstore.order.dto;

import com.foodstore.common.enums.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para crear un Pedido.
 *
 * HU-017: POST /api/orders → 201 Created
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCreate {

    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "La forma de pago es obligatoria")
    private FormaPago formaPago;

    @NotNull(message = "Los detalles son obligatorios")
    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<DetallePedidoCreate> detalles;
}
