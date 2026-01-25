import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ObradorDto, ObradorService } from 'src/app/service/obrador.service';

@Component({
  selector: 'app-obradores-listado',
  templateUrl: './obradores-listado.component.html',
  styleUrls: ['./obradores-listado.component.scss'],
})
export class ObradoresListadoComponent implements OnInit {
  obradores: ObradorDto[] = [];
  loading = false;
  error?: string;

  constructor(private obradorService: ObradorService, private router: Router) {}

  ngOnInit(): void {
    this.fetchObradores();
  }

  fetchObradores(): void {
    this.loading = true;
    this.error = undefined;
    this.obradorService.listar().subscribe({
      next: (data) => {
        this.obradores = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al obtener obradores', err);
        this.error = 'No se pudieron cargar los obradores';
        this.loading = false;
      },
    });
  }

  crearNuevo(): void {
    this.router.navigate(['/obradores/nuevo']);
  }

  verDetalle(id: number): void {
    this.router.navigate(['/obradores', id]);
  }
}
