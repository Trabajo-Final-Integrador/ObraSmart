import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-step-catalogo',
  templateUrl: './step-catalogo.component.html',
  styleUrls: ['./step-catalogo.component.scss']
})
export class StepCatalogoComponent {

  /** 🟠 Control de pestaña activa (viene del padre) */
  @Input() tabActiva: string = '';

  /** ✅ Datos del proveedor */
  @Input() nuevoProveedor: any = {
    tieneCatalogo: false,
    urlCatalogo: '',
    codigoCliente: ''
  };

  /** 🔁 Eventos para avanzar o retroceder */
  @Output() next = new EventEmitter<void>();
  @Output() prev = new EventEmitter<void>();

  /** 🧭 Validación antes de continuar */
  continuar(): void {
    if (this.nuevoProveedor.tieneCatalogo) {
      if (!this.nuevoProveedor.urlCatalogo?.trim()) {
        alert('Por favor, completá la URL del catálogo.');
        return;
      }
    }
    this.next.emit(); // avanza al siguiente paso
  }

  volver(): void {
    this.prev.emit(); // vuelve al paso anterior
  }
}
