import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReparacionService, ReparacionResponseDTO, ReparacionRequestDto } from 'src/app/service/reparaciones.service';
import { EquipoService, EquipoDTO } from 'src/app/service/equipo.service';
import Swal from 'sweetalert2';

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

  reparacion: ReparacionResponseDTO | null = null;
  equipos: EquipoDTO[] = [];

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
    private equipoService: EquipoService
  ) {}

  ngOnInit(): void {
    // Determinar si es modo detalle o edición según la URL
    this.modoDetalle = this.router.url.includes('/detalle/');
    this.modoEdicion = !this.modoDetalle;

    this.reparacionId = +this.route.snapshot.paramMap.get('id')!;

    this.cargarEquipos();
    this.cargarReparacion();
  }

  cargarEquipos(): void {
    this.equipoService.listar().subscribe({
      next: (data) => this.equipos = data,
      error: err => console.error("Error cargando equipos:", err)
    });
  }

  cargarReparacion(): void {
    this.repSrv.obtenerPorId(this.reparacionId).subscribe({
      next: (data) => {
        this.reparacion = data;
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
          title: 'Error al cargar reparación',
          text: 'No se pudo cargar la información de la reparación.'
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
        title: 'Campos incompletos',
        text: 'Por favor completa todos los campos requeridos.'
      });
      return;
    }

    this.repSrv.actualizar(this.reparacionId, this.formulario).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Reparación actualizada',
          text: 'Los cambios han sido guardados correctamente.',
          timer: 1500,
          showConfirmButton: false
        });
        this.router.navigate(['/reparaciones']);
      },
      error: (err) => {
        console.error(err);
        Swal.fire({
          icon: 'error',
          title: 'Error al actualizar',
          text: err?.error?.message ?? 'No se pudo actualizar la reparación.'
        });
      }
    });
  }

  cambiarEstado(nuevoEstado: string): void {
    const textos: any = {
      'EN_PROCESO': { title: 'Iniciar reparación', text: 'La reparación cambiará a estado EN PROCESO.' },
      'FINALIZADA': { title: 'Finalizar reparación', text: 'La reparación se marcará como FINALIZADA.' }
    };

    const config = textos[nuevoEstado] || { title: 'Cambiar estado', text: '¿Deseas continuar?' };

    Swal.fire({
      title: config.title,
      text: config.text,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, continuar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.repSrv.cambiarEstado(this.reparacionId, nuevoEstado).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Estado actualizado',
              text: 'El estado de la reparación ha sido actualizado.',
              timer: 1500,
              showConfirmButton: false
            });
            this.cargarReparacion();
          },
          error: (err) => {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: err?.error?.message ?? 'No se pudo cambiar el estado.'
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
}
