import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LogisticaService, TrasladoDto } from 'src/app/service/logistica.service';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';

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

  constructor(private logisticaService: LogisticaService, private router: Router, private equipoService: EquipoService) {}

  ngOnInit(): void {
    this.fetchTraslados();
    this.equipoService.listar().subscribe({
      next: (eqs) => (this.equipos = eqs || []),
      error: (err) => console.warn('No se pudieron cargar equipos', err),
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

  verDetalle(id: number): void {
    this.router.navigate(['/traslados', id]);
  }

  nombreEquipo(id?: number): string {
    if (!id) return '';
    const eq = this.equipos.find((e) => e.id === id);
    return eq ? eq.nombre || eq.codigoInterno || String(id) : String(id);
  }
}
