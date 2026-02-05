import { NgModule } from '@angular/core';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router'; 
import { LoginComponent } from './pages/auth/login/login.component';
import { ResetPasswordComponent } from 'src/app/pages/auth/reset-password/reset-password.component';
import { ForgotPasswordComponent } from 'src/app/pages/auth/forgot-password/forgot-password.component';
import { PrincipalComponent } from './pages/principal/principal.component';
import { AltaUsuarioComponent } from './pages/auth/usuarios/alta-usuario/alta-usuario.component';
import { ListadoUsuariosComponent } from './pages/auth/usuarios/listado-usuario/listado-usuario.component';
import { EditarUsuarioComponent } from './pages/auth/usuarios/editar-usuario/editar-usuario.component';

import { CookieInterceptor } from './interceptors/cookie.interceptor';
import { AuthGuard } from './guards/auth.guard';

import { SharedModule } from './shared/shared.module';
import { BrowserModule } from '@angular/platform-browser';
import { ServiceWorkerModule } from '@angular/service-worker';
import { EquiposModule } from './pages/equipos/equipos.module';
import { ChatAsistenteComponent } from 'src/app/pages/asistente/chat-asistente/chat-asistente.component';
import { AsistenteModule } from './pages/asistente/asistente.module';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { MapaOperacionesComponent } from './pages/mapa-operaciones/mapa-operaciones.component';
import { environment } from '../environments/environment';

export function HttpLoaderFactory(http: HttpClient) {
  const v = environment.i18nVersion || '20260131';
  return new TranslateHttpLoader(http, './assets/i18n/', `.json?v=${v}`);
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ResetPasswordComponent,
    ForgotPasswordComponent,
    PrincipalComponent,
    AltaUsuarioComponent, 
    ListadoUsuariosComponent,
    EditarUsuarioComponent,
    MapaOperacionesComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    SharedModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    AppRoutingModule,
    EquiposModule,
    AsistenteModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
      registrationStrategy: 'registerWhenStable:30000'
    })
  ],
  providers: [
    AuthGuard,
    { provide: HTTP_INTERCEPTORS, useClass: CookieInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
