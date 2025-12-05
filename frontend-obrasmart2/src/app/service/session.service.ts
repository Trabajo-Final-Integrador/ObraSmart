import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })


export class SessionService {

  private KEY = 'user_session';

  setUser(user: SessionUser) {
    localStorage.setItem(this.KEY, JSON.stringify(user));
  }

  getUser(): SessionUser | null {
    const stored = localStorage.getItem(this.KEY);
    return stored ? JSON.parse(stored) : null;
  }

  clear() {
    localStorage.removeItem(this.KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getUser();
  }

  /** 🔹 Devuelve el rol actual (ADMINISTRADOR, TECNICO, etc.) */
  getRole(): string | null {
    const user = this.getUser();
    return user ? user.role : null;
  }

  /** 🔹 Devuelve el estado actual (ACTIVO / INACTIVO) */
  getStatus(): string | null {
    const user = this.getUser();
    return user ? user.status : null;
  }
}

export interface SessionUser {
  id: number;
  username: string;
  email: string;
  role: string;
  status: string;
}

