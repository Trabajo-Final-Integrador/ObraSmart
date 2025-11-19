# Entidades del Microservicio GestiónEquipos

Este documento describe las principales entidades de dominio utilizadas por el microservicio **GestionEquipos**.

---

## 1. Equipo

Representa un equipo de obra civil privada.

Campos principales:

- `id: Long`  
- `nombre: String`  
- `tipoEquipo: TipoEquipo` (ManyToOne)  
- `marca: Marca` (ManyToOne)  
- `modelo: Modelo` (ManyToOne)  
- `numeroSerie: String` (único, obligatorio)  
- `anioFabricacion: Integer`  
- `potenciaHp: Double`  
- `combustible: Combustible` (enum)  
- `estadoOperativo: Estado_Operativo` (enum)  
- `kilometrajeHorasUso: Double`  
- `fechaUltimoMantenimiento: LocalDate`  
- `proximoMantenimiento: LocalDate`  
- `responsableMantenimiento: String`  
- `numeroPatente: String` (opcional, único)  
- `seguroVigente: Boolean`  
- `fechaVencimientoSeguro: LocalDate` (opcional)  
- `ubicacionActual: String`  
- `activo: Boolean`  
- `latitud: Double` (opcional)  
- `longitud: Double` (opcional)  

---

## 2. TipoEquipo

Clasifica el tipo de equipo.

- `id: Long`  
- `nombre: String`  
- `descripcion: String`  
- `imagenURL: String`  

---

## 3. Marca

Define la marca del equipo.

- `id: Long`  
- `nombre: String`  

---

## 4. Modelo

Modelo de una marca determinada.

- `id: Long`  
- `nombre: String`  
- `marca: Marca` (ManyToOne)  

---

## 5. Enums

### 5.1 Combustible

- `DIESEL`  
- `GASOLINA`  

### 5.2 Estado_Operativo

- `DISPONIBLE`  
- `EN_MANTENIMIENTO`  
- `FUERA_DE_SERVICIO`  
- `ASIGNADO`  

---

Estas entidades se corresponden con las tablas principales del esquema de base de datos `GestionEquipos`.
