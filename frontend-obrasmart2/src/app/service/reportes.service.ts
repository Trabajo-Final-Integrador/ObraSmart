import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardReporteDto } from 'src/app/pages/reportes/reporte.model';

@Injectable({ providedIn: 'root' })
export class ReportesService {

  private apiUrl = 'http://localhost:8085/reportes';

  constructor(private http: HttpClient) {}

  getDashboard(): Observable<DashboardReporteDto> {
    console.log('🔍 Solicitando dashboard de reportes...');
    return this.http.get<DashboardReporteDto>(`${this.apiUrl}/dashboard`);
  }

  generarDashboard(): Observable<DashboardReporteDto> {
    console.log('🔄 Generando nuevo dashboard...');
    return this.http.post<DashboardReporteDto>(`${this.apiUrl}/dashboard`, {});
  }
}
