# IdentityService

Microservicio de identidad para ObraSmart que reemplaza GestionAuth + GestionUsuario manteniendo compatibilidad con el frontend actual. Autenticación con JWT en cookies HttpOnly (ACCESS_TOKEN / REFRESH_TOKEN), sin sesiones servidor ni Redis. Almacena usuarios y refresh tokens en PostgreSQL y documentos en storage S3-compatible (MinIO en dev).

## Requisitos
- Java 17, Maven
- Docker / Docker Compose (para entorno dev)

## Cómo correr (dev)
```bash
mvn clean package
docker-compose up --build
```
El servicio queda en `http://localhost:8082`.

## Variables de entorno relevantes
- DB_HOST/DB_PORT/DB_NAME/DB_USER/DB_PASS
- JWT_SECRET, JWT_ACCESS_TTL_MIN (15), JWT_REFRESH_TTL_H (168)
- COOKIES_SECURE (false dev), COOKIES_SAMESITE (Lax dev)
- STORAGE_ENDPOINT, STORAGE_ACCESS_KEY, STORAGE_SECRET_KEY, STORAGE_BUCKET, STORAGE_REGION
- ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_REQUIRE_ENV (true en prod para exigir credenciales por env)
- MAIL_HOST, MAIL_PORT, MAIL_USERNAME, MAIL_PASSWORD, MAIL_FROM (envío de correo)
- FRONTEND_BASE_URL (link de recuperación)
- RESET_TOKEN_TTL_MIN (minutos)

## Cookies DEV/PROD
- Dev (http): HttpOnly, Secure=false, SameSite=Lax, Path="/", sin Domain.
- Prod (https): HttpOnly, Secure=true, SameSite=None si front está en dominio distinto; Path="/"; Domain opcional según despliegue.

## Endpoints (contrato legacy)
- POST /auth/login: body `{username,password}`; setea cookies ACCESS_TOKEN/REFRESH_TOKEN; 200/401.
- GET /auth/me: responde `SessionUser { userId, roles[], username, email, status }`; usa cookies; refresh silencioso si access expiró y refresh válido.
- POST /auth/logout: revoca refresh y limpia cookies; 204.
- POST /auth/forgot: `{email}`; 200 mensaje genérico.
- POST /auth/reset: `{email,password}`; 200 con `{token}` opcional.
- /users CRUD (roles, status y campos según frontend): GET /users, POST /users, PUT /users/{id}, DELETE /users/{id} (soft delete status=INACTIVO), GET /users/{id}.
- Documentos:
  - POST /users/{id}/profile-photo (multipart file)
  - GET  /users/{id}/profile-photo
  - POST /users/{id}/documents?type=DOCUMENT_TYPE (multipart file)
  - GET  /users/{id}/documents
  - GET  /users/{id}/documents/{docId}
  - DELETE /users/{id}/documents/{docId} (active=false)

## Ejemplos curl
```bash
# Login y guardar cookies
curl -i -c cookies.txt -X POST http://localhost:8082/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Me
curl -b cookies.txt http://localhost:8082/auth/me

# Listar usuarios (requiere rol ADMINISTRACION)
curl -b cookies.txt http://localhost:8082/users

# Subir foto de perfil
curl -b cookies.txt -F "file=@./foto.jpg" http://localhost:8082/users/1/profile-photo

## Envío de correo (recuperación)
Se puede elegir Mailtrap o Gmail usando perfiles:

- Mailtrap:
  - `SPRING_PROFILES_ACTIVE=mailtrap`
  - `MAILTRAP_USERNAME`, `MAILTRAP_PASSWORD`
- Gmail (real):
  - `SPRING_PROFILES_ACTIVE=gmail`
  - `GMAIL_USERNAME`, `GMAIL_APP_PASSWORD` (app password)

En ambos casos usar `MAIL_FROM` y `FRONTEND_BASE_URL` según el entorno.

## Opción simple (recomendada)
Usá archivos `.env` separados para Gmail y Mailtrap:

1) Completá `.env.gmail` y/o `.env.mailtrap`.
2) Ejecutá:
```
bash run-with-env-gmail.sh
```
o
```
bash run-with-env-mailtrap.sh
```
```

## Gateway
Enrutar `/auth/**` y `/users/**` (y variantes `/api/**` con RewritePath) al puerto 8082.
