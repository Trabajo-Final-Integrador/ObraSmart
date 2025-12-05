import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-step-observaciones',
  templateUrl: './step-observaciones.component.html',
  styleUrls: ['./step-observaciones.component.scss']
})
export class StepObservacionesComponent {

  @Input() tabActiva: string = '';

  @Input() nuevoProveedor: any = {
    observaciones: ''
  };

  @Output() prev = new EventEmitter<void>();
  @Output() guardar = new EventEmitter<void>();

  volver(): void {
    this.prev.emit();
  }

  guardarProveedor(): void {
    this.guardar.emit();
  }
}
