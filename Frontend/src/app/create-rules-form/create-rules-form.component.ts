import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {RulesDTO} from '../model/rules-dto-model';
import {RulesService} from '../rules-service.service';

@Component({
  selector: 'app-create-rules-form',
  templateUrl: './create-rules-form.component.html',
  styleUrls: ['./create-rules-form.component.css'],
})
export class CreateRulesFormComponent implements OnInit {
  public form: FormGroup;
  public preFlopErrors = 'No errors';
  public postFlopErrors = 'No errors';

  constructor(private rulesService: RulesService, private formBuilder: FormBuilder) {

  }

  public ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.form = this.formBuilder.group({
      rules: this.formBuilder.group({
        alias: [''],
        preFlopRules: [''],
        postFlopRules: [''],
      }),
    });
  }

  public createRules() {
    const rulesDTO: RulesDTO = this.form.controls.rules.value;
    this.rulesService.createRules(rulesDTO).subscribe({
      next: (message: string) => {
        console.log(message);
      },
      error: (object: object) => {
        this.handleError(object);
      },
    });
  }

  private handleError(object) {
    const errors = object.error.split('POST-FLOP');
    this.preFlopErrors = errors[0].substring(9);
    this.postFlopErrors = errors[1];
  }
}
