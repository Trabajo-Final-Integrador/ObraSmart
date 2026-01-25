import { Injectable } from '@angular/core';
import { MenuItem, SidebarConfig } from '../shared/interfaces/menu.interface';

@Injectable({
  providedIn: 'root'
})
export class MenuConfigService {

  constructor() { }

  /**
   * Menu completo con submenus colapsables
   * Usado en: Principal, Dashboard Reparaciones, Reportes, Listado Reparaciones, Dashboard Stock
   */
  getFullMenuConfig(userRole?: string): SidebarConfig {
    return {
      showHeader: true,
      showSubmenus: true,
      showFooter: true,
      menuItems: this.getFullMenuItems(userRole)
    };
  }

  /**
   * Menu simplificado sin submenus
   * Usado en: Alta Usuario, Listado Usuario, Equipos, Stock (listados), etc.
   */
  getSimplifiedMenuConfig(userRole?: string): SidebarConfig {
    return {
      showHeader: false,
      showSubmenus: false,
      showFooter: false,
      menuItems: this.getSimplifiedMenuItems(userRole)
    };
  }

  /**
   * Items del menu completo con estructura de submenus
   */
  private getFullMenuItems(userRole?: string): MenuItem[] {
    return [
      {
        label: 'menu.users.label',
        icon: 'bi-people',
        visible: true,
        requiredRoles: ['ADMINISTRACION', 'SUPERVISOR', 'ADMIN'],
        submenu: [
          {
            label: 'menu.users.register',
            icon: 'bi-person-plus-fill',
            route: '/alta-usuario'
          },
          {
            label: 'menu.users.list',
            icon: 'bi-people-fill',
            route: '/listado-usuario'
          }
        ]
      },
      {
        label: 'menu.repairs.label',
        icon: 'bi-tools',
        visible: true,
        submenu: [
          {
            label: 'menu.repairs.dashboard',
            icon: 'bi-columns-gap',
            route: '/reparaciones/dashboard'
          },
          {
            label: 'menu.repairs.list',
            icon: 'bi-list-ul',
            route: '/reparaciones/listado'
          }
        ]
      },
      {
        label: 'menu.equipos',
        icon: 'bi-truck-flatbed',
        route: '/equipos',
        visible: true
      },
      {
        label: 'menu.obradores',
        icon: 'bi-building',
        route: '/obradores',
        visible: true
      },
      {
        label: 'menu.traslados',
        icon: 'bi-truck',
        route: '/traslados',
        visible: true
      },
      {
        label: 'menu.stock.label',
        icon: 'bi-box-seam',
        visible: true,
        submenu: [
          {
            label: 'menu.stock.dashboard',
            icon: 'bi-columns-gap',
            route: '/stock/dashboard'
          },
          {
            label: 'menu.stock.spares',
            icon: 'bi-box',
            route: '/stock/repuestos'
          },
          {
            label: 'menu.stock.suppliers',
            icon: 'bi-truck',
            route: '/stock/proveedores'
          },
          {
            label: 'menu.stock.movements',
            icon: 'bi-graph-up-arrow',
            route: '/stock/movimientos'
          },
          {
            label: 'menu.stock.orders',
            icon: 'bi-cart2',
            route: '/stock/ordenes'
          }
        ]
      },
      {
        label: 'menu.reports',
        icon: 'bi-file-bar-graph',
        route: '/reportes',
        visible: true
      }
    ];
  }

  /**
   * Items del menu simplificado (sin submenus)
   */
  private getSimplifiedMenuItems(userRole?: string): MenuItem[] {
    return [
      {
        label: 'menu.home',
        icon: 'bi-house-door',
        route: '/principal',
        visible: true
      },
      {
        label: 'menu.users.label',
        icon: 'bi-people',
        route: '/listado-usuario',
        visible: true,
        requiredRoles: ['ADMINISTRACION', 'SUPERVISOR', 'ADMIN']
      },
      {
        label: 'menu.repairs.label',
        icon: 'bi-tools',
        route: '/reparaciones/listado',
        visible: true
      },
      {
        label: 'menu.equipos',
        icon: 'bi-truck-flatbed',
        route: '/equipos',
        visible: true
      },
      {
        label: 'menu.stock.label',
        icon: 'bi-box-seam',
        route: '/stock/repuestos',
        visible: true
      },
      {
        label: 'menu.obradores',
        icon: 'bi-building',
        route: '/obradores',
        visible: true
      },
      {
        label: 'menu.traslados',
        icon: 'bi-truck',
        route: '/traslados',
        visible: true
      },
      {
        label: 'menu.reports',
        icon: 'bi-file-bar-graph',
        route: '/reportes',
        visible: true
      }
    ];
  }

  /**
   * Filtra items del menu basandose en el rol del usuario
   */
  filterMenuByRole(items: MenuItem[], roles: string[]): MenuItem[] {
    const roleList = (roles || []).map(r => r.toUpperCase());
    return items.filter(item => {
      if (item.requiredRoles && item.requiredRoles.length > 0) {
        const allowed = item.requiredRoles.some(role => roleList.includes(role.toUpperCase()));
        if (!allowed) {
          return false;
        }
      }

      if (item.submenu && item.submenu.length > 0) {
        item.submenu = this.filterMenuByRole(item.submenu, roles);
        if (item.submenu.length === 0) {
          return false;
        }
      }

      return item.visible !== false;
    });
  }

  /**
   * Obtiene la configuracion del sidebar segun el tipo de pagina
   */
  getConfigByPageType(pageType: 'dashboard' | 'list' | 'form' | 'detail', userRole?: string): SidebarConfig {
    switch (pageType) {
      case 'dashboard':
        return this.getFullMenuConfig(userRole);
      case 'list':
      case 'form':
      case 'detail':
        return this.getSimplifiedMenuConfig(userRole);
      default:
        return this.getSimplifiedMenuConfig(userRole);
    }
  }
}
