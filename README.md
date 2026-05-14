# Food Store Backend API - TPI Programacion 3

API REST del sistema Food Store, desarrollada como parte del Trabajo Practico Integrador de Programacion 3 (UTN - Tecnicatura Universitaria en Programacion).

El sistema permite registrar usuarios, iniciar sesion, gestionar categorias y productos, y crear pedidos con control de stock transaccional.

## Estado del proyecto

Backend completamente implementado. Todos los modulos estan operativos y documentados.

- Infraestructura base (entidad base, repositorio base, excepciones, BCrypt, CORS, Swagger)
- CRUD completo de Categorias (HU-001 a HU-005)
- CRUD completo de Usuarios + Autenticacion (HU-006 a HU-010)
- CRUD completo de Productos con filtro por categoria (HU-011 a HU-016)
- CRUD completo de Pedidos con creacion transaccional y control de stock (HU-017 a HU-022)
- Documentacion Swagger/OpenAPI disponible en `/swagger-ui.html`

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Spring Web / Spring Data JPA
- Bean Validation (Jakarta)
- Lombok
- Gradle 8.8
- H2 Database (desarrollo) / PostgreSQL (produccion)
- BCrypt (spring-security-crypto)
- SpringDoc OpenAPI 2.6.0

## Estructura del proyecto

```text
food-store-backend-api/
+-- src/
|   +-- main/
|   |   +-- java/
|   |   |   +-- com/foodstore/
|   |   |       +-- common/
|   |   |       |   +-- enums/       (Rol, Estado, FormaPago)
|   |   |       |   +-- exception/   (GlobalExceptionHandler, excepciones)
|   |   |       |   +-- interfaces/  (Calculable)
|   |   |       |   +-- model/       (BaseEntity)
|   |   |       |   +-- repository/  (BaseRepository)
|   |   |       |   +-- security/    (PasswordService)
|   |   |       +-- config/          (CorsConfig, OpenApiConfig)
|   |   |       +-- health/          (HealthController)
|   |   |       +-- auth/            (AuthController)
|   |   |       +-- category/        (model, repository, dto, service, controller)
|   |   |       +-- user/            (model, repository, dto, service, controller, init)
|   |   |       +-- product/         (model, repository, dto, service, controller)
|   |   |       +-- order/           (model, repository, dto, service, controller)
|   |   |       +-- FoodStoreBackendApiApplication.java
|   |   +-- resources/
|   |       +-- application.properties
|   +-- test/
+-- build.gradle
+-- settings.gradle
+-- README.md
```

## Como ejecutar

```bash
# Linux / Mac
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

La API levanta en `http://localhost:8080`.

Usuario administrador creado automaticamente al iniciar:
- Email: `admin@admin.com`
- Password: `123456`

## Documentacion

Con la API corriendo:

```
Swagger UI:  http://localhost:8080/swagger-ui.html
API Docs:    http://localhost:8080/api-docs
H2 Console:  http://localhost:8080/h2-console
```

H2 Console (solo desarrollo):
- JDBC URL: `jdbc:h2:mem:foodstore`
- Usuario: `sa` / Password: (vacio)

## Endpoints

### Autenticacion
```
POST /api/auth/register   Registrar nuevo usuario
POST /api/auth/login      Iniciar sesion
```

### Categorias
```
GET    /api/categories        Listar todas
GET    /api/categories/{id}   Obtener por ID
POST   /api/categories        Crear
PUT    /api/categories/{id}   Actualizar (parcial)
DELETE /api/categories/{id}   Eliminar (soft delete)
```

### Usuarios
```
GET    /api/users        Listar todos
GET    /api/users/{id}   Obtener por ID
PUT    /api/users/{id}   Actualizar (parcial)
DELETE /api/users/{id}   Eliminar (soft delete)
```

### Productos
```
GET    /api/products                  Listar todos
GET    /api/products/{id}             Obtener por ID
GET    /api/products/category/{id}    Listar por categoria
POST   /api/products                  Crear
PUT    /api/products/{id}             Actualizar (parcial)
DELETE /api/products/{id}             Eliminar (soft delete)
```

### Pedidos
```
GET    /api/orders              Listar todos
GET    /api/orders/{id}         Obtener por ID
GET    /api/orders/user/{id}    Listar por usuario
POST   /api/orders              Crear (transaccional, reduce stock)
PUT    /api/orders/{id}         Actualizar estado / forma de pago
DELETE /api/orders/{id}         Eliminar (soft delete)
```

### Utilidades
```
GET /api/health   Estado del servidor
```

## Repositorios

```
Backend:  https://github.com/CMorales2703/food-store-backend-api
Frontend: https://github.com/CMorales2703/food-store-frontend
```

## Datos del equipo

- Integrantes: Diego R. Montes, Ramiro Morales y Cristian R. Morales
- Comision: 04
- Materia: Programacion III - UTN
