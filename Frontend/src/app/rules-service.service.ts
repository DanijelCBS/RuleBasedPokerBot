import {Injectable} from '@angular/core';
import {RulesDTO} from './model/rules-dto-model';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class RulesService {

  constructor(private http: HttpClient) {
  }

  public createRules(rulesDTO: RulesDTO) {
    return this.http.post('http://localhost:8080/rules', rulesDTO, {responseType: 'text'});
  }
}
