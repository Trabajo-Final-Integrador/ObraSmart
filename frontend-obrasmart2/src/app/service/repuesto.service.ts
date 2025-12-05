import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RepuestoDTO {
 id: number;
  codigo: string;
  nombre: string;
  idCategoria: number;
  stock: number;
  stockMinimo: number;
  unidadMedida: string;
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
  private apiUrl = 'http://localhost:8085/repuestos';
  private apiCategorias = 'http://localhost:8085/categorias';

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
  return this.http.get<CategoriaRepuestoDTO[]>('http://localhost:8085/categorias', { withCredentials: true });
}

crearCategoria(nombre: string): Observable<CategoriaRepuestoDTO> {
  return this.http.post<CategoriaRepuestoDTO>('http://localhost:8085/categorias', { nombre }, { withCredentials: true });
}

}
