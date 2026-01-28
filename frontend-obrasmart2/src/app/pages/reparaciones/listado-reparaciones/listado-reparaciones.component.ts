import { Component, OnInit } from '@angular/core';
import { ReparacionService, ReparacionResponseDTO } from 'src/app/service/reparaciones.service';
import { SidebarService } from 'src/app/service/sidebar.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { MediaService } from 'src/app/service/media.service';
import { appendCacheBust, buildReparacionMediaId, MEDIA_PLACEHOLDER } from '../../../shared/utils/media-helper';
import { TranslateService } from '@ngx-translate/core';

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
  readonly placeholderData = MEDIA_PLACEHOLDER;
  private cacheBust: Record<number, number> = {};
  private imageFailed = new Set<number>();

  detalleVisible = false;
  reparacionSeleccionada?: ReparacionResponseDTO;
  detalleImagen = this.placeholderData;

  // Paginación
  paginaActual = 1;
  reparacionesPorPagina = 5;

  constructor(
    private repSrv: ReparacionService,
    private sidebarService: SidebarService,
    private router: Router,
    private media: MediaService,
    private translate: TranslateService
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
        data?.forEach((r) => {
          const rid = this.getReparacionId(r);
          if (rid) this.cacheBust[rid] = Date.now();
        });
        this.loading = false;
        console.log('Datos de reparaciones:', data);
        console.log('Primera reparación responsable:', data[0]?.responsableNombreCompleto);
      },
      error: err => {
        console.error(err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('repairs.alert.loadErrorTitle'),
          text: this.translate.instant('repairs.alert.loadErrorText')
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

  abrirFiltros(): void {
    console.log('Abrir modal de filtros (si lo querés lo hacemos)');
  }

  exportar(): void {
    console.log('Exportar a Excel / CSV — te lo agrego si querés');
  }

  editarReparacion(id: number): void {
    this.router.navigate(['/reparaciones/editar', id]);
  }

  verDetalle(id: number): void {
    const rep = this.reparaciones.find((r) => this.getReparacionId(r) === id);
    if (rep) {
      this.reparacionSeleccionada = rep;
      this.detalleImagen = this.getReparacionImg(rep);
      this.detalleVisible = true;
    }
  }

  cerrarDetalleModal(): void {
    this.detalleVisible = false;
    this.reparacionSeleccionada = undefined;
    this.detalleImagen = this.placeholderData;
  }

  cancelarReparacion(id: number): void {
    Swal.fire({
      title: this.translate.instant('repairs.alert.cancel.title'),
      text: this.translate.instant('repairs.alert.cancel.text'),
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: this.translate.instant('repairs.alert.cancel.confirm'),
      cancelButtonText: this.translate.instant('common.cancel')
    }).then((result) => {
      if (result.isConfirmed) {
        this.repSrv.cancelar(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: this.translate.instant('repairs.alert.cancel.successTitle'),
              text: this.translate.instant('repairs.alert.cancel.successText'),
              timer: 1500,
              showConfirmButton: false
            });
            this.cargarReparaciones();
          },
          error: (err) => {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: this.translate.instant('repairs.alert.cancel.errorTitle'),
              text: err?.error?.message ?? this.translate.instant('repairs.alert.cancel.errorText')
            });
          }
        });
      }
    });
  }

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

  primeraPagina(): void {
    this.paginaActual = 1;
    console.log('Primera página:', this.paginaActual);
  }

  paginaAnterior(): void {
    if (this.paginaActual > 1) {
      this.paginaActual--;
      console.log('Página anterior:', this.paginaActual);
    }
  }

  getReparacionId(r: ReparacionResponseDTO): number | undefined {
    return (r as any)?.id ?? (r as any)?.reparacionId ?? (r as any)?.idReparacion;
  }

  paginaSiguiente(): void {
    console.log('Intentando ir a siguiente página. Actual:', this.paginaActual, 'Total:', this.totalPaginas);
    if (this.paginaActual < this.totalPaginas) {
      this.paginaActual++;
      console.log('Página siguiente:', this.paginaActual);
    } else {
      console.log('Ya estás en la última página');
    }
  }

  ultimaPagina(): void {
    this.paginaActual = this.totalPaginas;
    console.log('Última página:', this.paginaActual);
  }

  onCambiarPorPagina(valor: number): void {
    console.log('Cambiando items por página a:', valor);
    this.reparacionesPorPagina = valor;
    this.paginaActual = 1;
  }

  getReparacionImg(r: ReparacionResponseDTO): string {
    const rid = this.getReparacionId(r);
    if (!rid || this.imageFailed.has(rid)) return this.placeholderData;
    const mediaId = buildReparacionMediaId(rid);
    const base = this.media.getEquipoImageUrl(mediaId);
    const bust = this.cacheBust[rid];
    return appendCacheBust(base, bust);
  }

  onImgError(r: ReparacionResponseDTO, event?: Event): void {
    if (event) {
      const img = event.target as HTMLImageElement;
      if (img) {
        img.onerror = null as any;
        img.src = this.placeholderData;
      }
    }
    const rid = this.getReparacionId(r);
    if (rid) {
      this.imageFailed.add(rid);
    }
  }
}
