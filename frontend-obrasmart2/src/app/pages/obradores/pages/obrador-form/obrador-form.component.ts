import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ObradorDto, ObradorService } from 'src/app/service/obrador.service';
import { UserDto, UserService } from 'src/app/service/user.service';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';

@Component({
  selector: 'app-obrador-form',
  templateUrl: './obrador-form.component.html',
  styleUrls: ['./obrador-form.component.scss'],
})
export class ObradorFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  loading = false;
  error?: string;
  obradorId?: number;
  usuarios: UserDto[] = [];
  equiposDisponibles: EquipoDTO[] = [];
  equiposAsignados: EquipoDTO[] = [];
  equipoSeleccionado: number | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private obradorService: ObradorService,
    private userService: UserService,
    private equipoService: EquipoService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.cargarUsuarios();
    this.cargarEquipos();
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isEdit = true;
        this.obradorId = Number(id);
        this.loadObrador(this.obradorId);
      }
    });
  }

  private buildForm(): void {
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      ubicacion: [''],
      lat: [null],
      lng: [null],
      estado: ['ACTIVO'],
      responsableUserId: [null],
    });
  }

  private loadObrador(id: number): void {
    this.loading = true;
    this.obradorService.obtener(id).subscribe({
      next: (obrador) => {
        this.form.patchValue({
          nombre: obrador.nombre,
          ubicacion: obrador.ubicacion,
          lat: obrador.lat ?? null,
          lng: obrador.lng ?? null,
          estado: (obrador as any).estado || 'ACTIVO',
          responsableUserId: obrador.supervisorUserId ?? null,
        });
        this.equiposAsignados = this.equiposDisponibles.filter((e) => (obrador.equipoIds || []).includes(e.id || 0));
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar obrador', err);
        this.error = 'No se pudo cargar el obrador';
        this.loading = false;
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const payload: Partial<ObradorDto> = this.form.value;

    const request$ = this.isEdit && this.obradorId
      ? this.obradorService.actualizar(this.obradorId, payload)
      : this.obradorService.crear(payload);

    request$.subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/obradores']);
      },
      error: (err) => {
        console.error('Error al guardar obrador', err);
        this.error = 'No se pudo guardar el obrador';
        this.loading = false;
      },
    });
  }

  cancelar(): void {
    this.router.navigate(['/obradores']);
  }

  private cargarUsuarios(): void {
    this.userService.listar().subscribe({
      next: (usuarios) => (this.usuarios = usuarios),
      error: (err) => console.warn('No se pudieron cargar usuarios', err),
    });
  }

  private cargarEquipos(): void {
    this.equipoService.listar().subscribe({
      next: (equipos) => (this.equiposDisponibles = equipos || []),
      error: (err) => console.warn('No se pudieron cargar equipos', err),
    });
  }

  asignarEquipo(): void {
    if (!this.obradorId || !this.equipoSeleccionado) return;
    this.obradorService.asignarEquipo(this.obradorId, this.equipoSeleccionado).subscribe({
      next: (obrador) => {
        this.equiposAsignados = this.equiposDisponibles.filter((e) => (obrador.equipoIds || []).includes(e.id || 0));
        this.equipoSeleccionado = null;
      },
      error: (err) => {
        console.error('Error al asignar equipo', err);
        this.error = 'No se pudo asignar el equipo';
      },
    });
  }
}
