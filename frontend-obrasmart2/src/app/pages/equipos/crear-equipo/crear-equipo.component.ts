import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { MarcaService, MarcaDTO } from 'src/app/service/marca.service';
import { ModeloService, ModeloDTO } from 'src/app/service/modelo.service';
import { TipoEquipoService, TipoEquipoDTO } from 'src/app/service/tipo-equipo.service';
import Swal from 'sweetalert2';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';


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
  totalPasos = 4;

  // Modo edición
  modoEdicion = false;
  idEquipo: number | null = null;

  marcas: MarcaDTO[] = [];
  modelos: ModeloDTO[] = [];
  tipos: TipoEquipoDTO[] = [];
  idMarcaSeleccionada: number | null = null;

  nuevoPrefijo: string = '';
nuevaDescripcion: string = '';    // opcional
  nuevaImagen: string = '';         // opcional

   nuevaMarca = '';
  nuevoModelo = '';
  nuevoTipo = '';

  // Geocoding
  private locationSubject = new Subject<string>();
  geocodificando = false;
  
  equipo: any = {
  nombre: '',
  codigoInterno: '',
  numeroSerie: '',
  potenciaHp: null,
  combustible: null,
  estadoOperativo: null,
  kilometrajeHorasUso: null,
  fechaUltimoMantenimiento: null,
  proximoMantenimiento: null,
  responsableMantenimiento: '',
  seguroVigente: false,
  ubicacionActual: '',
  activo: true,

  // relaciones
  idMarca: null,
  idModelo: null,
  idTipoEquipo: null,

  // extras
  anioFabricacion: null,
  latitud: null,
  longitud: null
};


  constructor(
    private service: EquipoService,
      private marcaService: MarcaService,
    private modeloService: ModeloService,
    private tipoService: TipoEquipoService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.cargarCombos();
    this.inicializarGeocodificacion();

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
          title: 'Error',
          text: 'No se pudo cargar el equipo',
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
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(ubicacion)}&limit=1`;

    this.http.get<any[]>(url).subscribe({
      next: (resultados) => {
        this.geocodificando = false;
        if (resultados && resultados.length > 0) {
          this.equipo.latitud = parseFloat(resultados[0].lat);
          this.equipo.longitud = parseFloat(resultados[0].lon);

          Swal.fire({
            icon: 'success',
            title: 'Coordenadas encontradas',
            text: `Lat: ${this.equipo.latitud.toFixed(6)}, Lon: ${this.equipo.longitud.toFixed(6)}`,
            timer: 2000,
            showConfirmButton: false
          });
        } else {
          Swal.fire({
            icon: 'info',
            title: 'No se encontraron coordenadas',
            text: 'Intente con una ubicación más específica.',
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
        // Validar Información Básica
        return !!(this.equipo.nombre && this.equipo.codigoInterno && this.equipo.numeroSerie);
      case 2:
        // Validar Especificaciones Técnicas
        return !!(this.equipo.idMarca && this.equipo.idModelo && this.equipo.idTipoEquipo &&
                  this.equipo.anioFabricacion && this.equipo.potenciaHp);
      case 3:
        // Validar Estado y Operación
        return !!(this.equipo.estadoOperativo && this.equipo.fechaUltimoMantenimiento &&
                  this.equipo.proximoMantenimiento && this.equipo.kilometrajeHorasUso &&
                  this.equipo.responsableMantenimiento);
      case 4:
        // Último paso - Ubicación (opcional, siempre válido)
        return true;
      default:
        return true;
    }
  }

  mostrarMensajeValidacion() {
    Swal.fire({
      icon: 'warning',
      title: 'Campos incompletos',
      text: 'Por favor complete todos los campos requeridos antes de continuar.',
      confirmButtonColor: '#00796b'
    });
  }

  cargarCombos() {
    this.marcaService.listar().subscribe(r => this.marcas = r);
    this.modeloService.listar().subscribe(r => this.modelos = r);
    this.tipoService.listar().subscribe(r => this.tipos = r);
  }

crearMarca() {
  if (!this.nuevaMarca.trim()) return;

  this.marcaService.crear({ nombre: this.nuevaMarca }).subscribe({
    next: () => {
      Swal.fire({
        icon: 'success',
        title: '¡Marca creada!',
        text: 'La marca fue registrada correctamente.',
        timer: 1500,
        showConfirmButton: false
      });

      this.cargarCombos();
      this.nuevaMarca = '';
    },
    error: () => {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudo crear la marca. Intente nuevamente.',
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
        title: '¡Modelo creado!',
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
        title: 'Error',
        text: 'No se pudo crear el modelo.'
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
    prefijo: this.nuevoPrefijo.toUpperCase(), // requerido por el backend
    descripcion: this.nuevaDescripcion || null,
    imagenURL: this.nuevaImagen || null
  };

  this.tipoService.crear(dto).subscribe({
    next: () => {
      Swal.fire({
        icon: 'success',
        title: 'Tipo de equipo creado',
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
        title: 'Error',
        text: 'No se pudo crear el tipo de equipo'
      });
    }
  });
}

  guardar() {
    if (this.modoEdicion && this.idEquipo) {
      // Actualizar equipo existente
      this.service.actualizar(this.idEquipo, this.equipo).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Equipo actualizado',
            text: 'Los cambios se guardaron correctamente',
            timer: 1500,
            showConfirmButton: false
          });

          this.router.navigate(['/equipos']);
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al actualizar equipo',
            text: 'Verificá los datos.',
          });

          console.error(err);
        }
      });
    } else {
      // Crear nuevo equipo
      this.service.crear(this.equipo).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Equipo creado',
            timer: 1500,
            showConfirmButton: false
          });

          this.router.navigate(['/equipos']);
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al crear equipo',
            text: 'Verificá los datos.',
          });

          console.error(err);
        }
      });
    }
  }

}


