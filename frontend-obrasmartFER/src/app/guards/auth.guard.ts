import { Injectable } from '@angular/core';
import { CanActivate, Router,ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import { SessionService } from '../service/session.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private session: SessionService, private router: Router) {}

   canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    console.log('🧭 Entrando al AuthGuard - URL:', state.url);
    const url = state.url;
    const user = this.session.getUser();

  

  // ⚙️ 2️⃣ Si no hay usuario logueado → redirigir al login
  

     if (!user) {
    this.router.navigate(['/auth/login']);
    return false;
  }

  // 🚫 Si el usuario está inactivo → login
    if (user!.status === 'INACTIVO') {
      alert('Tu cuenta está inactiva. Contacte al administrador.');
      this.session.clear();
      this.router.navigate(['/login']);
      return false;
    }

    // 4️⃣ Acceso libre para rutas de stock
    if (state.url.startsWith('/stock')) {
      console.log('✅ Acceso libre permitido para rutas de STOCK');
      return true;
    }

    console.log('🔎 Rol guardado en sesión:', user!.role);
    console.log('🔎 Datos esperados:', route.data['role']);
   
    // 🔒 Si la ruta requiere rol ADMINISTRADOR y no lo es → sin acceso
    if (route.data['role'] === 'ADMINISTRACION' && user.role != 'ADMINISTRACION') {
      alert('Acceso denegado. Solo los administradores pueden acceder a esta sección.');
      this.router.navigate(['/principal']);
      return false;
    }

    // ✅ Si todo está correcto, permitir acceso
    return true;
  }
}
