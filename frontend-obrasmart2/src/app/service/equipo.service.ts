import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface EquipoDTO {
  id?: number;
  nombre: string;
  codigoInterno:string;
  idTipoEquipo: number;
  idMarca: number;
  idModelo: number;
  numeroSerie: string;
  anioFabricacion: number;
  potenciaHp: number;
  combustible: string;
  estadoOperativo: string;
  kilometrajeHorasUso: number;
  fechaUltimoMantenimiento: string;
  proximoMantenimiento: string;
  responsableMantenimiento: string;
  numeroPatente?: string;
  seguroVigente: boolean;
  fechaVencimientoSeguro?: string;
  ubicacionActual: string;
  activo: boolean;
   latitud: number;     // 👈 agregado
  longitud: number; 
}

@Injectable({ providedIn: 'root' })
export class EquipoService {
  private apiUrl = `${environment.apiUrl}/equipos`;
  private gatewayUrl = (environment as any).gatewayUrl || 'http://localhost:8085';

  constructor(private http: HttpClient) {}

  listar(): Observable<EquipoDTO[]> {
    return this.http.get<EquipoDTO[]>(this.apiUrl);
  }

  getById(id: number): Observable<EquipoDTO> {
    return this.http.get<EquipoDTO>(`${this.apiUrl}/${id}`);
  }

  obtenerPorId(id: number): Observable<EquipoDTO> {
    return this.http.get<EquipoDTO>(`${this.apiUrl}/${id}`);
  }

  crear(equipo: EquipoDTO): Observable<EquipoDTO> {
    return this.http.post<EquipoDTO>(this.apiUrl, equipo);
  }

  actualizar(id: number, equipo: EquipoDTO): Observable<EquipoDTO> {
    return this.http.put<EquipoDTO>(`${this.apiUrl}/${id}`, equipo);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  normalizeImageUrl(raw: string | null | undefined): string | null {
    if (!raw || !raw.toString().trim()) return null;
    const val = raw.toString().trim();
    if (val.startsWith('http://') || val.startsWith('https://')) return val;
    if (val.startsWith('/')) return `${this.gatewayUrl}${val}`;
    const prefix = '/files/';
    return `${this.gatewayUrl}${prefix}${val}`;
  }
}
