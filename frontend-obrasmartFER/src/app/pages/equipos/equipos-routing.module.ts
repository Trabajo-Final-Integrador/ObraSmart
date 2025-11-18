import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EquiposListadoComponent } from './equipos-listado/equipos-listado.component';
import { EquipoDetalleComponent } from './equipo-detalle/equipo-detalle.component';

// 👇 Importás los nuevos componentes
import { ListadoMarcasComponent } from './marcas/listado-marcas/listado-marcas.component';
import { CrearMarcaComponent } from './marcas/crear-marca/crear-marca.component';
import { ListadoModelosComponent } from './modelos/listado-modelos/listado-modelos.component';
import { CrearModeloComponent } from './modelos/crear-modelo/crear-modelo.component';
import { ListadoTipoEquipoComponent } from './tipo-equipo/listado-tipo-equipo/listado-tipo-equipo.component';
import { CrearTipoEquipoComponent } from './tipo-equipo/crear-tipo-equipo/crear-tipo-equipo.component';

const routes: Routes = [
  { path: '', component: EquiposListadoComponent },
  { path: ':id', component: EquipoDetalleComponent },

  // 🔸 Secciones de Marcas
  {
    path: 'marcas',
    children: [
      { path: '', component: ListadoMarcasComponent },
      { path: 'crear', component: CrearMarcaComponent }
    ]
  },

  // 🔸 Secciones de Modelos
  {
    path: 'modelos',
    children: [
      { path: '', component: ListadoModelosComponent },
      { path: 'crear', component: CrearModeloComponent }
    ]
  },

  // 🔸 Secciones de Tipos de Equipo
  {
    path: 'tipo-equipo',
    children: [
      { path: '', component: ListadoTipoEquipoComponent },
      { path: 'crear', component: CrearTipoEquipoComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EquiposRoutingModule {}
