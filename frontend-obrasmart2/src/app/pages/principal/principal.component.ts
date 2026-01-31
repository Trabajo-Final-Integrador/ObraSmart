import { Component, AfterViewInit, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { SessionService } from '../../service/session.service';
import { SidebarService } from '../../service/sidebar.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import * as L from 'leaflet';
import { GeolocalizacionService, EquipoUbicacion } from 'src/app/service/geolocalizacion.service';
import { EquipoService, EquipoDTO } from 'src/app/service/equipo.service';
import { environment } from '../../../environments/environment';
import { MediaService } from 'src/app/service/media.service';
import { appendCacheBust, MEDIA_OFFSETS } from '../../shared/utils/media-helper';
import { buildTileLayer, DEFAULT_THEME_ID, MAP_THEMES, MapTheme } from '../../shared/maps/map-themes';
import { MapHelpersService } from '../../shared/maps/map-helpers.service';
import { APP_THEMES, AppTheme, ThemeService } from 'src/app/service/theme.service';
import { LanguageService } from 'src/app/service/language.service';
import { TranslateService } from '@ngx-translate/core';

// Icono pin personalizado
const createCustomIcon = (color: string) =>
  L.divIcon({
    className: 'custom-marker',
    html: `
      <svg width="32" height="44" viewBox="0 0 32 44" xmlns="http://www.w3.org/2000/svg">
        <ellipse cx="16" cy="41" rx="6" ry="2.5" fill="rgba(0,0,0,0.3)"/>
        <path d="M16 0C9.373 0 4 5.373 4 12c0 6.627 12 32 12 32s12-25.373 12-32C28 5.373 22.627 0 16 0z" fill="${color}"/>
        <circle cx="16" cy="12" r="5" fill="white"/>
      </svg>
    `,
    iconSize: [32, 44],
    iconAnchor: [16, 44],
    popupAnchor: [0, -44],
  });

const iconOperativo = createCustomIcon('#28a745');
const iconMantenimiento = createCustomIcon('#ffc107');
const iconFueraServicio = createCustomIcon('#dc3545');

@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.scss'],
})
export class PrincipalComponent implements AfterViewInit, OnInit {
  avatarSrc: string = 'assets/img/user-default.png';
  readonly avatarPlaceholder = 'assets/img/user-default.png';
  userMenuAbierto = false;
  configMenuAbierto = false;
  themeMenuAbierto = false;
  selectedThemeId: AppTheme = 'obra-light';
  selectedLanguage = 'es';
  readonly uiThemeOptions = APP_THEMES;
  readonly themeOptions = MAP_THEMES;
  readonly languageOptions = [
    { id: 'es', labelKey: 'language.spanish' },
    { id: 'en', labelKey: 'language.english' }
  ];

  private map!: L.Map;
  private baseLayers = new Map<string, L.TileLayer>();
  private currentBaseLayer?: L.TileLayer;
  baseThemeId: string = DEFAULT_THEME_ID;
  equipos: EquipoUbicacion[] = [];
  detalleVisible = false;
  selectedEquipoId?: number;
  private apiUrl = `${environment.apiUrl}/auth`;
  notificacionesAbierto = false;
  notificacionesMantenimiento: Array<{
    id?: number;
    nombre: string;
    proximoMantenimiento: string;
    diasRestantes: number;
    visto: boolean;
  }> = [];

  constructor(
    private session: SessionService,
    private auth: AuthService,
    private geoService: GeolocalizacionService,
    private sidebarService: SidebarService,
    private router: Router,
    private http: HttpClient,
    private media: MediaService,
    private themeService: ThemeService,
    private languageService: LanguageService,
    private translate: TranslateService,
    private equipoService: EquipoService,
    private mapHelpers: MapHelpersService
  ) {}

