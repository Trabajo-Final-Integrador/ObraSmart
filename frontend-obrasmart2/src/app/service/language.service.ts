import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

type Lang = 'es' | 'en';

@Injectable({ providedIn: 'root' })
export class LanguageService {
  private readonly KEY = 'lang';
  private readonly LEGACY_KEYS = ['obrasmart_ui_lang', 'obrasmart_lang'];

  constructor(private translate: TranslateService) {}

  init(): void {
    const saved =
      (localStorage.getItem(this.KEY) as Lang | null) ||
      (this.LEGACY_KEYS.map((k) => localStorage.getItem(k)).find(Boolean) as Lang | null) ||
      'es';
    this.translate.setDefaultLang('es');
    this.translate.use(saved);
    localStorage.setItem(this.KEY, saved);
  }

  setLang(lang: Lang): void {
    localStorage.setItem(this.KEY, lang);
    this.LEGACY_KEYS.forEach((k) => localStorage.setItem(k, lang));
    this.translate.use(lang);
  }

  getLang(): Lang {
    return (this.translate.currentLang as Lang) || ((localStorage.getItem(this.KEY) as Lang) ?? 'es');
  }
}
