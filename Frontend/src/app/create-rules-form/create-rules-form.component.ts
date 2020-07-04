import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatSnackBar} from '@angular/material';
import {Router} from '@angular/router';
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
  private preFlopShell = 'rule "pre-flop"\n\n' +
    '    when\n\n' +
    '    then\n\n' +
    'end';
  private postFlopShell = 'rule "post-flop"\n\n' +
    '    when\n\n' +
    '    then\n\n' +
    'end';
  public preFlopTooltip = 'IMPORTS\n\n import com.biotools.meerkat.Card;\n' +
    'import com.biotools.meerkat.Action;\n' +
    'import com.biotools.meerkat.GameInfo;\n' +
    'import bots.rulebasedbot.Utility;\n' +
    'import bots.rulebasedbot.PlayStyle;\n' +
    'import bots.rulebasedbot.Strategy;\n' +
    'import com.biotools.meerkat.Holdem;\n' +
    '\n\n' +
    'GLOBALS\n\n global Integer phase;\n' +
    'global Integer numOfPlayersToAct;\n' +
    'global GameInfo gameInfo;\n' +
    'FACTS - PlayerState;\n Thresholds';

  public postFlopTooltip = 'IMPORTS\n\n import com.biotools.meerkat.Card;\n' +
    'import com.biotools.meerkat.Action;\n' +
    'import com.biotools.meerkat.GameInfo;\n' +
    'import bots.rulebasedbot.Utility;\n' +
    'import bots.rulebasedbot.PlayStyle;\n' +
    'import bots.rulebasedbot.Strategy;\n' +
    'import com.biotools.meerkat.Holdem;\n' +
    'import bots.rulebasedbot.BettingEvent;\n' +
    'import bots.rulebasedbot.HandStrengthEnum;\n' +
    '\n' +
    'GLOBALS\n\n global Double make1PostFlopThreshold;\n' +
    'global Double make2PostFlopThreshold;\n' +
    'global Integer phase;\n' +
    'global GameInfo gameInfo;\n' +
    'FACTS - PlayerState;\n PostFlopParameters';

  constructor(private rulesService: RulesService, private formBuilder: FormBuilder, private snackBar: MatSnackBar,
              private router: Router) {

  }

  public ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.form = this.formBuilder.group({
      rules: this.formBuilder.group({
        alias: ['', [Validators.required, Validators.pattern('^[A-Z].*$')]],
        preFlopRules: [this.preFlopShell, [Validators.required]],
        postFlopRules: [this.postFlopShell, [Validators.required]],
      }),
    });
  }

  public createRules() {
    const rulesDTO: RulesDTO = this.form.controls.rules.value;
    if (rulesDTO.alias === 'rules') {
      this.snackBar.open('Rules alias can not be "rules"');
      return;
    }
    this.rulesService.createRules(rulesDTO).subscribe({
      next: (message: string) => {
        this.ngOnInit();
        this.snackBar.open(message, 'Dismiss', {
          duration: 3000,
        });
      },
      error: (object: object) => {
        this.handleError(object);
      },
    });
  }

  private handleError(object) {
    try {
      const errors = object.error.split('POST-FLOP');
      this.preFlopErrors = errors[0].substring(9);
      this.postFlopErrors = errors[1];
    } catch {
      this.snackBar.open(object.error, 'Dismiss', {
        duration: 3000,
      });
    }
  }
}
