# Food Store Backend API - TPI Programacion 3

Food Store Backend API es la API REST del sistema Food Store, desarrollada como parte del Trabajo Practico Integrador de Programacion 3.

El objetivo final del TPI es construir un sistema full stack donde el frontend pueda consumir datos reales del backend para registrar usuarios, iniciar sesion, listar categorias, listar productos, crear pedidos y consultar el estado/historial de compras.

## Estado del proyecto

Este repositorio contiene la base inicial del backend del sistema. La idea es usarlo como punto de partida para implementar las historias de usuario de la consigna relacionadas con categorias, usuarios, productos, pedidos e infraestructura.

Actualmente incluye:

- Proyecto base con Spring Boot y Gradle.
- Estructura inicial por capas.
- Configuracion base en `application.properties`.
- CORS preparado para consumir desde Vite.
- Entidad base con auditoria, versionado y soft delete.
- Excepciones base y manejador global.
- Servicio de encriptacion con BCrypt.
- Endpoint inicial de salud: `GET /api/health`.

Pendiente principal:

- Agregar Gradle Wrapper.
- Implementar entidades JPA del dominio.
- Implementar repositorios, servicios, DTOs y controladores REST.
- Conectar con base de datos final.
- Completar Swagger/OpenAPI con los endpoints reales.
- Probar integracion con el frontend.

## Tecnologias

### Backend

- Java 17+
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Bean Validation
- Lombok
- Gradle
- H2 Database
- PostgreSQL o MySQL
- BCrypt
- Swagger/OpenAPI

### Frontend

- HTML5
- CSS3
- TypeScript
- Vite
- LocalStorage

## Estructura actual

```text
food-store-backend-api/
+-- src/
|   +-- main/
|   |   +-- java/
|   |   |   +-- com/foodstore/
|   |   |       +-- common/
|   |   |       |   +-- exception/
|   |   |       |   +-- model/
|   |   |       |   +-- security/
|   |   |       +-- config/
|   |   |       +-- health/
|   |   |       +-- category/
|   |   |       +-- user/
|   |   |       +-- product/
|   |   |       +-- order/
|   |   |       +-- FoodStoreBackendApiApplication.java
|   |   +-- resources/
|   |       +-- application.properties
|   +-- test/
+-- build.gradle
+-- settings.gradle
+-- README.md
```

## Como ejecutar el backend

Cuando el proyecto tenga Gradle Wrapper:

```bash
./gradlew bootRun
```

En Windows:

```bash
gradlew.bat bootRun
```

Si se usa Gradle instalado globalmente:

```bash
gradle bootRun
```

Importante: en esta maquina se detecto Java instalado, pero no `gradle` global. Por eso el proximo paso tecnico recomendado es agregar Gradle Wrapper o regenerar la base desde Spring Initializr con wrapper incluido.

## Endpoints iniciales

```text
GET /api/health
```

Swagger, una vez levantada la API:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/api-docs
```

## Modulos del backend

### Infraestructura

- Entidad base.
- Repositorio base.
- Manejo global de excepciones.
- Encriptacion de contrasenas.
- Carga inicial de admin.
- Swagger/OpenAPI.
- CORS.

### Categorias

- Crear categoria.
- Listar categorias.
- Obtener categoria por ID.
- Actualizar categoria.
- Eliminar categoria con soft delete.

### Usuarios

- Registrar usuario.
- Listar usuarios.
- Obtener usuario por ID.
- Actualizar usuario.
- Eliminar usuario con soft delete.

### Productos

- Crear producto.
- Listar productos.
- Obtener producto por ID.
- Listar productos por categoria.
- Actualizar producto.
- Eliminar producto con soft delete.

### Pedidos

- Crear pedido.
- Listar pedidos.
- Obtener pedido por ID.
- Listar pedidos por usuario.
- Actualizar estado de pedido.
- Eliminar pedido con soft delete.

## Repositorios oficiales del TPI

Frontend:

```text
https://github.com/CMorales2703/food-store-frontend
```

Backend:

```text
https://github.com/CMorales2703/food-store-backend-api
```

## Datos del estudiantes

- Nombres y Apellidos: Diego R. Montes, Ramiro Morales y Cristian R. Morales
- Comision: 04
- Materia: Programacion III

## Video de presentacion

Pendiente de actualizar para el TPI final.

El video final debe durar entre 10 y 15 minutos y comenzar con presentacion con camara encendida.
