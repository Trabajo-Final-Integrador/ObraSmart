import { Component, OnInit } from '@angular/core';
import { CategoriaRepuestoService, CategoriaRepuestoDTO } from 'src/app/service/categoria-repuesto.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-listado-categorias',
  templateUrl: './listado-categorias.component.html',
  styleUrls: ['./listado-categorias.component.scss']
})
export class ListadoCategoriasComponent implements OnInit {
  categorias: CategoriaRepuestoDTO[] = [];
  cargando = false;
  error = '';

  constructor(private service: CategoriaRepuestoService, private router: Router) {}

  ngOnInit(): void {
    this.refrescar();
  }

  refrescar(): void {
    this.cargando = true;
    this.service.listar().subscribe({
      next: data => {
        this.categorias = data;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las categorías.';
        this.cargando = false;
      }
    });
  }

  crearNuevo(): void {
    this.router.navigate(['/stock/categorias/crear']);
  }
}
