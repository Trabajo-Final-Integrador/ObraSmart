import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CategoriaRepuestoDTO {
  id?: number;
  nombre: string;
  descripcion?: string;
}

@Injectable({ providedIn: 'root' })
export class CategoriaRepuestoService {
  private apiUrl = `${environment.apiUrl}/categorias`;

  constructor(private http: HttpClient) {}

  listar(): Observable<CategoriaRepuestoDTO[]> {
    return this.http.get<CategoriaRepuestoDTO[]>(this.apiUrl);
  }

  crear(dto: CategoriaRepuestoDTO): Observable<CategoriaRepuestoDTO> {
    return this.http.post<CategoriaRepuestoDTO>(this.apiUrl, dto);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
