import { Component, EventEmitter, Output } from '@angular/core';
import { MarcaService } from 'src/app/service/marca.service';
import { ModeloService } from 'src/app/service/modelo.service';
import { TipoEquipoService } from 'src/app/service/tipo-equipo.service';
import { EquipoService } from 'src/app/service/equipo.service';


@Component({
  selector: 'app-equipo-modal',
  templateUrl: './equipo-modal.component.html',
  styleUrls: ['./equipo-modal.component.scss']
})
export class EquipoModalComponent {
  @Output() cerrar = new EventEmitter<void>();

  equipo: any = {
    nombre: '',
    idTipoEquipo: null,
    idMarca: null,
    idModelo: null,
    numeroSerie: '',
    anioFabricacion: null,
    potenciaHp: null,
    combustible: '',
    estadoOperativo: '',
    kilometrajeHorasUso: null,
    fechaUltimoMantenimiento: '',
    proximoMantenimiento: '',
    responsableMantenimiento: '',
    numeroPatente: '',
    seguroVigente: false,
    fechaVencimientoSeguro: '',
    ubicacionActual: '',
    activo: true
  };

  // 👇 estas tres listas son las que usa el HTML con *ngFor
  marcas: any[] = [];
  modelos: any[] = [];
  tipos: any[] = []; // 🔥 ESTA LÍNEA FALTABA

  combustibles = ['NAFTA', 'DIESEL', 'GNC', 'ELECTRICO', 'HIBRIDO'];
  estados = ['OPERATIVO', 'EN_REPARACION', 'FUERA_DE_SERVICIO'];

  constructor(
    private marcaService: MarcaService,
    private modeloService: ModeloService,
    private tipoService: TipoEquipoService,
    private equipoService: EquipoService
  ) {}

  ngOnInit(): void {
    this.marcaService.listar().subscribe({ next: (d) => (this.marcas = d) });
    this.modeloService.listar().subscribe({ next: (d) => (this.modelos = d) });
    this.tipoService.listar().subscribe({ next: (d) => (this.tipos = d) });
  }

  guardar() {
    this.equipoService.crear(this.equipo).subscribe({
      next: () => {
        alert('✅ Equipo creado correctamente');
        this.cerrar.emit();
      },
      error: (err) => {
        console.error('❌ Error al crear equipo:', err);
        alert('Error al crear equipo');
      }
    });
  }
}
