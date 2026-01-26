import { AfterViewInit, Component, OnDestroy } from '@angular/core';
import * as L from 'leaflet';
import 'leaflet.markercluster';
import { interval, Subscription } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { ObradorDto, ObradorService } from '../../service/obrador.service';
import { LogisticaService, TrasladoDto } from '../../service/logistica.service';

type MarkerCluster = any;
type OverlayKey = 'logistica' | 'zonas' | 'rutas' | 'obradores' | 'perimetrosObradores';

@Component({
  selector: 'app-mapa-operaciones',
  templateUrl: './mapa-operaciones.component.html',
  styleUrls: ['./mapa-operaciones.component.scss'],
})
export class MapaOperacionesComponent implements AfterViewInit, OnDestroy {
  private readonly OBRADOR_RADIUS_METERS = 300;
  private __debugCircleDone = false;
  private map!: L.Map;
  private layerControl!: L.Control.Layers;
  private baseLayers!: Record<string, L.TileLayer>;
  private obradoresCluster: MarkerCluster = (L as any).markerClusterGroup({ disableClusteringAtZoom: 16 });
  private perimetrosObradores: L.LayerGroup = L.layerGroup();
  private overlays: Record<OverlayKey, L.LayerGroup>;

  filtros = {
    obradores: true,
    logistica: true,
    estado: 'todos',
    fecha: 'hoy',
  };

  selectedObrador?: ObradorDto;
  detalleAbierto = false;
  private refreshSub?: Subscription;

  constructor(private obradorService: ObradorService, private logisticaService: LogisticaService) {
    this.overlays = {
      logistica: L.layerGroup(),
      zonas: L.layerGroup(),
      rutas: L.layerGroup(),
      obradores: this.obradoresCluster as unknown as L.LayerGroup,
      perimetrosObradores: this.perimetrosObradores,
    };
  }

  ngAfterViewInit(): void {
    this.initMap();
    this.loadObradores();
    this.loadLogistica();
    this.startLogisticaRefresh();
  }

  ngOnDestroy(): void {
    this.refreshSub?.unsubscribe();
  }

  toggleOverlay(key: 'obradores' | 'logistica', active: boolean): void {
    this.filtros[key] = active;
    const layer = this.overlays[key];
    if (!this.map) return;
    if (active) {
      layer.addTo(this.map);
    } else {
      this.map.removeLayer(layer);
    }
  }

  onEstadoChange(value: string): void {
    this.filtros.estado = value;
    this.loadLogistica();
  }

  onFechaChange(value: string): void {
    this.filtros.fecha = value;
    // Hook para usar el filtro de fecha cuando el backend lo soporte
  }

