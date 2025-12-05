import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChatAsistenteComponent } from './chat-asistente/chat-asistente.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', component: ChatAsistenteComponent }
];

@NgModule({
  declarations: [
    ChatAsistenteComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    ChatAsistenteComponent   // 👈 IMPORTANTE: lo exportamos
  ]
})
export class AsistenteModule {}
