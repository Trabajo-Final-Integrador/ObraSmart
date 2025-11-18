import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface TipoEquipo {
  id: number;
  nombre: string;
  descripcion: string;
}

@Component({
  selector: 'app-listado-tipo-equipo',
  templateUrl: './listado-tipo-equipo.component.html',
  styleUrls: ['./listado-tipo-equipo.component.scss']
})
export class ListadoTipoEquipoComponent implements OnInit {

  tipos: TipoEquipo[] = [];
  loading = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.cargarTipos();
  }

  cargarTipos() {
    this.http.get<TipoEquipo[]>('http://localhost:8085/tipo-equipo')
      .subscribe({
        next: data => {
          this.tipos = data;
          this.loading = false;
        },
        error: err => {
          console.error('Error al cargar tipos de equipo', err);
          this.loading = false;
        }
      });
  }
}
