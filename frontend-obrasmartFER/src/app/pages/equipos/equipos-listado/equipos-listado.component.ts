import { Component, OnInit } from '@angular/core';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { MarcaService } from 'src/app/service/marca.service';
import { ModeloService } from 'src/app/service/modelo.service';
import { TipoEquipoService } from 'src/app/service/tipo-equipo.service';


@Component({
  selector: 'app-equipos-listado',
  templateUrl: './equipos-listado.component.html',
  styleUrls: ['./equipos-listado.component.scss']
})
export class EquiposListadoComponent implements OnInit {

  activeTab: 'equipos' | 'marcas' | 'modelos' | 'tipos' = 'equipos';
  equipos: EquipoDTO[] = [];
  marcas: any[] = [];
  modelos: any[] = [];
  tipos: any[] = [];

   showModalMarca: boolean = false;

  loading = false;

  showModal = false;
  showDetalle = false;
  selectedEquipo?: EquipoDTO;

  constructor(
    private equipoService: EquipoService,
    private marcaService: MarcaService,
    private modeloService: ModeloService,
    private tipoService: TipoEquipoService
  ) {}

  ngOnInit() {
    this.cargarDatos();
  }

  cambiarTab(tab: any) {
    this.activeTab = tab;
    this.cargarDatos();
  }

  cargarDatos() {
    this.loading = true;
    switch (this.activeTab) {
      case 'equipos':
        this.equipoService.listar().subscribe({
          next: (data) => (this.equipos = data),
          complete: () => (this.loading = false)
        });
        break;
      case 'marcas':
        this.marcaService.listar().subscribe({
          next: (data) => (this.marcas = data),
          complete: () => (this.loading = false)
        });
        break;
      case 'modelos':
        this.modeloService.listar().subscribe({
          next: (data) => (this.modelos = data),
          complete: () => (this.loading = false)
        });
        break;
      case 'tipos':
        this.tipoService.listar().subscribe({
          next: (data) => (this.tipos = data),
          complete: () => (this.loading = false)
        });
        break;
    }
  }

  abrirModal() {
    this.showModal = true;
  }

  cerrarModal() {
    this.showModal = false;
  }

  verDetalle(equipo: EquipoDTO) {
    this.selectedEquipo = equipo;
    this.showDetalle = true;
  }

  cerrarDetalle() {
    this.showDetalle = false;
  }

  abrirModalMarca() {
  console.log('🟦 Agregar Marca clickeado');
  this.showModalMarca = true; // o this.router.navigate(['/equipos/marcas/crear'])
}

cerrarModalMarca() {
    this.showModal = false;
  }

}
