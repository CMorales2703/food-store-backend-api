package com.foodstore.auth.controller;

import com.foodstore.user.dto.LoginRequest;
import com.foodstore.user.dto.UsuarioCreate;
import com.foodstore.user.dto.UsuarioDto;
import com.foodstore.user.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticación. Maneja login y registro de usuarios.
 * No usa JWT — autenticación educativa basada en localStorage.
 *
 * POST /api/auth/register → 201 Created (HU-006)
 * POST /api/auth/login    → 200 OK con datos del usuario (sin password)
 */
@Tag(name = "Autenticación", description = "Registro y login de usuarios (HU-006)")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario con rol USUARIO. La contraseña se almacena encriptada con BCrypt. (HU-006)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioDto> register(@Valid @RequestBody UsuarioCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(dto));
    }

    @Operation(summary = "Login", description = "Autentica un usuario por email y contraseña. Devuelve los datos del usuario sin el campo password.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }
}
