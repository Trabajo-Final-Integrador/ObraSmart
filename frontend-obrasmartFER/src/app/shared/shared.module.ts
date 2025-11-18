import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// 🔹 Tus componentes compartidos
import { GenericButtonComponent } from './components/generic-button/generic-button.component';
import { AlertComponent } from './components/alert/alert.component';
import { HelperComponent } from './components/helper/helper.component';

@NgModule({
  declarations: [
    GenericButtonComponent,
    AlertComponent,
    HelperComponent
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
    HelperComponent
  ]
})
export class SharedModule {}
