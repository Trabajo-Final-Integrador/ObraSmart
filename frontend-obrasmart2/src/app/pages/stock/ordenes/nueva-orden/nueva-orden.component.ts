import { Component, OnInit } from '@angular/core';
import { OrdenCompraService, OrdenCompraDTO } from 'src/app/service/orden-compra.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';


@Component({
  selector: 'app-nueva-orden',
  templateUrl: './nueva-orden.component.html',
  styleUrls: ['./nueva-orden.component.scss']
})
export class NuevaOrdenComponent implements OnInit {

  // Proveedores cargados desde el backend
  proveedores: any[] = [];

  // Repuestos cargados desde el backend
  repuestos: any[] = [];

  

  // Orden final que enviamos al backend
 nuevaOrden: any = {
  idProveedor: 0,
  items: []
};


   itemTemp = {
    idRepuesto: 0,
    cantidad: 1,
    precioUnitario: 0
  };


  isSubmitting = false;

  constructor(
    private router: Router,
    private http: HttpClient,
    private ordenService: OrdenCompraService
  ) {}

  ngOnInit(): void {
    this.cargarProveedores();
    this.cargarRepuestos();
  }

  // ==========================================================
  // 🔹 CARGA DE DATOS DEL BACKEND
  // ==========================================================

  cargarProveedores() {
    this.http.get<any[]>('http://localhost:8085/proveedores', { withCredentials: true })
      .subscribe({
        next: (data) => this.proveedores = data,
        error: () => alert("Error cargando proveedores")
      });
  }

  cargarRepuestos() {
    this.http.get<any[]>('http://localhost:8085/repuestos')
      .subscribe({
        next: (data) => this.repuestos = data,
        error: () => alert("Error cargando repuestos")
      });
  }

  // ==========================================================
  // 🔹 MANEJO DE ITEMS
  // ==========================================================

  agregarItemTemp() {
    if (!this.itemTemp.idRepuesto || this.itemTemp.cantidad <= 0 || this.itemTemp.precioUnitario <= 0) {
      alert("Complete todos los datos del repuesto");
      return;
    }

    //const repuesto = this.repuestos.find(r => r.id === this.itemTemp.idRepuesto);

    this.nuevaOrden.items.push({
      idRepuesto: this.itemTemp.idRepuesto,
      cantidad: this.itemTemp.cantidad,
      precioUnitario: this.itemTemp.precioUnitario
    });

    // Reset del item temporal
    this.itemTemp = { idRepuesto: 0, cantidad: 1, precioUnitario: 0 };

    // Para mostrar el nombre en la tabla (UI)
   
    
  }

  eliminarItem(i: number) {
    this.nuevaOrden.items.splice(i, 1);
  }

  // ==========================================================
  // 🔹 TOTALES
  // ==========================================================

  get totalGeneral(): number {
   return this.nuevaOrden.items.reduce(
  (sum: number, item: any) => sum + item.cantidad * item.precioUnitario,
  0
);

  }

  // ==========================================================
  // 🔹 GUARDAR ORDEN
  // ==========================================================

  guardar() {
    if (!this.nuevaOrden.idProveedor) {
      alert("Debe seleccionar un proveedor");
      return;
    }

    if (this.nuevaOrden.items.length === 0) {
      alert("Debe agregar al menos un repuesto");
      return;
    }

    this.isSubmitting = true;

    this.ordenService.crear(this.nuevaOrden)
      .subscribe({
        next: () => {
          alert("Orden creada exitosamente");
          this.router.navigate(['/stock/ordenes']);
        },
        error: (err) => {
          console.error('❌ Error al crear orden:', err);
          alert("Error al guardar la orden");
          this.isSubmitting = false;
        }
      });
  }
  obtenerNombreRepuesto(id: number): string {
  const r = this.repuestos.find(x => x.id === id);
  return r ? r.nombre : '—';
}


  volver() {
    this.router.navigate(['/stock/ordenes']);
  }

}
