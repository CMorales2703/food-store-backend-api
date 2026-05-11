package com.foodstore.user.dto;

import com.foodstore.common.enums.Rol;
import com.foodstore.user.model.Usuario;

/**
 * DTO de respuesta para Usuario. Usa Java Record (inmutable).
 * NUNCA incluye el campo password (RN-006-08).
 * El campo email se expone como "mail" para coincidir con el frontend.
 */
public record UsuarioDto(Long id, String nombre, String apellido, String mail, String celular, Rol rol) {

    public static UsuarioDto from(Usuario usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getRol()
        );
    }
}
