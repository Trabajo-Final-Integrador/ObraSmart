# Base de Datos del Microservicio GestiónEquipos

Este documento resume la configuración y el modelo de datos del microservicio **GestionEquipos**.

---

## 1. Configuración

Motor de base de datos: **PostgreSQL**  

Parámetros típicos de conexión (entorno local de desarrollo):

- URL: `jdbc:postgresql://localhost:5432/GestionEquipos`  
- Usuario: `postgres`  
- Password: `123456`  
- Driver: `org.postgresql.Driver`  

Propiedades JPA/Hibernate habituales:

- `spring.jpa.hibernate.ddl-auto=update`  
- `spring.jpa.show-sql=true`  
- `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect`  

---

## 2. Tablas principales

- `equipos`  
- `tipo_equipo`  
- `marca`  
- `modelo`  

Estas tablas se corresponden con las entidades JPA documentadas en `docs/entidades.md`.

---

## 3. Relaciones

- Un `Equipo` se asocia a un único `TipoEquipo` (ManyToOne).  
- Un `Equipo` se asocia a una única `Marca` (ManyToOne).  
- Un `Equipo` se asocia a un único `Modelo` (ManyToOne).  
- Un `Modelo` se asocia a una única `Marca` (ManyToOne).  

---

## 4. DER

El Diagrama Entidad–Relación (DER) detallado debe elaborarse en Draw.io y exportarse como imagen.  
Se recomienda guardar el archivo fuente de Draw.io y el PNG resultante en la carpeta `docs/diagramas/` con nombres tales como:

- `der-equipos.drawio`  
- `der-equipos.png`  

La descripción conceptual del DER se encuentra también en `docs/diagramas/der.md`.
