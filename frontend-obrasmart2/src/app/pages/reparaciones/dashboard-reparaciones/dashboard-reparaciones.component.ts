import { Component } from '@angular/core';
import { ReparacionService, ReparacionResponseDTO } from 'src/app/service/reparaciones.service';
import { EquipoService, EquipoDTO } from 'src/app/service/equipo.service';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard-reparaciones',
  templateUrl: './dashboard-reparaciones.component.html',
  styleUrls: ['./dashboard-reparaciones.component.scss']
})
export class DashboardReparacionesComponent {

  recientes: ReparacionResponseDTO[] = [];
  total = 0;
  enProceso = 0;
  pendientes = 0;
  finalizadas = 0;

  // Fleet data
  flotaOperativa = 0;
  flotaEnRevision = 0;
  flotaCriticos = 0;

  // Sidebar menu state
  menuAbierto = false;
  submenuUsuariosOpen = false;
  submenuReparacionOpen = false;
  submenuStockOpen = false;

  constructor(
    private service: ReparacionService,
    private equipoService: EquipoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Fetch both reparaciones and equipos data in parallel
    forkJoin({
      reparaciones: this.service.listar(),
      equipos: this.equipoService.listar()
    }).subscribe({
      next: ({ reparaciones, equipos }) => {
        // Process reparaciones data
        this.total = reparaciones.length;
        this.enProceso = reparaciones.filter(r => r.estadoReparacion === 'EN_PROCESO').length;
        this.pendientes = reparaciones.filter(r => r.estadoReparacion === 'CREADA').length;
        this.finalizadas = reparaciones.filter(r => r.estadoReparacion === 'FINALIZADA').length;
        this.recientes = reparaciones.slice(0, 5);

        // Calculate real fleet statistics
        this.calculateFleetStatus(equipos);
      },
      error: (error) => {
        console.error('Error loading dashboard data:', error);
      }
    });
  }

  private calculateFleetStatus(equipos: EquipoDTO[]): void {
    // Filter only active equipment
    const activeEquipos = equipos.filter(e => e.activo);

    // Operativa: estadoOperativo === 'OPERATIVO'
    this.flotaOperativa = activeEquipos.filter(e =>
      e.estadoOperativo === 'OPERATIVO' || e.estadoOperativo === 'Operativo'
    ).length;

    // En Revisión: estadoOperativo === 'EN_MANTENIMIENTO' or 'EN_REPARACION'
    this.flotaEnRevision = activeEquipos.filter(e =>
      e.estadoOperativo === 'EN_MANTENIMIENTO' ||
      e.estadoOperativo === 'EN_REPARACION' ||
      e.estadoOperativo === 'En Mantenimiento' ||
      e.estadoOperativo === 'En Reparación'
    ).length;

    // Críticos: estadoOperativo === 'FUERA_DE_SERVICIO' or 'CRITICO'
    this.flotaCriticos = activeEquipos.filter(e =>
      e.estadoOperativo === 'FUERA_DE_SERVICIO' ||
      e.estadoOperativo === 'CRITICO' ||
      e.estadoOperativo === 'Fuera de Servicio' ||
      e.estadoOperativo === 'Crítico'
    ).length;

    console.log('Fleet status calculated:', {
      total: activeEquipos.length,
      operativa: this.flotaOperativa,
      enRevision: this.flotaEnRevision,
      criticos: this.flotaCriticos
    });
  }

  

  nuevaReparacion() {
    this.router.navigate(['/reparaciones/crear']);
  }

  verTodas() {
    this.router.navigate(['/reparaciones']);
  }

  regresar() {
    this.router.navigate(['/principal']);
  }

  toggleMenu() {
    this.menuAbierto = !this.menuAbierto;
  }

  toggleUsuarios(event: Event) {
    event.preventDefault();
    this.submenuUsuariosOpen = !this.submenuUsuariosOpen;
  }

  toggleReparacion(event: Event) {
    event.preventDefault();
    this.submenuReparacionOpen = !this.submenuReparacionOpen;
  }

  toggleStock(event: Event) {
    event.preventDefault();
    this.submenuStockOpen = !this.submenuStockOpen;
  }

  getDescripcionCorta(texto: string): string {
    if (!texto) return '';
    return texto.length > 60 ? texto.slice(0, 60) + '...' : texto;
  }
}


