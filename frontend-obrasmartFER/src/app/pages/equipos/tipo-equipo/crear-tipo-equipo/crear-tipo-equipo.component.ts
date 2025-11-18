import { Component } from '@angular/core';
import { TipoEquipoService, TipoEquipoDTO } from 'src/app/service/tipo-equipo.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crear-tipo-equipo',
  templateUrl: './crear-tipo-equipo.component.html',
  styleUrls: ['./crear-tipo-equipo.component.scss']
})
export class CrearTipoEquipoComponent {
  tipo: TipoEquipoDTO = { nombre: '' };
  mensaje = '';

  constructor(private service: TipoEquipoService, private router: Router) {}

  guardar() {
    if (!this.tipo.nombre.trim()) {
      this.mensaje = 'El nombre del tipo de equipo es obligatorio.';
      return;
    }

    this.service.crear(this.tipo).subscribe({
      next: () => {
        alert('✅ Tipo de equipo creado correctamente');
        this.router.navigate(['/equipos/tipo-equipo']);
      },
      error: () => this.mensaje = '❌ Error al crear el tipo de equipo.'
    });
  }
}
