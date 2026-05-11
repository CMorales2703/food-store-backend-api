package com.foodstore.category.dto;

import com.foodstore.category.model.Categoria;

/**
 * DTO de respuesta para Categoria. Usa Java Record (inmutable).
 * Nunca expone campos de auditoría ni el campo 'eliminado'.
 */
public record CategoriaDto(Long id, String nombre, String descripcion) {

    /**
     * Factory method para convertir una entidad Categoria en su DTO de respuesta.
     */
    public static CategoriaDto from(Categoria categoria) {
        return new CategoriaDto(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}
