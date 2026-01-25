import { Component, OnInit } from '@angular/core';
import { AuthService } from './service/auth.service';
import { ThemeService } from './service/theme.service';
import { LanguageService } from './service/language.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'frontend-obrasmart';
  private readonly THEME_KEY = 'obrasmart_ui_theme';
  private readonly LANG_KEY = 'obrasmart_lang';

  constructor(private auth: AuthService, private themeService: ThemeService, private languageService: LanguageService) {
    this.themeService.init();
  }

  ngOnInit(): void {
    this.languageService.init();
    this.auth.loadMe().subscribe();
  }

}
