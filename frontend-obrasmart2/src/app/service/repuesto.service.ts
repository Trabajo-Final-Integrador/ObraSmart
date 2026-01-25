import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface RepuestoDTO {
 id: number;
  codigo: string;
  nombre: string;
  idCategoria: number;
  stock: number;
  stockMinimo: number;
  unidadMedida: string;
  imagenUrl?: string;
}


export interface CategoriaDTO {
  id: number;
  nombre: string;
}

export interface CategoriaRepuestoDTO {
  id: number;
  nombre: string;
}

export interface CategoriaRepuestoCreateDTO {
  nombre: string;
}


@Injectable({ providedIn: 'root' })
export class RepuestoService {
  private apiUrl = `${environment.apiUrl}/repuestos`;
  private apiCategorias = `${environment.apiUrl}/categorias`;

  constructor(private http: HttpClient) {}

  listar(): Observable<RepuestoDTO[]> {
    return this.http.get<RepuestoDTO[]>(this.apiUrl,{ withCredentials: true });
  }

  obtenerPorId(id: number): Observable<RepuestoDTO> {
    return this.http.get<RepuestoDTO>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  crear(dto: RepuestoDTO): Observable<RepuestoDTO> {
    return this.http.post<RepuestoDTO>(this.apiUrl, dto,{
      withCredentials: true});
  }

  actualizar(id: number, repuesto: RepuestoDTO): Observable<RepuestoDTO> {
    return this.http.put<RepuestoDTO>(`${this.apiUrl}/${id}`, repuesto, { withCredentials: true });
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

  listarCategorias(): Observable<CategoriaRepuestoDTO[]> {
  return this.http.get<CategoriaRepuestoDTO[]>(this.apiCategorias, { withCredentials: true });
}

crearCategoria(nombre: string): Observable<CategoriaRepuestoDTO> {
  return this.http.post<CategoriaRepuestoDTO>(this.apiCategorias, { nombre }, { withCredentials: true });
}

}
