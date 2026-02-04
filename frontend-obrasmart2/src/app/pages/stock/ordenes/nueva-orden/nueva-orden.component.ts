import { Component, OnInit } from '@angular/core';
import { OrdenCompraService, OrdenCompraDTO } from 'src/app/service/orden-compra.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { SidebarService } from 'src/app/service/sidebar.service';
import Swal from 'sweetalert2';
import { environment } from '../../../../../environments/environment';


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
    private ordenService: OrdenCompraService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit(): void {
    this.cargarProveedores();
    this.cargarRepuestos();
  }

  // ==========================================================
  // 🔹 CARGA DE DATOS DEL BACKEND
  // ==========================================================

  cargarProveedores() {
    this.http.get<any[]>(`${environment.apiUrl}/proveedores`, { withCredentials: true })
      .subscribe({
        next: (data) => this.proveedores = data,
        error: () => Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Error cargando proveedores'
        })
      });
  }

  cargarRepuestos() {
    this.http.get<any[]>(`${environment.apiUrl}/repuestos`)
      .subscribe({
        next: (data) => this.repuestos = data,
        error: () => Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Error cargando repuestos'
        })
      });
  }

  // ==========================================================
  // 🔹 MANEJO DE ITEMS
  // ==========================================================

  agregarItemTemp() {
    if (!this.itemTemp.idRepuesto || this.itemTemp.cantidad <= 0 || this.itemTemp.precioUnitario <= 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Datos incompletos',
        text: 'Complete todos los datos del repuesto'
      });
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
      Swal.fire({
        icon: 'warning',
        title: 'Proveedor requerido',
        text: 'Debe seleccionar un proveedor'
      });
      return;
    }

    if (this.nuevaOrden.items.length === 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Items requeridos',
        text: 'Debe agregar al menos un repuesto'
      });
      return;
    }

    this.isSubmitting = true;

    this.ordenService.crear(this.nuevaOrden)
      .subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: 'Orden creada exitosamente',
            confirmButtonText: 'Aceptar'
          }).then(() => {
            this.router.navigate(['/stock/ordenes']);
          });
        },
        error: (err) => {
          console.error('❌ Error al crear orden:', err);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al guardar la orden'
          });
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

  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }

}
