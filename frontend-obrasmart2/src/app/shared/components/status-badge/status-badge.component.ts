import { Component, Input } from '@angular/core';

type Variant = 'success' | 'warning' | 'danger' | 'info' | 'muted';

@Component({
  selector: 'app-status-badge',
  templateUrl: './status-badge.component.html',
  styleUrls: ['./status-badge.component.scss'],
})
export class StatusBadgeComponent {
  @Input() estado: string | null | undefined = '';

  get texto(): string {
    return this.estado?.toString().trim() || 'SIN ESTADO';
  }

  get variant(): Variant {
    const normalized = this.normalize(this.estado);
    if (['FINALIZADA', 'ACTIVO', 'EN_STOCK', 'OPERATIVO', 'DISPONIBLE', 'OK'].includes(normalized)) return 'success';
    if (['CREADA', 'MANTENIMIENTO', 'EN_MANTENIMIENTO', 'PAUSADA', 'STOCK_BAJO'].includes(normalized)) return 'warning';
    if (['EN_PROCESO', 'EN_TRASLADO', 'ASIGNADO', 'INFO'].includes(normalized)) return 'info';
    if (['CANCELADA', 'INACTIVO', 'REPONER', 'FUERA_DE_SERVICIO', 'BAJA'].includes(normalized)) return 'danger';
    return 'muted';
  }

  get variantClass(): string {
    return `status-${this.variant}`;
  }

  private normalize(value: string | null | undefined): string {
    if (!value) return 'SIN_ESTADO';
    return value
      .toString()
      .trim()
      .toUpperCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, '_');
  }
}
