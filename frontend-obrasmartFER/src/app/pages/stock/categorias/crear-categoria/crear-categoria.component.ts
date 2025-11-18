import { Component } from '@angular/core';
import { CategoriaRepuestoService, CategoriaRepuestoDTO } from 'src/app/service/categoria-repuesto.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crear-categoria',
  templateUrl: './crear-categoria.component.html',
  styleUrls: ['./crear-categoria.component.scss']
})
export class CrearCategoriaComponent {
  modelo: CategoriaRepuestoDTO = { nombre: '', descripcion: '' };
  cargando = false;
  error = '';
  exito = false;

  constructor(private service: CategoriaRepuestoService, private router: Router) {}

  guardar(): void {
    this.cargando = true;
    this.service.crear(this.modelo).subscribe({
      next: () => {
        this.exito = true;
        this.cargando = false;
        setTimeout(() => this.router.navigate(['/stock/categorias']), 1000);
      },
      error: () => {
        this.error = 'Error al crear la categoría.';
        this.cargando = false;
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/stock/categorias']);
  }
}
