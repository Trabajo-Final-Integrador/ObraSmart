import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface PuntoRutaDto {
  lat: number;
  lng: number;
}

export interface TrasladoDto {
  id: number;
  equipoId: number;
  origenObradorId?: number;
  origenUbicacion?: string;
  destinoObradorId: number;
  programadoPara?: string;
  fechaCreacion: string;
  estado: string;
  notas?: string;
  // Campos de geolocalización a exponer por backend o gateway
  origenLat?: number;
  origenLng?: number;
  destinoLat?: number;
  destinoLng?: number;
  posicionActual?: { lat: number; lng: number; heading?: number };
  ruta?: PuntoRutaDto[];
}

@Injectable({
  providedIn: 'root',
})
export class LogisticaService {
  private baseUrl = `${environment.apiUrl}/traslados`;

  constructor(private http: HttpClient) {}

  listar(): Observable<TrasladoDto[]> {
    return this.http.get<TrasladoDto[]>(this.baseUrl);
  }

  obtener(id: number): Observable<TrasladoDto> {
    return this.http.get<TrasladoDto>(`${this.baseUrl}/${id}`);
  }

  crear(dto: Partial<TrasladoDto>): Observable<TrasladoDto> {
    return this.http.post<TrasladoDto>(this.baseUrl, dto);
  }

  cambiarEstado(id: number, estado: string): Observable<TrasladoDto> {
    return this.http.patch<TrasladoDto>(`${this.baseUrl}/${id}/estado`, null, {
      params: { estado },
    });
  }
}
