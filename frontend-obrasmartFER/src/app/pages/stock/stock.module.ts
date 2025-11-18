import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { StockRoutingModule } from './stock-routing.module';

import { ListadoRepuestosComponent } from './repuestos/listado-repuestos/listado-repuestos.component';
import { CrearRepuestoComponent } from './repuestos/crear-repuesto/crear-repuesto.component';

import { CrearCategoriaComponent } from './categorias/crear-categoria/crear-categoria.component';
import { ListadoCategoriasComponent } from './categorias/listado-categorias/listado-categorias.component';

import { ListadoProveedoresComponent } from './proveedores/listado-proveedores/listado-proveedores.component';
import { CrearProveedorComponent } from './proveedores/crear-proveedor/crear-proveedor.component';


import { ListadoMovimientosComponent } from './movimientos/listado-movimientos/listado-movimientos.component';
import { RegistrarMovimientoComponent } from './movimientos/registrar-movimiento/registrar-movimiento.component';
import { StepContactoComponent } from './proveedores/crear-proveedor/step-contacto/step-contacto.component';
import { StepConfirmarComponent } from './proveedores/crear-proveedor/step-confirmar/step-confirmar.component';
import { StepBancariosComponent } from './proveedores/crear-proveedor/step-bancarios/step-bancarios.component';
import {StepComercioComponent} from './proveedores/crear-proveedor/step-comercio/step-comercio.component'
import { StepCatalogoComponent } from './proveedores/crear-proveedor/step-catalogo/step-catalogo.component';


@NgModule({
  declarations: [
    ListadoRepuestosComponent,
    CrearRepuestoComponent,
    CrearCategoriaComponent,
    ListadoCategoriasComponent,
    ListadoProveedoresComponent,
    CrearProveedorComponent,
    ListadoMovimientosComponent,
    RegistrarMovimientoComponent,
    StepContactoComponent,
    StepConfirmarComponent,
    StepBancariosComponent,
    StepComercioComponent,
    StepCatalogoComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    StockRoutingModule
  ]
})
export class StockModule {}
