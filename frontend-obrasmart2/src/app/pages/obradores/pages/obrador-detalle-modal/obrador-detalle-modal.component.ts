import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ObradorDto } from 'src/app/service/obrador.service';
import { Usuario } from 'src/app/pages/usuarios/usuario.model';

@Component({
  selector: 'app-obrador-detalle-modal',
  templateUrl: './obrador-detalle-modal.component.html',
  styleUrls: ['./obrador-detalle-modal.component.scss'],
})
export class ObradorDetalleModalComponent implements OnChanges {
  @Input() obrador?: ObradorDto;
  @Input() supervisores: Usuario[] = [];
  @Output() close = new EventEmitter<void>();

  estadoLabel = 'ACTIVO';
  supervisorLabel = '-';
  latLabel: string | number = '-';
  lngLabel: string | number = '-';
  equipoIdsSafe: Array<number | string> = [];

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['obrador']) {
      this.syncFields();
    }
  }

  private syncFields(): void {
    const o = this.obrador;
    if (!o) {
      this.estadoLabel = 'ACTIVO';
      this.supervisorLabel = '-';
      this.latLabel = '-';
      this.lngLabel = '-';
      this.equipoIdsSafe = [];
      return;
    }
    this.estadoLabel = (o as any).estado || 'ACTIVO';
    this.latLabel = o.lat ?? '-';
    this.lngLabel = o.lng ?? '-';
    this.equipoIdsSafe = (o.equipoIds as any) || [];
    this.supervisorLabel = this.getSupervisorLabel(o.supervisorUserId);
  }

  private getSupervisorLabel(id?: number): string {
    if (!id) return '-';
    const sup = this.supervisores.find((u) => u.id === id);
    if (!sup) return id.toString();
    const fullName = `${sup.firstname || ''} ${sup.lastname || ''}`.trim();
    return sup.username || sup.email || fullName || id.toString();
  }

  onClose(): void {
    this.close.emit();
  }
}
