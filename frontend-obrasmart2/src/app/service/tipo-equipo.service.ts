import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TipoEquipoDTO {
  id?: number;
  nombre: string;
}

@Injectable({ providedIn: 'root' })
export class TipoEquipoService {
  private apiUrl = 'http://localhost:8085/tipo-equipo';

  constructor(private http: HttpClient) {}

  listar(): Observable<TipoEquipoDTO[]> {
    return this.http.get<TipoEquipoDTO[]>(this.apiUrl);
  }

  crear(dto: TipoEquipoDTO): Observable<TipoEquipoDTO> {
    return this.http.post<TipoEquipoDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: TipoEquipoDTO): Observable<TipoEquipoDTO> {
    return this.http.put<TipoEquipoDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
