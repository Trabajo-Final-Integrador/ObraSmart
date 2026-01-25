import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { environment } from '../../environments/environment';

@Injectable()
export class CookieInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isLogin = req.url.includes('/auth/login');
    const token = this.auth.getToken();

    let headers = req.headers;
    if (token && !isLogin) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    const cloned = req.clone({
      headers,
      withCredentials: true
    });

    return next.handle(cloned).pipe(
      tap({
        error: (err) => {
          if (err instanceof HttpErrorResponse && (err.status === 401 || err.status === 403)) {
            const url = req.url;
            if (url.includes('/users') || url.includes('/auth')) {
              const roles = this.auth.getRoles();
              if (!environment.production) {
                console.warn('[AuthInterceptor] 401/403', { status: err.status, url, body: err.error, roles });
              }
            }
          }
        }
      })
    );
  }
}
