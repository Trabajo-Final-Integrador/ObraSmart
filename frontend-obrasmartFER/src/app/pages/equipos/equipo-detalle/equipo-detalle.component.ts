import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { ActivatedRoute, Router  } from '@angular/router';
import { EquipoService } from 'src/app/service/equipo.service';
import { EquipoDTO } from 'src/app/service/equipo.service';


@Component({
  selector: 'app-equipo-detalle',
  templateUrl: './equipo-detalle.component.html',
  styleUrls: ['./equipo-detalle.component.scss']
})
export class EquipoDetalleComponent implements OnInit {

  @Input() equipo!: EquipoDTO; // recibe del padre
  @Output() cerrar = new EventEmitter<void>(); // 👈 emite al padre

 

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private equipoService: EquipoService
  ) {}

  ngOnInit(): void {
    // Captura el parámetro de la URL (por ejemplo /equipos/5)
    const id = Number(this.route.snapshot.paramMap.get('id'));

    // Si tenés un servicio para obtener el equipo:
    if (id) {
      this.equipoService.obtenerPorId(id).subscribe({
        next: (data) => this.equipo = data,
        error: (err) => console.error('Error cargando equipo:', err)
      });
    }
  }
 cerrarModal() {
    this.cerrar.emit();
  }
}
