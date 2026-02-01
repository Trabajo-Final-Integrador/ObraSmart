import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { MarcaService, MarcaDTO } from 'src/app/service/marca.service';
import { ModeloService, ModeloDTO } from 'src/app/service/modelo.service';
import { TipoEquipoService, TipoEquipoDTO } from 'src/app/service/tipo-equipo.service';
import { SidebarService } from 'src/app/service/sidebar.service';
import Swal from 'sweetalert2';
import { MediaService } from 'src/app/service/media.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-equipos-listado',
  templateUrl: './equipos-listado.component.html',
  styleUrls: ['./equipos-listado.component.scss'],
})
export class EquiposListadoComponent implements OnInit {
  equipos: EquipoDTO[] = [];
  marcas: MarcaDTO[] = [];
  modelos: ModeloDTO[] = [];
  tipos: TipoEquipoDTO[] = [];

  private _filtro: string = '';
  paginaActual = 1;
  equiposPorPagina = 5;

  selectedEquipo?: EquipoDTO;
  showModal = false;
  cacheBust = Date.now();
  fileToUpload?: File;
  linkUrl = '';
  readonly placeholderData =
    'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="120" height="90" viewBox="0 0 120 90"><rect width="120" height="90" fill="%23e5e7eb"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%239ca3af" font-family="Arial" font-size="12">Sin imagen</text></svg>';

  get filtro(): string {
    return this._filtro;
  }

  set filtro(value: string) {
    this._filtro = value;
    this.paginaActual = 1;
  }

  constructor(
    private equipoService: EquipoService,
    private marcaService: MarcaService,
    private modeloService: ModeloService,
    private tipoService: TipoEquipoService,
    private router: Router,
    private sidebarService: SidebarService,
    private mediaService: MediaService,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.equipoService.listar().subscribe({
      next: (data) => (this.equipos = data),
      error: (err) => console.error(err),
    });
    this.marcaService.listar().subscribe((r) => (this.marcas = r));
    this.modeloService.listar().subscribe((r) => (this.modelos = r));
    this.tipoService.listar().subscribe((r) => (this.tipos = r));
  }

  nuevoEquipo() {
    this.router.navigate(['/equipos/nuevo']);
  }

  editarEquipo(id: number) {
    this.router.navigate(['/equipos/editar', id]);
  }

  verDetalles(equipo: EquipoDTO) {
    this.selectedEquipo = equipo;
    this.cacheBust = Date.now();
    this.linkUrl = '';
    this.fileToUpload = undefined;
    this.showModal = true;
  }

  verUbicacion(equipo: EquipoDTO) {
    if (!equipo?.id) return;
    if (!equipo.latitud || !equipo.longitud) {
      Swal.fire({
        icon: 'info',
        title: 'Información',
        text: 'Equipo sin ubicación registrada.',
        confirmButtonColor: '#00796b',
      });
      return;
    }
    this.router.navigate(['/principal'], { queryParams: { equipoId: equipo.id } });
  }

  eliminarEquipo(equipo: EquipoDTO) {
    Swal.fire({
      title: this.translate.instant('equipos.alert.delete.title'),
      text: this.translate.instant('equipos.alert.delete.text', { name: equipo.nombre, code: equipo.codigoInterno }),
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: this.translate.instant('equipos.alert.delete.confirm'),
      cancelButtonText: this.translate.instant('common.cancel'),
    }).then((result) => {
      if (result.isConfirmed && equipo.id) {
        this.equipoService.eliminar(equipo.id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: this.translate.instant('equipos.alert.delete.successTitle'),
              text: this.translate.instant('equipos.alert.delete.successText'),
              timer: 1500,
              showConfirmButton: false,
            });
            this.equipoService.listar().subscribe({
              next: (data) => (this.equipos = data),
              error: (err) => console.error(err),
            });
          },
          error: (err) => {
            console.error('Error al eliminar:', err);
            Swal.fire({
              icon: 'error',
              title: this.translate.instant('common.error'),
              text: this.translate.instant('equipos.alert.delete.error'),
              confirmButtonColor: '#00796b',
            });
          },
        });
      }
    });
  }

  getMarcaNombre(idMarca: number): string {
    const marca = this.marcas.find((m) => m.id === idMarca);
    return marca ? marca.nombre : '';
  }

  getModeloNombre(idModelo: number): string {
    const modelo = this.modelos.find((m) => m.id === idModelo);
    return modelo ? modelo.nombre : '';
  }

  getTipoEquipoNombre(idTipo: number): string {
    const tipo = this.tipos.find((t) => t.id === idTipo);
    return tipo ? tipo.nombre : '';
  }

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

  get equiposFiltrados(): EquipoDTO[] {
    if (!this.filtro.trim()) {
      return this.equipos;
    }
    const filtroLower = this.filtro.toLowerCase().trim();
    return this.equipos.filter(
      (e) =>
        e.codigoInterno?.toLowerCase().includes(filtroLower) ||
        e.nombre?.toLowerCase().includes(filtroLower) ||
        this.getMarcaNombre(e.idMarca).toLowerCase().includes(filtroLower) ||
        this.getModeloNombre(e.idModelo).toLowerCase().includes(filtroLower) ||
        this.getTipoEquipoNombre(e.idTipoEquipo).toLowerCase().includes(filtroLower)
    );
  }

  get totalPaginas(): number {
    return Math.ceil(this.equiposFiltrados.length / this.equiposPorPagina);
  }

  get equiposPaginados(): EquipoDTO[] {
    const start = (this.paginaActual - 1) * this.equiposPorPagina;
    return this.equiposFiltrados.slice(start, start + this.equiposPorPagina);
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

  getBadgeClass(estado: string): string {
    if (!estado) return 'badge';
    switch (estado.toUpperCase()) {
      case 'OPERATIVO':
        return 'badge badge-success';
      case 'MANTENIMIENTO':
        return 'badge badge-warning';
      case 'FUERA_SERVICIO':
      case 'FUERA DE SERVICIO':
        return 'badge badge-danger';
      default:
        return 'badge';
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.fileToUpload = input.files[0];
    }
  }

  subirImagen(): void {
    if (!this.selectedEquipo?.id || !this.fileToUpload) return;
    this.mediaService.uploadEquipoImage(this.selectedEquipo.id, this.fileToUpload).subscribe({
      next: () => {
        this.cacheBust = Date.now();
        this.fileToUpload = undefined;
      },
      error: (err) => console.error('Error subiendo imagen', err),
    });
  }

  guardarLink(): void {
    if (!this.selectedEquipo?.id || !this.linkUrl.trim()) return;
    this.mediaService.setEquipoImageLink(this.selectedEquipo.id, this.linkUrl.trim()).subscribe({
      next: () => {
        this.cacheBust = Date.now();
        this.linkUrl = '';
      },
      error: (err) => console.error('Error guardando link', err),
    });
  }

  cerrarModal(): void {
    this.showModal = false;
    this.selectedEquipo = undefined;
    this.fileToUpload = undefined;
    this.linkUrl = '';
  }

  getEquipoImageSrc(): string {
    if (!this.selectedEquipo?.id) return 'assets/no-image.png';
    return `${this.mediaService.getEquipoImageUrl(this.selectedEquipo.id)}?t=${this.cacheBust}`;
  }

  imgFallback(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img && img.src !== this.placeholderData) {
      img.src = this.placeholderData;
    }
  }

  getMiniaturaEquipoUrl(equipoId?: number | string): string {
    if (!equipoId) return this.placeholderData;
    return this.mediaService.getEquipoImageUrl(equipoId);
  }
}
