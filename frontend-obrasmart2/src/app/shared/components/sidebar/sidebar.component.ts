import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { SidebarService } from '../../../service/sidebar.service';
import { SessionService } from '../../../service/session.service';
import { MenuConfigService } from '../../../service/menu-config.service';
import { SidebarConfig, MenuItem } from '../../interfaces/menu.interface';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit, OnDestroy {
  @Input() config?: SidebarConfig;
  @Input() type: 'full' | 'simplified' = 'full';
  @Input() showConfigButton = false;
  @Output() configToggle = new EventEmitter<void>();

  menuAbierto$: Observable<boolean>;
  submenuUsuarios$: Observable<boolean>;
  submenuReparacion$: Observable<boolean>;
  submenuStock$: Observable<boolean>;

  sidebarConfig!: SidebarConfig;
  filteredMenuItems: MenuItem[] = [];
  submenuStates: { [key: string]: boolean } = {};

  private userSub?: Subscription;

  constructor(
    private sidebarService: SidebarService,
    private sessionService: SessionService,
    private menuConfigService: MenuConfigService
  ) {
    this.menuAbierto$ = this.sidebarService.menuAbierto$;
    this.submenuUsuarios$ = this.sidebarService.submenuUsuarios$;
    this.submenuReparacion$ = this.sidebarService.submenuReparacion$;
    this.submenuStock$ = this.sidebarService.submenuStock$;
  }

  ngOnInit(): void {
    this.userSub = this.sessionService.user$.subscribe(() => {
      this.initializeConfig();
      this.filterMenuItemsByRole();
    });
  }

  ngOnDestroy(): void {
    this.userSub?.unsubscribe();
  }

  private initializeConfig(): void {
    const roles = this.sessionService.getRoles();
    const userRole = roles[0] || '';

    if (this.config) {
      this.sidebarConfig = this.config;
    } else if (this.type === 'full') {
      this.sidebarConfig = this.menuConfigService.getFullMenuConfig(userRole);
    } else {
      this.sidebarConfig = this.menuConfigService.getSimplifiedMenuConfig(userRole);
    }
  }

  private filterMenuItemsByRole(): void {
    const roles = this.sessionService.getRoles();
    this.filteredMenuItems = this.menuConfigService.filterMenuByRole(
      this.sidebarConfig.menuItems,
      roles
    );
  }

  cerrarSidebar(): void {
    this.sidebarService.cerrarSidebar();
  }

  toggleSubmenu(itemLabel: string, event?: Event): void {
    if (event) {
      event.preventDefault();
    }

    if (!this.sidebarConfig.showSubmenus) {
      return;
    }

    this.submenuStates[itemLabel] = !this.submenuStates[itemLabel];

    if (itemLabel === 'Gestion de Usuarios') {
      this.sidebarService.toggleSubmenuUsuarios();
    } else if (itemLabel === 'Gestion de Reparaciones') {
      this.sidebarService.toggleSubmenuReparacion();
    } else if (itemLabel === 'Gestion de Stock') {
      this.sidebarService.toggleSubmenuStock();
    }
  }

  isSubmenuOpen(itemLabel: string): boolean {
    return this.submenuStates[itemLabel] || false;
  }

  getSubmenuObservable(itemLabel: string): Observable<boolean> | null {
    if (itemLabel === 'Gestion de Usuarios') {
      return this.submenuUsuarios$;
    } else if (itemLabel === 'Gestion de Reparaciones') {
      return this.submenuReparacion$;
    } else if (itemLabel === 'Gestion de Stock') {
      return this.submenuStock$;
    }
    return null;
  }

  abrirModalTerminos(): void {
    this.sidebarService.abrirModalTerminos();
  }

  abrirModalPrivacidad(): void {
    this.sidebarService.abrirModalPrivacidad();
  }

  abrirModalSoporte(): void {
    this.sidebarService.abrirModalSoporte();
  }

  onConfigToggle(): void {
    this.configToggle.emit();
  }

  hasSubmenu(item: MenuItem): boolean {
    return !!(item.submenu && item.submenu.length > 0);
  }
}
