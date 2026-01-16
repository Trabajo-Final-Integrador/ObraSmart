# GestionAuth (AuthV2)

Microservicio de autenticacion por sesion HTTP (sin JWT) para ObraSmart. Convive en paralelo con GestionLogin y expone los mismos endpoints `/auth/**`.

## Requisitos
- Java 17
- Maven 3.9+
- PostgreSQL

## Configuracion
Variables de entorno o `application.yml`:
- `DB_URL` (default `jdbc:postgresql://localhost:5432/gestionaAuth`)
- `DB_USER` (default `postgres`)
- `DB_PASSWORD` (default `postgres`)
- `CORS_ALLOWED_ORIGINS` (default `http://localhost:4200`)
- `SESSION_TIMEOUT` (default `30m`)
- `SESSION_COOKIE_SECURE` (default `false`)

## Ejecutar
```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

## Endpoints
- `POST /auth/login` -> `{ "username": "...", "password": "..." }` -> `{ "id", "username", "role" }` + cookie `JSESSIONID`
- `POST /auth/logout`
- `GET /auth/me` (o `/auth/id`)
- `POST /auth/forgot-password` (alias `/auth/forgot`) -> `{ "email": "..." }` devuelve token (modo demo)
- `POST /auth/reset-password` (alias `/auth/reset`) -> `{ "token": "...", "newPassword": "..." }`
- `POST /auth/seed-admin` (solo perfil `dev`) crea admin `admin/admin123` si no existe
- `GET /actuator/health`

## Prueba rapida
```bash
curl -X POST http://localhost:8082/auth/seed-admin  # solo dev

curl -i -c cookies.txt -X POST http://localhost:8082/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "username": "admin", "password": "admin123" }'

curl -b cookies.txt http://localhost:8082/auth/me
curl -X POST -b cookies.txt http://localhost:8082/auth/logout
```

## Integracion
- Gateway debe rutear `/auth/**` a `localhost:8082`.
- Cookies HttpOnly, SameSite=Lax; `secure` configurable por env.
