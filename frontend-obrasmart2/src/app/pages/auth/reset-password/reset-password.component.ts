import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from 'src/app/service/auth.service';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute } from '@angular/router';

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
    confirm: ['', Validators.required],
  });

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private translate: TranslateService,
    private route: ActivatedRoute
  ) {
    this.route.queryParamMap.subscribe(params => {
      this.token = params.get('token');
      const email = params.get('email');
      if (email) {
        this.form.patchValue({ email });
      }
    });
  }

  submit() {
    if (this.form.invalid) return;
    const { email, password, confirm } = this.form.value;
    if (password !== confirm) {
      this.msg = this.translate.instant('auth.reset.errors.mismatch');
      return;
    }

    if (!this.token) {
      this.msg = this.translate.instant('auth.reset.errors.token') || 'Token inválido o faltante';
      return;
    }

    this.loading = true;
    this.auth.resetPassword(email!, password!, this.token).subscribe({
      next: (res: any) => {
        this.loading = false;
        this.msg = this.translate.instant('auth.reset.success');
        this.token = res?.token ?? null;
      },
      error: (err) => {
        this.loading = false;
        this.msg = err?.error?.message || this.translate.instant('auth.reset.errors.unexpected');
      },
    });
  }
}
