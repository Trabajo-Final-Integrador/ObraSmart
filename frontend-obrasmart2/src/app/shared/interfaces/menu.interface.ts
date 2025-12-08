/**
 * Interfaz para definir un item del menú del sidebar
 */
export interface MenuItem {
  /** Texto que se muestra en el menú */
  label: string;

  /** Clase de icono de Bootstrap Icons (ej: 'bi-house-door') */
  icon: string;

  /** Ruta a la que navega (opcional si tiene submenu) */
  route?: string;

  /** Submenu items (opcional) */
  submenu?: MenuItem[];

  /** Indica si el item es visible. Por defecto true */
  visible?: boolean;

  /** Rol requerido para ver este item (opcional) */
  requiredRole?: 'ADMINISTRACION' | 'TECNICO' | 'OPERARIO';

  /** Si está activo (opcional, útil para indicadores visuales) */
  active?: boolean;
}

/**
 * Configuración completa del sidebar
 */
export interface SidebarConfig {
  /** Mostrar el header del sidebar con título "Menú Principal" */
  showHeader: boolean;

  /** Permitir submenus colapsables */
  showSubmenus: boolean;

  /** Mostrar el footer con links (Términos, Privacidad, Soporte) */
  showFooter: boolean;

  /** Lista de items del menú */
  menuItems: MenuItem[];

  /** Clase CSS adicional para el sidebar (opcional) */
  customClass?: string;
}

/**
 * Tipo de sidebar a utilizar
 */
export type SidebarType = 'full' | 'simplified' | 'none';
