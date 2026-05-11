package com.foodstore.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para el endpoint de login.
 * Usa Java Record (inmutable).
 */
public record LoginRequest(
        @NotBlank(message = "El email es obligatorio") String email,
        @NotBlank(message = "La contraseña es obligatoria") String password
) {}
