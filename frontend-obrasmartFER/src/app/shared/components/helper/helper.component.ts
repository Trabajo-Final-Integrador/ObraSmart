import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-helper',
  template: `
    <div class="helper" [title]="tooltip">
      <i class="bi bi-question-circle"></i>
    </div>
  `,
  styleUrls: ['./helper.component.scss']
})
export class HelperComponent {
  @Input() tooltip: string = 'Ayuda contextual';
}
