import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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
  especialidad?: string;
  marcas?: string[];
  tiempoEntrega?: number;
  pedidoMinimo?: number;
  descuentoVolumen?: number;
  condicionesPago?: string;
  tieneStock?: boolean;
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
  especialidad?: string;
  marcas?: string[];
  horarioAtencion?: string;
  tiempoEntrega?: number;
  pedidoMinimo?: number;
  descuentoVolumen?: number;
  condicionesPago?: string;
  tieneStock?: boolean;
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
  especialidad?: string;
  marcas?: string[];
  horarioAtencion?: string;
  tiempoEntrega?: number;
  pedidoMinimo?: number;
  descuentoVolumen?: number;
  condicionesPago?: string;
  tieneStock?: boolean;
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

export interface ProveedorListadoDTO {
  id: number;
  razonSocial: string;
  especialidad: string | null;
  contacto: string;
  estado: string;
}

@Injectable({ providedIn: 'root' })
export class ProveedorService {
  private apiUrl = 'http://localhost:8085/proveedores';

  constructor(private http: HttpClient) {}

  listar(): Observable<ProveedorListadoDTO[]> {
    return this.http.get<ProveedorListadoDTO[]>(this.apiUrl, { withCredentials: true });
  }

  obtenerPorId(id: number): Observable<ProveedorDto> {
    return this.http.get<ProveedorDto>(`${this.apiUrl}/${id}`, { withCredentials: true });
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
    const params = new HttpParams().set('nuevoEstado', 'Inactivo');
    return this.http.patch<void>(
      `${this.apiUrl}/${id}/estado`,
      null,
      {
        params,
        withCredentials: true
      }
    );
  }

  cambiarEstado(id: number, estado: string) {
    return this.http.patch(
      `${this.apiUrl}/${id}/estado`,
      null,
      {
        params: { nuevoEstado: estado.toUpperCase() }
      }
    );
  }
}
