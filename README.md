# ObraSmart Docker Stack

## Build artifacts
- Spring Boot modules (gateway, microservicios y services):  
  `./mvnw -pl Gateway/Gateway,GestionAsistente,GestionEquipos,GestionGeolocalizacion,GestionLogistica,GestionObrador,GestionReparaciones,GestionReportes,GestionStock,IdentityService,MediaService clean package`
- Frontend Angular (produce `dist/`):  
  `cd frontend-obrasmart2 && npm ci && npm run build -- --configuration production`

## Run stack
`docker compose up -d --build`

## Media persistence
- MediaService monta `media-storage:/app/storage` (uso por defecto).  
- Para desarrollo local puedes descomentar `./media-storage:/app/storage` en `docker-compose.yml`.

## Notes
- No uses `docker compose down -v` si querés mantener los datos en PostgreSQL y las imágenes guardadas.
