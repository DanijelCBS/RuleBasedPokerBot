import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CreateRulesFormComponent} from './create-rules-form/create-rules-form.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: CreateRulesFormComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
