import { Component, Input, OnInit } from '@angular/core';
import { MovimientoStockService, MovimientoStockDTO } from 'src/app/service/movimiento-stock.service';

@Component({
  selector: 'app-listado-movimientos',
  templateUrl: './listado-movimientos.component.html',
  styleUrls: ['./listado-movimientos.component.scss']
})
export class ListadoMovimientosComponent implements OnInit {
  @Input() repuestoId!: number;
  movimientos: MovimientoStockDTO[] = [];
  cargando = false;

  constructor(private service: MovimientoStockService) {}

  ngOnInit(): void {
    if (this.repuestoId) this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.service.listarPorRepuesto(this.repuestoId).subscribe({
      next: d => {
        this.movimientos = d;
        this.cargando = false;
      },
      error: () => (this.cargando = false)
    });
  }
}
