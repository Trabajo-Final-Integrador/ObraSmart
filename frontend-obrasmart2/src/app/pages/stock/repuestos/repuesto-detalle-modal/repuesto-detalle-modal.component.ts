import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { RepuestoDTO } from 'src/app/service/repuesto.service';
import { MediaService } from 'src/app/service/media.service';
import { appendCacheBust, buildRepuestoMediaId, MEDIA_PLACEHOLDER, validateImageFile } from '../../../../shared/utils/media-helper';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-repuesto-detalle-modal',
  templateUrl: './repuesto-detalle-modal.component.html',
  styleUrls: ['./repuesto-detalle-modal.component.scss'],
})
export class RepuestoDetalleModalComponent implements OnChanges {
  @Input() visible = false;
  @Input() repuesto?: RepuestoDTO;
  @Output() closed = new EventEmitter<void>();
  @Output() imagenActualizada = new EventEmitter<number>();

  readonly placeholder = MEDIA_PLACEHOLDER;
  cacheBust = Date.now();
  imageSrc: string = this.placeholder;
  fileToUpload?: File;
  uploading = false;

  constructor(private media: MediaService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['repuesto']?.currentValue) {
      this.cacheBust = Date.now();
      this.fileToUpload = undefined;
      this.setImageFromRepuesto();
    }

    if (changes['visible'] && !this.visible) {
      this.fileToUpload = undefined;
    }
  }

  cerrar(): void {
    this.closed.emit();
  }

  get imageLabel(): string {
    return this.fileToUpload?.name || 'Elegir archivo (max 5MB)';
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const file = input.files[0];
    const validation = validateImageFile(file);
    if (validation) {
      Swal.fire({ icon: 'error', title: 'Archivo inválido', text: validation });
      input.value = '';
      this.fileToUpload = undefined;
      return;
    }
    this.fileToUpload = file;
  }

  subirImagen(): void {
    if (!this.repuesto?.id || !this.fileToUpload) return;
    const mediaId = buildRepuestoMediaId(this.repuesto.id);
    this.uploading = true;
    this.media.uploadEquipoImage(mediaId, this.fileToUpload).subscribe({
      next: () => {
        this.cacheBust = Date.now();
        const base = this.media.getEquipoImageUrl(mediaId);
        this.imageSrc = appendCacheBust(base, this.cacheBust);
        this.fileToUpload = undefined;
        this.uploading = false;
        if (this.repuesto?.id != null) {
          this.imagenActualizada.emit(this.repuesto.id);
        }
        Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Imagen subida', showConfirmButton: false, timer: 1500 });
      },
      error: (err) => {
        console.error('Error subiendo imagen de repuesto', err);
        this.uploading = false;
        Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo subir la imagen' });
      },
    });
  }

  onImgError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.onerror = null as any;
      img.src = this.placeholder;
    }
  }

  private setImageFromRepuesto(): void {
    if (!this.repuesto?.id) {
      this.imageSrc = this.placeholder;
      return;
    }
    const mediaId = buildRepuestoMediaId(this.repuesto.id);
    const base = this.media.getEquipoImageUrl(mediaId);
    this.imageSrc = appendCacheBust(base, this.cacheBust);
  }
}
