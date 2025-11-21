# Diagrama DER – GestiónEquipos

Este documento describe el Diagrama Entidad–Relación (DER) del microservicio **GestionEquipos**.

Se recomienda modelar al menos las siguientes entidades:

- `Equipo`  
- `TipoEquipo`  
- `Marca`  
- `Modelo`  

Relaciones sugeridas:

- `Equipo` 1 — N `TipoEquipo`  
- `Equipo` 1 — N `Marca`  
- `Equipo` 1 — N `Modelo`  
- `Modelo` N — 1 `Marca`  

Una vez creado el DER en Draw.io, exportar la imagen como `der-equipos.png` y guardarla en esta misma carpeta.
