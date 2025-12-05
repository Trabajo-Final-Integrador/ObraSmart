import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { SessionService } from '../../service/session.service';
import * as L from 'leaflet'; 
import { GeolocalizacionService, EquipoUbicacion } from 'src/app/service/geolocalizacion.service';

// Iconos personalizados tipo pin de Google Maps
const createCustomIcon = (color: string) => {
  return L.divIcon({
    className: 'custom-marker',
    html: `
      <svg width="32" height="44" viewBox="0 0 32 44" xmlns="http://www.w3.org/2000/svg">
        <!-- Sombra -->
        <ellipse cx="16" cy="41" rx="6" ry="2.5" fill="rgba(0,0,0,0.3)"/>
        <!-- Pin -->
        <path d="M16 0C9.373 0 4 5.373 4 12c0 6.627 12 32 12 32s12-25.373 12-32C28 5.373 22.627 0 16 0z"
              fill="${color}"/>
        <!-- Círculo interior blanco -->
        <circle cx="16" cy="12" r="5" fill="white"/>
      </svg>
    `,
    iconSize: [32, 44],
    iconAnchor: [16, 44],
    popupAnchor: [0, -44]
  });
};

const iconOperativo = createCustomIcon('#28a745'); // Verde
const iconMantenimiento = createCustomIcon('#ffc107'); // Amarillo
const iconFueraServicio = createCustomIcon('#dc3545'); // Rojo
const iconDesconocido = createCustomIcon('#6c757d'); // Gris


@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.scss']
})
export class PrincipalComponent {
  menuAbierto = false;
  user = this.session.getUser();
   submenuStockOpen = false;
   submenuReparacionOpen = false;

  private map!: L.Map;
  equipos: EquipoUbicacion[] = [];



  constructor(private session: SessionService, private auth: AuthService,  private geoService: GeolocalizacionService  ) {}

  toggleMenu() {
    this.menuAbierto = !this.menuAbierto;
  }

  logout() {
    this.auth.logout();
    window.location.href = '/auth/login';
  }

  submenuUsuariosOpen = false;

toggleUsuarios(event: Event) {
  event.preventDefault(); // evita que el enlace recargue la página
  this.submenuUsuariosOpen = !this.submenuUsuariosOpen;
}

toggleStock(event: Event) {
    event.preventDefault();
    this.submenuStockOpen = !this.submenuStockOpen;
  }


   ngAfterViewInit(): void {
    this.initMap();
    this.cargarEquiposEnMapa();
  }

  toggleReparacion(event: Event) {
    event.preventDefault();
    this.submenuReparacionOpen = !this.submenuReparacionOpen;
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [-31.4167, -64.1833], // Córdoba Capital
      zoom: 7,
      zoomControl: true
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
      maxZoom: 19
    }).addTo(this.map);
  }
private cargarEquiposEnMapa(): void {
  this.geoService.obtenerUbicaciones().subscribe({
    next: (data: EquipoUbicacion[]) => {
      this.equipos = data;
      this.equipos.forEach(eq => {
        // Evita puntos inválidos
        if (eq.lat !== 0 && eq.lon !== 0 && eq.lat && eq.lon) {
          const icon = this.getIconByEstado(eq.estado);
          const estadoTexto = this.getEstadoTexto(eq.estado);

          L.marker([eq.lat, eq.lon], { icon })
            .addTo(this.map)
            .bindPopup(`
              <div style="text-align: center;">
                <b style="font-size: 14px;">${eq.nombre}</b><br>
                <span style="font-size: 12px; color: ${this.getEstadoColor(eq.estado)};">
                  <i class="bi ${this.getEstadoIcon(eq.estado)}"></i> ${estadoTexto}
                </span>
              </div>
            `);
        } else {
          console.warn(`⚠️ Coordenadas inválidas para ${eq.nombre}: ${eq.lat}, ${eq.lon}`);
        }
      });
    },
    error: (err) => {
      console.error('❌ Error al cargar ubicaciones:', err);
    }
  });
}

private getIconByEstado(estado: string): L.DivIcon {
  const estadoUpper = (estado || '').toUpperCase().trim();

  // Normalizar el estado (quitar guiones bajos y espacios)
  const estadoNormalizado = estadoUpper.replace(/_/g, ' ').replace(/\s+/g, ' ');

  if (estadoNormalizado.includes('OPERATIVO')) {
    return iconOperativo; // Verde
  } else if (estadoNormalizado.includes('MANTENIMIENTO')) {
    return iconMantenimiento; // Amarillo
  } else if (estadoNormalizado.includes('FUERA') || estadoNormalizado.includes('REPARACION')) {
    return iconFueraServicio; // Rojo
  } else {
    // Por defecto: verde para equipos sin estado definido
    return iconOperativo;
  }
}

private getEstadoTexto(estado: string): string {
  const estadoUpper = estado?.toUpperCase() || '';

  switch (estadoUpper) {
    case 'OPERATIVO':
      return 'Operativo';
    case 'MANTENIMIENTO':
    case 'EN_MANTENIMIENTO':
      return 'En Mantenimiento';
    case 'FUERA_SERVICIO':
    case 'FUERA DE SERVICIO':
      return 'Fuera de Servicio';
    case 'EN_REPARACION':
      return 'En Reparación';
    default:
      return 'Estado Desconocido';
  }
}

private getEstadoColor(estado: string): string {
  const estadoUpper = estado?.toUpperCase() || '';

  switch (estadoUpper) {
    case 'OPERATIVO':
      return '#28a745'; // Verde
    case 'MANTENIMIENTO':
    case 'EN_MANTENIMIENTO':
      return '#ffc107'; // Amarillo
    case 'FUERA_SERVICIO':
    case 'FUERA DE SERVICIO':
    case 'EN_REPARACION':
      return '#dc3545'; // Rojo
    default:
      return '#6c757d'; // Gris
  }
}

private getEstadoIcon(estado: string): string {
  const estadoUpper = estado?.toUpperCase() || '';

  switch (estadoUpper) {
    case 'OPERATIVO':
      return 'bi-check-circle-fill';
    case 'MANTENIMIENTO':
    case 'EN_MANTENIMIENTO':
      return 'bi-tools';
    case 'FUERA_SERVICIO':
    case 'FUERA DE SERVICIO':
    case 'EN_REPARACION':
      return 'bi-x-circle-fill';
    default:
      return 'bi-question-circle';
  }
}


 
}


