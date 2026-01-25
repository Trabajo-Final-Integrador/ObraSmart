import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MediaService {
  // Usar prefijo /api para pasar por el proxy/gateway
  private baseUrl = '/api/media';

  constructor(private http: HttpClient) {}

  getEquipoImageUrl(equipoId: string | number): string {
    return `${this.baseUrl}/equipos/${encodeURIComponent(String(equipoId))}/image`;
  }

  uploadEquipoImage(equipoId: string | number, file: File) {
    const form = new FormData();
    form.append('file', file);
    return this.http.post(`${this.baseUrl}/equipos/${encodeURIComponent(String(equipoId))}/image`, form);
  }

  setEquipoImageLink(equipoId: string | number, url: string) {
    return this.http.post(`${this.baseUrl}/equipos/${encodeURIComponent(String(equipoId))}/image-link`, { url });
  }

  //FIX: identity-service users media reuse same media-service endpoints pattern
  getUserImageUrl(userId: string | number, slot: string = 'profile'): string {
    return `${this.baseUrl}/users/${encodeURIComponent(String(userId))}/${encodeURIComponent(slot)}`;
  }

  uploadUserImage(userId: string | number, slot: string, file: File) {
    const form = new FormData();
    form.append('file', file);
    return this.http.post(`${this.baseUrl}/users/${encodeURIComponent(String(userId))}/${encodeURIComponent(slot)}`, form);
  }

  // Stub para compatibilidad con llamadas existentes
  notifyPhotoUpdated(_evt: any): void {
    return;
  }
}
