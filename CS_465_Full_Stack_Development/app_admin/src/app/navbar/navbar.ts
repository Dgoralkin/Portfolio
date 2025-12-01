//
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Authentication } from '../services/authentication';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})


export class Navbar implements OnInit {
  constructor(
    private Authentication: Authentication 
  ) { }

  ngOnInit() { }

  public isLoggedIn(): boolean {
    return this.Authentication.isLoggedIn();
  }

  public onLogout(): void {
    return this.Authentication.logout(); 
  }
}
