import { Injectable } from '@angular/core';
import * as L from 'leaflet';
import { EquipoDTO } from '../../service/equipo.service';
import { ObradorDto } from '../../service/obrador.service';

@Injectable({ providedIn: 'root' })
export class MapHelpersService {
  toNumber(value: unknown): number | undefined {
    if (typeof value === 'number' && isFinite(value)) return value;
    if (typeof value === 'string' && value.trim() !== '' && isFinite(Number(value))) return Number(value);
    return undefined;
  }

  isValidLatLng(lat?: number, lng?: number): boolean {
    return typeof lat === 'number' && typeof lng === 'number' && isFinite(lat) && isFinite(lng);
  }

  getLatLngFromEquipo(equipo: EquipoDTO): { lat: number; lng: number } | undefined {
    const lat = this.toNumber(equipo.latitud);
    const lng = this.toNumber(equipo.longitud);
    if (!this.isValidLatLng(lat, lng)) return undefined;
    return { lat: lat as number, lng: lng as number };
  }

  getLatLngFromObrador(obrador: ObradorDto): { lat: number; lng: number } | undefined {
    const lat = this.toNumber((obrador as any).lat ?? (obrador as any).latitud);
    const lng = this.toNumber((obrador as any).lng ?? (obrador as any).longitud);
    if (!this.isValidLatLng(lat, lng)) return undefined;
    return { lat: lat as number, lng: lng as number };
  }

  getLatLngFromEquipoUbicacion(equipo: { lat?: unknown; lon?: unknown }): { lat: number; lng: number } | undefined {
    const lat = this.toNumber(equipo.lat);
    const lng = this.toNumber(equipo.lon);
    if (!this.isValidLatLng(lat, lng)) return undefined;
    return { lat: lat as number, lng: lng as number };
  }

  createObradorIcon(selected = false): L.DivIcon {
    const html = `
      <div class="pin pin-obrador ${selected ? 'pin-selected' : ''}">
        <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M3 10.5L12 3l9 7.5"></path>
          <path d="M5 10v10h14V10"></path>
          <path d="M9 20v-6h6v6"></path>
        </svg>
      </div>
    `;
    return L.divIcon({
      html,
      className: 'marker marker-obrador',
      iconSize: [selected ? 34 : 32, selected ? 38 : 36],
      iconAnchor: [selected ? 17 : 16, selected ? 38 : 36],
      popupAnchor: [0, selected ? -34 : -32],
    });
  }

  createEquipoIcon(): L.DivIcon {
    const html = `
      <div class="pin pin-equipo">
        <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <rect x="3" y="7" width="14" height="10" rx="2"></rect>
          <path d="M17 10h4l1.5 2.5V17h-5"></path>
          <circle cx="7.5" cy="18.5" r="1.5"></circle>
          <circle cx="17.5" cy="18.5" r="1.5"></circle>
        </svg>
      </div>
    `;
    return L.divIcon({
      html,
      className: 'marker marker-equipo',
      iconSize: [34, 38],
      iconAnchor: [17, 38],
      popupAnchor: [0, -34],
    });
  }

  createObradorCircle(lat: number, lng: number, radius: number): L.Circle {
    return L.circle([lat, lng], {
      radius,
      color: '#4caf50',
      weight: 3,
      fillColor: '#4caf50',
      fillOpacity: 0.25,
    });
  }
}
