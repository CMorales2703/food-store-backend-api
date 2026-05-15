package com.foodstore.common.init;

import com.foodstore.category.model.Categoria;
import com.foodstore.category.repository.CategoriaRepository;
import com.foodstore.product.model.Producto;
import com.foodstore.product.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Carga inicial de datos de demostración: crea 10 categorías y 10 productos
 * si la base de datos está vacía al arrancar la aplicación.
 *
 * HU-026: Implementar Carga Inicial de Datos
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class DataLoad implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Override
    public void run(String... args) {
        if (!categoriaRepository.findAll().isEmpty()) {
            log.info("Categorías existentes encontradas — omitiendo carga de datos de demostración");
            return;
        }

        log.info("Base de datos vacía — cargando datos de demostración...");

        // ─── Categorías ───────────────────────────────────────────────────────────
        // Los nombres en minúscula deben coincidir con el fallbackByCategory del frontend
        // (hamburguesas→hamburguesa.jpg, pizzas→pizza.jpg, bebidas→bebida.jpg,
        //  empanadas→pizza.jpg, postres→pizza.jpg)
        Categoria hamburguesas = save(cat("Hamburguesas", "Hamburguesas artesanales de lomo y vacío, al estilo argentino"));
        Categoria pizzas       = save(cat("Pizzas",       "Pizzas al molde y a la piedra, estilo porteño"));
        Categoria bebidas      = save(cat("Bebidas",      "Bebidas frías y calientes para acompañar tu pedido"));
        Categoria empanadas    = save(cat("Empanadas",    "Empanadas caseras de distintos sabores, recién horneadas"));
        Categoria postres      = save(cat("Postres",      "Postres y facturas caseras para cerrar con algo dulce"));
        Categoria parrilla     = save(cat("Parrilla",     "Cortes de parrilla y clásicos del asado argentino"));
        Categoria minutas      = save(cat("Minutas",      "Milanesas, escalopes y minutas de la cocina criolla"));
        Categoria pastas       = save(cat("Pastas",       "Pastas frescas caseras con salsas de autor"));
        Categoria sandwiches   = save(cat("Sándwiches",   "Sándwiches fríos y calientes para cualquier momento"));
        Categoria papas        = save(cat("Papas fritas", "Papas crocantes con distintas salsas y toppings"));

        log.info("10 categorías creadas");

        // ─── Productos ────────────────────────────────────────────────────────────
        // imagen null → el frontend usa el fallback por categoría o logo.png
        // imagen "papasconcheddar.jpg" → único asset sin categoría mapeada
        productoRepository.saveAll(List.of(

            // Hamburguesas → hamburguesa.jpg (via category fallback)
            prod("Hamburguesa de Lomo",
                 "Medallón de lomo 200g, cheddar fundido, panceta crocante y huevo frito en pan de campo",
                 new BigDecimal("2500.00"), 40, null, hamburguesas),

            // Pizzas → pizza.jpg (via category fallback)
            prod("Pizza al Molde Porteña",
                 "Salsa de tomate casera, doble mozzarella y aceitunas negras al molde",
                 new BigDecimal("2200.00"), 30, null, pizzas),

            // Bebidas → bebida.jpg (via category fallback)
            prod("Fernet con Coca",
                 "Fernet Branca con Coca-Cola en vaso largo con hielo, clásico argentino",
                 new BigDecimal("1500.00"), 80, null, bebidas),

            // Empanadas → pizza.jpg (via category fallback)
            prod("Empanadas de Carne x6",
                 "Empanadas de carne cortada a cuchillo, estilo salteño, recién horneadas",
                 new BigDecimal("1800.00"), 50, null, empanadas),

            // Postres → pizza.jpg (via category fallback)
            prod("Medialunas de Manteca x6",
                 "Medialunas de manteca tipo confitería porteña, recién salidas del horno",
                 new BigDecimal("900.00"), 60, null, postres),

            // Parrilla → logo.png fallback (sin imagen propia)
            prod("Choripán",
                 "Chorizo parrillero de primera en pan de campo crocante con chimichurri casero",
                 new BigDecimal("1200.00"), 60, null, parrilla),

            // Minutas → logo.png fallback
            prod("Milanesa Napolitana",
                 "Milanesa de ternera con salsa de tomate, jamón y mozzarella gratinada, con papas fritas",
                 new BigDecimal("2800.00"), 35, null, minutas),

            // Pastas → logo.png fallback
            prod("Fideos con Tuco",
                 "Tallarines caseros al huevo con tuco de carne argentino, lento a fuego bajo",
                 new BigDecimal("1900.00"), 35, null, pastas),

            // Sándwiches → logo.png fallback
            prod("Lomito Completo",
                 "Lomito de cerdo, lechuga, tomate, huevo frito y mayonesa en pan casero",
                 new BigDecimal("1800.00"), 45, null, sandwiches),

            // Papas fritas → papasconcheddar.jpg (via imagen field, único asset sin categoría mapeada)
            prod("Papas con Cheddar",
                 "Papas fritas crocantes bañadas en salsa cheddar casera, con cebollita de verdeo",
                 new BigDecimal("800.00"), 80, "papasconcheddar.jpg", papas)
        ));

        log.info("10 productos creados — datos de demostración listos");
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Categoria cat(String nombre, String descripcion) {
        return Categoria.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .build();
    }

    private Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    private Producto prod(String nombre, String descripcion,
                          BigDecimal precio, int stock,
                          String imagen, Categoria categoria) {
        return Producto.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .precio(precio)
                .stock(stock)
                .imagen(imagen)
                .disponible(true)
                .categoria(categoria)
                .build();
    }
}
