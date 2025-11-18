import { Component, OnInit } from '@angular/core';
import { RepuestoService, RepuestoDTO } from 'src/app/service/repuesto.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-listado-repuestos',
  templateUrl: './listado-repuestos.component.html',
  styleUrls: ['./listado-repuestos.component.scss']
})
export class ListadoRepuestosComponent implements OnInit {

  repuestos: RepuestoDTO[] = [];   // 🔹 lista para mostrar en la tabla
  cargando = false;                // 🔹 indicador de carga
  error: string | null = null;     // 🔹 mensaje de error (si ocurre)

  constructor(
    private repuestoService: RepuestoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarRepuestos();
  }

  // 🔸 carga inicial de datos
  cargarRepuestos(): void {
    this.cargando = true;
    this.error = null;

    this.repuestoService.listar().subscribe({
      next: (data) => {
        this.repuestos = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al obtener repuestos', err);
        this.error = 'No se pudieron cargar los repuestos.';
        this.cargando = false;
      }
    });
  }

  // 🔸 refresca manualmente los datos
  refrescar(): void {
    this.cargarRepuestos();
  }

  // 🔸 redirige al formulario de creación
  crearNuevo(): void {
    this.router.navigate(['/stock/repuestos/crear']);
  }
}
