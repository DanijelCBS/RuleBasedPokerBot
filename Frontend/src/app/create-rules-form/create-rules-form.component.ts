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
        console.log("pozdrav");
        console.log(message);
      },
      error: (message: string) => {
        console.log("ozdrav");
        console.log(message);
      },
    });
  }
}
