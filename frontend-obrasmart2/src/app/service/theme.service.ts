import { Injectable } from '@angular/core';

export type AppTheme =
  | 'obra-light'
  | 'obra-dark'
  | 'office-light'
  | 'office-dark'
  | 'windows11-light'
  | 'windows11-dark'
  | 'opera-gx';

export type ThemeMode = 'light' | 'dark';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly STORAGE_KEY = 'obrasmart_ui_theme';
  private readonly DEFAULT_THEME: AppTheme = 'obra-light';

  private currentTheme: AppTheme = this.DEFAULT_THEME;

  /** Llamar una sola vez al arrancar la app (AppComponent) */
  init(): void {
    const saved = this.getStoredTheme();
    this.apply(saved);
  }

  /** Aplica theme global (data-theme en body) y lo persiste */
  apply(theme: AppTheme | string): void {
    const target: AppTheme = this.isAppTheme(theme) ? theme : this.DEFAULT_THEME;

    this.currentTheme = target;

    // Aplicar al DOM (sin romper SSR)
    if (typeof document !== 'undefined') {
      document.body.setAttribute('data-theme', target);
    }

    this.saveTheme(target);
  }

  /** Alterna entre los dos themes base (obra-light/obra-dark) */
  toggleTheme(): void {
    const next: AppTheme = this.currentTheme === 'obra-dark' ? 'obra-light' : 'obra-dark';
    this.apply(next);
  }

  /** Retorna el theme actual (completo) */
  getTheme(): AppTheme {
    return this.currentTheme;
  }

  /** Retorna modo light/dark derivado (para charts/mapas/etc) */
  getMode(theme?: AppTheme | string): ThemeMode {
    const val = (theme ?? this.currentTheme) as string;
    return val.includes('dark') || val === 'opera-gx' ? 'dark' : 'light';
  }

  // ----------------- privados -----------------

  private saveTheme(theme: AppTheme): void {
    try {
      localStorage.setItem(this.STORAGE_KEY, theme);
    } catch {
      // ignore
    }
  }

  private getStoredTheme(): AppTheme {
    try {
      const stored = localStorage.getItem(this.STORAGE_KEY);
      if (stored && this.isAppTheme(stored)) return stored;
    } catch {
      // ignore
    }
    return this.DEFAULT_THEME;
  }

  private isAppTheme(val: any): val is AppTheme {
    return (
      val === 'obra-light' ||
      val === 'obra-dark' ||
      val === 'office-light' ||
      val === 'office-dark' ||
      val === 'windows11-light' ||
      val === 'windows11-dark' ||
      val === 'opera-gx'
    );
  }
}
