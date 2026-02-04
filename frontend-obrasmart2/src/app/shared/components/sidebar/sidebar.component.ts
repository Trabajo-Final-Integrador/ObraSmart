import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { SidebarService } from '../../../service/sidebar.service';
import { SessionService } from '../../../service/session.service';
import { MenuConfigService } from '../../../service/menu-config.service';
import { SidebarConfig, MenuItem } from '../../interfaces/menu.interface';
import { APP_THEMES, AppTheme, ThemeService } from '../../../service/theme.service';
import { LanguageService } from '../../../service/language.service';

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
  configMenuAbierto = false;
  selectedThemeId: AppTheme = 'obra-light';
  selectedLanguage = 'es';
  readonly uiThemeOptions = APP_THEMES;
  readonly languageOptions = [
    { id: 'es', labelKey: 'language.spanish' },
    { id: 'en', labelKey: 'language.english' }
  ];

  sidebarConfig!: SidebarConfig;
  filteredMenuItems: MenuItem[] = [];
  submenuStates: { [key: string]: boolean } = {};

  private userSub?: Subscription;

  constructor(
    private sidebarService: SidebarService,
    private sessionService: SessionService,
    private menuConfigService: MenuConfigService,
    private themeService: ThemeService,
    private languageService: LanguageService
  ) {
    this.menuAbierto$ = this.sidebarService.menuAbierto$;
    this.submenuUsuarios$ = this.sidebarService.submenuUsuarios$;
    this.submenuReparacion$ = this.sidebarService.submenuReparacion$;
    this.submenuStock$ = this.sidebarService.submenuStock$;
  }

  ngOnInit(): void {
    this.selectedThemeId = this.themeService.getTheme() || 'obra-light';
    this.selectedLanguage = this.languageService.getLang();
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
    this.configMenuAbierto = false;
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
    if (this.configToggle.observers.length) {
      this.configToggle.emit();
      return;
    }

    this.configMenuAbierto = !this.configMenuAbierto;
  }

  seleccionarIdioma(id: string): void {
    this.selectedLanguage = id;
    this.languageService.setLang(id as 'es' | 'en');
    this.configMenuAbierto = false;
  }

  seleccionarTheme(id: AppTheme): void {
    this.selectedThemeId = id;
    this.themeService.apply(this.selectedThemeId);
    this.configMenuAbierto = false;
  }

  hasSubmenu(item: MenuItem): boolean {
    return !!(item.submenu && item.submenu.length > 0);
  }
}
