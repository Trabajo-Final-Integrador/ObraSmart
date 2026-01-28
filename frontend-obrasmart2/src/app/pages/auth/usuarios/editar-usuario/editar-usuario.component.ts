import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { SidebarService } from 'src/app/service/sidebar.service';
import { UsuarioService } from 'src/app/service/usuario.service';
import { Usuario } from '../usuario.model';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-editar-usuario',
  templateUrl: './editar-usuario.component.html',
  styleUrls: ['./editar-usuario.component.scss']
})
export class EditarUsuarioComponent implements OnInit {
  usuario: Usuario = {
    email: '',
    firstname: '',
    lastname: '',
    username: '',
    role: 'OPERARIO',
    status: 'ACTIVO',
    observaciones: ''
  };
  loading = false;
  userId?: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sidebarService: SidebarService,
    private usuarioService: UsuarioService,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : NaN;
    if (!id || Number.isNaN(id)) {
      this.router.navigate(['/listado-usuario']);
      return;
    }
    this.userId = id;
    this.cargarUsuario(id);
  }

  toggleMenu() {
    this.sidebarService.toggleSidebar();
  }

  private cargarUsuario(id: number) {
    this.loading = true;
    this.usuarioService.getById(id).subscribe({
      next: (data) => {
        this.usuario = {
          ...data,
          password: ''
        };
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar usuario:', err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('common.error'),
          text: this.translate.instant('users.edit.errors.load')
        }).then(() => this.router.navigate(['/listado-usuario']));
      }
    });
  }

  guardarCambios() {
    if (!this.userId) return;
    const payload: Usuario = { ...this.usuario };
    if (!payload.password) {
      delete (payload as any).password;
    }
    this.loading = true;
    this.usuarioService.actualizarUsuario(this.userId, payload).subscribe({
      next: () => {
        this.loading = false;
        Swal.fire({
          icon: 'success',
          title: this.translate.instant('users.edit.alert.successTitle'),
          text: this.translate.instant('users.edit.alert.successText'),
          confirmButtonText: this.translate.instant('users.edit.alert.successConfirm')
        }).then(() => this.router.navigate(['/listado-usuario']));
      },
      error: (err) => {
        console.error('Error al actualizar usuario:', err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('users.edit.alert.errorTitle'),
          text: this.translate.instant('users.edit.alert.errorText')
        });
      }
    });
  }
}
