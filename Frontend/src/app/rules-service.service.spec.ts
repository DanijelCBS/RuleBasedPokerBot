import { TestBed } from '@angular/core/testing';

import { RulesServiceService } from './rules-service.service';

describe('RulesServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RulesServiceService = TestBed.get(RulesServiceService);
    expect(service).toBeTruthy();
  });
});
