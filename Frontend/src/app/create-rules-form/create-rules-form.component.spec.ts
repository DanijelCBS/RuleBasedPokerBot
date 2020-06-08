import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateRulesFormComponent } from './create-rules-form.component';

describe('CreateRulesFormComponent', () => {
  let component: CreateRulesFormComponent;
  let fixture: ComponentFixture<CreateRulesFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateRulesFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateRulesFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
