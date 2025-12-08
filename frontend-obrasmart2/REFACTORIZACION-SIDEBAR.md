# Refactorización del Sidebar - Componentes Compartidos

## 📋 Resumen

Se ha eliminado la duplicación de código del **sidebar** y **modales** que se repetían en múltiples componentes (`PrincipalComponent`, `ListadoReparacionesComponent`, `ReportesComponent`).

## 🎯 Problema Resuelto

**ANTES:**
- El código del sidebar estaba duplicado en 3+ componentes
- Los modales (Términos, Privacidad, Soporte) también estaban duplicados
- Mantenimiento difícil (cambiar algo requería editar múltiples archivos)
- Inconsistencias en la UI entre páginas

**DESPUÉS:**
- 1 componente `SidebarComponent` reutilizable
- 1 componente `ModalInfoComponent` reutilizable
- 1 servicio `SidebarService` para manejar el estado
- Código DRY (Don't Repeat Yourself)

---

## 📁 Archivos Creados

### 1. SidebarService
**Ubicación:** `src/app/service/sidebar.service.ts`

**Responsabilidad:** Gestionar el estado global del sidebar y modales usando RxJS BehaviorSubject.

**API:**
```typescript
// Estado del sidebar
menuAbierto$: Observable<boolean>
toggleSidebar(): void
abrirSidebar(): void
cerrarSidebar(): void

// Submenús
submenuUsuarios$: Observable<boolean>
submenuReparacion$: Observable<boolean>
submenuStock$: Observable<boolean>
toggleSubmenuUsuarios(): void
toggleSubmenuReparacion(): void
toggleSubmenuStock(): void

// Modales
modalTerminos$: Observable<boolean>
modalPrivacidad$: Observable<boolean>
modalSoporte$: Observable<boolean>
abrirModalTerminos(): void
cerrarModalTerminos(): void
abrirModalPrivacidad(): void
cerrarModalPrivacidad(): void
abrirModalSoporte(): void
cerrarModalSoporte(): void

// Utilidades
cerrarTodosLosSubmenus(): void
cerrarTodosLosModales(): void
resetearEstado(): void
```

---

### 2. SidebarComponent
**Ubicación:** `src/app/shared/components/sidebar/`

**Archivos:**
- `sidebar.component.ts` - Lógica del componente
- `sidebar.component.html` - Template del sidebar
- `sidebar.component.scss` - Estilos del sidebar

**Características:**
- Navegación completa del sistema
- Submenús colapsables (Usuarios, Reparaciones, Stock)
- Footer con links a modales
- Control de permisos (el menú de Usuarios solo aparece para rol ADMINISTRACION)
- Overlay para cerrar en mobile
- Totalmente reactivo usando Observables

**Selector:** `<app-sidebar></app-sidebar>`

---

### 3. ModalInfoComponent
**Ubicación:** `src/app/shared/components/modal-info/`

**Archivos:**
- `modal-info.component.ts` - Lógica del componente
- `modal-info.component.html` - Template de los 3 modales
- `modal-info.component.scss` - Estilos de modales

**Modales incluidos:**
1. **Términos y Condiciones** - Políticas de uso
2. **Política de Privacidad** - GDPR, datos personales
3. **Soporte Técnico** - Contacto, FAQs

**Selector:** `<app-modal-info></app-modal-info>`

---

## 🚀 Cómo Usar en Componentes Existentes

### Opción 1: Layout Component (Recomendado para el futuro)

Crear un componente `LayoutComponent` que envuelva todas las páginas:

```typescript
// layout.component.html
<header class="navbar">
  <button class="menu-btn" (click)="toggleSidebar()">
    <i class="bi bi-list"></i>
  </button>
  <div class="navbar-title">
    <img src="assets/asistente/logo.png" class="logo-navbar me-2" />
    <span class="brand-text">Obra<span class="highlight">Smart</span></span>
  </div>
  <div class="navbar-actions">
    <!-- Botones de navbar -->
  </div>
</header>

<app-sidebar></app-sidebar>
<app-modal-info></app-modal-info>

<main class="content">
  <router-outlet></router-outlet>
</main>
```

```typescript
// layout.component.ts
import { Component } from '@angular/core';
import { SidebarService } from '../service/sidebar.service';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent {
  constructor(private sidebarService: SidebarService) {}

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }
}
```

---

### Opción 2: Refactorizar Componentes Existentes

#### ANTES (PrincipalComponent):
```typescript
export class PrincipalComponent {
  menuAbierto = false;
  submenuUsuariosOpen = false;
  submenuReparacionOpen = false;
  submenuStockOpen = false;
  modalTerminos = false;
  modalPrivacidad = false;
  modalSoporte = false;

  toggleMenu() {
    this.menuAbierto = !this.menuAbierto;
  }

  toggleUsuarios(event: Event) {
    event.preventDefault();
    this.submenuUsuariosOpen = !this.submenuUsuariosOpen;
  }

  // ... más métodos duplicados
}
```

**HTML con 200+ líneas de sidebar + modales hardcodeados**

---

#### DESPUÉS (Refactorizado):

**TypeScript:**
```typescript
import { Component, OnInit } from '@angular/core';
import { SidebarService } from '../../service/sidebar.service';

export class PrincipalComponent implements OnInit {
  // ✅ Ya no necesitas estas variables locales
  // menuAbierto, submenuUsuariosOpen, etc.

  constructor(private sidebarService: SidebarService) {}

  ngOnInit(): void {
    // Tu lógica de inicialización
  }

  toggleMenu(): void {
    this.sidebarService.toggleSidebar();
  }
}
```

**HTML:**
```html
<div class="dashboard">
  <header class="navbar">
    <button class="menu-btn" (click)="toggleMenu()">
      <i class="bi bi-list"></i>
    </button>
    <div class="navbar-title">
      <img src="assets/asistente/logo.png" class="logo-navbar me-2" />
      <span class="brand-text">Obra<span class="highlight">Smart</span></span>
    </div>
    <div class="navbar-actions">
      <!-- Botones de navbar -->
    </div>
  </header>

  <!-- ✅ Sidebar compartido -->
  <app-sidebar></app-sidebar>

  <!-- ✅ Modales compartidos -->
  <app-modal-info></app-modal-info>

  <!-- Tu contenido específico -->
  <div class="map-container">
    <div id="map"></div>
  </div>

  <footer class="footer-bar">
    <span class="version">v1.0.0</span>
  </footer>
</div>
```

**SCSS:**
- Eliminar los estilos de `.sidebar`, `.sidebar-overlay`, `.modal-overlay`, etc.
- Mantener solo estilos específicos del componente (`.navbar`, `.map-container`, etc.)

---

## 📝 Pasos para Refactorizar Cada Componente

### 1. PrincipalComponent
```bash
✅ ARCHIVOS A MODIFICAR:
- src/app/pages/principal/principal.component.ts
- src/app/pages/principal/principal.component.html
- src/app/pages/principal/principal.component.scss
```

**Cambios:**
1. **TS:** Eliminar todas las propiedades y métodos del sidebar/modales
2. **TS:** Inyectar `SidebarService` en constructor
3. **TS:** Cambiar `toggleMenu()` para llamar a `this.sidebarService.toggleSidebar()`
4. **HTML:** Reemplazar todo el sidebar (líneas 26-175) con `<app-sidebar></app-sidebar>`
5. **HTML:** Reemplazar todos los modales (líneas 193-426) con `<app-modal-info></app-modal-info>`
6. **SCSS:** Eliminar estilos de sidebar y modales (líneas 195-757)

---

### 2. ListadoReparacionesComponent
```bash
✅ ARCHIVOS A MODIFICAR:
- src/app/pages/reparaciones/listado-reparaciones/listado-reparaciones.component.ts
- src/app/pages/reparaciones/listado-reparaciones/listado-reparaciones.component.html
- src/app/pages/reparaciones/listado-reparaciones/listado-reparaciones.component.scss
```

**Mismo proceso que PrincipalComponent**

---

### 3. ReportesComponent
```bash
✅ ARCHIVOS A MODIFICAR:
- src/app/pages/reportes/components/reporte.component.ts
- src/app/pages/reportes/components/reporte.component.html
- src/app/pages/reportes/components/reporte.component.scss
```

**Mismo proceso que PrincipalComponent**

---

## 🎨 Estilos que SE DEBEN MANTENER en componentes individuales

```scss
// ✅ MANTENER - Estilos específicos del componente
.dashboard { }
.navbar { }
.navbar-title { }
.menu-btn { }
.navbar-actions { }
.nav-icon-btn { }
.user-menu { }
.main-content { }
.footer-bar { }

// Y cualquier otro estilo específico de ese componente
```

## 🗑️ Estilos que SE DEBEN ELIMINAR (ya están en shared)

```scss
// ❌ ELIMINAR - Ahora están en SidebarComponent
.sidebar { }
.sidebar.open { }
.sidebar-header { }
.sidebar ul { }
.sidebar-menu { }
.sidebar .nav-item { }
.sidebar .nav-link { }
.toggle-icon { }
.submenu { }
.sidebar-footer { }
.sidebar-overlay { }

// ❌ ELIMINAR - Ahora están en ModalInfoComponent
.modal-overlay { }
.modal-content { }
.modal-header { }
.modal-body { }
.modal-footer { }
```

---

## 🔄 Ventajas de esta Refactorización

### ✅ Mantenibilidad
- Un solo lugar para modificar el sidebar
- Cambios se reflejan automáticamente en todos los componentes

### ✅ Consistencia
- Mismo comportamiento en toda la app
- Mismos estilos y animaciones

### ✅ Performance
- Código compartido = bundle size más pequeño
- Servicio singleton = un solo estado compartido

### ✅ Escalabilidad
- Fácil agregar nuevas secciones al sidebar
- Fácil agregar nuevos modales

### ✅ Testing
- Un solo componente para testear
- Un solo servicio para testear

---

## 📦 Módulos que Deben Importar SharedModule

Asegurarse que estos módulos importen `SharedModule`:

```typescript
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,  // ✅ Importar aquí
    // ... otros imports
  ]
})
export class ReparacionesModule { }
```

**Módulos que necesitan importar SharedModule:**
- `PagesModule` (si existe)
- `ReparacionesModule`
- `ReportesModule`
- `StockModule`
- Cualquier módulo que use `<app-sidebar>` o `<app-modal-info>`

---

## 🐛 Troubleshooting

### Error: "Can't bind to 'routerLink' since it isn't a known property"
**Solución:** Asegurarse que `RouterModule` está importado en `SharedModule`

### Error: "Property 'menuAbierto$' does not exist on type 'SidebarComponent'"
**Solución:** Verificar que `SidebarService` esté correctamente inyectado

### El sidebar no se abre
**Solución:** Verificar que el botón llame a `sidebarService.toggleSidebar()` y no a una variable local

### Los estilos del sidebar se ven mal
**Solución:** Asegurarse de que los estilos del navbar (`.navbar`, `.menu-btn`) estén en el componente padre, no en SidebarComponent

---

## 📚 Próximos Pasos Recomendados

1. ✅ **Crear LayoutComponent** - Envolver toda la app con un layout único
2. ✅ **Refactorizar routing** - Usar children routes con el layout
3. ✅ **Agregar tests** - Unit tests para SidebarService y componentes
4. ✅ **Mejorar navbar** - Convertir navbar también en componente compartido
5. ✅ **Estado de usuario** - Mover lógica de usuario al SidebarService

---

## 📖 Ejemplo Completo de Uso

```typescript
// app-routing.module.ts (FUTURO)
const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,  // Layout con sidebar
    children: [
      { path: 'principal', component: PrincipalComponent },
      { path: 'reparaciones', loadChildren: () => import('./pages/reparaciones/reparaciones.module').then(m => m.ReparacionesModule) },
      { path: 'reportes', loadChildren: () => import('./pages/reportes/reportes.module').then(m => m.ReportesModule) },
      // ... más rutas
    ]
  },
  {
    path: 'auth',  // Sin layout (sin sidebar)
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'reset-password', component: ResetPasswordComponent }
    ]
  }
];
```

---

## ✨ Resumen

Esta refactorización elimina **~500 líneas de código duplicado** y centraliza la lógica del sidebar y modales en componentes reutilizables, siguiendo las mejores prácticas de Angular y arquitectura de software.

**Tiempo estimado de refactorización por componente:** 15-20 minutos
**Componentes a refactorizar:** 3 (Principal, ListadoReparaciones, Reportes)
**Tiempo total:** ~1 hora

**Beneficio:** Mantenimiento infinitamente más fácil y codebase más limpia.
