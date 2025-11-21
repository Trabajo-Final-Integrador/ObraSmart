# ADR-003: Modelo de Equipo enriquecido con información de mantenimiento

## Contexto

El negocio de obras civiles privadas requiere controlar no solo los datos básicos del equipo, sino también su mantenimiento, seguro y estado operativo.

## Decisión

Se define una entidad `Equipo` enriquecida, con campos adicionales como:

- `estadoOperativo`  
- `fechaUltimoMantenimiento`  
- `proximoMantenimiento`  
- `seguroVigente`  
- `fechaVencimientoSeguro`  
- `responsableMantenimiento`

## Justificación

- Permite gestionar el ciclo de vida completo del equipo dentro del sistema.  
- Facilita la integración con microservicios de reparaciones y reportes.  
- Aporta valor técnico y operativo al usuario final.

## Consecuencias

- La lógica de negocio debe contemplar estos campos en validaciones y reportes.  
- La carga de datos inicial puede requerir migraciones o procesos ETL.
