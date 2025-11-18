import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ModeloService, ModeloDTO } from 'src/app/service/modelo.service';
import { MarcaService, MarcaDTO } from '../../../../service/marca.service';

@Component({
  selector: 'app-crear-modelo',
  templateUrl: './crear-modelo.component.html',
  styleUrls: ['./crear-modelo.component.scss']
})
export class CrearModeloComponent {
  modelo = { nombre: '', idMarca: 0 }; // Usamos idMarca para el <select>
  marcas: MarcaDTO[] = [];

mensaje='';


 
   constructor(private modeloService: ModeloService, private marcaService: MarcaService, private router: Router) {}
 
    guardar() {
  const dto = {
    nombre: this.modelo.nombre,
    marca: { id: this.modelo.idMarca }  // 👈 se adapta al backend
  };

  this.modeloService.crear(dto).subscribe({
    next: () => {
      alert('✅ Modelo creado correctamente');
      this.router.navigate(['/equipos/modelos']);
    },
    error: err => console.error('❌ Error al crear modelo', err)
  });
  }

 }
