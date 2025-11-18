import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Modelo {
  id: number;
  nombre: string;
  marca: string;
}

@Component({
  selector: 'app-listado-modelos',
  templateUrl: './listado-modelos.component.html',
  styleUrls: ['./listado-modelos.component.scss']
})
export class ListadoModelosComponent implements OnInit {

  modelos: Modelo[] = [];
  loading = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.cargarModelos();
  }

  cargarModelos() {
    this.http.get<Modelo[]>('http://localhost:8085/modelo')
      .subscribe({
        next: data => {
          this.modelos = data;
          this.loading = false;
        },
        error: err => {
          console.error('Error al cargar modelos', err);
          this.loading = false;
        }
      });
  }
}
