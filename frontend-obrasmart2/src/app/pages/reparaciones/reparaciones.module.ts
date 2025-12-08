import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ReparacionesRoutingModule } from './reparaciones-routing.module';
import { SharedModule } from '../../shared/shared.module';

import { ListadoReparacionesComponent } from './listado-reparaciones/listado-reparaciones.component';
import { CrearReparacionComponent } from './crear-reparaciones/crear-reparaciones.component';
import { DashboardReparacionesComponent } from './dashboard-reparaciones/dashboard-reparaciones.component';
import { EditarReparacionesComponent } from './editar-reparaciones/editar-reparaciones.component';

@NgModule({
  declarations: [
    ListadoReparacionesComponent,
    CrearReparacionComponent,
    DashboardReparacionesComponent,
    EditarReparacionesComponent
  ],
  imports: [
    CommonModule,     // 🔹 ngIf, ngFor, ngClass, pipes
    FormsModule,      // 🔹 ngModel
    ReparacionesRoutingModule,
    SharedModule      // 🔹 Sidebar y ModalInfo compartidos
  ]
})
export class ReparacionesModule {}
