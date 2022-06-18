import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {ProfileComponent} from './profile/profile.component';
import {DiscoverComponent} from './discover/discover.component';
import {SettingsComponent} from './settings/settings.component';
import {GamesComponent} from './games/games.component';

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'discover', component: DiscoverComponent},
  {path: 'login', component: LoginComponent},
  {path: 'games', component: GamesComponent},
  {path: 'settings', component: SettingsComponent},
  {path: 'profile', component: ProfileComponent},
  {path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {relativeLinkResolution: 'legacy'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
