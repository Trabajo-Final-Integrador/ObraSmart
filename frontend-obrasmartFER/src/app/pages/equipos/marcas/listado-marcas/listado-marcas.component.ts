import { Component, OnInit } from '@angular/core';
import { MarcaService, MarcaDTO } from 'src/app/service/marca.service';

@Component({
  selector: 'app-listado-marcas',
  templateUrl: './listado-marcas.component.html',
  styleUrls: ['./listado-marcas.component.scss']
})
export class ListadoMarcasComponent implements OnInit {
  marcas: MarcaDTO[] = [];
  loading = false;

  constructor(private service: MarcaService) {}

  ngOnInit() {
    this.cargarMarcas();
  }

  cargarMarcas() {
    this.loading = true;
    this.service.listar().subscribe({
      next: data => {
        this.marcas = data;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  eliminar(id: number) {
    if (confirm('¿Seguro que querés eliminar esta marca?')) {
      this.service.delete(id).subscribe(() => this.cargarMarcas());
    }
  }
}
