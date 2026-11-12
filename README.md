# 🍎 Fruit & Provider API - MySQL

API REST desarrollada con **Spring Boot 4** y **Java 21** para gestionar el inventario de frutas y sus proveedores (operaciones CRUD completas), utilizando una base de datos relacional **MySQL 8**, **Spring Data JPA** y **Docker Compose**.

---

### 🏗️ Estructura y Decisiones Técnicas

A diferencia del Nivel 1 (*Package by Layer*), en este nivel el proyecto evoluciona hacia una arquitectura **Package by Feature** (`provider`, `fruit`, `common/exceptions` y `config`), logrando mayor cohesión y escalabilidad al trabajar con múltiples dominios relacionados:

- **Relación `@ManyToOne` e Integridad Referencial:** Cada entidad `Fruit` pertenece obligatoriamente a un `Provider`. El sistema impide registrar frutas con proveedores inexistentes y bloquea la eliminación de un proveedor si aún tiene frutas asociadas (`ProviderHasFruits`).
- **DTOs con Java Records y Bean Validation:** Uso de `record` inmutables (`ProviderRequestDto`, `ProviderResponseDto`, `FruitRequestDto`, `FruitResponseDto`) con validaciones (`@NotBlank`, `@NotNull`, `@Positive`) verificadas con `@Valid` en los controladores.
- **Manejo centralizado de errores (`GlobalExceptionHandler`):** Ubicado en el paquete transversal `common.exceptions`, intercepta excepciones de negocio (`404 Not Found` y `400 Bad Request`) y errores de validación devolviendo respuestas uniformes mediante `ErrorDto`.
- **Carga inicial de datos (`DataBaseLoader`):** Implementa `CommandLineRunner` comprobando previamente si la tabla de proveedores está vacía (`count() == 0`) para sembrar datos de prueba solo en el primer arranque sin duplicar registros en reinicios posteriores.
- **Variables de entorno y Docker Compose:** Configuración externalizada mediante variables de entorno (`.env` / `.env.example`) con valores por defecto en `application.properties`. El `docker-compose.yml` orquesta el contenedor de MySQL (con `healthcheck` y volumen persistente) junto con el contenedor de la API compilado mediante un `Dockerfile` multi-stage.

---

## 📌 Endpoints de la API

### Proveedores (`/providers`)

| Método | Endpoint | Descripción | Respuestas HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/providers` | Registrar un nuevo proveedor | `201 Created` / `400 Bad Request` |
| `GET` | `/providers` | Obtener todos los proveedores | `200 OK` |
| `GET` | `/providers/{id}` | Obtener un proveedor por su ID | `200 OK` / `404 Not Found` |
| `PUT` | `/providers/{id}` | Actualizar nombre y país de un proveedor | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/providers/{id}` | Eliminar un proveedor (si no tiene frutas asociadas) | `204 No Content` / `400 Bad Request` / `404 Not Found` |

**Ejemplo de cuerpo JSON (`POST` y `PUT` en `/providers`):**
```json
{
  "name": "Frutas del Valle",
  "country": "España"
}
```

### Frutas (`/fruits`)

| Método | Endpoint | Descripción | Respuestas HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/fruits` | Registrar una fruta asociada a un proveedor | `201 Created` / `400 Bad Request` / `404 Not Found` |
| `GET` | `/fruits` | Obtener todas las frutas | `200 OK` |
| `GET` | `/fruits?providerId={id}` | Filtrar frutas por ID de proveedor | `200 OK` / `404 Not Found` |
| `GET` | `/fruits/{id}` | Obtener una fruta por su ID | `200 OK` / `404 Not Found` |
| `PUT` | `/fruits/{id}` | Actualizar nombre, peso y/o proveedor de una fruta | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/fruits/{id}` | Eliminar una fruta por su ID | `204 No Content` / `404 Not Found` |

**Ejemplo de cuerpo JSON (`POST` y `PUT` en `/fruits`):**
```json
{
  "name": "Manzana Fuji",
  "weightInKilos": 10,
  "providerId": 1
}
```

---

## 🚀 Cómo ejecutar el proyecto

### 1. Con Docker Compose (MySQL + API)
Levanta tanto la base de datos como la aplicación en contenedores:
```bash
docker compose up --build
```
La API quedará disponible en `http://localhost:8080`.

### 2. En local (con Maven + MySQL en Docker)
Si prefieres ejecutar la aplicación desde el IDE o terminal local, levanta únicamente la base de datos:
```bash
docker compose up -d mysql
./mvnw spring-boot:run
```

---

## 🧪 Ejecutar los tests

```bash
./mvnw test
```
