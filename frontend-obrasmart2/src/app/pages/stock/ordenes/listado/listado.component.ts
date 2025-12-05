import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrdenCompra } from '../orden-compra.model';
import { OrdenCompraService } from 'src/app/service/orden-compra.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listado-ordenes',
  templateUrl: './listado.component.html',
  styleUrls: ['./listado.component.scss']
})
export class ListadoComponent implements OnInit {

  // UI States
  menuAbierto = false;
  loading = true;
  submenuUsuariosOpen = false;
  submenuReparacionOpen = false;
  submenuStockOpen = false;

  // Datos
  ordenes: OrdenCompra[] = [];

  // Paginación
  paginaActual = 1;
  ordenesPorPagina = 5;

  constructor(
    private router: Router,
    private ordenSrv: OrdenCompraService
  ) {}

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes() {
    this.ordenSrv.listar().subscribe({
      next: (data) => {
        this.ordenes = data;
        this.loading = false;
      },
      error: (err) => {
        console.error("❌ Error cargando órdenes:", err);
        this.loading = false;
      }
    });
  }

  // Paginación
  get totalPaginas(): number {
    return Math.ceil(this.ordenes.length / this.ordenesPorPagina) || 1;
  }

  get ordenesPaginadas(): OrdenCompra[] {
    const inicio = (this.paginaActual - 1) * this.ordenesPorPagina;
    const fin = inicio + this.ordenesPorPagina;
    return this.ordenes.slice(inicio, fin);
  }

  primeraPagina() {
    this.paginaActual = 1;
  }

  paginaAnterior() {
    if (this.paginaActual > 1) {
      this.paginaActual--;
    }
  }

  paginaSiguiente() {
    if (this.paginaActual < this.totalPaginas) {
      this.paginaActual++;
    }
  }

  ultimaPagina() {
    this.paginaActual = this.totalPaginas;
  }

  formatFecha(f: string) {
    return new Date(f).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
  }

  formatMoneda(v: number | undefined | null): string {
    if (v === null || v === undefined) return '0,00';
    return Number(v).toLocaleString('es-ES', { minimumFractionDigits: 2 });
  }

  // UI
  toggleSidebar() {
    this.menuAbierto = !this.menuAbierto;
  }

  toggleUsuarios(event: Event) {
    event.preventDefault();
    this.submenuUsuariosOpen = !this.submenuUsuariosOpen;
  }

  toggleReparacion(event: Event) {
    event.preventDefault();
    this.submenuReparacionOpen = !this.submenuReparacionOpen;
  }

  toggleStock(event: Event) {
    event.preventDefault();
    this.submenuStockOpen = !this.submenuStockOpen;
  }

  // Acciones
  nuevaOrden() {
    this.router.navigate(['/stock/ordenes/crear']);
  }

  verDetalle(id: number) {
    this.router.navigate(['/stock/ordenes/detalle', id]);
  }

  editarOrden(id: number) {
    this.router.navigate(['/stock/ordenes/editar', id]);
  }

  eliminarOrden(id: number) {
    Swal.fire({
      title: '¿Eliminar orden?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        // Aquí irá la llamada al servicio de eliminación
        Swal.fire({
          icon: 'success',
          title: 'Orden eliminada',
          text: 'La orden ha sido eliminada correctamente',
          timer: 1500,
          showConfirmButton: false
        });
        this.cargarOrdenes();
      }
    });
  }

  descargarPDF(id: number) {
    Swal.fire({
      icon: 'info',
      title: 'Descargando PDF',
      text: 'La orden de compra se está generando...',
      timer: 1500,
      showConfirmButton: false
    });
    // Aquí irá la llamada al servicio de descarga PDF
  }

  aprobar(id: number) {
    Swal.fire({
      title: '¿Aprobar esta orden?',
      text: 'La orden será marcada como aprobada',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#00796b',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, aprobar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.ordenSrv.aprobar(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Orden aprobada',
              text: 'La orden ha sido aprobada correctamente',
              timer: 1500,
              showConfirmButton: false
            });
            this.cargarOrdenes();
          },
          error: (err) => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo aprobar la orden',
              confirmButtonColor: '#00796b'
            });
          }
        });
      }
    });
  }

  recibir(id: number) {
    Swal.fire({
      title: '¿Marcar como recibida?',
      text: 'La orden será marcada como recibida',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#00796b',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, recibir',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.ordenSrv.recibir(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Orden recibida',
              text: 'La orden ha sido marcada como recibida',
              timer: 1500,
              showConfirmButton: false
            });
            this.cargarOrdenes();
          },
          error: (err) => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo marcar la orden como recibida',
              confirmButtonColor: '#00796b'
            });
          }
        });
      }
    });
  }

  cancelar(id: number) {
    Swal.fire({
      title: '¿Cancelar orden?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, cancelar',
      cancelButtonText: 'No'
    }).then((result) => {
      if (result.isConfirmed) {
        this.ordenSrv.cancelar(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Orden cancelada',
              text: 'La orden ha sido cancelada',
              timer: 1500,
              showConfirmButton: false
            });
            this.cargarOrdenes();
          },
          error: (err) => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo cancelar la orden',
              confirmButtonColor: '#00796b'
            });
          }
        });
      }
    });
  }

}
