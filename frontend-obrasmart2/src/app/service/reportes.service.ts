import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardReporteDto } from 'src/app/pages/reportes/reporte.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReportesService {

  private apiUrl = `${environment.apiUrl}/reportes`;

  constructor(private http: HttpClient) {}

  getDashboard(filters?: { startDate?: string; endDate?: string; preset?: string }): Observable<DashboardReporteDto> {
    console.log('🔍 Solicitando dashboard de reportes...');

    let params = new HttpParams();
    if (filters?.startDate) {
      params = params.set('startDate', filters.startDate);
    }
    if (filters?.endDate) {
      params = params.set('endDate', filters.endDate);
    }
    if (filters?.preset) {
      params = params.set('preset', filters.preset);
    }

    return this.http.get<DashboardReporteDto>(`${this.apiUrl}/dashboard`, {
      params,
      withCredentials: true
    });
  }

  generarDashboard(): Observable<DashboardReporteDto> {
    console.log('🔄 Generando nuevo dashboard...');
    return this.http.post<DashboardReporteDto>(`${this.apiUrl}/dashboard`, {});
  }
}
