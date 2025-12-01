import { TestBed } from '@angular/core/testing';

import { TripData } from './trip-data';

describe('TripData', () => {
  let service: TripData;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TripData);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
