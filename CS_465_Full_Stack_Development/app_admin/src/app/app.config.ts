// * * * * * * * * * * Configuration settings for the Angular application * * * * * * * * * * 
import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

// Allow the Angular environment to communicate via HTTP
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

import { routes } from './app.routes';
import { authInterceptProvider } from './utils/jwt-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    // apply interceptors from DI
    provideHttpClient(withInterceptorsFromDi()),
    // register the JWT interceptor
    authInterceptProvider
  ]
};
