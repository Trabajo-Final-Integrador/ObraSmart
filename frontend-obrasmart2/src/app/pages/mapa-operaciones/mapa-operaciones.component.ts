import { AfterViewInit, Component, OnDestroy } from '@angular/core';
import * as L from 'leaflet';
import 'leaflet.markercluster';
import { interval, Subscription } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { DistanciaCalculo, DistanciaService } from '../../service/distancia.service';
import { EquipoDTO, EquipoService } from '../../service/equipo.service';
import { ObradorDto, ObradorService } from '../../service/obrador.service';
import { LogisticaService, TrasladoDto } from '../../service/logistica.service';
import { buildTileLayer, DEFAULT_THEME_ID, MAP_THEMES } from '../../shared/maps/map-themes';
import { MapHelpersService } from '../../shared/maps/map-helpers.service';

type MarkerCluster = any;
type OverlayKey = 'logistica' | 'zonas' | 'rutas' | 'obradores' | 'perimetrosObradores' | 'distancia';

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
  private distanciaLayer: L.LayerGroup = L.layerGroup();
  private lineaDistancia?: L.Polyline;
  private equipoMarker?: L.Marker;
  private obradorMarker?: L.Marker;
  private overlays: Record<OverlayKey, L.LayerGroup>;

  filtros = {
    obradores: true,
    logistica: true,
    estado: 'todos',
    fecha: 'hoy',
  };

  equipos: EquipoDTO[] = [];
  obradores: ObradorDto[] = [];
  equipoSeleccionado?: number;
  obradorSeleccionado?: number;
  distanciaInfo?: DistanciaCalculo;
  cargandoDistancia = false;
  errorDistancia?: string;

  selectedObrador?: ObradorDto;
  detalleAbierto = false;
  private refreshSub?: Subscription;

  constructor(
    private obradorService: ObradorService,
    private logisticaService: LogisticaService,
    private equipoService: EquipoService,
    private distanciaService: DistanciaService,
    private mapHelpers: MapHelpersService
  ) {
    this.overlays = {
      logistica: L.layerGroup(),
      zonas: L.layerGroup(),
      rutas: L.layerGroup(),
      obradores: this.obradoresCluster as unknown as L.LayerGroup,
      perimetrosObradores: this.perimetrosObradores,
      distancia: this.distanciaLayer,
    };
  }

  ngAfterViewInit(): void {
    this.initMap();
    this.loadObradores();
    this.loadEquipos();
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

    this.initBaseLayers();
    const initial = this.baseLayers[DEFAULT_THEME_ID] || Object.values(this.baseLayers)[0];
    if (initial) {
      initial.addTo(this.map);
    }

    // Añadir overlays al mapa inicial
    this.obradoresCluster.addTo(this.map);
    this.perimetrosObradores.addTo(this.map);
    this.distanciaLayer.addTo(this.map);
    this.overlays.logistica.addTo(this.map);
    this.overlays.zonas.addTo(this.map);
    this.overlays.rutas.addTo(this.map);

    const overlayMaps: Record<string, L.Layer> = {
      Obradores: this.overlays.obradores,
      'Perímetro Obradores': this.overlays.perimetrosObradores,
      Distancia: this.overlays.distancia,
      Logistica: this.overlays.logistica,
      Zonas: this.overlays.zonas,
      Rutas: this.overlays.rutas,
    };

    this.layerControl = L.control.layers(this.baseLayers, overlayMaps).addTo(this.map);
  }

  private initBaseLayers(): void {
    this.baseLayers = {};
    MAP_THEMES.forEach((theme) => {
      this.baseLayers[theme.id] = buildTileLayer(theme);
    });
  }

  private loadObradores(): void {
    this.obradorService.listar().subscribe({
      next: (obradores) => {
        this.obradores = obradores;
        this.renderObradores(obradores);
      },
      error: (err) => console.error('Error al cargar obradores', err),
    });
  }

  private loadEquipos(): void {
    this.equipoService.listar().subscribe({
      next: (equipos) =>
        (this.equipos = (equipos || []).filter((e) => !!this.mapHelpers.getLatLngFromEquipo(e))),
      error: (err) => console.error('Error al cargar equipos', err),
    });
  }

  calcularDistancia(): void {
    if (!this.equipoSeleccionado || !this.obradorSeleccionado) return;
    this.cargandoDistancia = true;
    this.errorDistancia = undefined;

    this.distanciaService
      .calcularDistancia(this.equipoSeleccionado, this.obradorSeleccionado)
      .subscribe({
        next: (distancia) => {
          this.distanciaInfo = distancia;
          this.dibujarLineaDistancia();
          this.ajustarVistaMapaDistancia();
          this.cargandoDistancia = false;
        },
        error: (err) => {
          console.error('Error al calcular distancia:', err);
          this.errorDistancia = err.error?.message || 'Error al calcular distancia';
          this.cargandoDistancia = false;
        },
      });
  }

  onEquipoChange(): void {
    this.limpiarDistancia();
    const equipo = this.equipos.find((e) => e.id === this.equipoSeleccionado);
    if (!equipo) return;
    const coords = this.mapHelpers.getLatLngFromEquipo(equipo);
    if (!coords) return;

    if (this.equipoMarker) {
      this.distanciaLayer.removeLayer(this.equipoMarker);
    }
    this.equipoMarker = L.marker([coords.lat, coords.lng], { icon: this.mapHelpers.createEquipoIcon() }).addTo(
      this.distanciaLayer
    );
    this.equipoMarker.bindPopup(`<b>Equipo:</b> ${equipo.nombre || equipo.codigoInterno || equipo.id}`);
    this.map.setView([coords.lat, coords.lng], 13);

    if (this.obradorSeleccionado) {
      this.enfocarAmbos();
    }
  }

  onObradorChange(): void {
    this.limpiarDistancia();
    const coords = this.getObradorCoords(this.obradorSeleccionado);
    if (!coords) return;

    if (this.obradorMarker) {
      this.distanciaLayer.removeLayer(this.obradorMarker);
    }
    this.obradorMarker = L.marker([coords.lat, coords.lng], { icon: this.mapHelpers.createObradorIcon(true) }).addTo(
      this.distanciaLayer
    );
    const obrador = this.obradores.find((o) => o.id === this.obradorSeleccionado);
    this.obradorMarker.bindPopup(`<b>Obrador:</b> ${obrador?.nombre || this.obradorSeleccionado}`);
    this.map.setView([coords.lat, coords.lng], 13);

    if (this.equipoSeleccionado) {
      this.enfocarAmbos();
    }
  }

  private enfocarAmbos(): void {
    const equipo = this.equipos.find((e) => e.id === this.equipoSeleccionado);
    const obradorCoords = this.getObradorCoords(this.obradorSeleccionado);
    if (!equipo || !obradorCoords) return;
    const bounds = L.latLngBounds([
      [equipo.latitud, equipo.longitud],
      [obradorCoords.lat, obradorCoords.lng],
    ]);
    this.map.fitBounds(bounds, { padding: [50, 50] });
  }

  private limpiarDistancia(): void {
    this.distanciaInfo = undefined;
    this.errorDistancia = undefined;
    if (this.lineaDistancia) {
      this.distanciaLayer.removeLayer(this.lineaDistancia);
      this.lineaDistancia = undefined;
    }
  }

  private dibujarLineaDistancia(): void {
    if (this.lineaDistancia) {
      this.distanciaLayer.removeLayer(this.lineaDistancia);
    }
    if (this.equipoMarker) {
      this.distanciaLayer.removeLayer(this.equipoMarker);
    }
    if (this.obradorMarker) {
      this.distanciaLayer.removeLayer(this.obradorMarker);
    }
    if (!this.distanciaInfo) return;

    this.equipoMarker = L.marker(
      [this.distanciaInfo.equipoLatitud, this.distanciaInfo.equipoLongitud],
      { icon: this.mapHelpers.createEquipoIcon() }
    ).addTo(this.distanciaLayer);
    this.equipoMarker.bindPopup(`<b>Equipo:</b> ${this.distanciaInfo.equipoNombre}`);

    this.obradorMarker = L.marker(
      [this.distanciaInfo.obradorLatitud, this.distanciaInfo.obradorLongitud],
      { icon: this.mapHelpers.createObradorIcon(true) }
    ).addTo(this.distanciaLayer);
    this.obradorMarker.bindPopup(`<b>Obrador:</b> ${this.distanciaInfo.obradorNombre}`);

    const latlngs: L.LatLngExpression[] = [
      [this.distanciaInfo.equipoLatitud, this.distanciaInfo.equipoLongitud],
      [this.distanciaInfo.obradorLatitud, this.distanciaInfo.obradorLongitud],
    ];

    this.lineaDistancia = L.polyline(latlngs, {
      color: '#7c3aed',
      weight: 4,
      opacity: 0.85,
      dashArray: '8 6',
    }).addTo(this.distanciaLayer);

    const midLat = (this.distanciaInfo.equipoLatitud + this.distanciaInfo.obradorLatitud) / 2;
    const midLng = (this.distanciaInfo.equipoLongitud + this.distanciaInfo.obradorLongitud) / 2;

    L.popup()
      .setLatLng([midLat, midLng])
      .setContent(`<b>Distancia: ${this.distanciaInfo.distanciaKm} km</b>`)
      .openOn(this.map);
  }

  private ajustarVistaMapaDistancia(): void {
    if (!this.distanciaInfo) return;
    const bounds = L.latLngBounds([
      [this.distanciaInfo.equipoLatitud, this.distanciaInfo.equipoLongitud],
      [this.distanciaInfo.obradorLatitud, this.distanciaInfo.obradorLongitud],
    ]);
    this.map.fitBounds(bounds, { padding: [50, 50] });
  }

  private renderObradores(obradores: ObradorDto[]): void {
    this.obradoresCluster.clearLayers();
    this.perimetrosObradores.clearLayers();

    obradores.forEach((obrador) => {
      const coords = this.mapHelpers.getLatLngFromObrador(obrador);
      if (!coords) {
        console.warn('[obradores] sin coords', obrador);
        return;
      }

      const marker = L.marker([coords.lat, coords.lng], {
        icon: this.mapHelpers.createObradorIcon(),
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
        const circle = this.mapHelpers.createObradorCircle(
          coords.lat,
          coords.lng,
          this.OBRADOR_RADIUS_METERS
        );
        this.perimetrosObradores.addLayer(circle);
      }
    });

    // Traer al frente los perímetros y loguear cuántos se dibujaron
    (this.perimetrosObradores as any).bringToFront?.();
    const drawn = (this.perimetrosObradores as any).getLayers?.()?.length ?? 'n/a';
    const markers = (this.obradoresCluster as any).getLayers?.()?.length ?? 'n/a';
    console.log('[obradores] dibujados -> markers:', markers, 'perímetros:', drawn);
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

  private getObradorCoords(id?: number): { lat: number; lng: number } | undefined {
    if (!id) return undefined;
    const obrador = this.obradores.find((o) => o.id === id);
    if (!obrador) return undefined;
    return this.mapHelpers.getLatLngFromObrador(obrador);
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




