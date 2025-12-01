//

/**
 * We need to add the FormsModule to be able to process the HTML form in our template.
 * We need the Router module because we would like to redirect the user to another page after they login.
 * We need the AuthenticationService so that we can process the user login.
 * we need the User object so that we have a way of manipulating the User credentials.
 */
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { Router } from '@angular/router';
import { Authentication } from '../services/authentication';
import { User } from '../models/user';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})


export class Login {

  public formError: string = '';
  submitted = false;
  credentials = {
    name: '',
    email: '',
    password: ''
  }

  constructor(
    private router: Router,
    private Authentication: Authentication
  ) { }

  ngOnInit(): void { }

  public onLoginSubmit(): void {
    this.formError = '';
    if (!this.credentials.email || !this.credentials.password || !this.credentials.name) {
      this.formError = 'All fields are required, please try again';
      this.router.navigateByUrl('#'); // Return to login page
    } else {
      this.doLogin();
    }
  }

  private doLogin(): void {
    let newUser = {
      name: this.credentials.name,
      email: this.credentials.email
    } as User;

    console.log('LoginComponent::doLogin');
    // console.log(this.credentials);

    this.Authentication.login(newUser, this.credentials.password);

    if(this.Authentication.isLoggedIn()){
      // console.log('Router::Direct'); 
      this.router.navigate(['']);
    } else {
      var timer = setTimeout(() => {
        if(this.Authentication.isLoggedIn()){
          console.log('Router::Pause');
          this.router.navigate(['']);
        }
      },3000);
    }
  }

}
