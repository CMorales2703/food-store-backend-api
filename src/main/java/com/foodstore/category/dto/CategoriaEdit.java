package com.foodstore.category.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para actualizar una Categoria (actualización parcial).
 * Todos los campos son opcionales: solo se actualizan los que llegan no-nulos.
 * Sin @NotBlank — un campo ausente (null) simplemente no modifica el valor existente.
 *
 * HU-004: Actualizar Categoría
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaEdit {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
}
