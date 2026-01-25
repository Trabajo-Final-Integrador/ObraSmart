import { Component, OnInit } from '@angular/core';
import { RepuestoService, RepuestoDTO } from 'src/app/service/repuesto.service';
import { Router } from '@angular/router';
import { SidebarService } from 'src/app/service/sidebar.service';
import Swal from 'sweetalert2';
import { MediaService } from 'src/app/service/media.service';
import { appendCacheBust, buildRepuestoMediaId, MEDIA_PLACEHOLDER } from '../../../../shared/utils/media-helper';

@Component({
  selector: 'app-listado-repuestos',
  templateUrl: './listado-repuestos.component.html',
  styleUrls: ['./listado-repuestos.component.scss']
})
export class ListadoRepuestosComponent implements OnInit {

  repuestos: RepuestoDTO[] = [];
  loading = true;
  readonly placeholderData = MEDIA_PLACEHOLDER;
  private cacheBust: Record<number, number> = {};
  selectedRepuesto?: RepuestoDTO;
  mostrarDetalle = false;

  // Variables para el filtro y paginación
  private _filtro: string = '';
  paginaActual = 1;
  repuestosPorPagina = 5;

  get filtro(): string {
    return this._filtro;
  }

  set filtro(value: string) {
    this._filtro = value;
    this.paginaActual = 1; // resetear a la primera página cuando se filtra
  }

  constructor(
    private repuestoService: RepuestoService,
    private router: Router,
    private sidebarService: SidebarService,
    private media: MediaService
  ) {}

  ngOnInit(): void {
    this.cargarRepuestos();
  }

  cargarRepuestos() {
    this.repuestoService.listar().subscribe({
      next: (data) => {
        this.repuestos = data;
        data?.forEach((r) => {
          if (r?.id) this.cacheBust[r.id] = Date.now();
        });
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

  verDetalles(repuesto: RepuestoDTO) {
    this.selectedRepuesto = { ...repuesto };
    this.mostrarDetalle = true;
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
    this.sidebarService.toggleSidebar();
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

  cerrarModal(): void {
    this.mostrarDetalle = false;
    this.selectedRepuesto = undefined;
  }

  onImagenActualizada(id: number): void {
    if (!id) return;
    this.cacheBust[id] = Date.now();
  }

  getRepuestoImg(r: RepuestoDTO): string {
    if (!r?.id) return this.placeholderData;
    const mediaId = buildRepuestoMediaId(r.id);
    const base = this.media.getEquipoImageUrl(mediaId);
    const bust = this.cacheBust[r.id];
    return appendCacheBust(base, bust);
  }

  onImgError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.onerror = null as any;
      img.src = this.placeholderData;
    }
  }

  // Estado del repuesto
  getEstado(r: RepuestoDTO): string {
    if (r.stock <= r.stockMinimo) return "REPONER";
    if (r.stock <= r.stockMinimo * 1.5) return "STOCK BAJO";
    return "EN STOCK";
  }

  // Método para obtener la clase del badge según el estado
  getBadgeClass(r: RepuestoDTO): string {
    const estado = this.getEstado(r);

    switch (estado) {
      case 'REPONER':
        return 'badge badge-danger';
      case 'STOCK BAJO':
        return 'badge badge-warning';
      case 'EN STOCK':
        return 'badge badge-success';
      default:
        return 'badge';
    }
  }
}
