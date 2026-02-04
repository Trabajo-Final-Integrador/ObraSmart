import { Component } from '@angular/core';
import { ReparacionService, ReparacionRequestDto } from 'src/app/service/reparaciones.service';
import { Router } from '@angular/router';
import { EquipoService, EquipoDTO } from 'src/app/service/equipo.service';
import { UsuarioService } from 'src/app/service/usuario.service';
import { Usuario } from '../../auth/usuarios/usuario.model';
import Swal from 'sweetalert2';
import { TranslateService } from '@ngx-translate/core';
import { SidebarService } from 'src/app/service/sidebar.service';

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
    private router: Router,
    private translate: TranslateService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit(): void {
    this.equipoService.listar().subscribe({
      next: (data) => this.equipos = data,
      error: err => console.error('Error cargando equipos:', err)
    });

    this.usuarioService.listarUsuarios().subscribe({
      next: (data) => this.usuarios = data.filter(u => u.status === 'ACTIVO'),
      error: err => console.error('Error cargando usuarios:', err)
    });
  }

  nombreUsuario(u: Usuario): string {
    const nombre = `${u.firstname || ''} ${u.lastname || ''}`.trim();
    return nombre || u.username || u.email;
  }

  guardar() {
  this.repSrv.crear(this.nueva).subscribe({
    next: () => {
      Swal.fire({
        icon: 'success',
        title: this.translate.instant('repairs.alert.createSuccessTitle'),
        text: this.translate.instant('repairs.alert.createSuccessText'),
        timer: 1500,
        showConfirmButton: false
      });
      this.router.navigate(['/reparaciones']);
    },
    error: (err) => {
      console.error(err);

      Swal.fire({
        icon: 'error',
        title: this.translate.instant('repairs.alert.createErrorTitle'),
        text: err?.error?.message ?? this.translate.instant('repairs.alert.createErrorText')
      });
    }
  });
}


  volver() {
    this.router.navigate(['/reparaciones']);
  }

  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }
 
}
