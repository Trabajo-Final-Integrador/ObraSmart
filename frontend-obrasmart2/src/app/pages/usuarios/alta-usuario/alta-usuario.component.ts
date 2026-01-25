import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SidebarService } from 'src/app/service/sidebar.service';
import Swal from 'sweetalert2';
import { Usuario } from '../usuario.model';
import { environment } from '../../../../environments/environment';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-alta-usuario',
  templateUrl: './alta-usuario.component.html',
  styleUrls: ['./alta-usuario.component.scss']
})
export class AltaUsuarioComponent {
  usuario: Usuario = {
    email: '',
    firstname: '',
    lastname: '',
    username: '',
    password: '',
    role: 'OPERARIO',
    status: 'ACTIVO'
  };

  constructor(
    private http: HttpClient,
    private router: Router,
    private sidebarService: SidebarService,
    private translate: TranslateService
  ) {}

  toggleMenu() {
    this.sidebarService.toggleSidebar();
  }

  crearUsuario() {
    const baseUrl = `${(environment as any).gatewayUrl || environment.apiUrl}/users`;
    this.http.post(baseUrl, this.usuario, { withCredentials: true }).subscribe({
      next: (res) => {
        console.log('Usuario creado con éxito:', res);
        Swal.fire({
          icon: 'success',
          title: this.translate.instant('users.form.alert.successTitle'),
          text: this.translate.instant('users.form.alert.successText'),
          confirmButtonText: this.translate.instant('users.form.alert.successConfirm')
        }).then(() => this.router.navigate(['/auth/login']));
      },
      error: (err) => {
        console.error('Error al crear usuario:', err);
        Swal.fire({
          icon: 'error',
          title: this.translate.instant('users.form.alert.errorTitle'),
          text: this.translate.instant('users.form.alert.errorText'),
          confirmButtonText: this.translate.instant('users.form.alert.errorConfirm')
        });
      }
    });
  }
}
