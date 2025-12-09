import { Injectable } from '@angular/core';
import { MenuItem, SidebarConfig } from '../shared/interfaces/menu.interface';

@Injectable({
  providedIn: 'root'
})
export class MenuConfigService {

  constructor() { }

  /**
   * Menú completo con submenus colapsables
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
   * Menú simplificado sin submenus
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
   * Items del menú completo con estructura de submenus
   */
  private getFullMenuItems(userRole?: string): MenuItem[] {
    return [
      {
        label: 'Gestión de Usuarios',
        icon: 'bi-people',
        visible: true,
        requiredRole: 'ADMINISTRACION',
        submenu: [
          {
            label: 'Registrar Usuario',
            icon: 'bi-person-plus-fill',
            route: '/alta-usuario'
          },
          {
            label: 'Listado de Usuarios',
            icon: 'bi-people-fill',
            route: '/listado-usuario'
          }
        ]
      },
      {
        label: 'Gestión de Reparaciones',
        icon: 'bi-tools',
        visible: true,
        submenu: [
          {
            label: 'Centro de Reparaciones',
            icon: 'bi-columns-gap',
            route: '/reparaciones/dashboard'
          },
          {
            label: 'Listado de Reparaciones',
            icon: 'bi-list-ul',
            route: '/reparaciones/listado'
          }
        ]
      },
      {
        label: 'Gestión de Equipos',
        icon: 'bi-truck-flatbed',
        route: '/equipos',
        visible: true
      },
      {
        label: 'Gestión de Stock',
        icon: 'bi-box-seam',
        visible: true,
        submenu: [
          {
            label: 'Indicadores',
            icon: 'bi-columns-gap',
            route: '/stock/dashboard'
          },
          {
            label: 'Repuestos',
            icon: 'bi-box',
            route: '/stock/repuestos'
          },
          {
            label: 'Proveedores',
            icon: 'bi-truck',
            route: '/stock/proveedores'
          },
          {
            label: 'Movimientos',
            icon: 'bi-graph-up-arrow',
            route: '/stock/movimientos'
          },
          {
            label: 'Órdenes',
            icon: 'bi-cart2',
            route: '/stock/ordenes'
          }
        ]
      },
      {
        label: 'Gestión de Reportes',
        icon: 'bi-file-bar-graph',
        route: '/reportes',
        visible: true
      }
    ];
  }

  /**
   * Items del menú simplificado (sin submenus)
   */
  private getSimplifiedMenuItems(userRole?: string): MenuItem[] {
    return [
      {
        label: 'Inicio',
        icon: 'bi-house-door',
        route: '/principal',
        visible: true
      },
      {
        label: 'Usuarios',
        icon: 'bi-people',
        route: '/listado-usuario',
        visible: true,
        requiredRole: 'ADMINISTRACION'
      },
      {
        label: 'Reparaciones',
        icon: 'bi-tools',
        route: '/reparaciones/listado',
        visible: true
      },
      {
        label: 'Equipos',
        icon: 'bi-truck-flatbed',
        route: '/equipos',
        visible: true
      },
      {
        label: 'Stock',
        icon: 'bi-box-seam',
        route: '/stock/repuestos',
        visible: true
      },
      {
        label: 'Reportes',
        icon: 'bi-file-bar-graph',
        route: '/reportes',
        visible: true
      }
    ];
  }

  /**
   * Filtra items del menú basándose en el rol del usuario
   */
  filterMenuByRole(items: MenuItem[], userRole: string): MenuItem[] {
    return items.filter(item => {
      // Si el item requiere un rol específico y no coincide, ocultarlo
      if (item.requiredRole && item.requiredRole !== userRole) {
        return false;
      }

      // Si tiene submenu, filtrar recursivamente
      if (item.submenu && item.submenu.length > 0) {
        item.submenu = this.filterMenuByRole(item.submenu, userRole);
        // Si después de filtrar no quedan items en el submenu, ocultar el item padre
        if (item.submenu.length === 0) {
          return false;
        }
      }

      return item.visible !== false;
    });
  }

  /**
   * Obtiene la configuración del sidebar según el tipo de página
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
