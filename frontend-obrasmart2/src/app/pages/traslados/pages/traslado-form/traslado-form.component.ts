import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LogisticaService, TrasladoDto } from 'src/app/service/logistica.service';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { ObradorDto, ObradorService } from 'src/app/service/obrador.service';
import { SessionService } from 'src/app/service/session.service';

@Component({
  selector: 'app-traslado-form',
  templateUrl: './traslado-form.component.html',
  styleUrls: ['./traslado-form.component.scss'],
})
export class TrasladoFormComponent implements OnInit {
  form: FormGroup;
  loading = false;
  error?: string;
  equipos: EquipoDTO[] = [];
  obradores: ObradorDto[] = [];
  origenUbicacion = '';
  puedeCrear = false;
  private readonly rolesPermitidos = ['SUPERVISOR', 'ADMINISTRACION'];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private logisticaService: LogisticaService,
    private equipoService: EquipoService,
    private obradorService: ObradorService,
    private sessionService: SessionService
  ) {
    this.form = this.fb.group({
      equipoId: [null, Validators.required],
      origenObradorId: [null, Validators.required],
      destinoObradorId: [null, Validators.required],
      programadoPara: [''],
      notas: [''],
    });
  }

  ngOnInit(): void {
    this.puedeCrear = this.sessionService.hasAnyRole(this.rolesPermitidos);
    this.equipoService.listar().subscribe({
      next: (eqs) => (this.equipos = eqs || []),
      error: (err) => console.warn('No se pudieron cargar equipos', err),
    });
    this.obradorService.listar().subscribe({
      next: (list) => (this.obradores = list || []),
      error: (err) => console.warn('No se pudieron cargar obradores', err),
    });
  }

  onEquipoChange(equipoId: number | string | null): void {
    const id = Number(equipoId);
    const seleccionado = this.equipos.find((e) => e.id === id);
    this.origenUbicacion = seleccionado?.ubicacionActual || '';
    const origenId = (seleccionado as any)?.obradorId ?? (seleccionado as any)?.origenObradorId ?? null;
    this.form.patchValue({ origenObradorId: origenId });
    if (!origenId) {
      this.error = 'El equipo seleccionado no tiene obrador de origen asignado.';
    } else if (this.error?.includes('obrador de origen')) {
      this.error = undefined;
    }
  }

  guardar(): void {
    if (!this.puedeCrear) {
      this.error = 'No tienes permisos para crear traslados (SUPERVISOR o ADMINISTRACION).';
      return;
    }
    if (!this.form.value?.origenObradorId) {
      this.error = 'El equipo seleccionado no tiene obrador de origen asignado.';
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const payload: Partial<TrasladoDto> = this.form.value;

    this.logisticaService.crear(payload).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/traslados']);
      },
      error: (err) => {
        console.error('Error al crear traslado', err);
        this.error = 'No se pudo crear el traslado (requiere rol SUPERVISOR o ADMINISTRACION).';
        this.loading = false;
      },
    });
  }

  cancelar(): void {
    this.router.navigate(['/traslados']);
  }

}
