import { Component, EventEmitter, Input, Output } from '@angular/core';

type BasemapOption = { id: string; labelKey: string };

@Component({
  selector: 'app-map-basemap-selector',
  templateUrl: './map-basemap-selector.component.html',
  styleUrls: ['./map-basemap-selector.component.scss'],
})
export class MapBasemapSelectorComponent {
  @Input() themes: BasemapOption[] = [];
  @Input() selectedId = '';
  @Input() labelKey = 'navbar.map.label';
  @Output() selectTheme = new EventEmitter<string>();

  menuOpen = false;

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  closeMenu(): void {
    this.menuOpen = false;
  }

  onSelect(id: string): void {
    this.selectTheme.emit(id);
    this.closeMenu();
  }

  getThemeLabel(id: string): string {
    return this.themes.find((t) => t.id === id)?.labelKey || `navbar.map.themes.${id}`;
  }
}
