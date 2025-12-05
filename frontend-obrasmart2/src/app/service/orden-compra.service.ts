import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrdenCompra } from 'src/app/pages/stock/ordenes/orden-compra.model';

export interface OrdenCompraDTO {
  id?: number;
  idProveedor: number;
  proveedorNombre?: string;
  estado?: string;
  fecha?: string;
   totalItems?: number;
  total?: number;
  
   items: OrdenItemDTO[];
}


export interface OrdenItemDTO {
  idRepuesto: number;
  repuestoNombre: string; 
  cantidad: number;
  precioUnitario: number;
}

export interface OrdenCompraListadoDTO {
  id: number;
  idProveedor: number;
  proveedorNombre: string;
  estado: string;
  totalItems: number;
  total: number;
  fecha: string;
}

export interface OrdenItemCreateDTO {
  idRepuesto: number;
  cantidad: number;
  precioUnitario: number;
}

export interface OrdenCompraCreateDTO {
  idProveedor: number;
  items: OrdenItemCreateDTO[];
}


@Injectable({ providedIn: 'root' })
export class OrdenCompraService {
  private apiUrl = 'http://localhost:8085/ordenes';

  constructor(private http: HttpClient) {}

  crear(dto: OrdenCompraCreateDTO): Observable<OrdenCompraDTO> {
  return this.http.post<OrdenCompraDTO>(this.apiUrl, dto, { withCredentials: true });
}


 aprobar(id: number): Observable<OrdenCompraDTO> {
  return this.http.post<OrdenCompraDTO>(
    `${this.apiUrl}/${id}/aprobar`,
    {},
    { withCredentials: true }
  );
}

recibir(id: number): Observable<OrdenCompraDTO> {
  return this.http.post<OrdenCompraDTO>(
    `${this.apiUrl}/${id}/recibir`,
    {},
    { withCredentials: true }
  );
}

cancelar(id: number): Observable<OrdenCompraDTO> {
  return this.http.post<OrdenCompraDTO>(
    `${this.apiUrl}/${id}/cancelar`,
    {},
    { withCredentials: true }
  );
}

obtener(id: number): Observable<OrdenCompraDTO> {
  return this.http.get<OrdenCompraDTO>(
    `${this.apiUrl}/${id}`,
    { withCredentials: true }
  );
}


listar(): Observable<OrdenCompraListadoDTO[]> {
    return this.http.get<OrdenCompraListadoDTO[]>(this.apiUrl, { withCredentials: true });
  }
  
}
