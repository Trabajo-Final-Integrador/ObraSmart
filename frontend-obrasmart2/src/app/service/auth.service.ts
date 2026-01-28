import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject, catchError, of, tap, throwError } from 'rxjs';
import { SessionService, SessionUser } from './session.service';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUserSubject = new BehaviorSubject<SessionUser | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();
  private accessToken: string | null = null;
  private baseUrl = (environment as any).gatewayUrl || environment.apiUrl;

  constructor(private http: HttpClient, private session: SessionService, private router: Router) {}

  login(username: string, password: string): Observable<any> {
    const body = { username, password };
    return this.http.post(`${this.baseUrl}/auth/login`, body, { withCredentials: true, observe: 'response' })
      .pipe(
        tap(res => {
          const token = (res.body as any)?.token || this.extractTokenFromCookie();
          this.setToken(token);
        }),
        catchError(err => {
          const message = err?.error?.error || err?.error?.message || 'Error al iniciar sesión';
          return throwError(() => new Error(message));
        })
      );
  }

  loadMe(): Observable<SessionUser | null> {
    return this.http.get<SessionUser>(`${this.baseUrl}/auth/me`, { withCredentials: true })
      .pipe(
        tap(user => {
          this.tryLoadToken();
          this.setCurrentUser(user);
        }),
        catchError(() => {
          this.setCurrentUser(null);
          return of(null);
        })
      );
  }

  setCurrentUser(user: SessionUser | null) {
    this.currentUserSubject.next(user);
    this.session.setUser(user);
  }

  getCurrentUser(): SessionUser | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return this.accessToken;
  }

  getRoles(): string[] {
    return this.session.getRoles();
  }

  resetPassword(email: string, password: string, token?: string | null): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/reset`, { email, password, token });
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/forgot`, { email });
  }

  logout(): void {
    this.http.post(`${this.baseUrl}/auth/logout`, {}, { withCredentials: true })
      .subscribe({
        next: () => this.session.clear(),
        error: () => this.session.clear()
      });
    this.currentUserSubject.next(null);
    this.accessToken = null;
    try {
      sessionStorage.removeItem('access_token');
    } catch {
      // ignore
    }
    this.router.navigate(['/auth/login']);
  }

  private setToken(token: string | null | undefined) {
    if (!token) return;
    this.accessToken = token;
    try {
      sessionStorage.setItem('access_token', token);
    } catch {
      // ignore storage errors
    }
  }

  private tryLoadToken() {
    if (this.accessToken) return;
    const fromCookie = this.extractTokenFromCookie();
    if (fromCookie) {
      this.setToken(fromCookie);
      return;
    }
    try {
      const stored = sessionStorage.getItem('access_token');
      if (stored) this.accessToken = stored;
    } catch {
      // ignore
    }
  }

  private extractTokenFromCookie(): string | null {
    if (typeof document === 'undefined') return null;
    const match = document.cookie
      ?.split(';')
      .map(c => c.trim())
      .find(c => c.startsWith('ACCESS_TOKEN='));
    if (!match) return null;
    return decodeURIComponent(match.split('=')[1] || '');
  }

  // JWT HttpOnly: no se decodifica en el front; roles vienen de /auth/me
}
