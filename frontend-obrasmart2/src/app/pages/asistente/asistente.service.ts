import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AsistenteResponse {
  respuesta: string;
}

@Injectable({
  providedIn: 'root'
})
export class AsistenteService {

  private baseUrl = `${environment.apiUrl}/asistente`;

  constructor(private http: HttpClient) {}

  enviarMensaje(mensaje: string): Observable<AsistenteResponse> {
    return this.http.post<AsistenteResponse>(
      `${this.baseUrl}/chat`,
      { mensaje }
    );
  }
}
