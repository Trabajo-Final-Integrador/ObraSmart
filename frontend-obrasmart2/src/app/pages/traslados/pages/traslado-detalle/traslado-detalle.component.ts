import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LogisticaService, TrasladoDto } from 'src/app/service/logistica.service';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';

@Component({
  selector: 'app-traslado-detalle',
  templateUrl: './traslado-detalle.component.html',
  styleUrls: ['./traslado-detalle.component.scss'],
})
export class TrasladoDetalleComponent implements OnInit {
  traslado?: TrasladoDto;
  loading = false;
  error?: string;
  estadoNuevo = '';
  equipos: EquipoDTO[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private logisticaService: LogisticaService,
    private equipoService: EquipoService
  ) {}

  ngOnInit(): void {
    this.equipoService.listar().subscribe({
      next: (eqs) => (this.equipos = eqs || []),
      error: (err) => console.warn('No se pudieron cargar equipos', err),
    });
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.cargarTraslado(Number(id));
      }
    });
  }

  cargarTraslado(id: number): void {
    this.loading = true;
    this.logisticaService.obtener(id).subscribe({
      next: (t) => {
        this.traslado = t;
        this.estadoNuevo = t.estado || '';
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar traslado', err);
        this.error = 'No se pudo cargar el traslado';
        this.loading = false;
      },
    });
  }

  actualizarEstado(): void {
    if (!this.traslado?.id || !this.estadoNuevo) return;
    this.loading = true;
    this.logisticaService.cambiarEstado(this.traslado.id, this.estadoNuevo).subscribe({
      next: (t) => {
        this.traslado = t;
        this.estadoNuevo = t.estado || this.estadoNuevo;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cambiar estado', err);
        this.error = 'No se pudo actualizar el estado (requiere rol SUPERVISOR)';
        this.loading = false;
      },
    });
  }

  volver(): void {
    this.router.navigate(['/traslados']);
  }

  nombreEquipo(id?: number): string {
    if (!id) return '';
    const eq = this.equipos.find((e) => e.id === id);
    return eq ? eq.nombre || eq.codigoInterno || String(id) : String(id);
  }
}
