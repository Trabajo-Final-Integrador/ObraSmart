import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// 🔹 Tus componentes compartidos
import { GenericButtonComponent } from './components/generic-button/generic-button.component';
import { AlertComponent } from './components/alert/alert.component';
import { HelperComponent } from './components/helper/helper.component';
import { ModalUsuarioComponent } from './modal-usuario/modal-usuario.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { ModalInfoComponent } from './components/modal-info/modal-info.component';

@NgModule({
  declarations: [
    GenericButtonComponent,
    AlertComponent,
    HelperComponent,
    ModalUsuarioComponent,
    SidebarComponent,
    ModalInfoComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  exports: [
    CommonModule,
    FormsModule,
    GenericButtonComponent,
    AlertComponent,
    HelperComponent,
    ModalUsuarioComponent,
    SidebarComponent,
    ModalInfoComponent
  ]
})
export class SharedModule {}
