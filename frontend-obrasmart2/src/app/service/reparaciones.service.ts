import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReparacionResponseDTO {
  id: number;
  equipoId: number;
  equipoNombre?: string;
  equipoCodigoInterno?: string;
  tipoMantenimiento: string;
  estadoReparacion: string;
  descripcion: string;
  responsableId: number;
  responsableNombreCompleto: string;
  direccion: string;
  lat: number;
  lon: number;
  fechaCreacion: string;
  fechaInicio: string;
  fechaFin: string;
  fechaUltimaActualizacion: string;
  usuarioUltimaActualizacionId: number;
  historialEstados: any;
}



export interface ReparacionDto {
  equipoId: number;
  descripcion: string;
  estado: string;
  direccion:string;
}

export interface ReparacionRequestDto {
  equipoId: number;
  tipoMantenimiento: string;
  direccion: string;
  responsableId: number;
  descripcion: string;
  estadoReparacion?: string;
  fechaInicio?: string;
}


@Injectable({ providedIn: 'root' })
export class ReparacionService {

  private apiUrl = 'http://localhost:8085/reparaciones';

  constructor(private http: HttpClient) {}

  listar(): Observable<ReparacionResponseDTO[]> {
    return this.http.get<ReparacionResponseDTO[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<ReparacionResponseDTO> {
    return this.http.get<ReparacionResponseDTO>(`${this.apiUrl}/${id}`);
  }

  crear(data: ReparacionRequestDto): Observable<ReparacionResponseDTO> {
    return this.http.post<ReparacionResponseDTO>(this.apiUrl, data);
  }

  actualizar(id: number, data: ReparacionRequestDto): Observable<ReparacionResponseDTO> {
    return this.http.put<ReparacionResponseDTO>(`${this.apiUrl}/${id}`, data);
  }

  cancelar(id: number): Observable<ReparacionResponseDTO> {
    return this.http.patch<ReparacionResponseDTO>(`${this.apiUrl}/${id}/estado?nuevoEstado=CANCELADA`, {});
  }

  cambiarEstado(id: number, nuevoEstado: string, comentario?: string): Observable<ReparacionResponseDTO> {
    let url = `${this.apiUrl}/${id}/estado?nuevoEstado=${nuevoEstado}`;
    if (comentario) {
      url += `&comentario=${encodeURIComponent(comentario)}`;
    }
    return this.http.patch<ReparacionResponseDTO>(url, {});
  }
}
