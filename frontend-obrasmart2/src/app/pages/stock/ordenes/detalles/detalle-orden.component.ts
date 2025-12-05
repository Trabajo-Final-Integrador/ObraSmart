import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { OrdenCompraService } from 'src/app/service/orden-compra.service';
import { OrdenCompraDTO } from 'src/app/service/orden-compra.service';

@Component({
  selector: 'app-detalle-orden',
  templateUrl: './detalle-orden.component.html',
  styleUrls: ['./detalle-orden.component.scss']
})
export class DetalleOrdenComponent implements OnInit {

  orden?: OrdenCompraDTO;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private ordenSrv: OrdenCompraService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.ordenSrv.obtener(id).subscribe({
      next: data => {
        this.orden = data;
        this.loading = false;
      },
      error: err => {
        console.error("Error obteniendo detalle:", err);
        this.loading = false;
      }
    });
  }
}
