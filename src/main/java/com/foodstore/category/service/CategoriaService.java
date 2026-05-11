package com.foodstore.category.service;

import com.foodstore.category.dto.CategoriaCreate;
import com.foodstore.category.dto.CategoriaDto;
import com.foodstore.category.dto.CategoriaEdit;
import com.foodstore.category.model.Categoria;
import com.foodstore.category.repository.CategoriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para Categorias.
 * Aplica el patrón Constructor Injection (RequiredArgsConstructor).
 *
 * HU-001 a HU-005: Gestión de Categorías
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Crea una nueva categoría.
     * HU-001: Crear Categoría → POST /api/categories → 201 Created
     */
    @Transactional
    public CategoriaDto save(CategoriaCreate dto) {
        log.info("Creando categoría: {}", dto.getNombre());
        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
        return CategoriaDto.from(categoriaRepository.save(categoria));
    }

    /**
     * Devuelve todas las categorías activas (eliminado = false).
     * HU-002: Listar Categorías → GET /api/categories → 200 OK
     */
    @Transactional(readOnly = true)
    public List<CategoriaDto> findAll() {
        log.info("Listando todas las categorías activas");
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaDto::from)
                .toList();
    }

    /**
     * Obtiene una categoría por ID. Lanza 404 si no existe o fue eliminada.
     * HU-003: Obtener Categoría por ID → GET /api/categories/{id} → 200 OK / 404
     */
    @Transactional(readOnly = true)
    public CategoriaDto findById(Long id) {
        log.info("Buscando categoría con id: {}", id);
        return CategoriaDto.from(categoriaRepository.findByIdOrThrow(id));
    }

    /**
     * Actualización parcial: solo modifica los campos no nulos del DTO.
     * HU-004: Actualizar Categoría → PUT /api/categories/{id} → 200 OK / 404
     */
    @Transactional
    public CategoriaDto update(Long id, CategoriaEdit dto) {
        log.info("Actualizando categoría con id: {}", id);
        Categoria categoria = categoriaRepository.findByIdOrThrow(id);

        if (dto.getNombre() != null) {
            categoria.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            categoria.setDescripcion(dto.getDescripcion());
        }

        return CategoriaDto.from(categoriaRepository.save(categoria));
    }

    /**
     * Soft delete: marca la categoría como eliminada (eliminado = true).
     * HU-005: Eliminar Categoría → DELETE /api/categories/{id} → 204 No Content / 404
     */
    @Transactional
    public void deleteById(Long id) {
        log.info("Eliminando (soft delete) categoría con id: {}", id);
        categoriaRepository.deleteById(id);
    }
}
