import { Injectable, Provider } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Authentication } from '../services/authentication';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authentication: Authentication) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const url = request.url.toLowerCase();

    // Do not attach token to login or register requests
    const isAuthAPI = url.includes('/login') || url.includes('/register');

    if (this.authentication.isLoggedIn() && !isAuthAPI) {
      const token = this.authentication.getToken();

      if (token) {
        console.log('Attaching JWT token:', token);

        // Clone the request and add the Authorization header
        request = request.clone({
          setHeaders: { Authorization: `Bearer ${token}` }
        });
      }
    }

    return next.handle(request);
  }
}

// Provide this interceptor in Angular
export const authInterceptProvider: Provider = {
  provide: HTTP_INTERCEPTORS,
  useClass: JwtInterceptor,
  multi: true
};
