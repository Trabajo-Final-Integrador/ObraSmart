import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AsistenteResponse {
  respuesta: string;
}

@Injectable({
  providedIn: 'root'
})
export class AsistenteService {

  private baseUrl = 'http://localhost:8085/api/asistente';

  constructor(private http: HttpClient) {}

  enviarMensaje(mensaje: string): Observable<AsistenteResponse> {
    return this.http.post<AsistenteResponse>(
      `${this.baseUrl}/chat`,
      { mensaje }
    );
  }
}
