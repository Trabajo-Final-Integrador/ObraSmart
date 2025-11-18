import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MovimientoStockDTO {
  id?: number;
  repuestoId: number;
  tipo: 'ENTRADA' | 'SALIDA' | 'AJUSTE';
  cantidad: number;
  motivo: string;
  usuario: string;
}

@Injectable({ providedIn: 'root' })
export class MovimientoStockService {
  private apiUrl = 'http://localhost:8083/api/movimientos';

  constructor(private http: HttpClient) {}

  registrar(dto: MovimientoStockDTO): Observable<MovimientoStockDTO> {
    return this.http.post<MovimientoStockDTO>(this.apiUrl, dto);
  }

  listarPorRepuesto(idRepuesto: number): Observable<MovimientoStockDTO[]> {
    return this.http.get<MovimientoStockDTO[]>(`${this.apiUrl}/repuesto/${idRepuesto}`);
  }

  listar(): Observable<MovimientoStockDTO[]> {
    return this.http.get<MovimientoStockDTO[]>(this.apiUrl);
  }
}
