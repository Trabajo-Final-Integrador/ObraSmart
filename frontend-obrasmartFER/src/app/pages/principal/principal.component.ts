import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { SessionService } from '../../service/session.service';
import * as L from 'leaflet'; 
import { GeolocalizacionService, EquipoUbicacion } from 'src/app/service/geolocalizacion.service';



@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.scss']
})
export class PrincipalComponent {
  menuAbierto = false;
  user = this.session.getUser();
   submenuStockOpen = false;

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
          L.marker([eq.lat, eq.lon])
            .addTo(this.map)
            .bindPopup(`<b>${eq.nombre}</b><br><i>${eq.estado}</i>`);
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


 
}


