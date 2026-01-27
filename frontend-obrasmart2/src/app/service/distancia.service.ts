// distancia.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

export interface DistanciaCalculo {
  equipoId: number;
  equipoNombre: string;
  equipoLatitud: number;
  equipoLongitud: number;
  obradorId: number;
  obradorNombre: string;
  obradorLatitud: number;
  obradorLongitud: number;
  distanciaKm: number;
  mensaje: string;
}

@Injectable({
  providedIn: 'root'
})
export class DistanciaService {
  
  private apiUrl = `${environment.apiUrl}/distancias`;

  constructor(private http: HttpClient) { }

  /**
   * Calcula la distancia entre un equipo y un obrador
   * @param equipoId ID del equipo
   * @param obradorId ID del obrador
   * @returns Observable con la información de distancia
   */
  calcularDistancia(equipoId: number, obradorId: number): Observable<DistanciaCalculo> {
    const params = new HttpParams()
      .set('equipoId', equipoId.toString())
      .set('obradorId', obradorId.toString());
    
    return this.http.get<DistanciaCalculo>(`${this.apiUrl}/calcular`, { params });
  }

  /**
   * Calcula distancia usando la fórmula de Haversine directamente en el frontend
   * Útil para cálculos rápidos sin llamar al backend
   */
  calcularDistanciaLocal(
    lat1: number, lon1: number, 
    lat2: number, lon2: number
  ): number {
    const R = 6371; // Radio de la Tierra en km
    const dLat = this.toRad(lat2 - lat1);
    const dLon = this.toRad(lon2 - lon1);
    
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(this.toRad(lat1)) * Math.cos(this.toRad(lat2)) *
              Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distancia = R * c;
    
    return Math.round(distancia * 100) / 100; // Redondear a 2 decimales
  }

  private toRad(grados: number): number {
    return grados * (Math.PI / 180);
  }
}