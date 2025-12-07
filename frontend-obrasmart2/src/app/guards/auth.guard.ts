import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { SessionService } from '../service/session.service';
import Swal from 'sweetalert2';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private session: SessionService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const user = this.session.getUser();

    // Sin sesión: redirigir a login
    if (!user) {
      this.router.navigate(['/auth/login']);
      return false;
    }

    // Usuario inactivo
    if (user.status === 'INACTIVO') {
      Swal.fire({
        icon: 'warning',
        title: 'Cuenta inactiva',
        text: 'Contacta al administrador para habilitar el acceso.',
        confirmButtonText: 'Entendido'
      });
      this.session.clear();
      this.router.navigate(['/login']);
      return false;
    }

    // Acceso libre para rutas de stock
    if (state.url.startsWith('/stock')) {
      return true;
    }

    // Validación de rol admin
    if (route.data['role'] === 'ADMINISTRACION' && user.role !== 'ADMINISTRACION') {
      Swal.fire({
        icon: 'error',
        title: 'Acceso denegado',
        text: 'Solo los administradores pueden acceder a esta sección.',
        confirmButtonText: 'Cerrar'
      });
      this.router.navigate(['/principal']);
      return false;
    }

    return true;
  }
}
