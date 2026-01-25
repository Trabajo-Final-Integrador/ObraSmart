import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { map } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { SessionService } from '../service/session.service';
import { environment } from '../../environments/environment';
import Swal from 'sweetalert2';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private session: SessionService,
    private auth: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const current = this.session.getUser();
    const baseRoles = this.session.getRoles();
    if (current) {
      return this.authorize(baseRoles, route, 'from-memory');
    }

    return this.auth.loadMe().pipe(
      map(user => {
        if (!user) {
          this.router.navigate(['/auth/login']);
          return false;
        }
        return this.authorize(this.session.getRoles(), route, 'from-api');
      })
    );
  }

  private authorize(userRoles: string[], route: ActivatedRouteSnapshot, source: string): boolean {
    const requiredRoles: string[] | undefined = route.data['roles'];
    const path = route.routeConfig?.path || '';
    const isUsuarios = path.includes('usuario');
    const allowedBase = isUsuarios ? ['ADMINISTRACION'] : (requiredRoles || []);
    const allowedNormalized = this.session.normalizeRoles(allowedBase.map(r => `ROLE_${r}`));
    const userNormalized = this.session.getRoles();

    const ok = !allowedNormalized.length || allowedNormalized.some(r => userNormalized.includes(r));
    if (this.isDev()) {
      const logFn = isUsuarios ? console.warn : console.debug;
      logFn('[AuthGuard] check', {
        source,
        route: path,
        required: allowedNormalized,
        roles: userNormalized,
        ok
      });
    }
    if (!ok) {
      Swal.fire({
        icon: 'error',
        title: 'Acceso denegado',
        text: 'No tienes permisos para acceder a esta sección.',
        confirmButtonText: 'Cerrar'
      });
      this.router.navigate(['/principal']);
      return false;
    }
    return true;
  }

  private isDev(): boolean {
    return !environment.production;
  }
}
