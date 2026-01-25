import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface EquipoUbicacion {
  id: number;
  nombre: string;
  lat: number;
  lon: number;
  estado: string;
}

@Injectable({
  providedIn: 'root'
})
export class GeolocalizacionService {

  private apiUrl = `${environment.apiUrl}/geolocalizacion/locations`;

  constructor(private http: HttpClient) {}

  obtenerEquipos(): Observable<EquipoUbicacion[]> {
    return this.http.get<EquipoUbicacion[]>(this.apiUrl);
  }

  obtenerUbicaciones(): Observable<EquipoUbicacion[]> {
    return this.http.get<EquipoUbicacion[]>(this.apiUrl);
  }
}
