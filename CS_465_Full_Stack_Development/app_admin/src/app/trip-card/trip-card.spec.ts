import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TripCard } from './trip-card';

describe('TripCard', () => {
  let component: TripCard;
  let fixture: ComponentFixture<TripCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TripCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TripCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
