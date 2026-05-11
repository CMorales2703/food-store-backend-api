package com.foodstore.user.controller;

import com.foodstore.user.dto.UsuarioDto;
import com.foodstore.user.dto.UsuarioEdit;
import com.foodstore.user.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de administración de usuarios.
 * El registro y login se manejan en AuthController (/api/auth).
 *
 * HU-007: GET    /api/users        → 200 OK (lista sin passwords)
 * HU-008: GET    /api/users/{id}   → 200 OK / 404
 * HU-009: PUT    /api/users/{id}   → 200 OK / 404
 * HU-010: DELETE /api/users/{id}   → 204 No Content / 404
 */
@Tag(name = "Usuarios", description = "Administración de usuarios: consulta, actualización y baja lógica (HU-007 a HU-010)")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios activos sin el campo password. (HU-007)")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario por su ID. Retorna 404 si no existe o fue eliminado. (HU-008)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualización parcial: solo se modifican los campos enviados. Si se envía password, se re-encripta. (HU-009)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioEdit dto) {
        return ResponseEntity.ok(usuarioService.update(id, dto));
    }

    @Operation(summary = "Eliminar usuario", description = "Soft delete: marca el usuario como eliminado sin borrarlo físicamente. (HU-010)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
