import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import './app/chartjs-config';



platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
