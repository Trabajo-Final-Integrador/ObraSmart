import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { ObradoresRoutingModule } from './obradores-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { ObradoresListadoComponent } from './pages/obradores-listado/obradores-listado.component';
import { ObradorFormComponent } from './pages/obrador-form/obrador-form.component';
import { ObradorDetalleComponent } from './pages/obrador-detalle/obrador-detalle.component';

@NgModule({
  declarations: [ObradoresListadoComponent, ObradorFormComponent, ObradorDetalleComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule, SharedModule, ObradoresRoutingModule],
})
export class ObradoresModule {}
