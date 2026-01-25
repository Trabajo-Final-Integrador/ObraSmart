import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ObradoresListadoComponent } from './pages/obradores-listado/obradores-listado.component';
import { ObradorFormComponent } from './pages/obrador-form/obrador-form.component';
import { ObradorDetalleComponent } from './pages/obrador-detalle/obrador-detalle.component';

const routes: Routes = [
  { path: '', component: ObradoresListadoComponent },
  { path: 'nuevo', component: ObradorFormComponent },
  { path: 'detalle/:id', component: ObradorDetalleComponent },
  { path: ':id', component: ObradorFormComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ObradoresRoutingModule {}
