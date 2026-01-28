import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export interface GeocodingResult {
  lat: number;
  lon: number;
  displayName?: string;
}

@Injectable({ providedIn: 'root' })
export class GeocodingService {
  private readonly baseUrl = 'https://nominatim.openstreetmap.org/search';

  private rawHttp: HttpClient;

  constructor(private http: HttpClient, httpBackend: HttpBackend) {
    this.rawHttp = new HttpClient(httpBackend);
  }

  buscar(query: string): Observable<GeocodingResult | null> {
    const params = new HttpParams()
      .set('format', 'json')
      .set('q', query)
      .set('limit', '1')
      .set('countrycodes', 'ar')
      .set('accept-language', 'es');

    return this.rawHttp.get<any[]>(this.baseUrl, { params }).pipe(
      map((res) => {
        if (!res || res.length === 0) return null;
        const first = res[0];
        return {
          lat: Number(first.lat),
          lon: Number(first.lon),
          displayName: first.display_name
        } as GeocodingResult;
      })
    );
  }
}
