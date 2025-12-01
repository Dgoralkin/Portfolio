import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TripListing } from './trip-listing';

describe('TripListing', () => {
  let component: TripListing;
  let fixture: ComponentFixture<TripListing>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TripListing]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TripListing);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
