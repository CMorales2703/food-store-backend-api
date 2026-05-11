package com.foodstore.product.service;

import com.foodstore.category.repository.CategoriaRepository;
import com.foodstore.product.dto.ProductoCreate;
import com.foodstore.product.dto.ProductoDto;
import com.foodstore.product.dto.ProductoEdit;
import com.foodstore.product.model.Producto;
import com.foodstore.product.repository.ProductoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para Productos.
 *
 * HU-011: Crear Producto (con validación de categoría existente)
 * HU-012: Listar Productos (con categoría anidada)
 * HU-013: Obtener Producto por ID
 * HU-014: Listar Productos por Categoría
 * HU-015: Actualizar Producto (parcial, cambio de categoría opcional)
 * HU-016: Eliminar Producto (soft delete)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    /**
     * Crea un nuevo producto asociado a una categoría existente.
     * Si disponible no se envía, se asigna true por defecto (RN-011-05).
     * HU-011 → POST /api/products → 201 Created
     */
    @Transactional
    public ProductoDto save(ProductoCreate dto) {
        log.info("Creando producto: {}", dto.getNombre());

        var categoria = categoriaRepository.findByIdOrThrow(dto.getIdCategoria());

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .descripcion(dto.getDescripcion())
                .stock(dto.getStock())
                .imagen(dto.getImagen())
                .disponible(dto.getDisponible() != null ? dto.getDisponible() : true)
                .categoria(categoria)
                .build();

        return ProductoDto.from(productoRepository.save(producto));
    }

    /**
     * Devuelve todos los productos activos con su categoría.
     * HU-012 → GET /api/products → 200 OK
     */
    @Transactional(readOnly = true)
    public List<ProductoDto> findAll() {
        log.info("Listando todos los productos activos");
        return productoRepository.findAll()
                .stream()
                .map(ProductoDto::from)
                .toList();
    }

    /**
     * Obtiene un producto por ID. Lanza 404 si no existe o fue eliminado.
     * HU-013 → GET /api/products/{id} → 200 OK / 404
     */
    @Transactional(readOnly = true)
    public ProductoDto findById(Long id) {
        log.info("Buscando producto con id: {}", id);
        return ProductoDto.from(productoRepository.findByIdOrThrow(id));
    }

    /**
     * Filtra productos activos por categoría.
     * Valida que la categoría exista antes de buscar (RN-014-01).
     * HU-014 → GET /api/products/category/{id} → 200 OK / 404
     */
    @Transactional(readOnly = true)
    public List<ProductoDto> findByCategoria(Long categoriaId) {
        log.info("Buscando productos de la categoría id: {}", categoriaId);
        categoriaRepository.findByIdOrThrow(categoriaId); // lanza 404 si no existe
        return productoRepository.findAllByCategoriaIdAndEliminadoFalse(categoriaId)
                .stream()
                .map(ProductoDto::from)
                .toList();
    }

    /**
     * Actualización parcial: solo modifica los campos no nulos.
     * Si se envía idCategoria, valida que la categoría exista (RN-015-02).
     * HU-015 → PUT /api/products/{id} → 200 OK / 404
     */
    @Transactional
    public ProductoDto update(Long id, ProductoEdit dto) {
        log.info("Actualizando producto con id: {}", id);
        Producto producto = productoRepository.findByIdOrThrow(id);

        if (dto.getNombre() != null) {
            producto.setNombre(dto.getNombre());
        }
        if (dto.getPrecio() != null) {
            producto.setPrecio(dto.getPrecio());
        }
        if (dto.getDescripcion() != null) {
            producto.setDescripcion(dto.getDescripcion());
        }
        if (dto.getStock() != null) {
            producto.setStock(dto.getStock());
        }
        if (dto.getImagen() != null) {
            producto.setImagen(dto.getImagen());
        }
        if (dto.getDisponible() != null) {
            producto.setDisponible(dto.getDisponible());
        }
        if (dto.getIdCategoria() != null) {
            var categoria = categoriaRepository.findByIdOrThrow(dto.getIdCategoria());
            producto.setCategoria(categoria);
        }

        return ProductoDto.from(productoRepository.save(producto));
    }

    /**
     * Soft delete: marca el producto como eliminado.
     * Los detalles de pedido históricos mantienen la referencia (RN-016-02).
     * HU-016 → DELETE /api/products/{id} → 204 No Content / 404
     */
    @Transactional
    public void deleteById(Long id) {
        log.info("Eliminando (soft delete) producto con id: {}", id);
        productoRepository.deleteById(id);
    }
}
