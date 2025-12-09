import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-step-comercio',
  templateUrl: './step-comercio.component.html',
  styleUrls: ['./step-comercio.component.scss']
})
export class StepComercioComponent implements OnChanges {

  /** Control de pestaña activa */
  @Input() tabActiva: string = '';

  /** Objeto del proveedor (recibido desde el padre) */
  @Input() nuevoProveedor: any = {
    especialidad: '',
    tiempoEntrega: null,
    marcas: [],
    pedidoMinimo: null,
    descuentoVolumen: null,
    condicionesPago: '',
    tieneStock: false,
    atiendeUrgencias: false,
    haceEnvios: false,
    aceptaDevoluciones: false,
    zonaCobertura: ''
  };

  /** Campos auxiliares del componente */
  marcaInput: string = '';
  marcas: string[] = [];

  /** Eventos de navegación */
  @Output() next = new EventEmitter<void>();
  @Output() prev = new EventEmitter<void>();

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['nuevoProveedor'] && this.nuevoProveedor?.marcas?.length) {
      // Inicializa marcas locales cuando el proveedor viene con datos (edición)
      this.marcas = [...this.nuevoProveedor.marcas];
    }
  }

  /** Agrega una marca a la lista */
  agregarMarca(): void {
    const marca = this.marcaInput.trim();
    if (marca && !this.marcas.includes(marca)) {
      this.marcas.push(marca);
      this.marcaInput = '';
      // Actualiza también el array en el proveedor
      this.nuevoProveedor.marcas = [...this.marcas];
    }
  }

  /** Elimina una marca por índice */
  eliminarMarca(index: number): void {
    this.marcas.splice(index, 1);
    this.nuevoProveedor.marcas = [...this.marcas];
  }

  /** Validación básica antes de continuar */
  continuar(): void {
    if (this.marcas.length === 0 && !this.nuevoProveedor.especialidad.trim()) {
      const confirmar = confirm('No cargaste ninguna marca ni especialidad. ¿Querés continuar igual?');
      if (!confirmar) return;
    }
    this.next.emit();
  }

  volver(): void {
    this.prev.emit();
  }
}
