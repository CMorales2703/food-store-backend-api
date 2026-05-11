package com.foodstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración global de OpenAPI / Swagger UI.
 * Disponible en: http://localhost:8080/swagger-ui/index.html
 *
 * HU-028: Documentación OpenAPI/Swagger
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI foodStoreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Food Store API")
                        .description("API REST del sistema de gestión de una tienda de comidas. " +
                                "Permite administrar categorías, productos, usuarios y pedidos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Food Store")
                                .email("admin@foodstore.com")));
    }
}
