import { Component } from '@angular/core';
import { MarcaService, MarcaDTO } from 'src/app/service/marca.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crear-marca',
  templateUrl: './crear-marca.component.html',
  styleUrls: ['./crear-marca.component.scss']
})
export class CrearMarcaComponent {
  marca: MarcaDTO = { nombre: '' };
  mensaje = '';

  constructor(private service: MarcaService, private router: Router) {}

  guardar() {
    if (!this.marca.nombre.trim()) {
      this.mensaje = 'El nombre es obligatorio.';
      return;
    }

    this.service.crear(this.marca).subscribe({
      next: () => this.router.navigate(['/equipos/marcas']),
      error: () => this.mensaje = 'Error al guardar la marca'
    });
  }
}
