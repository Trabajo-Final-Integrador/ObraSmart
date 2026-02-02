import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LogisticaService, TrasladoDto } from 'src/app/service/logistica.service';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { ObradorDto, ObradorService } from 'src/app/service/obrador.service';
import { SidebarService } from 'src/app/service/sidebar.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-traslados-listado',
  templateUrl: './traslados-listado.component.html',
  styleUrls: ['./traslados-listado.component.scss'],
})
export class TrasladosListadoComponent implements OnInit {
  traslados: TrasladoDto[] = [];
  loading = false;
  error?: string;
  equipos: EquipoDTO[] = [];
  obradores: ObradorDto[] = [];
  showModal = false;
  selectedTraslado?: TrasladoDto;
  estadoNuevo = '';
  estadoMenuOpen = false;
  estados = ['PENDIENTE', 'EN_PROCESO', 'COMPLETADO', 'CANCELADO'];

  constructor(
    private logisticaService: LogisticaService,
    private router: Router,
    private equipoService: EquipoService,
    private obradorService: ObradorService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit(): void {
    this.fetchTraslados();
    this.equipoService.listar().subscribe({
      next: (eqs) => (this.equipos = eqs || []),
      error: (err) => console.warn('No se pudieron cargar equipos', err),
    });
    this.obradorService.listar().subscribe({
      next: (list) => (this.obradores = list || []),
      error: (err) => console.warn('No se pudieron cargar obradores', err),
    });
  }

  fetchTraslados(): void {
    this.loading = true;
    this.error = undefined;
    this.logisticaService.listar().subscribe({
      next: (data) => {
        this.traslados = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al obtener traslados', err);
        this.error = 'No se pudieron cargar los traslados';
        this.loading = false;
      },
    });
  }

  crearNuevo(): void {
    this.router.navigate(['/traslados/nuevo']);
  }

  verDetalle(traslado: TrasladoDto): void {
    this.selectedTraslado = traslado;
    this.estadoNuevo = traslado.estado || '';
    this.estadoMenuOpen = false;
    this.showModal = true;
  }

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }


  nombreEquipo(id?: number): string {
    if (!id) return '';
    const eq = this.equipos.find((e) => e.id === id);
    return eq ? eq.nombre || eq.codigoInterno || String(id) : String(id);
  }

  nombreObrador(id?: number | null): string {
    if (!id) return '';
    const ob = this.obradores.find((o) => o.id === id);
    return ob ? ob.nombre || String(id) : String(id);
  }

  cerrarModal(): void {
    this.showModal = false;
    this.selectedTraslado = undefined;
    this.estadoMenuOpen = false;
  }

  actualizarEstado(): void {
    if (!this.selectedTraslado?.id || !this.estadoNuevo) return;
    this.logisticaService.cambiarEstado(this.selectedTraslado.id, this.estadoNuevo).subscribe({
      next: (t) => {
        this.selectedTraslado = t;
        this.estadoNuevo = t.estado || this.estadoNuevo;
        this.traslados = this.traslados.map((item) => (item.id === t.id ? t : item));
        this.error = undefined;
        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'success',
          title: 'Estado actualizado',
          showConfirmButton: false,
          timer: 1800,
          timerProgressBar: true,
        });
      },
      error: (err) => {
        console.error('Error al cambiar estado', err);
        this.error = 'No se pudo actualizar el estado (requiere rol ADMINISTRACION)';
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo actualizar el estado',
        });
      },
    });
  }

  toggleEstadoMenu(): void {
    this.estadoMenuOpen = !this.estadoMenuOpen;
  }

  seleccionarEstado(valor: string): void {
    this.estadoNuevo = valor;
    this.estadoMenuOpen = false;
  }
}
