import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ObradorDto {
  id: number;
  nombre: string;
  ubicacion?: string;
  supervisorUserId?: number;
  equipoIds?: number[];
  // Campos geoespaciales esperados (a completar en backend)
  lat?: number;
  lng?: number;
  zona?: Array<[number, number]>; // polígono opcional
  estado?: 'ACTIVO' | 'INACTIVO';
}

@Injectable({
  providedIn: 'root',
})
export class ObradorService {
  private baseUrl = `${environment.apiUrl}/obradores`;

  constructor(private http: HttpClient) {}

  listar(): Observable<ObradorDto[]> {
    return this.http.get<ObradorDto[]>(this.baseUrl);
  }

  obtener(id: number): Observable<ObradorDto> {
    return this.http.get<ObradorDto>(`${this.baseUrl}/${id}`);
  }

  crear(dto: Partial<ObradorDto>): Observable<ObradorDto> {
    return this.http.post<ObradorDto>(this.baseUrl, dto);
  }

  actualizar(id: number, dto: Partial<ObradorDto>): Observable<ObradorDto> {
    return this.http.put<ObradorDto>(`${this.baseUrl}/${id}`, dto);
  }

  asignarEquipo(id: number, equipoId: number): Observable<ObradorDto> {
    return this.http.post<ObradorDto>(`${this.baseUrl}/${id}/equipos`, { equipoId });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
