import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReparacionService, ReparacionResponseDTO, ReparacionRequestDto } from 'src/app/service/reparaciones.service';
import { EquipoService, EquipoDTO } from 'src/app/service/equipo.service';
import { UsuarioService } from '../../usuarios/usuario.service';
import { Usuario } from '../../usuarios/usuario.model';
import Swal from 'sweetalert2';
import { MediaService } from 'src/app/service/media.service';
import { appendCacheBust, buildReparacionMediaId, MEDIA_PLACEHOLDER, validateImageFile } from '../../../shared/utils/media-helper';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-editar-reparaciones',
  templateUrl: './editar-reparaciones.component.html',
  styleUrls: ['./editar-reparaciones.component.scss']
})
export class EditarReparacionesComponent implements OnInit {

  reparacionId!: number;
  modoEdicion = true;
  modoDetalle = false;
  loading = true;
  cacheBust = Date.now();
  imagenSrc: string = MEDIA_PLACEHOLDER;
  fileToUpload?: File;
  uploading = false;

  reparacion: ReparacionResponseDTO | null = null;
  equipos: EquipoDTO[] = [];
  usuarios: Usuario[] = [];

  formulario: ReparacionRequestDto = {
    equipoId: null as any,
    tipoMantenimiento: '',
    direccion: '',
    responsableId: null as any,
    descripcion: '',
    estadoReparacion: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private repSrv: ReparacionService,
    private equipoService: EquipoService,
    private usuarioService: UsuarioService,
    private media: MediaService,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    // Determinar si es modo detalle o edición según la URL
    this.modoDetalle = this.router.url.includes('/detalle/');
    this.modoEdicion = !this.modoDetalle;

    this.reparacionId = +this.route.snapshot.paramMap.get('id')!;

    this.cargarEquipos();
    this.cargarUsuarios();
    this.cargarReparacion();
  }

  cargarEquipos(): void {
    this.equipoService.listar().subscribe({
      next: (data) => this.equipos = data,
      error: err => console.error('Error cargando equipos:', err)
    });
  }

  cargarUsuarios(): void {
    this.usuarioService.listarUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data.filter(u => u.status === 'ACTIVO');
      },
      error: err => {
        console.error('Error cargando usuarios:', err);
        this.usuarios = [];
      }
    });
  }

  cargarReparacion(): void {
    this.repSrv.obtenerPorId(this.reparacionId).subscribe({
      next: (data) => {
        this.reparacion = data;
        this.setImagenDesdeReparacion(data);
        this.formulario = {
          equipoId: data.equipoId,
          tipoMantenimiento: data.tipoMantenimiento,
          direccion: data.direccion,
          responsableId: data.responsableId,
          descripcion: data.descripcion,
          estadoReparacion: data.estadoReparacion
        };
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('repairs.alert.loadErrorTitle'),
          text: this.translate.instant('repairs.alert.loadErrorDetail')
        }).then(() => {
          this.router.navigate(['/reparaciones']);
        });
      }
    });
  }

  guardar(): void {
    if (!this.validarFormulario()) {
      Swal.fire({
        icon: 'warning',
        title: this.translate.instant('repairs.alert.missingFields.title'),
        text: this.translate.instant('repairs.alert.missingFields.text')
      });
      return;
    }

    this.repSrv.actualizar(this.reparacionId, this.formulario).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: this.translate.instant('repairs.alert.updateSuccessTitle'),
          text: this.translate.instant('repairs.alert.updateSuccessText'),
          timer: 1500,
          showConfirmButton: false
        });
        this.router.navigate(['/reparaciones']);
      },
      error: (err) => {
        console.error(err);
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('repairs.alert.updateErrorTitle'),
          text: err?.error?.message ?? this.translate.instant('repairs.alert.updateErrorText')
        });
      }
    });
  }

  cambiarEstado(nuevoEstado: string): void {
    const textos: any = {
      'EN_PROCESO': { title: this.translate.instant('repairs.alert.state.startTitle'), text: this.translate.instant('repairs.alert.state.startText') },
      'FINALIZADA': { title: this.translate.instant('repairs.alert.state.finishTitle'), text: this.translate.instant('repairs.alert.state.finishText') }
    };

    const config = textos[nuevoEstado] || { title: this.translate.instant('repairs.alert.state.defaultTitle'), text: this.translate.instant('repairs.alert.state.defaultText') };

    Swal.fire({
      title: config.title,
      text: config.text,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#6c757d',
      confirmButtonText: this.translate.instant('repairs.alert.state.confirm'),
      cancelButtonText: this.translate.instant('common.cancel')
    }).then((result) => {
      if (result.isConfirmed) {
        this.repSrv.cambiarEstado(this.reparacionId, nuevoEstado).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: this.translate.instant('repairs.alert.state.successTitle'),
              text: this.translate.instant('repairs.alert.state.successText'),
              timer: 1500,
              showConfirmButton: false
            });
            this.cargarReparacion();
          },
          error: (err) => {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: this.translate.instant('repairs.alert.state.errorTitle'),
              text: err?.error?.message ?? this.translate.instant('repairs.alert.state.errorText')
            });
          }
        });
      }
    });
  }

  validarFormulario(): boolean {
    return !!(
      this.formulario.equipoId &&
      this.formulario.tipoMantenimiento &&
      this.formulario.responsableId &&
      this.formulario.descripcion &&
      this.formulario.estadoReparacion
    );
  }

  cancelar(): void {
    this.router.navigate(['/reparaciones']);
  }

  habilitarEdicion(): void {
    this.modoDetalle = false;
    this.modoEdicion = true;
  }

  private setImagenDesdeReparacion(data: ReparacionResponseDTO): void {
    if (!data?.id) {
      this.imagenSrc = MEDIA_PLACEHOLDER;
      return;
    }
    const mediaId = buildReparacionMediaId(data.id);
    const base = this.media.getEquipoImageUrl(mediaId);
    this.imagenSrc = appendCacheBust(base, this.cacheBust);
  }

  onImagenError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.onerror = null as any;
      img.src = MEDIA_PLACEHOLDER;
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const file = input.files[0];
    const validation = validateImageFile(file);
    if (validation) {
      Swal.fire({ icon: 'error', title: this.translate.instant('repairs.form.alert.invalidFileTitle'), text: validation });
      input.value = '';
      this.fileToUpload = undefined;
      return;
    }
    this.fileToUpload = file;
  }

  subirImagen(): void {
    if (!this.reparacion?.id || !this.fileToUpload) return;
    const mediaId = buildReparacionMediaId(this.reparacion.id);
    this.uploading = true;
    this.media.uploadEquipoImage(mediaId, this.fileToUpload).subscribe({
      next: () => {
        this.cacheBust = Date.now();
        const base = this.media.getEquipoImageUrl(mediaId);
        this.imagenSrc = appendCacheBust(base, this.cacheBust);
        this.fileToUpload = undefined;
        this.uploading = false;
        Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: this.translate.instant('repairs.form.alert.uploadSuccess'), showConfirmButton: false, timer: 1500 });
      },
      error: (err) => {
        console.error('Error subiendo imagen de reparación', err);
        this.uploading = false;
        Swal.fire({ icon: 'error', title: this.translate.instant('repairs.form.alert.uploadErrorTitle'), text: this.translate.instant('repairs.form.alert.uploadErrorText') });
      }
    });
  }
}
