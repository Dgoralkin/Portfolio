import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { Router } from '@angular/router';
import { Authentication } from '../services/authentication';
import { User } from '../models/user';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register implements OnInit {

  public formError: string = '';
  credentials = { name: '', email: '', password: '' };

  constructor(
    private router: Router,
    private authentication: Authentication
  ) { }

  ngOnInit(): void {}

  public onRegisterSubmit(): void {
    this.formError = '';

    if (!this.credentials.name || !this.credentials.email || !this.credentials.password) {
      this.formError = 'All fields are required, please try again';
      return;
    }

    const newUser: User = {
      name: this.credentials.name,
      email: this.credentials.email
    };

    // Call the service — no internal subscribe here if service already handles it
    this.authentication.register(newUser, this.credentials.password);
  }

}
