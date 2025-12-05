import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
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

    constructor(private http: HttpClient,private router: Router) {}

  crearUsuario() {
    console.log('📤 Enviando usuario:', this.usuario);
    console.log('📦 Datos que se envían al backend:', this.usuario);

    this.http.post('http://localhost:8085/users', this.usuario).subscribe({
        next: (res) => {
          console.log('✅ Usuario creado con éxito:', res);
          alert('Usuario creado correctamente. Ahora puede iniciar sesión.');
          this.router.navigate(['/auth/login']); // 👈 redirección al login
        },
        error: (err) => {
          console.error('❌ Error al crear usuario:', err);
          alert('Error al registrar usuario. Verifique los datos.');
        }
    });
  }
}