  ngOnInit(): void {
    this.selectedThemeId = this.themeService.getTheme() || 'obra-light';
    this.selectedLanguage = this.languageService.getLang();
    this.baseThemeId = this.loadSavedTheme();
    this.session.user$.subscribe((u) => {
      this.avatarSrc = this.buildAvatarUrl(u?.userId);
    });
    this.cargarNotificacionesMantenimiento();
  }

  ngAfterViewInit(): void {
    this.initMap();
    this.cargarEquiposEnMapa();
  }

  toggleMenu(): void {
    this.sidebarService.toggleSidebar();
  }

  toggleUserMenu(): void {
    // refresca avatar por si hubo subida en modal usuario
    this.avatarSrc = this.buildAvatarUrl(this.session.getUser()?.userId);
    this.userMenuAbierto = !this.userMenuAbierto;
    if (this.userMenuAbierto) this.configMenuAbierto = false;
  }

  cerrarSesion(): void {
    this.http
      .post(`${this.apiUrl}/logout`, {}, { withCredentials: true })
      .subscribe({
        next: () => {
          this.auth.logout();
          this.router.navigate(['/login']);
        },
        error: () => {
          this.auth.logout();
          this.router.navigate(['/login']);
        },
      });
  }

  onAvatarError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.onerror = null as any;
      img.src = this.avatarPlaceholder;
    }
  }

  private buildAvatarUrl(userId?: number): string {
    if (!userId) return this.avatarPlaceholder;
    const mediaId = MEDIA_OFFSETS.USUARIO + userId;
    const base = this.media.getEquipoImageUrl(mediaId);
    return appendCacheBust(base, userId);
  }

  onBaseThemeSelect(themeId: string): void {
    const targetId = this.getThemeById(themeId)?.id || DEFAULT_THEME_ID;
    this.swapBaseLayer(targetId);
    this.saveTheme(targetId);
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [-31.4167, -64.1833],
      zoom: 7,
      zoomControl: true,
    });

    this.initBaseLayers();
    const initialLayer = this.baseLayers.get(this.baseThemeId) || this.baseLayers.get(DEFAULT_THEME_ID);
    if (initialLayer) {
      this.currentBaseLayer = initialLayer;
      this.currentBaseLayer.addTo(this.map);
    }
  }

  private initBaseLayers(): void {
    MAP_THEMES.forEach((theme) => {
      this.baseLayers.set(theme.id, buildTileLayer(theme));
    });
  }

  private swapBaseLayer(themeId: string): void {
    if (!this.map || !this.baseLayers.size || this.baseThemeId === themeId) {
      return;
    }
    const nextLayer = this.baseLayers.get(themeId);
    if (!nextLayer || !this.currentBaseLayer) {
      return;
    }
    this.map.removeLayer(this.currentBaseLayer);
    this.currentBaseLayer = nextLayer;
    this.currentBaseLayer.addTo(this.map);
    this.baseThemeId = themeId;
  }

  private getThemeById(id: string): MapTheme | undefined {
    return MAP_THEMES.find((t) => t.id === id);
  }

  getThemeLabel(themeId: string): string {
    return this.getThemeById(themeId)?.labelKey || `navbar.map.themes.${themeId}`;
  }

  private loadSavedTheme(): string {
    try {
      const stored = localStorage.getItem('obrasmart_map_theme');
      if (stored && this.getThemeById(stored)) return stored;
    } catch {
      // ignore storage errors
    }
    return DEFAULT_THEME_ID;
  }

  private saveTheme(id: string): void {
    try {
      localStorage.setItem('obrasmart_map_theme', id);
    } catch {
      // ignore storage errors
    }
  }

  private cargarEquiposEnMapa(): void {
    this.geoService.obtenerUbicaciones().subscribe({
      next: (data: EquipoUbicacion[]) => {
        this.equipos = data;
        this.equipos.forEach((eq) => {
          const coords = this.mapHelpers.getLatLngFromEquipoUbicacion(eq);
          if (!coords || coords.lat === 0 || coords.lng === 0) {
            console.warn(`Coordenadas invalidas para ${eq.nombre}: ${eq.lat}, ${eq.lon}`);
            return;
          }

          const icon = this.getIconByEstado(eq.estado);
          const estadoTexto = this.getEstadoTexto(eq.estado);

          const marker = L.marker([coords.lat, coords.lng], { icon });
          marker.on('click', () => this.abrirDetalleEquipo(eq.id));

          marker
            .addTo(this.map)
            .bindTooltip(
              `<div><strong>${eq.nombre}</strong><br>${estadoTexto}</div>`,
              { sticky: true, direction: 'top' }
            )
            .bindPopup(
              `
              <div style="text-align: center;">
                <b style="font-size: 14px;">${eq.nombre}</b><br>
                <span style="font-size: 12px; color: ${this.getEstadoColor(eq.estado)};">
                  <i class="bi ${this.getEstadoIcon(eq.estado)}"></i> ${estadoTexto}
                </span>
              </div>
            `
            );
        });
      },
      error: (err) => {
        console.error('Error al cargar ubicaciones:', err);
      },
    });
  }

  private getIconByEstado(estado: string): L.DivIcon {
    const estadoNormalizado = this.normalizeEstado(estado);
    switch (estadoNormalizado) {
      case 'DISPONIBLE':
        return iconOperativo;
      case 'EN_MANTENIMIENTO':
        return iconMantenimiento;
      case 'FUERA_DE_SERVICIO':
      case 'BAJA':
        return iconFueraServicio;
      case 'EN_TRASLADO':
      case 'ASIGNADO':
        return iconOperativo;
      default:
        console.warn('Estado de equipo desconocido (icon):', estado);
        return iconOperativo;
    }
  }

  private getEstadoTexto(estado: string): string {
    const estadoNormalizado = this.normalizeEstado(estado);
    if (estadoNormalizado === 'DISPONIBLE') return 'Disponible';
    if (estadoNormalizado === 'EN_MANTENIMIENTO') return 'En mantenimiento';
    if (estadoNormalizado === 'FUERA_DE_SERVICIO') return 'Fuera de servicio';
    if (estadoNormalizado === 'EN_TRASLADO') return 'En traslado';
    if (estadoNormalizado === 'ASIGNADO') return 'Asignado';
    if (estadoNormalizado === 'BAJA') return 'Baja';
    return `Estado: ${estado || 'Desconocido'}`;
  }

  private getEstadoColor(estado: string): string {
    const estadoNormalizado = this.normalizeEstado(estado);
    if (estadoNormalizado === 'DISPONIBLE') return '#28a745';
    if (estadoNormalizado === 'EN_MANTENIMIENTO') return '#ffc107';
    if (estadoNormalizado === 'FUERA_DE_SERVICIO') return '#dc3545';
    if (estadoNormalizado === 'EN_TRASLADO' || estadoNormalizado === 'ASIGNADO') return '#0ea5e9';
    if (estadoNormalizado === 'BAJA') return '#6c757d';
    console.warn('Estado de equipo desconocido (color):', estado);
    return '#6c757d';
  }

  private getEstadoIcon(estado: string): string {
    const estadoNormalizado = this.normalizeEstado(estado);
    if (estadoNormalizado === 'DISPONIBLE') return 'bi-check-circle-fill';
    if (estadoNormalizado === 'EN_MANTENIMIENTO') return 'bi-tools';
    if (estadoNormalizado === 'FUERA_DE_SERVICIO' || estadoNormalizado === 'BAJA') return 'bi-x-circle-fill';
    if (estadoNormalizado === 'EN_TRASLADO' || estadoNormalizado === 'ASIGNADO') return 'bi-truck';
    return 'bi-question-circle';
  }

  private normalizeEstado(
    raw: string | null | undefined
  ):
    | 'DISPONIBLE'
    | 'EN_MANTENIMIENTO'
    | 'FUERA_DE_SERVICIO'
    | 'EN_TRASLADO'
    | 'ASIGNADO'
    | 'BAJA'
    | 'DESCONOCIDO' {
    const valor = (raw || '').toString().trim();
    if (!valor) return 'DESCONOCIDO';
    const upper = valor.toUpperCase().replace(/\s+/g, '_');
    if (upper.includes('DISP')) return 'DISPONIBLE';
    if (upper.includes('MANT')) return 'EN_MANTENIMIENTO';
    if (upper.includes('FUERA') || upper.includes('SERVICIO')) return 'FUERA_DE_SERVICIO';
    if (upper.includes('TRASL') || upper.includes('TRANSITO')) return 'EN_TRASLADO';
    if (upper.includes('ASIGN')) return 'ASIGNADO';
    if (upper.includes('BAJA')) return 'BAJA';
    return 'DESCONOCIDO';
  }

  abrirDetalleEquipo(id?: number): void {
    if (!id) return;
    this.selectedEquipoId = id;
    this.detalleVisible = true;
  }

  cerrarDetalleEquipo(): void {
    this.detalleVisible = false;
    this.selectedEquipoId = undefined;
  }

  toggleThemeMenu(): void {
    this.themeMenuAbierto = !this.themeMenuAbierto;
    if (this.themeMenuAbierto) this.configMenuAbierto = false;
  }

  closeThemeMenu(): void {
    this.themeMenuAbierto = false;
  }

  toggleConfigMenu(): void {
    this.configMenuAbierto = !this.configMenuAbierto;
    if (this.configMenuAbierto) this.userMenuAbierto = false;
  }

  toggleNotificaciones(): void {
    this.notificacionesAbierto = !this.notificacionesAbierto;
    if (this.notificacionesAbierto) {
      this.configMenuAbierto = false;
      this.userMenuAbierto = false;
    }
  }

  seleccionarIdioma(id: string): void {
    this.selectedLanguage = id;
    this.languageService.setLang(id as 'es' | 'en');
    this.configMenuAbierto = false;
  }

  seleccionarTheme(id: string): void {
    console.log('[Principal] seleccionarTheme click:', id);
    this.selectedThemeId = id as AppTheme;
    this.themeService.apply(this.selectedThemeId);
    this.configMenuAbierto = false;
  }

  get notificacionesPendientes(): number {
    return this.notificacionesMantenimiento.filter((n) => !n.visto).length;
  }

  onNotificacionClick(notificacion: { id?: number }): void {
    this.notificacionesMantenimiento = this.notificacionesMantenimiento.map((n) =>
      n.id === notificacion.id ? { ...n, visto: true } : n
    );
    this.notificacionesAbierto = false;
    this.router.navigate(['/reparaciones/crear'], {
      queryParams: notificacion.id ? { equipoId: notificacion.id } : {},
    });
  }

  private cargarNotificacionesMantenimiento(): void {
    this.equipoService.listar().subscribe({
      next: (equipos: EquipoDTO[]) => {
        const ahora = new Date();
        const msDia = 1000 * 60 * 60 * 24;
        this.notificacionesMantenimiento = (equipos || [])
          .filter((eq) => !!eq.proximoMantenimiento)
          .map((eq) => {
            const fecha = new Date(eq.proximoMantenimiento);
            const diffDias = Math.ceil((fecha.getTime() - ahora.getTime()) / msDia);
            return {
              id: eq.id,
              nombre: eq.nombre || eq.codigoInterno || 'Equipo',
              proximoMantenimiento: fecha.toISOString(),
              diasRestantes: diffDias,
              visto: false,
            };
          })
          .filter((n) => n.diasRestantes >= 0 && n.diasRestantes <= 5)
          .sort((a, b) => a.diasRestantes - b.diasRestantes);
      },
      error: (err) => console.error('Error cargando notificaciones de mantenimiento', err),
    });
  }
}
