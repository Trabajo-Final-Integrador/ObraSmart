import { Component } from '@angular/core';
import { MovimientoStockService, MovimientoStockDTO } from 'src/app/service/movimiento-stock.service';
import { RepuestoService, RepuestoDTO } from 'src/app/service/repuesto.service';

@Component({
  selector: 'app-registrar-movimiento',
  templateUrl: './registrar-movimiento.component.html',
  styleUrls: ['./registrar-movimiento.component.scss']
})
export class RegistrarMovimientoComponent {
  modelo: MovimientoStockDTO = {
    tipo: 'ENTRADA',
    cantidad: 1,
    motivo: '',
    usuario: 'admin',
    repuestoId: 0
  };

  repuestos: RepuestoDTO[] = [];
  cargando = false;
  exito = false;
  error = '';

  constructor(private service: MovimientoStockService, private repuestoService: RepuestoService) {}

  ngOnInit(): void {
    this.repuestoService.listar().subscribe({
      next: data => (this.repuestos = data)
    });
  }

  registrar(): void {
    this.cargando = true;
    this.service.registrar(this.modelo).subscribe({
      next: () => {
        this.exito = true;
        this.cargando = false;
        this.modelo = { tipo: 'ENTRADA', cantidad: 1, motivo: '', usuario: 'admin', repuestoId: 0 };
      },
      error: () => {
        this.error = 'Error al registrar el movimiento.';
        this.cargando = false;
      }
    });
  }
}
