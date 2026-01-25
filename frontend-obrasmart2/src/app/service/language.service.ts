import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

type Lang = 'es' | 'en';

@Injectable({ providedIn: 'root' })
export class LanguageService {
  private readonly KEY = 'obrasmart_ui_lang';

  constructor(private translate: TranslateService) {}

  init(): void {
    const saved = (localStorage.getItem(this.KEY) as Lang | null) ?? 'es';
    this.translate.setDefaultLang('es');
    this.translate.use(saved);
  }

  setLang(lang: Lang): void {
    localStorage.setItem(this.KEY, lang);
    this.translate.use(lang);
  }

  getLang(): Lang {
    return (this.translate.currentLang as Lang) || ((localStorage.getItem(this.KEY) as Lang) ?? 'es');
  }
}
