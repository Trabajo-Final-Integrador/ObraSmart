import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { SessionService, SessionUser } from './session.service';

const API_URL = 'http://localhost:8085';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient, private session: SessionService) {}

  login(username: string, password: string): Observable<any> {
     const body = { username, password };
    return this.http.post(`${API_URL}/auth/login`, body, { withCredentials: true });
  }

  resetPassword(email: string, password: string): Observable<any> {
    return this.http.post(`${API_URL}/reset`, { email, password });
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${API_URL}/forgot`, { email });
  }

  logout(): void {
    this.http.post(`${API_URL}/logout`, {}).subscribe(() => this.session.clear());
  }

   getUserInfo(): Observable<any> {
    return this.http.get(`${API_URL}/id`, { withCredentials: true });
  }
}
