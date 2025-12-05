import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MarcaDTO {
  id?: number;
  nombre: string;
}

@Injectable({ providedIn: 'root' })
export class MarcaService {
  private apiUrl = 'http://localhost:8085/marca';

  constructor(private http: HttpClient) {}

  
  listar(): Observable<MarcaDTO[]> {
    return this.http.get<MarcaDTO[]>(this.apiUrl);
  }

  crear(dto: MarcaDTO): Observable<MarcaDTO> {
    return this.http.post<MarcaDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: MarcaDTO): Observable<MarcaDTO> {
    return this.http.put<MarcaDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
