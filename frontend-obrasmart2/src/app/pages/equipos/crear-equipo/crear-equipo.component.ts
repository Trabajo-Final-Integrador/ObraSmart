import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { MarcaService, MarcaDTO } from 'src/app/service/marca.service';
import { ModeloService, ModeloDTO } from 'src/app/service/modelo.service';
import { TipoEquipoService, TipoEquipoDTO } from 'src/app/service/tipo-equipo.service';
import { UsuarioService } from 'src/app/service/usuario.service';
import { Usuario } from 'src/app/pages/auth/usuarios/usuario.model';
import Swal from 'sweetalert2';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { GeocodingService } from 'src/app/service/geocoding.service';

@Component({
  selector: 'app-crear-equipo',
  templateUrl: './crear-equipo.component.html',
  styleUrls: ['./crear-equipo.component.scss']
})
export class CrearEquipoComponent {

  // Menu control
  menuAbierto = false;
  submenuUsuariosOpen = false;
  submenuStockOpen = false;
  submenuReparacionOpen = false;

  // Stepper control
  pasoActual = 1;
  totalPasos = 3;

  // Modo edición
  modoEdicion = false;
  idEquipo: number | null = null;

  marcas: MarcaDTO[] = [];
  modelos: ModeloDTO[] = [];
  tipos: TipoEquipoDTO[] = [];
  idMarcaSeleccionada: number | null = null;
  usuariosResponsables: Usuario[] = [];

  nuevoPrefijo: string = '';
  nuevaDescripcion: string = '';
  nuevaImagen: string = '';

  nuevaMarca = '';
  nuevoModelo = '';
  nuevoTipo = '';

  // Geocoding
  private locationSubject = new Subject<string>();
  geocodificando = false;
  maxFechaUltimoMantenimiento = '';
  minProximoMantenimiento = '';
  
  equipo: EquipoDTO  = {
  nombre: '',
  codigoInterno: '',
  numeroSerie: '',
  potenciaHp: 0,
  combustible: '',
  estadoOperativo: '',
  kilometrajeHorasUso: 0,
  fechaUltimoMantenimiento: '',
  proximoMantenimiento: '',
  responsableMantenimiento: '',
  seguroVigente: false,
  ubicacionActual: '',
  activo: true,
  idMarca: 0,
  idModelo: 0,
  idTipoEquipo: 0,
  anioFabricacion: 0,
  latitud: 0,
  longitud: 0
};


  constructor(
    private service: EquipoService,
      private marcaService: MarcaService,
    private modeloService: ModeloService,
    private tipoService: TipoEquipoService,
    private usuarioService: UsuarioService,
    private router: Router,
    private route: ActivatedRoute,
    private translate: TranslateService,
    private geocoding: GeocodingService
  ) {}

