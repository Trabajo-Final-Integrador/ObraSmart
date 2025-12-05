import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EquiposListadoComponent } from './equipos-listado/equipos-listado.component';
import { CrearEquipoComponent} from './crear-equipo/crear-equipo.component'

const routes: Routes = [
  { path: '', component: EquiposListadoComponent },
  { path: 'nuevo', component: CrearEquipoComponent },
  { path: 'editar/:id', component: CrearEquipoComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EquiposRoutingModule {}
