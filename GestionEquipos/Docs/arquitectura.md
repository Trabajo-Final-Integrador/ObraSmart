# Arquitectura del Microservicio GestiónEquipos

Este documento describe la arquitectura interna del microservicio **GestionEquipos** dentro del ecosistema **ObraSmart**.

---

## 1. Capas lógicas

El microservicio adopta una arquitectura en capas típica de aplicaciones Spring Boot:

- **Capa de Presentación (API / Controllers)**  
  Expone endpoints REST bajo el prefijo `/api/...`. Recibe y retorna objetos DTO en formato JSON.

- **Capa de Aplicación / Servicios (Service)**  
  Implementa la lógica de negocio: validaciones, reglas de mantenimiento, estados operativos, etc.  
  Orquesta las operaciones entre los repositorios y, en el futuro, entre otros microservicios.

- **Capa de Dominio (Entities)**  
  Contiene las entidades JPA que reflejan el modelo de datos persistente: `Equipo`, `TipoEquipo`, `Marca`, `Modelo`, así como los enums `Combustible` y `Estado_Operativo`.

- **Capa de Persistencia (Repositories)**  
  Usa Spring Data JPA para acceder a la base de datos PostgreSQL. Se definen interfaces `Repository` para cada agregado principal.

- **Capa de Infraestructura / Configuración**  
  Incluye clases de configuración, beans comunes (por ejemplo, `ModelMapper`) y la configuración de acceso a datos definida en `application.properties`.

---

## 2. Integración con el ecosistema ObraSmart

A nivel solución, la comunicación entre microservicios se realiza vía HTTP REST a través de un **API Gateway**.  
El microservicio GestionEquipos forma parte de esta arquitectura de microservicios y se vincula principalmente con:

- **GestiónReparaciones**: provee información de los equipos para la apertura y seguimiento de reparaciones.  
- **GestiónGeolocalizacion**: trabaja con coordenadas (`latitud`, `longitud`) para representar la posición del equipo en el mapa.  
- **GestiónStock**: considera ciertos equipos como activos dentro del inventario general.  
- **GestiónReportes**: expone datos relevantes para generar reportes de utilización, estado y mantenimiento.

La vista global de los microservicios se documenta en `docs/diagramas/microservicios.md`.

---

## 3. Flujo general de uso

1. Un usuario autenticado ingresa al **frontend Angular** de ObraSmart.  
2. A través del API Gateway, el frontend invoca los endpoints de GestiónEquipos.  
3. El controller recibe la solicitud, valida los datos de entrada (usando Bean Validation) y delega en el servicio correspondiente.  
4. El servicio aplica reglas de negocio (por ejemplo, no permitir equipos duplicados por número de serie) y utiliza los repositorios para leer/escribir en la base de datos.  
5. La respuesta se devuelve hacia el frontend en formato JSON.

---

## 4. Configuración de entorno

La configuración por defecto del microservicio se encuentra en `config/application.properties`, donde se establece:

- Puerto de escucha del servicio (por ejemplo, `server.port=8087`).  
- URL de conexión a PostgreSQL.  
- Usuario y contraseña de la base.  
- Estrategia de generación de esquema (`spring.jpa.hibernate.ddl-auto`).

En entornos dockerizados, estos valores se parametrizarán mediante variables de entorno o perfiles específicos de Spring.

---

## 5. Consideraciones de seguridad (a futuro)

- Integración con el microservicio de **GestiónLogin / Seguridad** mediante JWT.  
- Restricción de endpoints por rol de usuario (admin, técnico de mantenimiento, supervisor, etc.).  
- Auditoría de cambios sobre la entidad `Equipo` (quién modificó qué y cuándo).

---

## 6. Escalabilidad y mantenimiento

Al estar basado en Spring Boot y PostgreSQL, el microservicio puede escalar vertical y horizontalmente según la infraestructura disponible.  
La separación en microservicios permite desplegar GestionEquipos de manera independiente frente a otras partes del sistema, facilitando:

- Releases incrementales.  
- Mantenimiento focalizado.  
- Escalado por demanda del módulo de equipos.
