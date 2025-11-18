import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrdenCompraDTO {
  id?: number;
  fechaCreacion?: string;
  estado?: string;
  total?: number;
  proveedorId: number;
  repuestos: { idRepuesto: number; cantidad: number; precioUnitario: number }[];
}

@Injectable({ providedIn: 'root' })
export class OrdenCompraService {
  private apiUrl = 'http://localhost:8083/api/ordenes';

  constructor(private http: HttpClient) {}

  crear(dto: OrdenCompraDTO): Observable<OrdenCompraDTO> {
    return this.http.post<OrdenCompraDTO>(this.apiUrl, dto);
  }

  aprobar(id: number): Observable<OrdenCompraDTO> {
    return this.http.post<OrdenCompraDTO>(`${this.apiUrl}/${id}/aprobar`, {});
  }

  recibir(id: number, dto: OrdenCompraDTO): Observable<OrdenCompraDTO> {
    return this.http.post<OrdenCompraDTO>(`${this.apiUrl}/${id}/recibir`, dto);
  }

  cancelar(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/cancelar`, {});
  }

  listarPendientes(): Observable<OrdenCompraDTO[]> {
    return this.http.get<OrdenCompraDTO[]>(`${this.apiUrl}/pendientes`);
  }
}
