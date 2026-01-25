import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { TrasladosRoutingModule } from './traslados-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { TrasladosListadoComponent } from './pages/traslados-listado/traslados-listado.component';
import { TrasladoFormComponent } from './pages/traslado-form/traslado-form.component';
import { TrasladoDetalleComponent } from './pages/traslado-detalle/traslado-detalle.component';

@NgModule({
  declarations: [TrasladosListadoComponent, TrasladoFormComponent, TrasladoDetalleComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule, SharedModule, TrasladosRoutingModule],
})
export class TrasladosModule {}
