import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from "@angular/forms";
import { TripData } from '../services/trip-data';
import { Trip } from '../models/trip';

@Component({
  selector: 'app-edit-trip',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-trip.html',
  styleUrl: './edit-trip.css'
})

export class EditTrip implements OnInit {

  public editForm!: FormGroup;
  trip!: Trip;
  submitted = false;
  message : string = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private TripData: TripData
  ) {}

  ngOnInit() : void{
    // Retrieve stashed trip ID
    let tripCode = localStorage.getItem("tripCode");
    if (!tripCode) {
      alert("Something wrong, couldn't find where I stashed tripCode!");
      this.router.navigate(['']);
      return;
    }
    console.log('EditTripComponent::ngOnInit');
    // console.log('tripcode: ' + tripCode);

    this.editForm = this.formBuilder.group({
      _id: [],
      code: [tripCode,Validators.required],
      name: [localStorage.getItem("tripName"), Validators.required],
      length: [localStorage.getItem("tripLength"), Validators.required],
      start: ['', Validators.required],
      resort: [localStorage.getItem("tripResort"), Validators.required],
      perPerson: [localStorage.getItem("tripPerPerson"), Validators.required],
      image: [localStorage.getItem("tripImage"), Validators.required],
      description: [localStorage.getItem("tripDescription"), Validators.required]
    })

    this.TripData.getTrip(tripCode).subscribe({
      next: (value: any) => {
        this.trip = value;
        // Populate our record into the form
        this.editForm.patchValue(value[0]);
        if(!value){
          this.message = 'No Trip Retrieved!';
        } else {
          this.message = 'Trip: ' + tripCode + ' retrieved';
        }
        console.log(this.message);
      },
      error: (error: any) => {
        console.log('Error: ' + error);
      }
    })
  }

  get f() {
    return this.editForm.controls;
  }

  public onSubmit() {
    this.submitted = true;
    if(this.editForm.valid) {
      this.TripData.updateTrip(this.editForm.value).subscribe({
        next: (value: any) => {
          console.log(value);
          this.router.navigate(['']);
        },
        error: (error: any) => {
          console.log('Error: ' + error);
        }
      })
    }
  }
}