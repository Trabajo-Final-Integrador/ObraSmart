import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { SessionService } from '../../service/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username = '';
  password = '';
  loading = false;
  errorMessage = '';

  constructor(private auth: AuthService, private router: Router, private session: SessionService) {}

  onSubmit() {
     if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = 'Debe ingresar usuario y contraseña.';
       return;
     }

    this.loading = true;
    this.auth.login(this.username, this.password).subscribe({
      next: (res) => {
         console.log('✅ Login exitoso:', res);
         console.log("🔍 RESPUESTA LOGIN:", res);
        this.loading = false;

        // 🧠 Si tu backend devuelve un objeto { id, username, email, role, status }
        // guardalo así:
        this.session.setUser(res);
       


         setTimeout(() =>
          this.router.navigate(['/principal']), 200 as number);
         
      },
      error: err => {
         console.error('❌ Error en login:', err);
        this.loading = false;

        
        if (err.status === 401) {
          this.errorMessage = 'Credenciales inválidas. Verifique su usuario o contraseña.';
        } else if (err.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Intente nuevamente.';
        } else {
          this.errorMessage = err.error?.message || 'Ocurrió un error inesperado.';
        }
      }
       
    
  });
}
}
