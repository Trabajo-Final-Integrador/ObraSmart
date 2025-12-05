import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// 🔹 Tus componentes compartidos
import { GenericButtonComponent } from './components/generic-button/generic-button.component';
import { AlertComponent } from './components/alert/alert.component';
import { HelperComponent } from './components/helper/helper.component';
import { ModalUsuarioComponent } from './modal-usuario/modal-usuario.component';

@NgModule({
  declarations: [
    GenericButtonComponent,
    AlertComponent,
    HelperComponent,
    ModalUsuarioComponent
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
    CommonModule,
    FormsModule,
    GenericButtonComponent,
    AlertComponent,
    HelperComponent,
    ModalUsuarioComponent
  ]
})
export class SharedModule {}
