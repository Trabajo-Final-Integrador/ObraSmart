import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from 'src/app/service/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  loading = false;
  msg = '';
  success = false;

  // Formulario simple: solo email
  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]]
  });

  constructor(
    private fb: FormBuilder, 
    private auth: AuthService,
    private router: Router
  ) {}

  submit() {
    if (this.form.invalid) return;
    
    const email = this.form.value.email!;
    this.loading = true;
    this.msg = '';

    // Llamar al backend para enviar el email
    this.auth.forgotPassword(email).subscribe({
      next: (res: any) => {
        this.loading = false;
        this.success = true;
        this.msg = 'Se ha enviado un correo con las instrucciones para restablecer tu contraseña.';
        console.log('✅ Email enviado:', res);
      },
      error: err => {
        this.loading = false;
        this.success = false;
        this.msg = err?.error?.message || 'Error al procesar la solicitud. Verifica tu email.';
        console.error('❌ Error:', err);
      }
    });
  }

  backToLogin() {
    this.router.navigate(['/auth/login']);
  }
}