  ngOnInit(): void {
    this.configurarFechasMantenimiento();
    this.cargarCombos();
    this.inicializarGeocodificacion();
    this.cargarUsuariosResponsables();

    // Verificar si es modo edición
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.modoEdicion = true;
        this.idEquipo = +params['id'];
        this.cargarEquipo(this.idEquipo);
      }
    });
  }

  cargarEquipo(id: number): void {
    this.service.obtenerPorId(id).subscribe({
      next: (data) => {
        this.equipo = data;
        console.log('Equipo cargado:', data);
      },
      error: (err) => {
        console.error('Error al cargar equipo:', err);
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('equipos.create.alert.loadErrorTitle'),
          text: this.translate.instant('equipos.create.alert.loadErrorText'),
          confirmButtonColor: '#00796b'
        });
        this.router.navigate(['/equipos']);
      }
    });
  }

  inicializarGeocodificacion() {
    this.locationSubject
      .pipe(
        debounceTime(800)
      )
      .subscribe(ubicacion => {
        if (ubicacion && ubicacion.trim().length > 3) {
          this.buscarCoordenadas(ubicacion);
        }
      });
  }

  onUbicacionChange(ubicacion: string) {
    console.log('Ubicacion cambiada:', ubicacion);
    this.locationSubject.next(ubicacion);
  }

  buscarCoordenadas(ubicacion: string) {
    console.log('Buscando coordenadas para:', ubicacion);
    this.geocodificando = true;

    this.geocoding.buscar(ubicacion).subscribe({
      next: (resultado) => {
        this.geocodificando = false;
        if (resultado) {
          this.equipo.latitud = resultado.lat;
          this.equipo.longitud = resultado.lon;

          Swal.fire({
            icon: 'success',
            title: this.translate.instant('equipos.create.alert.coordsFoundTitle'),
            text: this.translate.instant('equipos.create.alert.coordsFoundText', { lat: this.equipo.latitud.toFixed(6), lon: this.equipo.longitud.toFixed(6) }),
            timer: 2000,
            showConfirmButton: false
          });
        } else {
          Swal.fire({
            icon: 'info',
            title: this.translate.instant('equipos.create.alert.coordsNotFoundTitle'),
            text: this.translate.instant('equipos.create.alert.coordsNotFoundText'),
            timer: 2000,
            showConfirmButton: false
          });
        }
      },
      error: () => {
        this.geocodificando = false;
        console.error('Error al buscar coordenadas');
      }
    });
  }

  // Menu methods
  toggleSidebar() {
    this.menuAbierto = !this.menuAbierto;
  }

  toggleUsuarios(event: Event) {
    event.preventDefault();
    this.submenuUsuariosOpen = !this.submenuUsuariosOpen;
  }

  toggleReparacion(event: Event) {
    event.preventDefault();
    this.submenuReparacionOpen = !this.submenuReparacionOpen;
  }

  toggleStock(event: Event) {
    event.preventDefault();
    this.submenuStockOpen = !this.submenuStockOpen;
  }

  // Stepper methods
  siguientePaso() {
    if (this.pasoActual < this.totalPasos) {
      if (this.validarPasoActual()) {
        this.pasoActual++;
      }
    }
  }

  pasoAnterior() {
    if (this.pasoActual > 1) {
      this.pasoActual--;
    }
  }

  irAPaso(paso: number) {
    if (paso >= 1 && paso <= this.totalPasos) {
      this.pasoActual = paso;
    }
  }

  validarPasoActual(): boolean {
    switch (this.pasoActual) {
      case 1:
        return !!(
          this.equipo.nombre &&
          this.equipo.codigoInterno &&
          this.equipo.numeroSerie &&
          this.equipo.idMarca &&
          this.equipo.idModelo &&
          this.equipo.idTipoEquipo &&
          this.equipo.anioFabricacion &&
          this.equipo.potenciaHp
        );
      case 2:
        return !!(this.equipo.estadoOperativo && this.equipo.fechaUltimoMantenimiento &&
                  this.equipo.proximoMantenimiento && this.equipo.kilometrajeHorasUso &&
                  this.equipo.responsableMantenimiento);
      case 3:
        return true;
      default:
        return true;
    }
  }

  mostrarMensajeValidacion() {
    Swal.fire({
      icon: 'warning',
      title: this.translate.instant('equipos.create.alert.missingFieldsTitle'),
      text: this.translate.instant('equipos.create.alert.missingFieldsText'),
      confirmButtonColor: '#00796b'
    });
  }

  cargarCombos() {
    this.marcaService.listar().subscribe(r => this.marcas = r);
    this.modeloService.listar().subscribe(r => this.modelos = r);
    this.tipoService.listar().subscribe(r => this.tipos = r);
  }

  cargarUsuariosResponsables() {
    this.usuarioService.listarUsuarios().subscribe({
      next: (usuarios) => {
        const activos = (usuarios || []).filter(u => u?.status === 'ACTIVO');
        const filtrados = activos.filter(u => u?.role === 'OPERARIO' || u?.role === 'TECNICO');
        this.usuariosResponsables = filtrados.length ? filtrados : activos;
      },
      error: (err) => {
        console.error('Error al cargar usuarios:', err);
        this.usuariosResponsables = [];
      }
    });
  }

  nombreUsuario(u: Usuario): string {
    const nombre = `${u.firstname || ''} ${u.lastname || ''}`.trim();
    return nombre || u.username || u.email;
  }

  crearMarca() {
    if (!this.nuevaMarca.trim()) return;

    this.marcaService.crear({ nombre: this.nuevaMarca }).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: this.translate.instant('equipos.create.alert.brandSuccessTitle'),
          text: this.translate.instant('equipos.create.alert.brandSuccessText'),
          timer: 1500,
          showConfirmButton: false
        });

        this.cargarCombos();
        this.nuevaMarca = '';
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('equipos.create.alert.brandErrorTitle'),
          text: this.translate.instant('equipos.create.alert.brandErrorText'),
        });
      }
    });
  }


  crearModelo() {
    if (!this.nuevoModelo.trim() || !this.idMarcaSeleccionada) return;

    const dto = {
      nombre: this.nuevoModelo,
      marca: {
        id: this.idMarcaSeleccionada
      }
    };

    this.modeloService.crear(dto).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: this.translate.instant('equipos.create.alert.modelSuccessTitle'),
          timer: 1500,
          showConfirmButton: false
        });

        this.cargarCombos();
        this.nuevoModelo = '';
        this.idMarcaSeleccionada = null;
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('equipos.create.alert.modelErrorTitle'),
          text: this.translate.instant('equipos.create.alert.modelErrorText')
        });
      }
    });
  }



  crearTipo() {
    if (!this.nuevoTipo.trim() || !this.nuevoPrefijo.trim()) {
      return;
    }

    const dto = {
    nombre: this.nuevoTipo,
    prefijo: this.nuevoPrefijo.toUpperCase(),
    descripcion: this.nuevaDescripcion || null,
    imagenURL: this.nuevaImagen || null
  };

    this.tipoService.crear(dto).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: this.translate.instant('equipos.create.alert.typeSuccessTitle'),
          timer: 1500,
          showConfirmButton: false
        });
        this.cargarCombos();
        this.nuevoTipo = '';
        this.nuevoPrefijo = '';
        this.nuevaDescripcion = '';
        this.nuevaImagen = '';
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('equipos.create.alert.typeErrorTitle'),
          text: this.translate.instant('equipos.create.alert.typeErrorText')
        });
      }
    });
  }

  guardar() {
    this.normalizarCoordenadas();
    this.normalizarEquipoPayload();
    if (this.modoEdicion && this.idEquipo) {
      this.service.actualizar(this.idEquipo, this.equipo).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: this.translate.instant('equipos.create.alert.updateSuccessTitle'),
            text: this.translate.instant('equipos.create.alert.updateSuccessText'),
            timer: 1500,
            showConfirmButton: false
          });

          this.router.navigate(['/equipos']);
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: this.translate.instant('equipos.create.alert.updateErrorTitle'),
            text: this.translate.instant('equipos.create.alert.updateErrorText'),
          });

          console.error(err);
        }
      });
    } else {
      this.service.crear(this.equipo).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: this.translate.instant('equipos.create.alert.createSuccessTitle'),
            text: this.translate.instant('equipos.create.alert.createSuccessText'),
            timer: 1500,
            showConfirmButton: false
          });

          this.router.navigate(['/equipos']);
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: this.translate.instant('equipos.create.alert.createErrorTitle'),
            text: this.translate.instant('equipos.create.alert.createErrorText'),
          });

          console.error(err);
        }
      });
    }
  }

  private configurarFechasMantenimiento() {
    const hoy = new Date();
    this.maxFechaUltimoMantenimiento = this.formatearFecha(hoy);
    const manana = new Date(hoy);
    manana.setDate(hoy.getDate() + 1);
    this.minProximoMantenimiento = this.formatearFecha(manana);
  }

  private formatearFecha(fecha: Date): string {
    const yyyy = fecha.getFullYear();
    const mm = String(fecha.getMonth() + 1).padStart(2, '0');
    const dd = String(fecha.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  normalizarCoordenadas() {
    const lat = this.normalizarNumero(this.equipo.latitud);
    const lon = this.normalizarNumero(this.equipo.longitud);
    this.equipo.latitud = lat ?? this.equipo.latitud;
    this.equipo.longitud = lon ?? this.equipo.longitud;
  }

  private normalizarNumero(valor: any): number | null {
    if (valor === null || valor === undefined || valor === '') return null;
    if (typeof valor === 'number') return Number.isFinite(valor) ? valor : null;
    const normalizado = String(valor).trim().replace(',', '.');
    const parsed = Number.parseFloat(normalizado);
    return Number.isFinite(parsed) ? parsed : null;
  }

  private normalizarEquipoPayload() {
    const idMarca = this.normalizarEntero(this.equipo.idMarca);
    const idModelo = this.normalizarEntero(this.equipo.idModelo);
    const idTipo = this.normalizarEntero(this.equipo.idTipoEquipo);
    const anio = this.normalizarEntero(this.equipo.anioFabricacion);
    const potencia = this.normalizarNumero(this.equipo.potenciaHp);
    const km = this.normalizarNumero(this.equipo.kilometrajeHorasUso);

    if (idMarca !== null) this.equipo.idMarca = idMarca;
    if (idModelo !== null) this.equipo.idModelo = idModelo;
    if (idTipo !== null) this.equipo.idTipoEquipo = idTipo;
    if (anio !== null) this.equipo.anioFabricacion = anio;
    if (potencia !== null) this.equipo.potenciaHp = potencia;
    if (km !== null) this.equipo.kilometrajeHorasUso = km;
  }

  private normalizarEntero(valor: any): number | null {
    if (valor === null || valor === undefined || valor === '') return null;
    if (typeof valor === 'number') return Number.isFinite(valor) ? Math.trunc(valor) : null;
    const parsed = Number.parseInt(String(valor).trim(), 10);
    return Number.isFinite(parsed) ? parsed : null;
  }

}
