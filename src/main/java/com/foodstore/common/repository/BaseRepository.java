package com.foodstore.common.repository;

import com.foodstore.common.exception.ResourceNotFoundException;
import com.foodstore.common.model.BaseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repositorio base que centraliza las operaciones comunes:
 * - findAll() filtra por eliminado = false (soft delete)
 * - findByIdOrThrow() lanza 404 si no existe o está eliminado
 * - deleteById() hace soft delete (eliminado = true)
 *
 * HU-024: Implementar Repositorio Base
 */
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity, ID> extends JpaRepository<E, ID> {

    Optional<E> findByIdAndEliminadoFalse(ID id);

    List<E> findAllByEliminadoFalse();

    /**
     * Busca una entidad activa por ID. Lanza ResourceNotFoundException si no existe
     * o si fue eliminada lógicamente.
     */
    default E findByIdOrThrow(ID id) {
        return findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Entidad con id " + id + " no encontrado"));
    }

    /**
     * Devuelve solo las entidades no eliminadas.
     */
    @Override
    default List<E> findAll() {
        return findAllByEliminadoFalse();
    }

    /**
     * Soft delete: marca la entidad como eliminada sin borrarla físicamente.
     * Lanza ResourceNotFoundException si el ID no existe o ya fue eliminado.
     */
    @Override
    default void deleteById(ID id) {
        E entity = findByIdOrThrow(id);
        entity.softDelete();
        save(entity);
    }
}
