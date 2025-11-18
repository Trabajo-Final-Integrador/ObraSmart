import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-simple-tab',
  templateUrl: './simple-tab.component.html',
  styleUrls: ['./simple-tab.component.scss']
})
export class SimpleTabComponent {
  @Input() title!: string;
  @Input() items: any[] = [];
   @Input() addLink?: string; 
  @Output() add = new EventEmitter<void>(); // ✅ evento para el botón

  onAddClick() {
    this.add.emit();
  }
}
