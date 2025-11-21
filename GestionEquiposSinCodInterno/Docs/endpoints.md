# Endpoints del Microservicio GestiónEquipos

Este documento describe los endpoints REST expuestos por el microservicio **GestionEquipos**.  
Los ejemplos de request/response se presentan en formato JSON.

---

## 1. Equipos

### 1.1 Obtener todos los equipos

**Acción:** Listar equipos  
**Método:** GET  
**URL:** `/api/equipos`  

**Descripción:**  
Retorna el listado completo de equipos registrados en el sistema.

**Response (ejemplo):**
```json
[
  {
    "id": 1,
    "nombre": "Retroexcavadora CAT",
    "tipoEquipo": "Maquinaria pesada",
    "marca": "Caterpillar",
    "modelo": "320D",
    "numeroSerie": "ABC12345",
    "anioFabricacion": 2015,
    "potenciaHp": 150.0,
    "combustible": "DIESEL",
    "estadoOperativo": "DISPONIBLE",
    "kilometrajeHorasUso": 3500.0,
    "fechaUltimoMantenimiento": "2025-01-10",
    "proximoMantenimiento": "2025-06-10",
    "responsableMantenimiento": "Juan Pérez",
    "numeroPatente": "AA123BB",
    "seguroVigente": true,
    "fechaVencimientoSeguro": "2025-12-31",
    "ubicacionActual": "Depósito Central",
    "activo": true,
    "latitud": -31.4201,
    "longitud": -64.1888
  }
]
```

---

### 1.2 Obtener un equipo por ID

**Acción:** Obtener detalle de equipo  
**Método:** GET  
**URL:** `/api/equipos/{id}`  

**Path variable:**  
- `id`: identificador del equipo.

**Response (ejemplo):**
```json
{
  "id": 1,
  "nombre": "Retroexcavadora CAT",
  "tipoEquipo": "Maquinaria pesada",
  "marca": "Caterpillar",
  "modelo": "320D",
  "numeroSerie": "ABC12345",
  "anioFabricacion": 2015,
  "potenciaHp": 150.0,
  "combustible": "DIESEL",
  "estadoOperativo": "DISPONIBLE",
  "kilometrajeHorasUso": 3500.0,
  "fechaUltimoMantenimiento": "2025-01-10",
  "proximoMantenimiento": "2025-06-10",
  "responsableMantenimiento": "Juan Pérez",
  "numeroPatente": "AA123BB",
  "seguroVigente": true,
  "fechaVencimientoSeguro": "2025-12-31",
  "ubicacionActual": "Depósito Central",
  "activo": true,
  "latitud": -31.4201,
  "longitud": -64.1888
}
```

---

### 1.3 Crear un equipo

**Acción:** Registrar nuevo equipo  
**Método:** POST  
**URL:** `/api/equipos`  

**Request (ejemplo):**
```json
{
  "nombre": "Retroexcavadora CAT",
  "tipoEquipoId": 1,
  "marcaId": 2,
  "modeloId": 3,
  "numeroSerie": "ABC12345",
  "anioFabricacion": 2015,
  "potenciaHp": 150.0,
  "combustible": "DIESEL",
  "estadoOperativo": "DISPONIBLE",
  "kilometrajeHorasUso": 0.0,
  "fechaUltimoMantenimiento": "2025-01-10",
  "proximoMantenimiento": "2025-06-10",
  "responsableMantenimiento": "Juan Pérez",
  "numeroPatente": "AA123BB",
  "seguroVigente": true,
  "fechaVencimientoSeguro": "2025-12-31",
  "ubicacionActual": "Depósito Central",
  "activo": true,
  "latitud": -31.4201,
  "longitud": -64.1888
}
```

**Response (ejemplo):**
```json
{
  "id": 1,
  "mensaje": "Equipo creado correctamente"
}
```

---

### 1.4 Actualizar un equipo

**Acción:** Actualizar equipo  
**Método:** PUT  
**URL:** `/api/equipos/{id}`  

**Descripción:**  
Actualiza la información de un equipo existente. El cuerpo del request es similar al de creación.

---

### 1.5 Eliminar un equipo

**Acción:** Eliminar / dar de baja equipo  
**Método:** DELETE  
**URL:** `/api/equipos/{id}`  

**Descripción:**  
Puede implementar baja lógica (`activo = false`) o eliminación física según las necesidades del negocio.

---

## 2. Tipos de equipo

### 2.1 Listar tipos de equipo

**Método:** GET  
**URL:** `/api/tipo-equipo`  

### 2.2 Crear tipo de equipo

**Método:** POST  
**URL:** `/api/tipo-equipo`  

**Request (ejemplo):**
```json
{
  "nombre": "Maquinaria pesada",
  "descripcion": "Equipos de gran porte utilizados en movimiento de suelos",
  "imagenURL": "https://ejemplo.com/maquinaria-pesada.png"
}
```

---

## 3. Marcas

- `GET /api/marcas`  
- `POST /api/marcas`  
- `PUT /api/marcas/{id}`  
- `DELETE /api/marcas/{id}`  

---

## 4. Modelos

- `GET /api/modelos`  
- `POST /api/modelos`  
- `PUT /api/modelos/{id}`  
- `DELETE /api/modelos/{id}`  

---

## 5. Notas

- La documentación OpenAPI/Swagger debe estar habilitada en entorno de desarrollo para facilitar pruebas manuales.  
- Los ejemplos anteriores pueden ajustarse a los DTO reales definidos en el proyecto.  
- Es recomendable referenciar aquí las Historias de Usuario (HU) relacionadas con la gestión de equipos.
