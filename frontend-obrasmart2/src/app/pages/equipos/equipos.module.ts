import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { EquiposRoutingModule } from './equipos-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';

import { EquiposListadoComponent } from './equipos-listado/equipos-listado.component';
import { CrearEquipoComponent } from './crear-equipo/crear-equipo.component';
import { EquipoDetalleModalComponent } from './equipo-detalle-modal/equipo-detalle-modal.component';

@NgModule({
  declarations: [
    EquiposListadoComponent,
    CrearEquipoComponent,
    EquipoDetalleModalComponent,

  ],
  imports: [
    CommonModule,
    FormsModule,
    EquiposRoutingModule,
    SharedModule
  ],
  exports: [EquipoDetalleModalComponent]

})
export class EquiposModule {}
