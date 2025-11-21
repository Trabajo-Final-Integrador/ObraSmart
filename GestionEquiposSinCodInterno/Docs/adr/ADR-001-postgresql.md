# ADR-001: Uso de PostgreSQL para GestiónEquipos

## Contexto

El microservicio GestionEquipos requiere una base de datos relacional robusta para almacenar información de equipos, tipos, marcas y modelos.

## Decisión

Se adopta **PostgreSQL** como motor de base de datos para este microservicio.

## Justificación

- Es un motor maduro, open source y ampliamente utilizado.  
- Excelente soporte para tipos de datos avanzados.  
- Integración nativa con Spring Data JPA y Hibernate.  
- Alineamiento con el resto del ecosistema de ObraSmart.

## Consecuencias

- La configuración de conexión se centraliza en `application.properties`.  
- El despliegue future en Docker deberá incluir un contenedor PostgreSQL.
