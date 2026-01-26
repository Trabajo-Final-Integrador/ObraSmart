import { NgModule } from '@angular/core';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router'; 
import { LoginComponent } from './pages/auth/login/login.component';
import { ResetPasswordComponent } from 'src/app/pages/auth/reset-password/reset-password.component';
import { PrincipalComponent } from './pages/principal/principal.component';
import { AltaUsuarioComponent } from './pages/auth/usuarios/alta-usuario/alta-usuario.component';
import { ListadoUsuariosComponent } from './pages/auth/usuarios/listado-usuario/listado-usuario.component';

import { CookieInterceptor } from './interceptors/cookie.interceptor';
import { AuthGuard } from './guards/auth.guard';

import { SharedModule } from './shared/shared.module';
import { BrowserModule } from '@angular/platform-browser';
import { EquiposModule } from './pages/equipos/equipos.module';
import { ChatAsistenteComponent } from 'src/app/pages/asistente/chat-asistente/chat-asistente.component';
import { AsistenteModule } from './pages/asistente/asistente.module';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ResetPasswordComponent,
    PrincipalComponent,
    AltaUsuarioComponent, 
    ListadoUsuariosComponent  
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

  ],
  providers: [
    AuthGuard,
    { provide: HTTP_INTERCEPTORS, useClass: CookieInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
