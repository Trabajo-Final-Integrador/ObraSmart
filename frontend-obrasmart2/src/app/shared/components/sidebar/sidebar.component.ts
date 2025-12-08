import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { SidebarService } from '../../../service/sidebar.service';
import { SessionService } from '../../../service/session.service';
import { MenuConfigService } from '../../../service/menu-config.service';
import { SidebarConfig, MenuItem } from '../../interfaces/menu.interface';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  // ===== INPUTS =====

  /**
   * Configuración del sidebar (opcional)
   * Si no se proporciona, usa la configuración por defecto (full menu)
   */
  @Input() config?: SidebarConfig;

  /**
   * Tipo de sidebar: 'full' | 'simplified'
   * Si se proporciona, genera automáticamente la config
   */
  @Input() type: 'full' | 'simplified' = 'full';

  // ===== STATE =====

  // Observables del servicio
  menuAbierto$: Observable<boolean>;
  submenuUsuarios$: Observable<boolean>;
  submenuReparacion$: Observable<boolean>;
  submenuStock$: Observable<boolean>;

  // Configuración actual del sidebar
  sidebarConfig!: SidebarConfig;

  // Items del menú filtrados por rol
  filteredMenuItems: MenuItem[] = [];

  // Estado local de submenus (para componentes que no usan el servicio)
  submenuStates: { [key: string]: boolean } = {};

  constructor(
    private sidebarService: SidebarService,
    private sessionService: SessionService,
    private menuConfigService: MenuConfigService
  ) {
    // Suscribirse a los observables del servicio
    this.menuAbierto$ = this.sidebarService.menuAbierto$;
    this.submenuUsuarios$ = this.sidebarService.submenuUsuarios$;
    this.submenuReparacion$ = this.sidebarService.submenuReparacion$;
    this.submenuStock$ = this.sidebarService.submenuStock$;
  }

  ngOnInit(): void {
    // Inicializar la configuración del sidebar
    this.initializeConfig();

    // Filtrar items del menú por rol de usuario
    this.filterMenuItemsByRole();
  }

  /**
   * Inicializa la configuración del sidebar
   */
  private initializeConfig(): void {
    const user = this.sessionService.getUser();
    const userRole = user?.role || '';

    // Si se proporcionó una config personalizada, usarla
    if (this.config) {
      this.sidebarConfig = this.config;
    } else {
      // Sino, generar config según el tipo
      if (this.type === 'full') {
        this.sidebarConfig = this.menuConfigService.getFullMenuConfig(userRole);
      } else {
        this.sidebarConfig = this.menuConfigService.getSimplifiedMenuConfig(userRole);
      }
    }
  }

  /**
   * Filtra los items del menú basándose en el rol del usuario
   */
  private filterMenuItemsByRole(): void {
    const user = this.sessionService.getUser();
    const userRole = user?.role || '';

    this.filteredMenuItems = this.menuConfigService.filterMenuByRole(
      this.sidebarConfig.menuItems,
      userRole
    );
  }

  // ===== MÉTODOS PARA SIDEBAR =====

  /**
   * Cierra el sidebar
   */
  cerrarSidebar(): void {
    this.sidebarService.cerrarSidebar();
  }

  // ===== MÉTODOS PARA SUBMENÚS =====

  /**
   * Alterna un submenu específico
   */
  toggleSubmenu(itemLabel: string, event?: Event): void {
    if (event) {
      event.preventDefault();
    }

    // Si showSubmenus es false, no hacer nada
    if (!this.sidebarConfig.showSubmenus) {
      return;
    }

    // Alternar el estado del submenu
    this.submenuStates[itemLabel] = !this.submenuStates[itemLabel];

    // También actualizar el servicio para los submenus principales
    if (itemLabel === 'Gestión de Usuarios') {
      this.sidebarService.toggleSubmenuUsuarios();
    } else if (itemLabel === 'Gestión de Reparaciones') {
      this.sidebarService.toggleSubmenuReparacion();
    } else if (itemLabel === 'Gestión de Stock') {
      this.sidebarService.toggleSubmenuStock();
    }
  }

  /**
   * Verifica si un submenu está abierto
   */
  isSubmenuOpen(itemLabel: string): boolean {
    return this.submenuStates[itemLabel] || false;
  }

  /**
   * Obtiene el observable del estado de un submenu desde el servicio
   */
  getSubmenuObservable(itemLabel: string): Observable<boolean> | null {
    if (itemLabel === 'Gestión de Usuarios') {
      return this.submenuUsuarios$;
    } else if (itemLabel === 'Gestión de Reparaciones') {
      return this.submenuReparacion$;
    } else if (itemLabel === 'Gestión de Stock') {
      return this.submenuStock$;
    }
    return null;
  }

  // ===== MÉTODOS PARA MODALES =====

  /**
   * Abre el modal de términos y condiciones
   */
  abrirModalTerminos(): void {
    this.sidebarService.abrirModalTerminos();
  }

  /**
   * Abre el modal de política de privacidad
   */
  abrirModalPrivacidad(): void {
    this.sidebarService.abrirModalPrivacidad();
  }

  /**
   * Abre el modal de soporte técnico
   */
  abrirModalSoporte(): void {
    this.sidebarService.abrirModalSoporte();
  }

  // ===== MÉTODOS DE UTILIDAD =====

  /**
   * Verifica si un item tiene submenu
   */
  hasSubmenu(item: MenuItem): boolean {
    return !!(item.submenu && item.submenu.length > 0);
  }
}
