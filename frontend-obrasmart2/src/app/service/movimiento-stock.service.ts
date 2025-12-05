import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movimiento } from 'src/app/pages/stock/movimientos/movimiento.model';
import { MovimientoStockDto } from '../pages/stock/movimientos/movimiento-stock-dto.model';

export interface MovimientoStockResponseDto {
  id: number;
  idRepuesto: number;
  repuestoNombre: string;
  tipo: 'ENTRADA' | 'SALIDA';
  cantidad: number;
  observacion?: string;
  fecha: string;
}



@Injectable({ providedIn: 'root' })
export class MovimientosService {

  private apiUrl = 'http://localhost:8085/movimientos';

  constructor(private http: HttpClient) {}

  listar(): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(this.apiUrl, { withCredentials: true });
  }
   crear(dto: MovimientoStockDto): Observable<MovimientoStockResponseDto> {
    return this.http.post<MovimientoStockResponseDto>(this.apiUrl, dto, { withCredentials: true });
  }
}
