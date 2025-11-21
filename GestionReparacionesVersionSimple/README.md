# 🛠️ Microservicio: Gestión de Reparaciones – ObraSmart

El microservicio **GestionReparaciones** forma parte del ecosistema **ObraSmart** y se encarga de gestionar las reparaciones y tareas de mantenimiento de los equipos de obra civil privada.

Administra el ciclo de vida de una reparación, desde su creación (solicitud), inicio de trabajos, actualización de estado, registro opcional de ubicación (lat/lon) y finalización.

---

## 📌 Tecnologías utilizadas

- Java 17  
- Spring Boot 3.x  
- Spring Web  
- Spring Data JPA  
- Hibernate  
- Spring Validation  
- PostgreSQL  
- Maven  

La configuración concreta de dependencias y propiedades se encuentra en:

- `config/pom.xml`  
- `config/application.properties`

---

## 🧩 Arquitectura general

Capas principales:

- **API (ReparacionController)**  
  Expone endpoints REST bajo `/api/reparaciones` para crear, consultar, actualizar y (opcionalmente) eliminar reparaciones.

- **Servicios (ReparacionService, GeocodingService, etc.)**  
  Implementan la lógica de negocio: validación de datos, manejo de estados de la reparación, integración con geocodificación, etc.

- **Persistencia (Repositories)**  
  Usa Spring Data JPA para acceder a la tabla `reparaciones` en PostgreSQL.

- **Dominio (Entidad Reparacion + enum EstadoReparacion)**  
  Modelan el concepto de reparación/mantenimiento de un equipo.

- **DTOs (ReparacionDto, ReparacionResponseDto, etc.)**  
  Encapsulan la información expuesta por la API y desacoplan el dominio del contrato externo.

Más detalle se encuentra en `docs/arquitectura.md`.

---

## 🗄️ Modelo de datos (resumen)

Entidades principales:

- `Reparacion`  
  - `id`  
  - `equipoId` (referencia lógica al microservicio GestionEquipos)  
  - `descripcion`  
  - `estado` (`EstadoReparacion`)  
  - `fechaCreacion`  
  - `fechaInicio`  
  - `fechaFin`  
  - `lat`, `lon` (geolocalización opcional)  
  - `direccion` (texto opcional)  

- Enum `EstadoReparacion`  
  - `DISPONIBLE`  
  - `FUERA_DE_SERVICIO`  
  - `EN_TRASLADO`  
  - `EN_MANTENIMIENTO`  
  - `FINALIZADA`  

Detalle completo en `docs/entidades.md`.

---

## 🔗 Integración con otros microservicios

- **GestionEquipos**  
  - Usa `equipoId` para referenciar el equipo reparado.  
  - Llamadas HTTP contra el micro de equipos para validar/consultar información (cuando aplique).

- **GestionGeolocalizacion / API externa de geocodificación**  
  - Opcionalmente, se puede obtener `lat/lon` a partir de una dirección textual utilizando servicios de geocodificación.  
  - La propiedad `locationiq.key` permite integrar LocationIQ o, si está vacía, usar Nominatim como alternativa gratuita.

- **Gateway**  
  - El acceso desde el frontend se realiza a través del API Gateway de ObraSmart.

La visión global de microservicios se amplía en `docs/diagramas/microservicios.md`.

---

## 🧰 Endpoints principales (resumen)

- `GET /api/reparaciones`  
- `GET /api/reparaciones/{id}`  
- `POST /api/reparaciones`  
- `PUT /api/reparaciones/{id}`  
- `DELETE /api/reparaciones/{id}` (según reglas de negocio)  

Documentación detallada en `docs/endpoints.md`.

---

## 🗃️ Base de datos

- Motor: **PostgreSQL**  
- Tabla principal: `reparaciones`  

JPA/Hibernate se encarga de crear/actualizar el esquema a partir de la entidad `Reparacion` (`spring.jpa.hibernate.ddl-auto=update`).  

Más detalles en `docs/bd.md`.

---

## 🐳 Dockerización (planificada)

Este microservicio está pensado para dockerizarse junto con el resto de ObraSmart.  
En la documentación se asume que:

- El micro se empaqueta como un `jar` de Spring Boot.  
- Se ejecutará en un contenedor que expone el puerto configurado (`server.port`).  
- La base de datos PostgreSQL se ejecutará en un contenedor independiente o gestionada externamente.  
- Las propiedades de conexión (`spring.datasource.*`) se parametrizarán mediante variables de entorno.

> Nota: El **Dockerfile** y el archivo de **docker-compose** se definirán a nivel de solución completa, junto al resto de microservicios.

---

## 👥 Contexto funcional

El microservicio **GestionReparaciones** es el responsable de registrar y controlar todas las intervenciones de mantenimiento sobre los equipos de obra, aportando:

- Trazabilidad de cada reparación.  
- Historial temporal (creación, inicio, finalización).  
- Estado actual de la reparación.  
- Información de ubicación del equipo durante la intervención.

Es un módulo clave para el seguimiento operativo de ObraSmart.
