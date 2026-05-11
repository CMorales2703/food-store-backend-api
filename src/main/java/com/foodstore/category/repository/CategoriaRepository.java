package com.foodstore.category.repository;

import com.foodstore.category.model.Categoria;
import com.foodstore.common.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Categoria. Hereda todas las operaciones de BaseRepository:
 * findAll() (filtrado), findByIdOrThrow(), deleteById() (soft delete).
 */
@Repository
public interface CategoriaRepository extends BaseRepository<Categoria, Long> {
}
