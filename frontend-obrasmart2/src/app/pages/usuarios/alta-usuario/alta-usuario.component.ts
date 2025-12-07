import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';
import { Usuario } from '../usuario.model';

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

  constructor(private http: HttpClient, private router: Router) {}

  crearUsuario() {
    this.http.post('http://localhost:8085/users', this.usuario).subscribe({
      next: (res) => {
        console.log('Usuario creado con éxito:', res);
        Swal.fire({
          icon: 'success',
          title: 'Usuario creado',
          text: 'Ahora puedes iniciar sesión.',
          confirmButtonText: 'Ir a login'
        }).then(() => this.router.navigate(['/auth/login']));
      },
      error: (err) => {
        console.error('Error al crear usuario:', err);
        Swal.fire({
          icon: 'error',
          title: 'Registro fallido',
          text: 'No se pudo registrar el usuario. Verifica los datos.',
          confirmButtonText: 'Cerrar'
        });
      }
    });
  }
}
