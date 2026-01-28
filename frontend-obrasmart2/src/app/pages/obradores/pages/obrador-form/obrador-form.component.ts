import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ObradorDto, ObradorService } from 'src/app/service/obrador.service';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { UsuarioService } from 'src/app/service/usuario.service';
import { Usuario } from 'src/app/pages/auth/usuarios/usuario.model';
import { SidebarService } from 'src/app/service/sidebar.service';
import { GeocodingService } from 'src/app/service/geocoding.service';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-obrador-form',
  templateUrl: './obrador-form.component.html',
  styleUrls: ['./obrador-form.component.scss'],
})
export class ObradorFormComponent implements OnInit, OnDestroy {
  form!: FormGroup;
  isEdit = false;
  loading = false;
  error?: string;
  obradorId?: number;
  supervisores: Usuario[] = [];
  equiposDisponibles: EquipoDTO[] = [];
  equiposAsignados: EquipoDTO[] = [];
  equipoSeleccionado: number | null = null;
  geocodificando = false;
  geocodingError?: string;
  private locationSub = new Subject<string>();
  private locationSubscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private obradorService: ObradorService,
    private usuarioService: UsuarioService,
    private equipoService: EquipoService,
    private sidebarService: SidebarService,
    private geocoding: GeocodingService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.cargarUsuarios();
    this.cargarEquipos();
    this.initGeocoding();
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isEdit = true;
        this.obradorId = Number(id);
        this.loadObrador(this.obradorId);
      }
    });
  }

  ngOnDestroy(): void {
    this.locationSubscription.unsubscribe();
  }

  private buildForm(): void {
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      ubicacion: [''],
      lat: [null],
      lng: [null],
      estado: ['ACTIVO'],
      supervisorUserId: [null],
    });
  }

  private initGeocoding(): void {
    this.locationSubscription.add(
      this.locationSub.pipe(debounceTime(800), distinctUntilChanged()).subscribe((ubicacion) => {
        if (ubicacion && ubicacion.trim().length > 3) {
          this.buscarCoordenadas(ubicacion);
        }
      })
    );

    const ubicacionSub = this.form.get('ubicacion')?.valueChanges.subscribe((val) => {
      if (typeof val === 'string') {
        this.locationSub.next(val);
      }
    });
    if (ubicacionSub) this.locationSubscription.add(ubicacionSub);
  }

  private buscarCoordenadas(ubicacion: string): void {
    this.geocodificando = true;
    this.geocodingError = undefined;
    this.geocoding.buscar(ubicacion).subscribe({
      next: (resultado) => {
        this.geocodificando = false;
        if (resultado) {
          this.form.patchValue({
            lat: resultado.lat,
            lng: resultado.lon
          }, { emitEvent: false });
        } else {
          this.geocodingError = 'No se encontraron coordenadas';
        }
      },
      error: () => {
        this.geocodificando = false;
        console.error('Error al buscar coordenadas');
        this.geocodingError = 'Error al buscar coordenadas';
      }
    });
  }

  onUbicacionBlur(): void {
    const ubicacion = this.form.get('ubicacion')?.value;
    if (typeof ubicacion === 'string' && ubicacion.trim().length > 3) {
      this.buscarCoordenadas(ubicacion.trim());
    }
  }

  private loadObrador(id: number): void {
    this.loading = true;
    this.obradorService.obtener(id).subscribe({
      next: (obrador) => {
        const { lat, lng } = this.mapLatLngFromResponse(obrador);
        this.form.patchValue({
          nombre: obrador.nombre,
          ubicacion: obrador.ubicacion,
          lat,
          lng,
          estado: (obrador as any).estado || 'ACTIVO',
          supervisorUserId: obrador.supervisorUserId ?? null,
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
    const { lat, lng, ...rest } = this.form.value;
    const payload: Partial<ObradorDto> = {
      ...rest,
      latitud: lat ?? null,
      longitud: lng ?? null,
      supervisorUserId: this.form.value.supervisorUserId ?? null,
    };

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

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

  private cargarUsuarios(): void {
    this.usuarioService.listarUsuarios().subscribe({
      next: (usuarios) => {
        this.supervisores = (usuarios || []).filter(
          (u) => (u as any).role === 'ADMINISTRACION' || (u as any).rol === 'ADMINISTRACION'
        );
      },
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

  private mapLatLngFromResponse(obrador: any): { lat: number | null; lng: number | null } {
    if (!obrador) return { lat: null, lng: null };
    const lat = obrador.lat ?? obrador.latitud ?? null;
    const lng = obrador.lng ?? obrador.longitud ?? null;
    return { lat, lng };
  }
}
