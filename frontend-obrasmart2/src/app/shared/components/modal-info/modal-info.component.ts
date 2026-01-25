import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { SidebarService } from '../../../service/sidebar.service';

@Component({
  selector: 'app-modal-info',
  templateUrl: './modal-info.component.html',
  styleUrls: ['./modal-info.component.scss']
})
export class ModalInfoComponent {
  // Observables de los modales
  modalTerminos$: Observable<boolean>;
  modalPrivacidad$: Observable<boolean>;
  modalSoporte$: Observable<boolean>;

  constructor(private sidebarService: SidebarService) {
    // Suscribirse a los observables del servicio
    this.modalTerminos$ = this.sidebarService.modalTerminos$;
    this.modalPrivacidad$ = this.sidebarService.modalPrivacidad$;
    this.modalSoporte$ = this.sidebarService.modalSoporte$;
  }

  /**
   * Cierra el modal de términos y condiciones
   */
  cerrarModalTerminos(): void {
    this.sidebarService.cerrarModalTerminos();
  }

  /**
   * Cierra el modal de política de privacidad
   */
  cerrarModalPrivacidad(): void {
    this.sidebarService.cerrarModalPrivacidad();
  }

  /**
   * Cierra el modal de soporte técnico
   */
  cerrarModalSoporte(): void {
    this.sidebarService.cerrarModalSoporte();
  }
}
