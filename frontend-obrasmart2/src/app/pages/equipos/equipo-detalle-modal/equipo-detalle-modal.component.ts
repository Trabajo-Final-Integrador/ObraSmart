import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { forkJoin } from 'rxjs';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { MediaService } from 'src/app/service/media.service';
import { TipoEquipoDTO, TipoEquipoService } from 'src/app/service/tipo-equipo.service';
import { MarcaDTO, MarcaService } from 'src/app/service/marca.service';
import { ModeloDTO, ModeloService } from 'src/app/service/modelo.service';

@Component({
  selector: 'app-equipo-detalle-modal',
  templateUrl: './equipo-detalle-modal.component.html',
  styleUrls: ['./equipo-detalle-modal.component.scss'],
})
export class EquipoDetalleModalComponent implements OnChanges {
  @Input() equipo?: EquipoDTO;
  @Input() equipoId?: number;
  @Input() visible = false;
  @Input() marca = '';
  @Input() modelo = '';
  @Input() tipo = '';
  @Output() closed = new EventEmitter<void>();

  cacheBust = Date.now();
  imageSrc: string | null = null;
  fileToUpload?: File;
  linkUrl = '';
  message = '';
  messageType: 'success' | 'error' | '' = '';
  loading = false;
  loadingDetalle = false;
  private tiposCache: TipoEquipoDTO[] = [];
  private marcasCache: MarcaDTO[] = [];
  private modelosCache: ModeloDTO[] = [];
  private catLoaded = false;
  readonly placeholderData =
    'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="120" height="90" viewBox="0 0 120 90"><rect width="120" height="90" fill="%23e5e7eb"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%239ca3af" font-family="Arial" font-size="12">Sin imagen</text></svg>';

  constructor(
    private mediaService: MediaService,
    private equipoService: EquipoService,
    private tipoService: TipoEquipoService,
    private marcaService: MarcaService,
    private modeloService: ModeloService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['equipo']?.currentValue) {
      this.syncMetadataFromEquipo(changes['equipo'].currentValue);
    }

    if ((changes['equipoId'] && this.equipoId && this.visible) || (changes['visible']?.currentValue && this.equipoId)) {
      this.loadById(this.equipoId as number);
    }
  }

  loadById(id: number): void {
    if (!id) return;
    this.loadingDetalle = true;
    this.equipoService.getById(id).subscribe({
      next: (eq) => {
        this.equipo = eq;
        this.syncMetadataFromEquipo(eq);
        this.setImageFromEquipo(eq);
        this.loadingDetalle = false;
      },
      error: (err) => {
        console.error('No se pudo cargar el equipo', err);
        this.setMessage('No se pudo cargar el equipo', 'error');
        this.loadingDetalle = false;
      },
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      if (file.size > 5 * 1024 * 1024) {
        this.setMessage('El archivo supera 5MB', 'error');
        this.fileToUpload = undefined;
        return;
      }
      this.fileToUpload = file;
      this.clearMessage();
    }
  }

  subirImagen(): void {
    if (!this.equipo?.id || !this.fileToUpload) return;
    this.loading = true;
    this.mediaService.uploadEquipoImage(this.equipo.id, this.fileToUpload).subscribe({
      next: () => {
        this.cacheBust = Date.now();
        this.fileToUpload = undefined;
        this.setMessage('Imagen subida', 'success');
        this.imageSrc = `${this.mediaService.getEquipoImageUrl(this.equipo!.id!)}?t=${this.cacheBust}`;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.setMessage('Error al subir la imagen', 'error');
        this.loading = false;
      },
    });
  }

  guardarLink(): void {
    if (!this.equipo?.id || !this.linkUrl.trim()) return;
    this.loading = true;
    const link = this.linkUrl.trim();
    this.mediaService.setEquipoImageLink(this.equipo.id, link).subscribe({
      next: () => {
        this.cacheBust = Date.now();
        this.setMessage('Link guardado', 'success');
        this.imageSrc = this.equipoService.normalizeImageUrl(link) || this.imageSrc;
        this.linkUrl = '';
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.setMessage('Error al guardar el link', 'error');
        this.loading = false;
      },
    });
  }

  cerrar(): void {
    this.cacheBust = Date.now();
    this.fileToUpload = undefined;
    this.linkUrl = '';
    this.clearMessage();
    this.closed.emit();
  }

  getEquipoImageSrc(): string {
    if (this.imageSrc) return this.imageSrc;
    if (!this.equipo?.id) return this.placeholderData;
    return `${this.mediaService.getEquipoImageUrl(this.equipo.id)}?t=${this.cacheBust}`;
  }

  onImgError(event: Event): void {
    this.imageSrc = null;
  }

  private setMessage(msg: string, type: 'success' | 'error') {
    this.message = msg;
    this.messageType = type;
  }

  private clearMessage() {
    this.message = '';
    this.messageType = '';
  }

  private syncMetadataFromEquipo(eq: EquipoDTO): void {
    if (!eq) return;
    this.tipo = this.tipo || '';
    this.marca = this.marca || '';
    this.modelo = this.modelo || '';
    this.setImageFromEquipo(eq);
    this.ensureCatalogos(eq);
  }

  private setImageFromEquipo(eq: EquipoDTO): void {
    const raw: any = (eq as any).imagenUrl ?? (eq as any).imageUrl ?? (eq as any).fotoUrl;
    const normalizada = this.equipoService.normalizeImageUrl(raw);
    if (normalizada) {
      this.imageSrc = normalizada;
    } else if (eq.id) {
      this.imageSrc = `${this.mediaService.getEquipoImageUrl(eq.id)}?t=${this.cacheBust}`;
    } else {
      this.imageSrc = null;
    }
  }

  private ensureCatalogos(eq: EquipoDTO): void {
    const needsTipo = !this.tipo && !!eq.idTipoEquipo;
    const needsMarca = !this.marca && !!eq.idMarca;
    const needsModelo = !this.modelo && !!eq.idModelo;

    if (!needsTipo && !needsMarca && !needsModelo) {
      return;
    }

    if (this.catLoaded) {
      this.applyCatalogos(eq);
      return;
    }

    forkJoin({
      tipos: this.tipoService.listar(),
      marcas: this.marcaService.listar(),
      modelos: this.modeloService.listar(),
    }).subscribe({
      next: ({ tipos, marcas, modelos }) => {
        this.tiposCache = tipos || [];
        this.marcasCache = marcas || [];
        this.modelosCache = modelos || [];
        this.catLoaded = true;
        this.applyCatalogos(eq);
      },
      error: (err) => console.error('No se pudieron cargar catálogos', err),
    });
  }

  private applyCatalogos(eq: EquipoDTO): void {
    if (eq.idTipoEquipo) {
      this.tipo = this.tiposCache.find((t) => t.id === eq.idTipoEquipo)?.nombre || this.tipo;
    }
    if (eq.idMarca) {
      this.marca = this.marcasCache.find((m) => m.id === eq.idMarca)?.nombre || this.marca;
    }
    if (eq.idModelo) {
      this.modelo = this.modelosCache.find((m) => m.id === eq.idModelo)?.nombre || this.modelo;
    }
  }
}
