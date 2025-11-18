import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ListadoRepuestosComponent } from './repuestos/listado-repuestos/listado-repuestos.component';
import { CrearRepuestoComponent } from './repuestos/crear-repuesto/crear-repuesto.component';

import { ListadoProveedoresComponent } from './proveedores/listado-proveedores/listado-proveedores.component';
import { CrearProveedorComponent } from './proveedores/crear-proveedor/crear-proveedor.component';

import { ListadoMovimientosComponent } from './movimientos/listado-movimientos/listado-movimientos.component';
import { RegistrarMovimientoComponent } from './movimientos/registrar-movimiento/registrar-movimiento.component';


const routes: Routes = [
 
  // 🔹 Rutas de Repuestos
  { path: 'repuestos', component: ListadoRepuestosComponent },
  { path: 'repuestos/crear', component: CrearRepuestoComponent },

  // 🔹 Rutas de Proveedores
  { path: 'proveedores', component: ListadoProveedoresComponent },
  { path: 'proveedores/crear', component: CrearProveedorComponent },

  // Movimientos
  { path: 'movimientos', component: ListadoMovimientosComponent },
  { path: 'movimientos/registrar', component: RegistrarMovimientoComponent },

   { path: '', redirectTo: 'repuestos', pathMatch: 'full' },
   

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StockRoutingModule {}
