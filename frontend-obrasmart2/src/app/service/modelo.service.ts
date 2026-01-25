import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MarcaDTO } from './marca.service';
import { environment } from '../../environments/environment';

export interface ModeloDTO {
  id?: number;
  nombre: string;
  marca: MarcaDTO;
}

@Injectable({ providedIn: 'root' })
export class ModeloService {
  private apiUrl = `${environment.apiUrl}/modelo`;

  constructor(private http: HttpClient) {}

  listar(): Observable<ModeloDTO[]> {
    return this.http.get<ModeloDTO[]>(this.apiUrl);
  }

 crear(dto: any): Observable<ModeloDTO> {
  return this.http.post<ModeloDTO>(this.apiUrl, dto);
}


  update(id: number, dto: ModeloDTO): Observable<ModeloDTO> {
    return this.http.put<ModeloDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
