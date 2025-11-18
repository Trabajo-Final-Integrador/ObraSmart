import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type CondicionIVA =
  | 'RESPONSABLE_INSCRIPTO'
  | 'MONOTRIBUTISTA'
  | 'EXENTO'
  | 'CONSUMIDOR_FINAL';

export type TipoProveedor =
  | 'NACIONAL'
  | 'IMPORTADO'
  | 'SERVICIO';

export interface ProveedorDto {
  id: number;
  razonSocial: string;
  nombreComercial?: string;
  cuit: string;
  condicionIVA: string;
  estado: string;
  telefono: string;
  email: string;
  personaContacto?: string;
  horarioAtencion?: string;
  direccion: string;
  ciudad: string;
  provincia: string;
  codigoPostal?: string;
 
}

// DTO para crear un proveedor (sin id)
export interface ProveedorCreateDTO {
 
  razonSocial: string;
  nombreComercial: string;
  cuit: string;
  condicionIVA: string;
  estado: string;
  telefono: string;
  email: string;
  direccion: string;
  ciudad: string;
  provincia: string;
  codigoPostal: string;

  // 🧩 AGREGA ESTO:
  especialidad?: string;

  horarioAtencion?: string;

  tiempoEntrega?: string;
  pedidoMinimo?: string;
  descuentoVolumen?: string;
  condicionesPago?: string;
  tieneStock?: boolean;
  atieneUrgencias?: boolean;
  haceEnvios?: boolean;
  aceptaDevoluciones?: boolean;
  zonaCobertura?: string;
  tieneCatalogo?: boolean;
  urlCatalogo?: string;
  codigoCliente?: string;
  banco?: string;
  tipoCuenta?: string;
  cbu?: string;
  observaciones?: string;
 
}


export interface ProveedorUpdateDTO extends ProveedorCreateDTO {
  razonSocial: string;
  nombreComercial: string;
  cuit: string;
  condicionIVA: string;
  estado: string;
  telefono: string;
  email: string;
  direccion: string;
  ciudad: string;
  provincia: string;
  codigoPostal: string;

  // 🧩 AGREGA ESTO:
  especialidad?: string;

  horarioAtencion?: string;

  tiempoEntrega?: string;
  pedidoMinimo?: string;
  descuentoVolumen?: string;
  condicionesPago?: string;
  tieneStock?: boolean;
  atieneUrgencias?: boolean;
  haceEnvios?: boolean;
  aceptaDevoluciones?: boolean;
  zonaCobertura?: string;
  tieneCatalogo?: boolean;
  urlCatalogo?: string;
  codigoCliente?: string;
  banco?: string;
  tipoCuenta?: string;
  cbu?: string;
  observaciones?: string;
}

@Injectable({ providedIn: 'root' })
export class ProveedorService {
  private apiUrl = 'http://localhost:8085/api/proveedores';

  constructor(private http: HttpClient) {}

  listar(): Observable<ProveedorDto[]> {
    return this.http.get<ProveedorDto[]>(this.apiUrl, { withCredentials: true });
  }

  crear(dto: ProveedorCreateDTO): Observable<ProveedorCreateDTO> {
    return this.http.post<ProveedorCreateDTO>(this.apiUrl, dto, {
      withCredentials: true,
      headers: { 'Content-Type': 'application/json' }
    });
  }

  actualizar(id: number, dto: ProveedorUpdateDTO): Observable<ProveedorUpdateDTO> {
    return this.http.put<ProveedorUpdateDTO>(`${this.apiUrl}/${id}`, dto, {
      withCredentials: true,
      headers: { 'Content-Type': 'application/json' }
    });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
