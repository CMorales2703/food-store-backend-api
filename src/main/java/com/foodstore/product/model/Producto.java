package com.foodstore.product.model;

import com.foodstore.category.model.Categoria;
import com.foodstore.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad que representa un producto del catálogo.
 * Relación ManyToOne con Categoria (LAZY para evitar el problema N+1).
 *
 * HU-011 a HU-016: Gestión de Productos
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto extends BaseEntity {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Column(length = 500)
    private String imagen;

    @Builder.Default
    @Column(nullable = false)
    private Boolean disponible = true;

    // FK: categoria_id — LAZY evita cargar la categoría hasta que se necesite
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // ─── Métodos de negocio usados en la creación de Pedidos (HU-017) ─────────

    /**
     * Verifica si hay stock suficiente para la cantidad solicitada.
     */
    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock >= cantidad;
    }

    /**
     * Reduce el stock en la cantidad indicada.
     * Llamar solo después de verificar con tieneStockSuficiente().
     */
    public void reducirStock(int cantidad) {
        this.stock -= cantidad;
    }
}
