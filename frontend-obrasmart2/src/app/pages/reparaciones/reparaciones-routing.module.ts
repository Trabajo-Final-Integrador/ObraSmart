import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListadoReparacionesComponent } from './listado-reparaciones/listado-reparaciones.component';
import { CrearReparacionComponent } from './crear-reparaciones/crear-reparaciones.component';
import { DashboardReparacionesComponent } from './dashboard-reparaciones/dashboard-reparaciones.component';
import { EditarReparacionesComponent } from './editar-reparaciones/editar-reparaciones.component';

const routes: Routes = [
  { path: '', redirectTo: 'listado', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardReparacionesComponent },
  { path: 'listado', component: ListadoReparacionesComponent },
  { path: 'crear', component: CrearReparacionComponent },
  { path: 'editar/:id', component: EditarReparacionesComponent },
  { path: 'detalle/:id', component: EditarReparacionesComponent }
];



@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReparacionesRoutingModule {}
