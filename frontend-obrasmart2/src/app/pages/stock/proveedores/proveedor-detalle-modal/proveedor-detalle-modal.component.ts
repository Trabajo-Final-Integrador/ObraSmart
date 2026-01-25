import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ProveedorDto, ProveedorService } from 'src/app/service/proveedor.service';

@Component({
  selector: 'app-proveedor-detalle-modal',
  templateUrl: './proveedor-detalle-modal.component.html',
  styleUrls: ['./proveedor-detalle-modal.component.scss']
})
export class ProveedorDetalleModalComponent implements OnChanges {
  @Input() visible = false;
  @Input() proveedorId: number | null = null;
  @Output() closed = new EventEmitter<void>();

  proveedor?: ProveedorDto;
  loading = false;

  constructor(private proveedorSrv: ProveedorService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['proveedorId'] || changes['visible']) && this.visible && this.proveedorId) {
      this.cargarProveedor(this.proveedorId);
    }

    if (changes['visible'] && !this.visible) {
      this.reset();
    }
  }

  private cargarProveedor(id: number): void {
    this.loading = true;
    this.proveedorSrv.obtenerPorId(id).subscribe({
      next: (data) => {
        this.proveedor = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar proveedor', err);
        this.loading = false;
      }
    });
  }

  cerrar(): void {
    this.closed.emit();
    this.reset();
  }

  private reset(): void {
    this.proveedor = undefined;
    this.loading = false;
  }
}
