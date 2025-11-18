import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RepuestoDTO {
  id?: number;
  nombre: string;
  codigoInterno?: string;
  cantidad: number;
  stockMinimo: number;
  precioUnitario: number;
  unidadMedida: string;
  ubicacion?: string;
  categoriaId?: number;
  proveedorId?: number;
  estado?: boolean;
}

@Injectable({ providedIn: 'root' })
export class RepuestoService {
  private apiUrl = 'http://localhost:8083/api/repuestos';

  constructor(private http: HttpClient) {}

  listar(): Observable<RepuestoDTO[]> {
    return this.http.get<RepuestoDTO[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<RepuestoDTO> {
    return this.http.get<RepuestoDTO>(`${this.apiUrl}/${id}`);
  }

  crear(repuesto: RepuestoDTO): Observable<RepuestoDTO> {
    return this.http.post<RepuestoDTO>(this.apiUrl, repuesto);
  }

  actualizar(id: number, repuesto: RepuestoDTO): Observable<RepuestoDTO> {
    return this.http.put<RepuestoDTO>(`${this.apiUrl}/${id}`, repuesto);
  }

  agregarStock(id: number, cantidad: number): Observable<RepuestoDTO> {
    return this.http.post<RepuestoDTO>(`${this.apiUrl}/${id}/agregar/${cantidad}`, {});
  }

  sacarStock(id: number, cantidad: number): Observable<RepuestoDTO> {
    return this.http.post<RepuestoDTO>(`${this.apiUrl}/${id}/sacar/${cantidad}`, {});
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
