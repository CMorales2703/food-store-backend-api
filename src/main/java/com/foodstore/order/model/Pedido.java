package com.foodstore.order.model;

import com.foodstore.common.enums.Estado;
import com.foodstore.common.enums.FormaPago;
import com.foodstore.common.interfaces.Calculable;
import com.foodstore.common.model.BaseEntity;
import com.foodstore.user.model.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad Pedido. Implementa Calculable para calcular el total sumando los subtotales
 * de cada DetallePedido.
 *
 * HU-017: POST   /api/orders              → 201 Created
 * HU-018: GET    /api/orders              → 200 OK
 * HU-019: GET    /api/orders/{id}         → 200 OK / 404
 * HU-020: GET    /api/orders/user/{id}    → 200 OK / 404
 * HU-021: PUT    /api/orders/{id}         → 200 OK / 404
 * HU-022: DELETE /api/orders/{id}         → 204 No Content / 404
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido extends BaseEntity implements Calculable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles = new ArrayList<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.PENDIENTE;

    @NotNull(message = "La forma de pago es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FormaPago formaPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    /**
     * Implementación de Calculable: suma precioUnitario × cantidad de cada detalle.
     * Se llama en el servicio antes de persistir para fijar el total (RN-017-04).
     */
    @Override
    public BigDecimal calcularTotal() {
        return detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
