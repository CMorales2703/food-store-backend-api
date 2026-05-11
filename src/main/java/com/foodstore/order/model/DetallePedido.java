package com.foodstore.order.model;

import com.foodstore.common.model.BaseEntity;
import com.foodstore.product.model.Producto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Línea de detalle dentro de un Pedido.
 * Guarda snapshot del precio al momento de la compra (RN-017-03).
 *
 * HU-017: asociado a creación de Pedido
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalles_pedido")
public class DetallePedido extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer cantidad;

    /**
     * Precio unitario en el momento de la compra.
     * Inmutable: no cambia aunque el producto cambie de precio después.
     */
    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    /** Subtotal calculado: precioUnitario × cantidad */
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
