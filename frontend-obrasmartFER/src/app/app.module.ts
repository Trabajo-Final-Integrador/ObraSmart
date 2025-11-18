import { NgModule } from '@angular/core';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router'; 
import { LoginComponent } from './pages/login/login.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { PrincipalComponent } from './pages/principal/principal.component';
import { AltaUsuarioComponent } from './pages/usuarios/alta-usuario/alta-usuario.component';
import { ListadoUsuariosComponent } from './pages/usuarios/listado-usuario/listado-usuario.component';

import { CookieInterceptor } from './interceptors/cookie.interceptor';
import { AuthGuard } from './guards/auth.guard';

import { SharedModule } from './shared/shared.module';
import { BrowserModule } from '@angular/platform-browser';
import { EquiposModule } from './pages/equipos/equipos.module';

// 🔵 AGREGADO: Import del módulo del asistente
import { AsistenteModule } from './pages/asistente/asistente.module';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ResetPasswordComponent,
    PrincipalComponent,
    AltaUsuarioComponent, 
    ListadoUsuariosComponent, 
  ],
  imports: [
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    SharedModule,
    AppRoutingModule,
    EquiposModule,

    // 🔵 AGREGADO: Módulo del asistente
    AsistenteModule
  ],
  providers: [
    AuthGuard,
    { provide: HTTP_INTERCEPTORS, useClass: CookieInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
