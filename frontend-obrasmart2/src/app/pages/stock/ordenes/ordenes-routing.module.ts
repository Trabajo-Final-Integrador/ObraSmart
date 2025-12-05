import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ListadoComponent } from './listado/listado.component';
import { NuevaOrdenComponent } from './nueva-orden/nueva-orden.component';

const routes: Routes = [
  { path: '', component: ListadoComponent },
  { path: 'nueva', component: NuevaOrdenComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrdenesRoutingModule {}
