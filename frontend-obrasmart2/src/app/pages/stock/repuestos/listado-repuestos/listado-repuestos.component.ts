import { Component, OnInit } from '@angular/core';
import { RepuestoService, RepuestoDTO } from 'src/app/service/repuesto.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listado-repuestos',
  templateUrl: './listado-repuestos.component.html',
  styleUrls: ['./listado-repuestos.component.scss']
})
export class ListadoRepuestosComponent implements OnInit {

  repuestos: RepuestoDTO[] = [];
  loading = true;

  // Variables para el filtro y paginación
  private _filtro: string = '';
  paginaActual = 1;
  repuestosPorPagina = 5;

  // Variables para el menú
  menuAbierto = false;
  submenuUsuariosOpen = false;
  submenuStockOpen = false;
  submenuReparacionOpen = false;

  get filtro(): string {
    return this._filtro;
  }

  set filtro(value: string) {
    this._filtro = value;
    this.paginaActual = 1; // resetear a la primera página cuando se filtra
  }

  constructor(
    private repuestoService: RepuestoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarRepuestos();
  }

  cargarRepuestos() {
    this.repuestoService.listar().subscribe({
      next: (data) => {
        this.repuestos = data;
        this.loading = false;
      },
      error: (err) => {
        console.error("Error cargando repuestos:", err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los repuestos',
          confirmButtonColor: '#00796b'
        });
      }
    });
  }

  nuevoRepuesto() {
    this.router.navigate(['/stock/repuestos/crear']);
  }

  editarRepuesto(id: number) {
    this.router.navigate(['/stock/repuestos/editar', id]);
  }

  eliminarRepuesto(repuesto: RepuestoDTO) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `Se eliminará el repuesto "${repuesto.nombre}" (${repuesto.codigo})`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed && repuesto.id) {
        this.repuestoService.eliminar(repuesto.id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Repuesto eliminado',
              text: 'El repuesto fue eliminado correctamente',
              timer: 1500,
              showConfirmButton: false
            });

            // Recargar la lista
            this.cargarRepuestos();
          },
          error: (err) => {
            console.error('Error al eliminar:', err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo eliminar el repuesto. Puede estar relacionado con otros registros.',
              confirmButtonColor: '#00796b'
            });
          }
        });
      }
    });
  }

  // Métodos para el menú
  toggleSidebar(): void {
    this.menuAbierto = !this.menuAbierto;
  }

  toggleUsuarios(event: Event): void {
    event.preventDefault();
    this.submenuUsuariosOpen = !this.submenuUsuariosOpen;
  }

  toggleStock(event: Event): void {
    event.preventDefault();
    this.submenuStockOpen = !this.submenuStockOpen;
  }

  toggleReparacion(event: Event): void {
    event.preventDefault();
    this.submenuReparacionOpen = !this.submenuReparacionOpen;
  }

  // Filtro de repuestos
  get repuestosFiltrados(): RepuestoDTO[] {
    if (!this.filtro.trim()) {
      return this.repuestos;
    }

    const filtroLower = this.filtro.toLowerCase().trim();

    return this.repuestos.filter(r =>
      r.codigo?.toLowerCase().includes(filtroLower) ||
      r.nombre?.toLowerCase().includes(filtroLower)
    );
  }

  // Paginación
  get totalPaginas(): number {
    return Math.ceil(this.repuestosFiltrados.length / this.repuestosPorPagina);
  }

  get repuestosPaginados(): RepuestoDTO[] {
    const start = (this.paginaActual - 1) * this.repuestosPorPagina;
    return this.repuestosFiltrados.slice(start, start + this.repuestosPorPagina);
  }

  paginaAnterior(): void {
    if (this.paginaActual > 1) this.paginaActual--;
  }

  paginaSiguiente(): void {
    if (this.paginaActual < this.totalPaginas) this.paginaActual++;
  }

  primeraPagina(): void {
    this.paginaActual = 1;
  }

  ultimaPagina(): void {
    this.paginaActual = this.totalPaginas;
  }

  // Estado del repuesto
  getEstado(r: RepuestoDTO): string {
    if (r.stock <= r.stockMinimo) return "BAJO";
    if (r.stock <= r.stockMinimo * 1.5) return "MEDIO";
    return "OK";
  }

  // Método para obtener la clase del badge según el estado
  getBadgeClass(r: RepuestoDTO): string {
    const estado = this.getEstado(r);

    switch (estado) {
      case 'BAJO':
        return 'badge badge-danger';
      case 'MEDIO':
        return 'badge badge-warning';
      case 'OK':
        return 'badge badge-success';
      default:
        return 'badge';
    }
  }
}
