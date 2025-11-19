# ADR-002: Inclusión de coordenadas (latitud/longitud) en Equipo

## Contexto

ObraSmart necesita visualizar equipos en un mapa (Leaflet u otra librería) y relacionarlos con procesos de geolocalización y reparaciones.

## Decisión

Se agregan los campos `latitud` y `longitud` a la entidad `Equipo`.

## Justificación

- Permite representar la ubicación actual del equipo.  
- Facilita integraciones con microservicios de geolocalización.  
- Mejora las capacidades de reporte y seguimiento visual.

## Consecuencias

- La actualización de coordenadas debe ser coherente con el microservicio de geolocalización.  
- Se requieren validaciones de rango para los valores de latitud/longitud.
