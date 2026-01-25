import { Component, Input } from '@angular/core';
import { AlertTipo } from '../../models/alert-tipo'; // 

@Component({
  selector: 'app-alert',
  template: `
    <div class="alerta" [ngClass]="tipo" *ngIf="visible" role="alert" aria-live="polite">
      <i [ngClass]="icono"></i> {{ mensaje }}
    </div>
  `,
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent {
  @Input() mensaje: string = '';
  @Input() tipo: AlertTipo = 'info';
  @Input() visible: boolean = false;

    get icono() {
    switch (this.tipo) {
      case 'success': return 'bi bi-check-circle';
      case 'error':   return 'bi bi-x-circle';
      case 'warning': return 'bi bi-exclamation-triangle';
      default:        return 'bi bi-info-circle';
    }
  }
}
