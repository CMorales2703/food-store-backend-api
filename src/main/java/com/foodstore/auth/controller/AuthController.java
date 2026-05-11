package com.foodstore.auth.controller;

import com.foodstore.user.dto.LoginRequest;
import com.foodstore.user.dto.UsuarioCreate;
import com.foodstore.user.dto.UsuarioDto;
import com.foodstore.user.service.UsuarioService;
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
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioDto> register(@Valid @RequestBody UsuarioCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }
}
