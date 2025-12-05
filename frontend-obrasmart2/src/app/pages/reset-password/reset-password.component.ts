import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {
  loading = false;
  msg = '';
  token: string | null = null;

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirm: ['', Validators.required]
  });

  constructor(private fb: FormBuilder, private auth: AuthService) {}

  submit() {
    if (this.form.invalid) return;
    const { email, password, confirm } = this.form.value;
    if (password !== confirm) {
      this.msg = 'Las contraseñas no coinciden';
      return;
    }

    this.loading = true;
    this.auth.resetPassword(email!, password!).subscribe({
      next: (res: any) => {
        this.loading = false;
        this.msg = 'Contraseña restablecida correctamente.';
        this.token = res?.token ?? null;
      },
      error: err => {
        this.loading = false;
        this.msg = err?.error?.message || 'Error al restablecer la contraseña';
      }
    });
  }
}
