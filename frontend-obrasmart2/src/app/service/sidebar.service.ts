import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {
  // Estado del sidebar principal
  private menuAbiertoSubject = new BehaviorSubject<boolean>(false);
  public menuAbierto$: Observable<boolean> = this.menuAbiertoSubject.asObservable();

  // Estados de submenús
  private submenuUsuariosSubject = new BehaviorSubject<boolean>(false);
  public submenuUsuarios$: Observable<boolean> = this.submenuUsuariosSubject.asObservable();

  private submenuReparacionSubject = new BehaviorSubject<boolean>(false);
  public submenuReparacion$: Observable<boolean> = this.submenuReparacionSubject.asObservable();

  private submenuStockSubject = new BehaviorSubject<boolean>(false);
  public submenuStock$: Observable<boolean> = this.submenuStockSubject.asObservable();

  // Estados de modales
  private modalTerminosSubject = new BehaviorSubject<boolean>(false);
  public modalTerminos$: Observable<boolean> = this.modalTerminosSubject.asObservable();

  private modalPrivacidadSubject = new BehaviorSubject<boolean>(false);
  public modalPrivacidad$: Observable<boolean> = this.modalPrivacidadSubject.asObservable();

  private modalSoporteSubject = new BehaviorSubject<boolean>(false);
  public modalSoporte$: Observable<boolean> = this.modalSoporteSubject.asObservable();

  constructor() {}

  // ===== MÉTODOS PARA SIDEBAR =====

  /**
   * Alterna la visibilidad del sidebar principal
   */
  toggleSidebar(): void {
    this.menuAbiertoSubject.next(!this.menuAbiertoSubject.value);
  }

  /**
   * Abre el sidebar
   */
  abrirSidebar(): void {
    this.menuAbiertoSubject.next(true);
  }

  /**
   * Cierra el sidebar
   */
  cerrarSidebar(): void {
    this.menuAbiertoSubject.next(false);
  }

  /**
   * Obtiene el estado actual del sidebar
   */
  get menuAbierto(): boolean {
    return this.menuAbiertoSubject.value;
  }

  // ===== MÉTODOS PARA SUBMENÚS =====

  /**
   * Alterna el submenú de usuarios
   */
  toggleSubmenuUsuarios(): void {
    this.submenuUsuariosSubject.next(!this.submenuUsuariosSubject.value);
  }

  /**
   * Alterna el submenú de reparaciones
   */
  toggleSubmenuReparacion(): void {
    this.submenuReparacionSubject.next(!this.submenuReparacionSubject.value);
  }

  /**
   * Alterna el submenú de stock
   */
  toggleSubmenuStock(): void {
    this.submenuStockSubject.next(!this.submenuStockSubject.value);
  }

  /**
   * Cierra todos los submenús
   */
  cerrarTodosLosSubmenus(): void {
    this.submenuUsuariosSubject.next(false);
    this.submenuReparacionSubject.next(false);
    this.submenuStockSubject.next(false);
  }

  // ===== MÉTODOS PARA MODALES =====

  /**
   * Abre el modal de términos y condiciones
   */
  abrirModalTerminos(): void {
    this.modalTerminosSubject.next(true);
  }

  /**
   * Cierra el modal de términos y condiciones
   */
  cerrarModalTerminos(): void {
    this.modalTerminosSubject.next(false);
  }

  /**
   * Abre el modal de política de privacidad
   */
  abrirModalPrivacidad(): void {
    this.modalPrivacidadSubject.next(true);
  }

  /**
   * Cierra el modal de política de privacidad
   */
  cerrarModalPrivacidad(): void {
    this.modalPrivacidadSubject.next(false);
  }

  /**
   * Abre el modal de soporte técnico
   */
  abrirModalSoporte(): void {
    this.modalSoporteSubject.next(true);
  }

  /**
   * Cierra el modal de soporte técnico
   */
  cerrarModalSoporte(): void {
    this.modalSoporteSubject.next(false);
  }

  /**
   * Cierra todos los modales
   */
  cerrarTodosLosModales(): void {
    this.modalTerminosSubject.next(false);
    this.modalPrivacidadSubject.next(false);
    this.modalSoporteSubject.next(false);
  }

  // ===== MÉTODOS DE UTILIDAD =====

  /**
   * Resetea todo el estado del sidebar
   */
  resetearEstado(): void {
    this.cerrarSidebar();
    this.cerrarTodosLosSubmenus();
    this.cerrarTodosLosModales();
  }
}
