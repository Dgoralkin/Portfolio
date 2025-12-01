// This is Angular’s routing service file to enable routing across our entire application.

import { Routes } from '@angular/router';

// Import the required controllers from thier component files
import { AddTrip } from './add-trip/add-trip';                  // AddTrip controller
import { TripListing } from './trip-listing/trip-listing';      // TripListing controller
import { EditTrip } from './edit-trip/edit-trip';               // EditTrip controller
import { Login } from './login/login';                          // Login controller
import { Register } from './register/register';

// Activate each of the components separately.
export const routes: Routes = [
    { path : 'add-trip', component: AddTrip },                  // Enable path to add AddTrip
    { path : 'edit-trip', component: EditTrip },                // Enable path to AddTrip
    { path : '', component: TripListing, pathMatch: 'full' },   // path to admin's main page
    { path : 'login', component: Login },                       // path to Login page
    { path : 'register', component: Register }                  // path to register page
];
