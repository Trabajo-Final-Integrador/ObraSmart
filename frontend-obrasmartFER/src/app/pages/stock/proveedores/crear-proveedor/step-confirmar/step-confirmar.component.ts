import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-step-confirmar',
  templateUrl: './step-confirmar.component.html',
  styleUrls: ['./step-confirmar.component.scss']
})
export class StepConfirmarComponent {

  /** 🔹 Control de pestaña activa */
  @Input() tabActiva: string = '';

  /** 🔹 Datos completos del proveedor (recibidos del padre) */
  @Input() nuevoProveedor: any;

  /** 🔁 Eventos de navegación */
  @Output() prev = new EventEmitter<void>();
  @Output() confirmar = new EventEmitter<void>();

  /** ✅ Volver al paso anterior */
  volver(): void {
    this.prev.emit();
  }

  /** 💾 Confirmar y guardar */
  guardar(): void {
    // Podrías agregar validaciones finales acá si lo necesitás
    const confirmar = confirm('¿Deseás guardar este proveedor?');
    if (confirmar) {
      this.confirmar.emit(); // emite al padre para guardar definitivamente
    }
  }
}
