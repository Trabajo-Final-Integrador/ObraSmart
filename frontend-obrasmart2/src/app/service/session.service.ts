import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface SessionUser {
  userId?: number;
  roles?: string[];
  username?: string;
  email?: string;
  status?: string;
}

@Injectable({ providedIn: 'root' })
export class SessionService {
  private currentUserSubject = new BehaviorSubject<SessionUser | null>(null);
  user$: Observable<SessionUser | null> = this.currentUserSubject.asObservable();

  setUser(user: SessionUser | null) {
    if (user && user.roles) {
      user = { ...user, roles: this.normalizeRoles(user.roles) };
    }
    this.currentUserSubject.next(user);
  }

  getUser(): SessionUser | null {
    return this.currentUserSubject.value;
  }

  clear() {
    this.currentUserSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!this.currentUserSubject.value;
  }

  getRoles(): string[] {
    return this.normalizeRoles(this.currentUserSubject.value?.roles || []);
  }

  hasAnyRole(allowedRoles: string[]): boolean {
    const currentRoles = this.getRoles();
    const expected = this.normalizeRoles(allowedRoles);
    return expected.some(role => currentRoles.includes(role));
  }

  normalizeRoles(roles: string[]): string[] {
    return (roles || [])
      .map(r => (r || '').toString().toUpperCase())
      .map(r => r.startsWith('ROLE_') ? r.substring(5) : r)
      .filter(r => !!r);
  }
}
