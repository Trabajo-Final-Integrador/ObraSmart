import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LogisticaService, TrasladoDto } from 'src/app/service/logistica.service';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';

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

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private logisticaService: LogisticaService,
    private equipoService: EquipoService
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
    this.equipoService.listar().subscribe({
      next: (eqs) => (this.equipos = eqs || []),
      error: (err) => console.warn('No se pudieron cargar equipos', err),
    });
  }

  guardar(): void {
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
        this.error = 'No se pudo crear el traslado (requiere rol SUPERVISOR)';
        this.loading = false;
      },
    });
  }

  cancelar(): void {
    this.router.navigate(['/traslados']);
  }
}
