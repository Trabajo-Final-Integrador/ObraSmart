import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportesComponent } from './components/reporte.component';

import { ReportesRoutingModule } from './reportes-routing.module';
import { FormsModule } from '@angular/forms';
import { NgChartsModule } from 'ng2-charts'; // Para gráficos
import { HttpClientModule } from '@angular/common/http';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ReportesComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    NgChartsModule,
    ReportesRoutingModule,
    SharedModule  // Sidebar y ModalInfo compartidos
  ]
})
export class ReportesModule {}
