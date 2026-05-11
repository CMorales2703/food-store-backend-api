package com.foodstore.user.service;

import com.foodstore.common.enums.Rol;
import com.foodstore.common.exception.BusinessException;
import com.foodstore.common.security.PasswordService;
import com.foodstore.user.dto.LoginRequest;
import com.foodstore.user.dto.UsuarioCreate;
import com.foodstore.user.dto.UsuarioDto;
import com.foodstore.user.dto.UsuarioEdit;
import com.foodstore.user.model.Usuario;
import com.foodstore.user.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para Usuarios.
 *
 * HU-006: Registrar Usuario (email único, BCrypt, rol USUARIO por defecto)
 * HU-007: Listar Usuarios (sin password)
 * HU-008: Obtener Usuario por ID
 * HU-009: Actualizar Usuario (parcial, re-encripta password si se envía)
 * HU-010: Eliminar Usuario (soft delete)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;

    /**
     * Registra un nuevo usuario. El rol siempre es USUARIO.
     * Lanza BusinessException si el email ya está en uso (RN-006-03).
     * HU-006 → POST /api/auth/register → 201 Created
     */
    @Transactional
    public UsuarioDto save(UsuarioCreate dto) {
        log.info("Registrando nuevo usuario con email: {}", dto.getEmail());

        if (usuarioRepository.findByEmailAndEliminadoFalse(dto.getEmail()).isPresent()) {
            throw new BusinessException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .celular(dto.getCelular())
                .password(passwordService.encode(dto.getPassword()))
                .rol(Rol.USUARIO)
                .build();

        return UsuarioDto.from(usuarioRepository.save(usuario));
    }

    /**
     * Autentica un usuario por email y contraseña.
     * Lanza BusinessException con mensaje genérico para no revelar si el email existe.
     */
    @Transactional(readOnly = true)
    public UsuarioDto login(LoginRequest request) {
        log.info("Intento de login para email: {}", request.email());

        Usuario usuario = usuarioRepository
                .findByEmailAndEliminadoFalse(request.email())
                .orElseThrow(() -> new BusinessException("Credenciales incorrectas"));

        if (!passwordService.matches(request.password(), usuario.getPassword())) {
            throw new BusinessException("Credenciales incorrectas");
        }

        log.info("Login exitoso para usuario id: {}", usuario.getId());
        return UsuarioDto.from(usuario);
    }

    /**
     * Devuelve todos los usuarios activos sin exponer passwords.
     * HU-007 → GET /api/users → 200 OK
     */
    @Transactional(readOnly = true)
    public List<UsuarioDto> findAll() {
        log.info("Listando todos los usuarios activos");
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDto::from)
                .toList();
    }

    /**
     * Obtiene un usuario por ID. Lanza 404 si no existe o fue eliminado.
     * HU-008 → GET /api/users/{id} → 200 OK / 404
     */
    @Transactional(readOnly = true)
    public UsuarioDto findById(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return UsuarioDto.from(usuarioRepository.findByIdOrThrow(id));
    }

    /**
     * Actualización parcial: solo modifica los campos no nulos.
     * Si el email cambia, valida que sea único (RN-009-02).
     * Si el password cambia, lo re-encripta con BCrypt (RN-009-03).
     * HU-009 → PUT /api/users/{id} → 200 OK / 404
     */
    @Transactional
    public UsuarioDto update(Long id, UsuarioEdit dto) {
        log.info("Actualizando usuario con id: {}", id);
        Usuario usuario = usuarioRepository.findByIdOrThrow(id);

        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            usuario.setApellido(dto.getApellido());
        }
        if (dto.getEmail() != null) {
            if (usuarioRepository.existsByEmailAndEliminadoFalseAndIdNot(dto.getEmail(), id)) {
                throw new BusinessException("Ya existe un usuario con el email: " + dto.getEmail());
            }
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getCelular() != null) {
            usuario.setCelular(dto.getCelular());
        }
        if (dto.getPassword() != null) {
            usuario.setPassword(passwordService.encode(dto.getPassword()));
        }

        return UsuarioDto.from(usuarioRepository.save(usuario));
    }

    /**
     * Soft delete: marca el usuario como eliminado.
     * Los pedidos del usuario NO se eliminan (RN-010-02).
     * HU-010 → DELETE /api/users/{id} → 204 No Content / 404
     */
    @Transactional
    public void deleteById(Long id) {
        log.info("Eliminando (soft delete) usuario con id: {}", id);
        usuarioRepository.deleteById(id);
    }
}
