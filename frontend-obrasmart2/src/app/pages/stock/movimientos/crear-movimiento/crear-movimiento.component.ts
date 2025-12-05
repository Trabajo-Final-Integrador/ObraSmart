import { Component, OnInit } from '@angular/core';
import { MovimientosService } from 'src/app/service/movimiento-stock.service';
import { HttpClient } from '@angular/common/http';
import { Movimiento } from '../movimiento.model';
import { Router } from '@angular/router';
import { MovimientoStockDto } from 'src/app/pages/stock/movimientos/movimiento-stock-dto.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-movimientos',
  templateUrl: './crear-movimiento.component.html',
  styleUrls: ['./crear-movimiento.component.scss']
})
export class CrearMovimientoComponent implements OnInit {

  movimientos: Movimiento[] = [];
  repuestos: any[] = [];
  loading = true;

  form: MovimientoStockDto = {
    idRepuesto: 0,
    tipo: '' as any,
    cantidad: 0,
    observacion: ''
  };

  constructor(
    private movSrv: MovimientosService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarRepuestos();
  }

  cargarRepuestos() {
    this.http.get<any[]>('http://localhost:8085/repuestos', { withCredentials: true })
      .subscribe(r => this.repuestos = r);
  }

  registrar() {
    this.movSrv.crear(this.form).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Movimiento registrado',
          text: 'El movimiento de stock se registró correctamente',
          timer: 1500,
          showConfirmButton: false
        });
        this.router.navigate(['/stock/movimientos']);
      },
      error: (err) => {
        console.error('Error creando movimiento', err);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo registrar el movimiento de stock',
          confirmButtonColor: '#00796b'
        });
      }
    });
  }

  volver() {
    this.router.navigate(['/stock/movimientos']);
  }

}
