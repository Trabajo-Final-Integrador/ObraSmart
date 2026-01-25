import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { StockRoutingModule } from './stock-routing.module';
import { DatePipe, DecimalPipe } from '@angular/common';
import { ListadoRepuestosComponent } from './repuestos/listado-repuestos/listado-repuestos.component';
import { ListadoProveedoresComponent } from './proveedores/listado-proveedores/listado-proveedores.component';
import { CrearProveedorComponent } from './proveedores/crear-proveedor/crear-proveedor.component';
import { SharedModule } from '../../shared/shared.module';
import { StepContactoComponent } from './proveedores/crear-proveedor/step-contacto/step-contacto.component';
import { StepObservacionesComponent } from './proveedores/crear-proveedor/step-observaciones/step-observaciones.component';
import { StepBancariosComponent } from './proveedores/crear-proveedor/step-bancarios/step-bancarios.component';
import {StepComercioComponent} from './proveedores/crear-proveedor/step-comercio/step-comercio.component'
import { StepCatalogoComponent } from './proveedores/crear-proveedor/step-catalogo/step-catalogo.component';

import { ListadoComponent } from './ordenes/listado/listado.component';
import { NuevaOrdenComponent } from './ordenes/nueva-orden/nueva-orden.component';
import {DetalleOrdenComponent} from './ordenes/detalles/detalle-orden.component'
import { CrearRepuestoComponent } from './repuestos/crear-repuesto/crear-repuesto.component';
import { MovimientosListadoComponent } from './movimientos/listado-movimientos/listado-movimientos.component';
import { CrearMovimientoComponent} from './movimientos/crear-movimiento/crear-movimiento.component';
import { DashboardStockComponent } from './dashboard/dashboard-stock/dashboard-stock.component';
import { EditarRepuestoComponent } from './repuestos/editar-repuesto/editar-repuesto.component';
import { EditarProveedorComponent } from './proveedores/editar-proveedor/editar-proveedor.component'
import { RepuestoDetalleModalComponent } from './repuestos/repuesto-detalle-modal/repuesto-detalle-modal.component';
import { OrdenDetalleModalComponent } from './ordenes/detalles/orden-detalle-modal/orden-detalle-modal.component';
import { ProveedorDetalleModalComponent } from './proveedores/proveedor-detalle-modal/proveedor-detalle-modal.component';



@NgModule({
  declarations: [
    ListadoRepuestosComponent,
    ListadoProveedoresComponent,
    CrearProveedorComponent,
    StepContactoComponent,
    StepObservacionesComponent,
    StepBancariosComponent,
    StepComercioComponent,
    StepCatalogoComponent,
    ListadoComponent,
    NuevaOrdenComponent,
    CrearRepuestoComponent,
    MovimientosListadoComponent,
    DetalleOrdenComponent,
    CrearMovimientoComponent,
    DashboardStockComponent,
    EditarRepuestoComponent,
    EditarProveedorComponent,
    RepuestoDetalleModalComponent,
    OrdenDetalleModalComponent,
    ProveedorDetalleModalComponent
  
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    StockRoutingModule,
    SharedModule
  ],

  providers: [
    DatePipe,
    DecimalPipe
  ]
})
export class StockModule {}
