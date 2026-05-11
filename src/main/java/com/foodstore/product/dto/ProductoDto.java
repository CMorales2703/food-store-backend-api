package com.foodstore.product.dto;

import com.foodstore.category.dto.CategoriaDto;
import com.foodstore.product.model.Producto;
import java.math.BigDecimal;

/**
 * DTO de respuesta para Producto. Usa Java Record (inmutable).
 * Incluye la categoría anidada como CategoriaDto.
 */
public record ProductoDto(
        Long id,
        String nombre,
        BigDecimal precio,
        String descripcion,
        Integer stock,
        String imagen,
        Boolean disponible,
        CategoriaDto categoria
) {
    public static ProductoDto from(Producto producto) {
        return new ProductoDto(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getImagen(),
                producto.getDisponible(),
                CategoriaDto.from(producto.getCategoria())
        );
    }
}
