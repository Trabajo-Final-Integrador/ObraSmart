import { Component } from '@angular/core';
import { RepuestoService, RepuestoDTO } from 'src/app/service/repuesto.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crear-repuesto',
  templateUrl: './crear-repuesto.component.html',
  styleUrls: ['./crear-repuesto.component.scss']
})
export class CrearRepuestoComponent {
  modelo: RepuestoDTO = {
    nombre: '',
    cantidad: 0,
    stockMinimo: 0,
    precioUnitario: 0,
    unidadMedida: '',
    estado: true
  };

  cargando = false;
  error: string | null = null;
  exito = false;

  constructor(private repuestoService: RepuestoService, private router: Router) {}

  guardar(): void {
    this.cargando = true;
    this.error = null;

    this.repuestoService.crear(this.modelo).subscribe({
      next: () => {
        this.exito = true;
        setTimeout(() => this.router.navigate(['/stock/repuestos']), 1500);
      },
      error: (err) => {
        console.error('Error al crear repuesto', err);
        this.error = 'Error al crear el repuesto. Verifique los datos.';
      },
      complete: () => (this.cargando = false)
    });
  }

  cancelar(): void {
    this.router.navigate(['/stock/repuestos']);
  }
}
