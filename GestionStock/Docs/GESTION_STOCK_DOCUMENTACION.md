
# G08 - Gestión de Stock

## 1. ¿Qué hace?
Este microservicio administra el stock de repuestos y las operaciones con proveedores. 
Incluye: movimientos de stock, órdenes de compra, categorías, repuestos y proveedores.

## 2. Entidades principales
- Repuesto
- CategoriaRepuesto
- MovimientoStock
- OrdenCompra
- OrdenCompraItem
- Proveedor
- CondicionIVA
- TipoProveedor
- EstadoProveedor
- EstadoOrdenCompra
- TipoMovimiento

## 3. Endpoints expuestos
### /api/proveedores
- GET / → listar proveedores
- POST / → crear proveedor
- PUT /{id} → actualizar proveedor
- DELETE /{id} → eliminar proveedor

### /api/repuestos
- GET / → listar repuestos
- POST / → crear repuesto
- PUT /{id} → actualizar repuesto
- DELETE /{id} → eliminar repuesto

### /api/movimientos
- POST / → registrar movimiento de stock
- GET / → listar movimientos

### /api/ordenes
- GET / → listar órdenes de compra
- POST / → crear orden de compra
- PUT /{id}/aprobar → aprobar orden
- PUT /{id}/rechazar → rechazar orden

## 4. Comunicación con otros microservicios
- Se comunica con GestiónLogin mediante sesión (SessionAuthFilter).
- No consume otros microservicios directamente.

## 5. Base de datos
- Motor: PostgreSQL
- Base: gestion_stockdb
- Tablas:
  - categoria_repuesto
  - repuesto
  - movimiento_stock
  - orden_compra
  - orden_compra_item
  - proveedor

## 6. Dockerfile
Actualmente no posee, pero se recomienda agregar uno similar al resto de ObraSmart.

## 7. Dependencias principales del pom.xml
- Spring Web
- Spring Data JPA
- Spring Validation
- Lombok
- PostgreSQL Driver
- Spring Security (modificada)
- RestTemplate

## 8. Notas importantes
- Maneja stock mínimo.
- Maneja proveedores activos/inactivos.
- Validación de códigos duplicados en repuestos.
- Filtrado por categorías.
