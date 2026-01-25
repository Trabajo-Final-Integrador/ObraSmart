import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { TranslateService } from '@ngx-translate/core';

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

  constructor(private auth: AuthService, private router: Router, private translate: TranslateService) {}

  onSubmit() {
    if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = this.translate.instant('auth.login.errors.missingCredentials');
      return;
    }

    this.loading = true;
    this.auth.login(this.username, this.password).subscribe({
      next: () => {
        this.auth.loadMe().subscribe({
          next: () => {
            this.loading = false;
            setTimeout(() => this.router.navigate(['/principal']), 200 as number);
          },
          error: () => {
            this.loading = false;
            this.router.navigate(['/principal']);
          },
        });
      },
      error: (err) => {
        this.loading = false;

        if (err.status === 401) {
          this.errorMessage = this.translate.instant('auth.login.errors.invalidCredentials');
        } else if (err.status === 0) {
          this.errorMessage = this.translate.instant('auth.login.errors.connection');
        } else {
          this.errorMessage = err.error?.message || this.translate.instant('auth.login.errors.unexpected');
        }
      },
    });
  }
}
