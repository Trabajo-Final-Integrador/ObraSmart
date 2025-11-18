import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { EquiposRoutingModule } from './equipos-routing.module';

import { EquiposListadoComponent } from './equipos-listado/equipos-listado.component';
import { EquipoDetalleComponent } from './equipo-detalle/equipo-detalle.component';
import { EquipoModalComponent } from './equipo-modal/equipo-modal.component';
import { SimpleTabComponent } from './simple-tab/simple-tab.component';
import { ListadoMarcasComponent } from './marcas/listado-marcas/listado-marcas.component';
import { CrearMarcaComponent } from './marcas/crear-marca/crear-marca.component';
import { ListadoModelosComponent } from './modelos/listado-modelos/listado-modelos.component';
import { CrearModeloComponent } from './modelos/crear-modelo/crear-modelo.component';
import { ListadoTipoEquipoComponent } from './tipo-equipo/listado-tipo-equipo/listado-tipo-equipo.component';
import { CrearTipoEquipoComponent } from './tipo-equipo/crear-tipo-equipo/crear-tipo-equipo.component';

@NgModule({
  declarations: [
    EquiposListadoComponent,
    EquipoDetalleComponent,
    EquipoModalComponent,
    SimpleTabComponent,
    ListadoMarcasComponent,
    CrearMarcaComponent,
    ListadoModelosComponent,
    CrearModeloComponent,
    ListadoTipoEquipoComponent,
    CrearTipoEquipoComponent
  ],
  imports: [
    CommonModule,  // 👈 ESTA LÍNEA ES LA QUE HABILITA titlecase, ngIf, ngFor, etc.
    FormsModule,
    EquiposRoutingModule
  ]
  
})
export class EquiposModule {}
