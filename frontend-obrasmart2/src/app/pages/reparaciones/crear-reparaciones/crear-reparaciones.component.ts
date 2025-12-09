import { Component } from '@angular/core';
import { ReparacionService, ReparacionRequestDto } from 'src/app/service/reparaciones.service';
import { Router } from '@angular/router';
import { EquipoService, EquipoDTO } from 'src/app/service/equipo.service';
import { UsuarioService } from '../../usuarios/usuario.service';
import { Usuario } from '../../usuarios/usuario.model';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-crear-reparacion',
  templateUrl: './crear-reparaciones.component.html',
  styleUrls: ['./crear-reparaciones.component.scss']
})
export class CrearReparacionComponent {

  nueva: ReparacionRequestDto = {
    equipoId: null as any,
    tipoMantenimiento: '',
    direccion: '',
    responsableId: null as any,
    descripcion: '',
    fechaInicio: ''
  };

  equipos: EquipoDTO[] = [];
  usuarios: Usuario[] = [];

  constructor(
    private repSrv: ReparacionService,
    private equipoService: EquipoService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

   ngOnInit(): void {
    this.equipoService.listar().subscribe({
      next: (data) => this.equipos = data,
      error: err => console.error("Error cargando equipos:", err)
    });

    this.usuarioService.listarUsuarios().subscribe({
      next: (data) => this.usuarios = data.filter(u => u.status === 'ACTIVO'),
      error: err => console.error("Error cargando usuarios:", err)
    });
  }


  guardar() {
  this.repSrv.crear(this.nueva).subscribe({
    next: () => {
      Swal.fire({
        icon: 'success',
        title: 'Reparación creada',
        text: 'La reparación fue registrada correctamente.',
        timer: 1500,
        showConfirmButton: false
      });
      this.router.navigate(['/reparaciones']);
    },
    error: (err) => {
      console.error(err);

      Swal.fire({
        icon: 'error',
        title: 'Error al crear reparación',
        text: err?.error?.message ?? 'Ocurrió un error inesperado.'
      });
    }
  });
}


  volver() {
    this.router.navigate(['/reparaciones']);
  }
 
}
