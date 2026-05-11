package com.foodstore.user.repository;

import com.foodstore.common.repository.BaseRepository;
import com.foodstore.user.model.Usuario;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Usuario. Hereda soft delete, findByIdOrThrow y findAll filtrado.
 * Agrega búsqueda por email para login y validación de unicidad.
 */
@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

    // Para login y validación de email único en registro
    Optional<Usuario> findByEmailAndEliminadoFalse(String email);

    // Para validar email único al actualizar (excluye el usuario actual)
    boolean existsByEmailAndEliminadoFalseAndIdNot(String email, Long id);
}
