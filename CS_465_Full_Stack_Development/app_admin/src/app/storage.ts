// This is the storage class to hold the JWT in the BROWSER_STORAGE key

import { InjectionToken } from '@angular/core';

// BROWSER_STORAGE will be the token that is utilized when pulling the local 
// storage capabilities into other components in our application
export const BROWSER_STORAGE = new InjectionToken<Storage>('Browser Storage', {
    providedIn: 'root', factory: () => localStorage
});


export class Storage {
}
