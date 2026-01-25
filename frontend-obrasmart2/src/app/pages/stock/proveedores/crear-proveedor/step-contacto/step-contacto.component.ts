import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-step-contacto',
  templateUrl: './step-contacto.component.html',
  styleUrls: ['./step-contacto.component.scss']
})


export class StepContactoComponent {
   @Input() tabActiva: string = '';

  /** ✅ Objeto del proveedor que se está creando */
  @Input() nuevoProveedor: any = {
    telefono: '',
    email: '',
    horarioAtencion: '',
    direccion: '',
    ciudad: '',
    provincia: '',
    codigoPostal: ''
  };

  /** ✅ Lista de provincias argentinas para el select */
  provinciasArgentinas: string[] = [
    'Buenos Aires',
    'Catamarca',
    'Chaco',
    'Chubut',
    'Córdoba',
    'Corrientes',
    'Entre Ríos',
    'Formosa',
    'Jujuy',
    'La Pampa',
    'La Rioja',
    'Mendoza',
    'Misiones',
    'Neuquén',
    'Río Negro',
    'Salta',
    'San Juan',
    'San Luis',
    'Santa Cruz',
    'Santa Fe',
    'Santiago del Estero',
    'Tierra del Fuego',
    'Tucumán'
  ];
  

  /** 🔁 Eventos para ir al paso anterior o siguiente */
  @Output() next = new EventEmitter<void>();
  @Output() prev = new EventEmitter<void>();

  /** 🧭 Validación antes de avanzar */
  continuar(): void {
    if (!this.nuevoProveedor.telefono?.trim() ||
        !this.nuevoProveedor.email?.trim() ||
        !this.nuevoProveedor.direccion?.trim() ||
        !this.nuevoProveedor.ciudad?.trim() ||
        !this.nuevoProveedor.provincia?.trim()) {
      alert('Por favor, complete todos los campos obligatorios.');
      return;
    }

    this.next.emit(); // pasa al siguiente paso
  }

  volver(): void {
    this.prev.emit(); // retrocede al paso anterior
  }
}
