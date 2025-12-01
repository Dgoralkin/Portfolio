import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
// Add the editTrip method to the app
import { Router } from '@angular/router';
import { Trip } from '../models/trip';

// Add the authenticator to show/hide the add trip button
import { Authentication } from '../services/authentication';

@Component({
  selector: 'app-trip-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './trip-card.html',
  styleUrl: './trip-card.css'
})

export class TripCardComponent implements OnInit {
  @Input('trip') trip: any;

  constructor(
    private router: Router,
    private authentication: Authentication
  ) {}

  ngOnInit(): void {
    
  }

  // Populate localStorage key:value pairs to fill the edit-trip form
  public editTrip(trip: Trip) {
    localStorage.removeItem('tripCode');
    localStorage.setItem('tripCode', trip.code);
    localStorage.removeItem('tripName');
    localStorage.setItem('tripName', trip.name);
    localStorage.removeItem('tripLength');
    localStorage.setItem('tripLength', trip.length);
    localStorage.removeItem('tripResort');
    localStorage.setItem('tripResort', trip.resort);
    localStorage.removeItem('tripPerPerson');
    localStorage.setItem('tripPerPerson', trip.perPerson);
    localStorage.removeItem('tripImage');
    localStorage.setItem('tripImage', trip.image);
    localStorage.removeItem('tripDescription');
    localStorage.setItem('tripDescription', trip.description);
    this.router.navigate(['edit-trip']);
  }

  onImageError(event: Event) {
    const element = event.target as HTMLImageElement;
    element.src = 'assets/images/logo.png'; // fallback image path
  }

  // Add logic to checkif a user is logged in.
  public isLoggedIn() {
    return this.authentication.isLoggedIn();
  }
}
