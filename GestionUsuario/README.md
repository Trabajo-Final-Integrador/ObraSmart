# GestionUsuario

Microservicio para CRUD de usuarios extendidos (datos personales + licencias). Autenticación por sesión (JSESSIONID) proveniente de GestionAuth a través del Gateway. No gestiona login.

## Requisitos
- Java 17, Maven
- Spring Boot 3.x
- PostgreSQL

## Configuración (env vars)
- `DB_HOST` (default `localhost`)
- `DB_PORT` (default `5432`)
- `DB_NAME` (default `gestion_usuarios`)
- `DB_USER` (default `postgres`)
- `DB_PASS` (default `123456`)
- `SESSION_TIMEOUT` (default `60m`)
- `APP_CORS_ALLOWED_ORIGINS` (default `http://localhost:4200,http://localhost:8085`)

## Perfiles
- Default: `ddl-auto=update`
- `dev`: `ddl-auto=create-drop`, `show-sql=true`

## Cómo correr
```bash
mvn clean test
mvn spring-boot:run
```
Puerto por defecto: `8084`.

## Integración vía Gateway (ejemplo)
- Ruta: `/users/**` proxied al puerto 8084.
- Sesión: JSESSIONID emitida por GestionAuth (8082) via `/auth/login` en gateway (8085).

### Ejemplo curl (vía gateway 8085)
```bash
# 1) Login en GestionAuth (guarda cookie)
curl -i -c cookies.txt -X POST http://localhost:8085/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "username": "admin", "password": "admin123" }'

# 2) Listar usuarios con la cookie de sesión
curl -b cookies.txt http://localhost:8085/users

# 3) Ver usuario actual
curl -b cookies.txt http://localhost:8085/users/me
```

Tip Angular: usar `withCredentials: true` en HttpClient.

## Endpoints principales (base `/users`)
- `POST /users` (ADMINISTRACION) crear
- `GET /users` (ADMINISTRACION, SUPERVISOR) listar con filtros opcionales
- `GET /users/{id}` (ADMINISTRACION, SUPERVISOR) detalle
- `PUT /users/{id}` (ADMINISTRACION) editar
- `DELETE /users/{id}` (ADMINISTRACION) baja lógica (status=INACTIVE)
- `GET /users/me` (cualquiera autenticado) usuario logueado
- Licencias (multipart):
  - `POST /users/{id}/licencia/frente` (ADMINISTRACION)
  - `POST /users/{id}/licencia/dorso` (ADMINISTRACION)
  - `GET /users/{id}/licencia/frente` (ADMINISTRACION, SUPERVISOR)
  - `GET /users/{id}/licencia/dorso` (ADMINISTRACION, SUPERVISOR)

## Flujo de capas
1. Controller recibe request y valida (DTO + @Valid)
2. Service aplica reglas (duplicados, soft delete, carga de licencias)
3. Repository persiste en PostgreSQL (JPA/Hibernate)
4. Respuesta DTO (UsuarioResponse) al frontend
