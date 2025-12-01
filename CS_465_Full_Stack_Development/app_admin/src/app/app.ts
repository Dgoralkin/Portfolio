// * * * * * * * * * * The TypeScript class for the root AppComponent * * * * * * * * * * 

import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './navbar/navbar';

// Pull in our TripListing Component from trip-listing.ts
// import { TripListing } from './trip-listing/trip-listing';

@Component({
  selector: 'app-root',
  standalone: true,
  // imports: [CommonModule, TripListing, RouterOutlet],
  imports: [CommonModule, RouterOutlet, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App {
  // Pass a string title to the app.html page
  protected readonly title = 'Travlr Getaways Admin!';
}
