import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ListadoRepuestosComponent } from './repuestos/listado-repuestos/listado-repuestos.component';
import { CrearRepuestoComponent } from './repuestos/crear-repuesto/crear-repuesto.component';
import { EditarRepuestoComponent } from './repuestos/editar-repuesto/editar-repuesto.component';

import { ListadoProveedoresComponent } from './proveedores/listado-proveedores/listado-proveedores.component';
import { CrearProveedorComponent } from './proveedores/crear-proveedor/crear-proveedor.component';
import { EditarProveedorComponent } from './proveedores/editar-proveedor/editar-proveedor.component';

import { MovimientosListadoComponent } from './movimientos/listado-movimientos/listado-movimientos.component';
import { CrearMovimientoComponent } from './movimientos/crear-movimiento/crear-movimiento.component'


import { ListadoComponent } from './ordenes/listado/listado.component';
import { NuevaOrdenComponent } from './ordenes/nueva-orden/nueva-orden.component';
import {DetalleOrdenComponent} from './ordenes/detalles/detalle-orden.component'

import { DashboardStockComponent } from './dashboard/dashboard-stock/dashboard-stock.component'





const routes: Routes = [
 
  // 🔹 Rutas de Repuestos
  { path: 'repuestos', component: ListadoRepuestosComponent },
  { path: 'repuestos/crear', component: CrearRepuestoComponent },
  { path: 'repuestos/editar/:id', component: EditarRepuestoComponent },

  // 🔹 Rutas de Proveedores
  { path: 'proveedores', component: ListadoProveedoresComponent },
  { path: 'proveedores/crear', component: CrearProveedorComponent },
  { path: 'proveedores/editar/:id', component: EditarProveedorComponent },

  // Movimientos
  { path: 'movimientos',component: MovimientosListadoComponent},
  { path: 'movimientos/crear', component: CrearMovimientoComponent  },


 // { path: 'movimientos/registrar', component: RegistrarMovimientoComponent },

  //ordenes
  { path: 'ordenes', component: ListadoComponent },
  { path: 'ordenes/crear', component: NuevaOrdenComponent },
  { path: 'ordenes/detalle/:id', component: DetalleOrdenComponent },

  //dashboard
{ path: 'dashboard', component: DashboardStockComponent },



   { path: '', redirectTo: 'repuestos', pathMatch: 'full' },
   

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StockRoutingModule {}
