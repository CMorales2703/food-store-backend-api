package com.foodstore.product.repository;

import com.foodstore.common.repository.BaseRepository;
import com.foodstore.product.model.Producto;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Producto. Hereda soft delete, findByIdOrThrow y findAll filtrado.
 * Agrega búsqueda por categoría para el endpoint HU-014.
 */
@Repository
public interface ProductoRepository extends BaseRepository<Producto, Long> {

    // HU-014: filtrar productos activos por categoría
    List<Producto> findAllByCategoriaIdAndEliminadoFalse(Long categoriaId);
}
