import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-generic-button',
  template: `
    <button [ngClass]="['btn', color]" (click)="onClick()">
      <i *ngIf="icon" [class]="icon + ' me-2'"></i> {{ text }}
    </button>
  `,
  styleUrls: ['./generic-button.component.scss']
})
export class GenericButtonComponent {
  @Input() text: string = '';
  @Input() color: 'orange' | 'gray' | 'green' = 'gray';
  @Input() icon?: string;
  @Input() action?: () => void;

  onClick() {
    if (this.action) this.action();
  }
}
