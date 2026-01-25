import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';
import { environment } from '../../../environments/environment';
import { MediaService } from 'src/app/service/media.service';
import { TranslateService } from '@ngx-translate/core';

// Tipos fijos para los slots de usuario
type Slot = 'profile' | 'cred1' | 'cred2' | 'cred3' | 'cred4';
const ALL_SLOTS: readonly Slot[] = ['profile', 'cred1', 'cred2', 'cred3', 'cred4'] as const;
const CRED_SLOTS: readonly Slot[] = ['cred1', 'cred2', 'cred3', 'cred4'] as const;
const USER_MEDIA_OFFSET = 1_000_000;
const SLOT_OFFSETS: Record<Slot, number> = {
  profile: 0,
  cred1: 100_000,
  cred2: 200_000,
  cred3: 300_000,
  cred4: 400_000,
};

@Component({
  selector: 'app-modal-usuario',
  templateUrl: './modal-usuario.component.html',
  styleUrls: ['./modal-usuario.component.scss']
})
export class ModalUsuarioComponent implements OnChanges {
  readonly slots: readonly Slot[] = CRED_SLOTS;

  @Input() usuario: any = {};
  @Input() soloLectura = false;
@Output() cerrar = new EventEmitter<void>();
@Output() guardar = new EventEmitter<void>();
@Output() photoUploaded = new EventEmitter<number>();

  cacheBust: Record<string, number> = {};
  uploading: Record<Slot, boolean> = {
    profile: false,
    cred1: false,
    cred2: false,
    cred3: false,
    cred4: false
  };
  files: Record<Slot, File | null> = {
    profile: null,
    cred1: null,
    cred2: null,
    cred3: null,
    cred4: null
  };
  previews: Record<Slot, string | null> = {
    profile: null,
    cred1: null,
    cred2: null,
    cred3: null,
    cred4: null
  };
  readonly placeholderData =
    'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="120" height="90" viewBox="0 0 120 90"><rect width="120" height="90" fill="%23e5e7eb"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%239ca3af" font-family="Arial" font-size="12">Sin imagen</text></svg>';

  constructor(private http: HttpClient, private media: MediaService, private translate: TranslateService) {}

  private toUserSlotMediaId(userId: number, slot: Slot): number {
    return USER_MEDIA_OFFSET + Number(userId || 0) + SLOT_OFFSETS[slot];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['usuario']?.currentValue?.id) {
      const id = changes['usuario'].currentValue.id;
      ALL_SLOTS.forEach(slot => {
        const bust = Date.now();
        this.cacheBust[slot] = bust;
        this.files[slot] = null;
        const mediaId = this.toUserSlotMediaId(id, slot);
        this.previews[slot] = this.media.getEquipoImageUrl(mediaId).concat(`?t=${bust}`);
      });
    } else {
      ALL_SLOTS.forEach(slot => {
        this.files[slot] = null;
        this.previews[slot] = this.placeholderData;
      });
    }
  }

  guardarCambios() {
    const id = this.usuario.id;

    this.http.put(`${(environment as any).gatewayUrl || environment.apiUrl}/users/${id}`, this.usuario, { withCredentials: true })
      .subscribe({
        next: () => {
          this.guardar.emit();
          this.cerrar.emit();
          setTimeout(() => {
            Swal.fire({
              toast: true,
              position: 'top-end',
              icon: 'success',
              title: this.translate.instant('users.modal.alert.saveSuccess'),
              showConfirmButton: false,
              timer: 2000,
              customClass: {
                container: 'swal-no-backdrop'
              },
              didOpen: () => {
                const container = document.querySelector('.swal2-container') as HTMLElement;
                if (container) {
                  container.style.zIndex = '99999';
                }
              }
            });
          }, 100);
        },
        error: (err) => {
          console.error("Error al actualizar", err);
          Swal.fire({
            icon: 'error',
            title: this.translate.instant('common.error'),
            text: this.translate.instant('users.modal.alert.updateError')
          });
        }
      });
  }

  cancelar() {
    this.cerrar.emit();
  }

  selectFile(event: Event, slot: Slot) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const file = input.files[0];
    console.log('FILE_SELECTED', slot, file?.name);
    if (file.size > 5 * 1024 * 1024) {
      Swal.fire({ icon: 'error', title: this.translate.instant('users.modal.alert.fileTooLarge'), text: this.translate.instant('users.modal.alert.maxSize') });
      input.value = '';
      return;
    }
    if (!file.type.startsWith('image/')) {
      Swal.fire({ icon: 'error', title: this.translate.instant('users.modal.alert.invalidFormat'), text: this.translate.instant('users.modal.alert.onlyImages') });
      input.value = '';
      return;
    }
    this.files[slot] = file;
    this.previews[slot] = URL.createObjectURL(file);
  }

  upload(slot: Slot) {
    if (!this.usuario?.id || !this.files[slot]) return;
    const mediaId = this.toUserSlotMediaId(this.usuario.id, slot);
    const targetUrl = this.media.getEquipoImageUrl(mediaId);
    const file = this.files[slot] as File;
    console.log('[UPLOAD] url=', targetUrl);
    console.log('[UPLOAD] slot=', slot);
    console.log('[UPLOAD] file=', file?.name, file?.type, file?.size);
    console.log('UPLOAD_CLICK', slot);
    this.uploading[slot] = true;
    console.log('uploading set true', slot);
    const fd = new FormData();
    fd.append('file', file);
    fd.forEach((v, k) => console.log('[UPLOAD] formData', k, v));

    this.media.uploadEquipoImage(mediaId, file).subscribe({
      next: () => {
        this.cacheBust[slot] = Date.now();
        this.files[slot] = null;
        this.previews[slot] = this.media.getEquipoImageUrl(mediaId).concat(`?t=${this.cacheBust[slot]}`);
        this.photoUploaded.emit(this.usuario.id);
        this.uploading[slot] = false;
        console.log('uploading set false', slot);
        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'success',
          title: this.translate.instant('users.modal.alert.uploadSuccess'),
          showConfirmButton: false,
          timer: 1500
        });
      },
      error: (err) => {
        try {
          console.error('[UPLOAD ERROR]', err?.status, err?.message, err?.error, err?.url);
        } catch (_) {
          console.error('[UPLOAD ERROR]', err);
        }
        this.uploading[slot] = false;
        Swal.fire({ icon: 'error', title: this.translate.instant('common.error'), text: this.translate.instant('users.modal.alert.uploadError') });
      }
    });
  }

  imgFallback(event: Event, slot: Slot) {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.onerror = null as any;
      img.src = this.placeholderData;
    }
    this.cacheBust[slot] = Date.now();
    this.previews[slot] = this.placeholderData;
  }

}
