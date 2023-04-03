import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {ProfileComponent} from './profile/profile.component';
import {DialogEntryComponent, DiscoverComponent} from './discover/discover.component';
import {SettingsComponent} from './settings/settings.component';
import {GamesComponent} from './games/games.component';
import {LogoutGuard} from './_helpers/logout.guard';

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {
    path: 'discover', component: DiscoverComponent,
    children: [
      {
        path: 'dialog/:id',
        component: DialogEntryComponent,
      }
    ]
  },
  {path: 'login', component: LoginComponent},
  {path: 'games', component: GamesComponent},
  {path: 'settings', component: SettingsComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'logout', canActivate: [LogoutGuard], component: LoginComponent},
  {path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {relativeLinkResolution: 'legacy'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
