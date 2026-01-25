import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TrasladosListadoComponent } from './pages/traslados-listado/traslados-listado.component';
import { TrasladoFormComponent } from './pages/traslado-form/traslado-form.component';
import { TrasladoDetalleComponent } from './pages/traslado-detalle/traslado-detalle.component';

const routes: Routes = [
  { path: '', component: TrasladosListadoComponent },
  { path: 'nuevo', component: TrasladoFormComponent },
  { path: ':id', component: TrasladoDetalleComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TrasladosRoutingModule {}
