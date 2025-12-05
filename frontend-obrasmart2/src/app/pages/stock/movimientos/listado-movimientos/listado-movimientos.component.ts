import { Component, OnInit } from '@angular/core';
import { MovimientosService } from 'src/app/service/movimiento-stock.service';
import { Movimiento } from 'src/app/pages/stock/movimientos/movimiento.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-movimientos-listado',
  templateUrl: './listado-movimientos.component.html',
  styleUrls: ['./listado-movimientos.component.scss']
})
export class MovimientosListadoComponent implements OnInit {

  // UI States
  menuAbierto = false;
  loading = true;
  submenuUsuariosOpen = false;
  submenuReparacionOpen = false;
  submenuStockOpen = false;

  // Datos
  movimientos: Movimiento[] = [];
  repuestos: any[] = [];

  // Paginación
  paginaActual = 1;
  movimientosPorPagina = 5;

  constructor(private movSrv: MovimientosService, private router: Router) {}

  ngOnInit(): void {
    this.cargarMovimientos();
  }

  cargarMovimientos() {
    this.movSrv.listar().subscribe({
      next: data => {
        this.movimientos = data;
        this.loading = false;
      },
      error: err => {
        console.error('❌ Error cargando movimientos:', err);
        this.loading = false;
      }
    });
  }

  // Paginación
  get totalPaginas(): number {
    return Math.ceil(this.movimientos.length / this.movimientosPorPagina) || 1;
  }

  get movimientosPaginados(): Movimiento[] {
    const inicio = (this.paginaActual - 1) * this.movimientosPorPagina;
    const fin = inicio + this.movimientosPorPagina;
    return this.movimientos.slice(inicio, fin);
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

  formatFecha(f: string): string {
    return new Date(f).toLocaleString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
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
  nuevoMovimiento() {
    this.router.navigate(['/stock/movimientos/crear']);
  }

}