  private initMap(): void {
    this.map = L.map('map-operaciones', {
      center: [-31.4167, -64.1833],
      zoom: 6,
      zoomControl: true,
      worldCopyJump: true,
    });

    this.baseLayers = {
      Satellite: L.tileLayer(
        'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
        {
          attribution: 'Tiles © Esri — Source: Esri, Maxar, Earthstar Geographics',
          maxZoom: 19,
        }
      ),
      Calle: L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
        attribution:
          '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
        maxZoom: 19,
      }),
    };

    this.baseLayers['Satellite'].addTo(this.map);

    // Añadir overlays al mapa inicial
    this.obradoresCluster.addTo(this.map);
    this.perimetrosObradores.addTo(this.map);
    this.overlays.logistica.addTo(this.map);
    this.overlays.zonas.addTo(this.map);
    this.overlays.rutas.addTo(this.map);

    const overlayMaps: Record<string, L.Layer> = {
      Obradores: this.overlays.obradores,
      'Perímetro Obradores': this.overlays.perimetrosObradores,
      Logistica: this.overlays.logistica,
      Zonas: this.overlays.zonas,
      Rutas: this.overlays.rutas,
    };

    this.layerControl = L.control.layers(this.baseLayers, overlayMaps).addTo(this.map);
  }

  private loadObradores(): void {
    this.obradorService.listar().subscribe({
      next: (obradores) => this.renderObradores(obradores),
      error: (err) => console.error('Error al cargar obradores', err),
    });
  }

  private renderObradores(obradores: ObradorDto[]): void {
    this.obradoresCluster.clearLayers();
    this.perimetrosObradores.clearLayers();

    obradores.forEach((obrador) => {
      const lat = (obrador as any).lat ?? (obrador as any).latitud;
      const lng = (obrador as any).lng ?? (obrador as any).longitud;
      if (typeof lat !== 'number' || typeof lng !== 'number' || !isFinite(lat) || !isFinite(lng)) {
        console.warn('[obradores] sin coords', obrador);
        return;
      }

      const marker = L.marker([lat, lng], {
        icon: this.iconObrador(),
        title: obrador.nombre,
      });

      marker.bindPopup(
        `
        <div class="popup-obrador">
          <div class="popup-title">${obrador.nombre}</div>
          <div class="popup-body">
            <div>Supervisor: ${obrador.supervisorUserId ?? 'N/D'}</div>
            <button class="btn-link" data-obrador-id="${obrador.id}">Ver detalle</button>
          </div>
        </div>
      `
      );

      marker.on('popupopen', () => {
        const popupEl = document.querySelector(`button[data-obrador-id="${obrador.id}"]`);
        popupEl?.addEventListener('click', () => this.abrirDetalleObrador(obrador.id));
      });

      this.obradoresCluster.addLayer(marker);

      if (Array.isArray((obrador as any).zona) && (obrador as any).zona.length >= 3) {
        const poly = L.polygon((obrador as any).zona, {
          color: '#4caf50',
          weight: 3,
          fillColor: '#4caf50',
          fillOpacity: 0.35,
        });
        this.perimetrosObradores.addLayer(poly);
      } else {
        const square = this.buildSquare(lat, lng, this.OBRADOR_RADIUS_METERS);
        const poly = L.polygon(square, {
          color: '#4caf50',
          weight: 3,
          fillColor: '#4caf50',
          fillOpacity: 0.35,
        });
        this.perimetrosObradores.addLayer(poly);
      }
    });

    // Traer al frente los perímetros y loguear cuántos se dibujaron
    (this.perimetrosObradores as any).bringToFront?.();
    const drawn = (this.perimetrosObradores as any).getLayers?.()?.length ?? 'n/a';
    const markers = (this.obradoresCluster as any).getLayers?.()?.length ?? 'n/a';
    console.log('[obradores] dibujados -> markers:', markers, 'perímetros:', drawn);
  }

  private buildSquare(lat: number, lng: number, meters: number): [number, number][] {
    const dLat = meters / 111320;
    const dLng = meters / (111320 * Math.cos((lat * Math.PI) / 180));
    return [
      [lat + dLat, lng - dLng],
      [lat + dLat, lng + dLng],
      [lat - dLat, lng + dLng],
      [lat - dLat, lng - dLng],
    ];
  }

  private loadLogistica(): void {
    this.logisticaService.listar().subscribe({
      next: (traslados) => this.renderLogistica(traslados),
      error: (err) => console.error('Error al cargar traslados', err),
    });
  }

  private renderLogistica(traslados: TrasladoDto[]): void {
    this.overlays.logistica.clearLayers();
    this.overlays.rutas.clearLayers();

    traslados
      .filter((t) => this.filtros.estado === 'todos' || t.estado?.toLowerCase() === this.filtros.estado)
      .forEach((t) => {
        const origen = this.coordOrUndefined(t.origenLat, t.origenLng);
        const destino = this.coordOrUndefined(t.destinoLat, t.destinoLng);
        const actual = t.posicionActual ? this.coordOrUndefined(t.posicionActual.lat, t.posicionActual.lng) : undefined;

        // marcador principal: posicion actual > destino > origen
        const markerCoord = actual || destino || origen;
        if (markerCoord) {
          const marker = L.marker(markerCoord, {
            icon: this.iconCamion(t.estado),
            title: `Traslado #${t.id}`,
          });
          marker.bindPopup(
            `<div class="popup-logistica">
              <div class="popup-title">Traslado #${t.id}</div>
              <div>Equipo: ${t.equipoId}</div>
              <div>Estado: ${t.estado}</div>
              <div>Origen: ${t.origenObradorId} → Destino: ${t.destinoObradorId}</div>
            </div>`
          );
          this.overlays.logistica.addLayer(marker);
        } else {
          console.warn(`Traslado ${t.id} sin coordenadas para mostrar`);
        }

        const rutaCoordenadas = this.rutaAUsar(t, origen, destino);
        if (rutaCoordenadas && rutaCoordenadas.length > 1) {
          const polyline = L.polyline(rutaCoordenadas, {
            color: '#ff7043',
            weight: 4,
            opacity: 0.85,
            dashArray: '8 6',
          });
          this.overlays.rutas.addLayer(polyline);
        }
      });
  }

  private coordOrUndefined(lat?: number, lng?: number): L.LatLngExpression | undefined {
    if (typeof lat === 'number' && typeof lng === 'number') return [lat, lng];
    return undefined;
  }

  private rutaAUsar(
    traslado: TrasladoDto,
    origen?: L.LatLngExpression,
    destino?: L.LatLngExpression
  ): L.LatLngExpression[] | undefined {
    if (traslado.ruta && traslado.ruta.length > 1) {
      return traslado.ruta.map((p) => [p.lat, p.lng]);
    }
    if (origen && destino) {
      return [origen, destino];
    }
    return undefined;
  }

  private startLogisticaRefresh(): void {
    this.refreshSub = interval(15000)
      .pipe(startWith(0), switchMap(() => this.logisticaService.listar()))
      .subscribe({
        next: (traslados) => this.renderLogistica(traslados),
        error: (err) => console.error('Error refrescando logística', err),
      });
  }

  abrirDetalleObrador(id: number): void {
    this.obradorService.obtener(id).subscribe({
      next: (detalle) => {
        this.selectedObrador = detalle;
        this.detalleAbierto = true;
      },
      error: (err) => console.error('Error al obtener detalle de obrador', err),
    });
  }

  cerrarDetalle(): void {
    this.detalleAbierto = false;
    this.selectedObrador = undefined;
  }

  private iconObrador(): L.DivIcon {
    const html = `
      <div class="pin pin-obrador">
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M4 15v-3a4 4 0 0 1 8 0v3"></path>
          <path d="M12 15v-3a4 4 0 0 1 8 0v3"></path>
          <path d="M4 15h16"></path>
          <path d="M6 19h12"></path>
          <path d="M9 11h6"></path>
        </svg>
      </div>
    `;
    return L.divIcon({
      html,
      className: 'marker marker-obrador',
      iconSize: [32, 36],
      iconAnchor: [16, 36],
      popupAnchor: [0, -32],
    });
  }

  private iconCamion(estado?: string): L.DivIcon {
    const color = estado?.toLowerCase() === 'completado' ? '#2ecc71' : '#0ea5e9';
    const html = `
      <div class="pin pin-camion" style="background:${color}">
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <rect x="1" y="5" width="12" height="13" rx="2" ry="2"></rect>
          <path d="M13 8h5l3 3v4h-8z"></path>
          <circle cx="5.5" cy="18.5" r="1.5"></circle>
          <circle cx="17.5" cy="18.5" r="1.5"></circle>
        </svg>
      </div>
    `;
    return L.divIcon({
      html,
      className: 'marker marker-camion',
      iconSize: [34, 38],
      iconAnchor: [17, 38],
      popupAnchor: [0, -34],
    });
  }
}




