import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { PrincipalComponent } from './pages/principal/principal.component';
import { AuthGuard } from './guards/auth.guard';
import { AltaUsuarioComponent } from './pages/usuarios/alta-usuario/alta-usuario.component';
import { ListadoUsuariosComponent } from './pages/usuarios/listado-usuario/listado-usuario.component';


import {ChatAsistenteComponent} from './pages/asistente/chat-asistente/chat-asistente.component'



const routes: Routes = [
  // 🔐 Autenticación
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/reset-password', component: ResetPasswordComponent },

  // 🔸 Pantalla principal protegida
  { path: 'principal', component: PrincipalComponent, canActivate: [AuthGuard] },

  // 👥 Administración de usuarios
  {
    path: 'alta-usuario',
    component: AltaUsuarioComponent,
    canActivate: [AuthGuard],
    data: { role: 'ADMINISTRACION' }
  },
  {
    path: 'listado-usuario',
    component: ListadoUsuariosComponent,
    canActivate: [AuthGuard],
    data: { role: 'ADMINISTRACION' }
  },

  // ⚙️ Módulo de Stock (lazy loading)
  {
    path: 'stock',
    loadChildren: () =>
      import('./pages/stock/stock.module').then(m => m.StockModule),
    canActivate: [AuthGuard],
    
  },

{
  path: 'reparaciones',
  loadChildren: () =>
    import('./pages/reparaciones/reparaciones.module')
      .then(m => m.ReparacionesModule),
  canActivate: [AuthGuard]
},

  // 🧰 Módulo de Equipos (lazy loading)
  {
    path: 'equipos',
    loadChildren: () =>
      import('./pages/equipos/equipos.module').then(m => m.EquiposModule)
  },

 {
        path: 'reportes',
        loadChildren: () =>
          import('./pages/reportes/reportes.module').then(m => m.ReportesModule)
      },

//ASISTENTE
{
  path: 'asistente',
  loadChildren: () =>
    import('./pages/asistente/asistente.module').then(m => m.AsistenteModule),
  canActivate: [AuthGuard]
},


  // 🚫 Cualquier otra ruta redirige al login
  { path: '**', redirectTo: 'auth/login' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
