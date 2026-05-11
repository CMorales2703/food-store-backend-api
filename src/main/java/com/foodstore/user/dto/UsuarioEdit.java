package com.foodstore.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para actualizar un usuario (actualización parcial).
 * Todos los campos son opcionales — solo se actualizan los que llegan no-nulos.
 * Si se envía password, se re-encripta con BCrypt (RN-009-03).
 *
 * HU-009: Actualizar Usuario
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEdit {

    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Size(max = 20, message = "El celular no puede exceder 20 caracteres")
    private String celular;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}
