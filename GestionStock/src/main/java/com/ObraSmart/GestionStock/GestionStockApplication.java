package com.ObraSmart.GestionStock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionStockApplication.class, args);
	}

}
/**
                           ✅ info del GestionStok:
✅ Bajas lógicas - deleteRepuesto() ahora usa setActivo(false)

✅ Solo repuestos activos - Todos los métodos usan findByActivoTrue()

✅ Validación de nombre único - No permite duplicados en repuestos activos

✅ Validación de cantidades - No permite cantidades <= 0

✅ Mensajes de error específicos - Indica si es por inactividad

✅ Métodos de estadísticas - Para dashboards
*/

/**
✅ MEJORAS AGREGADAS:
ResponseEntity - Mejor manejo de respuestas HTTP

Manejo de errores - Con try-catch para operaciones de stock

Endpoints de stock - Para gestión completa del inventario

Parámetros con valores por defecto - stockMinimo tiene valor por defecto

🚀 ENDPOINTS DISPONIBLES:
CRUD Básico:
text
GET    /api/repuestos
GET    /api/repuestos/{id}
POST   /api/repuestos
PUT    /api/repuestos/{id}
DELETE /api/repuestos/{id}


 🚀 ENDPOINTS DISPONIBLES:
 Gestión de Stock:
 
text
POST   /api/repuestos/{id}/sacar?cantidad=3
POST   /api/repuestos/{id}/agregar?cantidad=10
GET    /api/repuestos/{id}/stock
GET    /api/repuestos/alertas-stock?stockMinimo=5

*/