import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { ReparacionResponseDTO } from 'src/app/service/reparaciones.service';
import { appendCacheBust, MEDIA_PLACEHOLDER, buildReparacionMediaId, validateImageFile } from 'src/app/shared/utils/media-helper';
import { MediaService } from 'src/app/service/media.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-reparacion-detalle-modal',
  templateUrl: './reparacion-detalle-modal.component.html',
  styleUrls: ['./reparacion-detalle-modal.component.scss']
})
export class ReparacionDetalleModalComponent implements OnChanges {
  @Input() visible = false;
  @Input() reparacion?: ReparacionResponseDTO;
  @Input() imageUrl: string = MEDIA_PLACEHOLDER;
  @Output() closed = new EventEmitter<void>();

  placeholder = MEDIA_PLACEHOLDER;
  imageSrc = MEDIA_PLACEHOLDER;
  fileToUpload?: File;
  urlLink = '';
  uploading = false;
  message = '';
  messageType: 'success' | 'error' | '' = '';

  constructor(
    private mediaService: MediaService,
    private translate: TranslateService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['imageUrl'] || changes['reparacion']) {
      this.imageSrc = this.imageUrl || this.placeholder;
    }
  }

  close(): void {
    this.resetUploadState();
    this.closed.emit();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const file = input.files[0];
    const validationError = validateImageFile(file);
    if (validationError) {
      this.setMessageFromKey('repairs.detail.upload.invalidFile', 'error');
      this.fileToUpload = undefined;
      return;
    }
    this.fileToUpload = file;
    this.clearMessage();
  }

  uploadFile(): void {
    if (!this.reparacion?.id || !this.fileToUpload) return;
    const mediaId = buildReparacionMediaId(this.reparacion.id);
    this.uploading = true;
    this.mediaService.uploadEquipoImage(mediaId, this.fileToUpload).subscribe({
      next: () => {
        this.imageSrc = appendCacheBust(this.mediaService.getEquipoImageUrl(mediaId), Date.now());
        this.fileToUpload = undefined;
        this.setMessageFromKey('repairs.detail.upload.success', 'success');
        this.uploading = false;
      },
      error: (err) => {
        console.error('Error subiendo imagen de reparación', err);
        this.setMessageFromKey('repairs.detail.upload.error', 'error');
        this.uploading = false;
      }
    });
  }

  saveLink(): void {
    if (!this.reparacion?.id || !this.urlLink.trim()) return;
    const mediaId = buildReparacionMediaId(this.reparacion.id);
    const link = this.urlLink.trim();
    this.uploading = true;
    this.mediaService.setEquipoImageLink(mediaId, link).subscribe({
      next: () => {
        this.imageSrc = appendCacheBust(this.mediaService.getEquipoImageUrl(mediaId), Date.now());
        this.urlLink = '';
        this.setMessageFromKey('repairs.detail.upload.linkSaved', 'success');
        this.uploading = false;
      },
      error: (err) => {
        console.error('Error guardando link de imagen', err);
        this.setMessageFromKey('repairs.detail.upload.errorLink', 'error');
        this.uploading = false;
      }
    });
  }

  onImgError(): void {
    this.imageSrc = this.placeholder;
  }

  private setMessage(msg: string, type: 'success' | 'error') {
    this.message = msg;
    this.messageType = type;
  }

  private setMessageFromKey(key: string, type: 'success' | 'error') {
    this.setMessage(this.translate.instant(key), type);
  }

  private clearMessage() {
    this.message = '';
    this.messageType = '';
  }

  private resetUploadState(): void {
    this.fileToUpload = undefined;
    this.urlLink = '';
    this.uploading = false;
    this.clearMessage();
  }
}
