import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from 'src/environments/environment';

export interface GeocodingResult {
  lat: number;
  lon: number;
  displayName?: string;
}

@Injectable({ providedIn: 'root' })
export class GeocodingService {
  private readonly baseUrl = `${environment.apiUrl}/geo`;

  constructor(private http: HttpClient) {}

  buscar(query: string): Observable<GeocodingResult | null> {
    const params = new HttpParams()
      .set('direccion', query);

    return this.http.get<GeocodingResult>(`${this.baseUrl}/geocode`, { params }).pipe(
      map((res) => {
        if (!res) return null;
        return {
          lat: Number(res.lat),
          lon: Number(res.lon),
          displayName: res.displayName
        } as GeocodingResult;
      })
    );
  }

  reverse(lat: number, lon: number): Observable<GeocodingResult | null> {
    const params = new HttpParams()
      .set('lat', String(lat))
      .set('lon', String(lon));

    return this.http.get<GeocodingResult>(`${this.baseUrl}/reverse`, { params }).pipe(
      map((res) => (res ? { lat: Number(res.lat), lon: Number(res.lon), displayName: res.displayName } : null))
    );
  }
}
