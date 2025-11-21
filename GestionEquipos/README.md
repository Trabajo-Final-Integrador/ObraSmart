# 🛠️ Microservicio: Gestión de Equipos – ObraSmart

El microservicio **GestionEquipos** forma parte del ecosistema **ObraSmart** y se encarga de administrar el ciclo de vida completo de los equipos de obra: registro, actualización, clasificación, estado operativo, mantenimiento y ubicación actual.

Este servicio expone endpoints REST que permiten operar sobre equipos, tipos de equipos, marcas y modelos, integrándose con otros microservicios como **GestiónReparaciones**, **GestiónGeolocalizacion**, **GestiónStock** y **GestiónReportes**.

---

## 📌 Tecnologías utilizadas

- Java 17  
- Spring Boot 3.5.x  
- Spring Web  
- Spring Data JPA  
- Hibernate  
- PostgreSQL  
- Spring Validation  
- Lombok  
- ModelMapper  
- OpenAPI / Swagger  
- WebFlux (reservado para integraciones reactivas futuras)  
- Maven  

La configuración detallada de dependencias y propiedades se encuentra en:
- `config/pom.xml`
- `config/application.properties`

---

## 🧩 Arquitectura general

Este microservicio sigue una arquitectura en capas:

- **API (controllers)**: exponen los endpoints REST bajo `/api/...`.  
- **Services**: contienen la lógica de negocio relacionada con la gestión de equipos.  
- **Repositories (Spring Data JPA)**: realizan el acceso a datos sobre la base PostgreSQL.  
- **Entities**: representan el modelo de datos persistente.  
- **DTOs + ModelMapper**: separan el modelo de dominio de los contratos expuestos por la API.

La base de datos utilizada es **PostgreSQL**, con una instancia lógica llamada `GestionEquipos`.

Más detalle en `docs/arquitectura.md` y `docs/bd.md`.

---

## 🗄️ Modelo de datos (resumen)

Entidades principales:

- `Equipo`  
- `TipoEquipo`  
- `Marca`  
- `Modelo`  
- Enums: `Combustible`, `Estado_Operativo`

El detalle completo de atributos y relaciones se encuentra documentado en `docs/entidades.md`.

---

## 🔗 Integración con otros microservicios

Este servicio se integra con otros componentes de ObraSmart a través del API Gateway:

- **GestiónReparaciones**: consulta equipos por `equipoId` para registrar y gestionar reparaciones.  
- **GestiónGeolocalizacion**: provee/valida coordenadas de los equipos mediante `latitud` y `longitud`.  
- **GestiónStock**: utiliza información de equipos como parte del inventario.  
- **GestiónReportes**: consume datos de equipos para generar reportes y dashboards.  

La vista a alto nivel de esta arquitectura se detalla en `docs/diagramas/microservicios.md`.

---

## 🧰 Endpoints principales

La especificación detallada de endpoints, junto con ejemplos de request/response, se documenta en `docs/endpoints.md`.  
A modo de resumen:

- `/api/equipos`  
- `/api/tipo-equipo`  
- `/api/marcas`  
- `/api/modelos`  

Cada recurso expone operaciones CRUD para gestionar la información de catálogo y los equipos de obra.

---

## 🗃️ Base de datos

- Motor: **PostgreSQL**  
- Nombre lógico: **GestionEquipos**  
- Estrategia: `spring.jpa.hibernate.ddl-auto=update` para entorno de desarrollo.  

La estructura conceptual (DER) y las decisiones de diseño de base de datos están documentadas en:

- `docs/bd.md`  
- `docs/diagramas/der.md`

---

## 🐳 Dockerización (planificada)

Este microservicio está pensado para ser dockerizado junto con el resto de ObraSmart.  
En la documentación se hace referencia a:

- Una futura imagen Docker basada en Java 17.  
- Configuración de variables de entorno para conexión a PostgreSQL.  
- Orquestación mediante `docker-compose` a nivel de solución completa.

> Nota: el **Dockerfile** y los archivos de **docker-compose** se definirán al momento de dockerizar todo el proyecto ObraSmart.

---

## 📚 Documentación adicional

- `docs/arquitectura.md` → vista lógica del microservicio.  
- `docs/endpoints.md` → detalle de endpoints y contratos JSON.  
- `docs/entidades.md` → descripción de las entidades de dominio.  
- `docs/bd.md` → detalles de la base de datos.  
- `docs/adr/*` → decisiones de arquitectura (Architecture Decision Records).  
- `docs/diagramas/*` → guía para los diagramas DER, clases, secuencias y microservicios.

---

## 👥 Contexto funcional

Los equipos gestionados corresponden a maquinaria utilizada en **obras civiles privadas**, permitiendo llevar un control centralizado de:

- Estado operativo.  
- Historial de mantenimiento.  
- Ubicación actual.  
- Información técnica y administrativa (seguro, patente, etc.).

Este microservicio es un bloque fundamental del sistema ObraSmart para la trazabilidad de activos físicos dentro de los proyectos de construcción.
