import { Component, OnInit } from '@angular/core';
import { ReparacionService, ReparacionResponseDTO } from 'src/app/service/reparaciones.service';
import { SidebarService } from 'src/app/service/sidebar.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listado-reparaciones',
  templateUrl: './listado-reparaciones.component.html',
  styleUrls: ['./listado-reparaciones.component.scss']
})
export class ListadoReparacionesComponent implements OnInit {

  reparaciones: ReparacionResponseDTO[] = [];
  loading = true;
  terminoBusqueda: string = '';
  reparacionesOriginal: ReparacionResponseDTO[] = [];
  reparacionesFiltradas: ReparacionResponseDTO[] = [];

  // Paginación
  paginaActual = 1;
  reparacionesPorPagina = 10;

  constructor(
    private repSrv: ReparacionService,
    private sidebarService: SidebarService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarReparaciones();
  }

  cargarReparaciones(): void {
    this.loading = true;
    this.repSrv.listar().subscribe({
      next: data => {
        this.reparaciones = data;
        this.reparacionesOriginal = [...data];
        this.reparacionesFiltradas = [...data];
        this.loading = false;
      },
      error: err => {
        console.error(err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Error al cargar reparaciones',
          text: 'No se pudieron cargar las reparaciones.'
        });
      }
    });
  }

  get reparacionesPaginadas(): ReparacionResponseDTO[] {
    const inicio = (this.paginaActual - 1) * this.reparacionesPorPagina;
    const fin = inicio + this.reparacionesPorPagina;
    return this.reparacionesFiltradas.slice(inicio, fin);
  }

  get totalPaginas(): number {
    return Math.ceil(this.reparacionesFiltradas.length / this.reparacionesPorPagina);
  }

  nuevaReparacion() {
    this.router.navigate(['/reparaciones/crear']);
  }

  filtrar() {
    const t = this.terminoBusqueda.toLowerCase().trim();

    if (!t) {
      this.reparacionesFiltradas = [...this.reparacionesOriginal];
    } else {
      this.reparacionesFiltradas = this.reparacionesOriginal.filter(r =>
        String(r.equipoCodigoInterno).toLowerCase().includes(t) ||
        r.equipoNombre?.toLowerCase().includes(t) ||
        r.descripcion?.toLowerCase().includes(t) ||
        r.responsableNombreCompleto?.toLowerCase().includes(t)
      );
    }
    this.paginaActual = 1;
  }

abrirFiltros() {
  console.log("Abrir modal de filtros (si lo querés lo hacemos)");
}

exportar() {
  console.log("Exportar a Excel / CSV — te lo agrego si querés");
}

editarReparacion(id: number): void {
  this.router.navigate(['/reparaciones/editar', id]);
}

verDetalle(id: number): void {
  this.router.navigate(['/reparaciones/detalle', id]);
}

cancelarReparacion(id: number): void {
  Swal.fire({
    title: '¿Cancelar reparación?',
    text: 'Esta acción marcará la reparación como cancelada.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Sí, cancelar',
    cancelButtonText: 'No, volver'
  }).then((result) => {
    if (result.isConfirmed) {
      this.repSrv.cancelar(id).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Reparación cancelada',
            text: 'La reparación ha sido cancelada correctamente.',
            timer: 1500,
            showConfirmButton: false
          });
          this.cargarReparaciones();
        },
        error: (err) => {
          console.error(err);
          Swal.fire({
            icon: 'error',
            title: 'Error al cancelar',
            text: err?.error?.message ?? 'No se pudo cancelar la reparación.'
          });
        }
      });
    }
  });
}

// Método del sidebar
toggleSidebar(): void {
  this.sidebarService.toggleSidebar();
}

// Métodos de paginación
primeraPagina(): void {
  this.paginaActual = 1;
}

paginaAnterior(): void {
  if (this.paginaActual > 1) {
    this.paginaActual--;
  }
}

paginaSiguiente(): void {
  if (this.paginaActual < this.totalPaginas) {
    this.paginaActual++;
  }
}

ultimaPagina(): void {
  this.paginaActual = this.totalPaginas;
}
}
