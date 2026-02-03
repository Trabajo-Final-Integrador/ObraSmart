import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-step-bancarios',
  templateUrl: './step-bancarios.component.html',
  styleUrls: ['./step-bancarios.component.scss']
})
export class StepBancariosComponent {

  /** 🟠 Pestaña activa (controlada por el componente padre) */
  @Input() tabActiva: string = '';
  @Input() forceShow = false;
  @Input() showActions = true;

  /** ✅ Datos del proveedor que se están editando o creando */
  @Input() nuevoProveedor: any = {
    banco: '',
    tipoCuenta: '',
    cbu: ''
  };

  /** 🔁 Eventos para ir al siguiente o anterior paso */
  @Output() next = new EventEmitter<void>();
  @Output() prev = new EventEmitter<void>();

  /** 🧭 Validación antes de avanzar */
  continuar(): void {
    // No todos los campos son obligatorios, pero podés agregar validaciones si querés
    if (!this.nuevoProveedor.banco?.trim() && !this.nuevoProveedor.cbu?.trim()) {
      const confirmar = confirm('No completaste los datos bancarios. ¿Querés continuar igual?');
      if (!confirmar) return;
    }

    this.next.emit(); // pasa al siguiente paso (confirmar)
  }

  volver(): void {
    this.prev.emit(); // vuelve al paso anterior (contacto)
  }
}
