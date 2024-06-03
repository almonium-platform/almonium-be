import {BrowserModule} from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {AppComponent} from './app.component';
import {LoginComponent, TrackCapsDirective} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {authInterceptorProviders} from './_helpers/auth.interceptor';
import {
  CardCreationDialog,
  DialogEntryComponent,
  DiscoverComponent,
  FocusOnShowDirective
} from './discover/discover.component';
import {SettingsComponent} from './settings/settings.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatListModule} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatTabsModule} from '@angular/material/tabs';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatTableModule} from '@angular/material/table';
import {AlertComponent} from './alert/alert.component';
import {MatRadioModule} from '@angular/material/radio';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {MatChipsModule} from '@angular/material/chips';
import {GamesComponent} from './games/games.component';
import {MatSliderModule} from '@angular/material/slider';
import {AboutComponent} from './about/about.component';
import {CardComponent, CardDeletionConfirmationDialog} from './card/card.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {ClipboardModule} from '@angular/cdk/clipboard';
import {AccountDeletionConfirmationDialog, ProfileComponent} from './profile/profile.component';
import {CardDialog} from './_services/card.service';
import {TileComponent} from './tile/tile.component';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {EmailVerificationComponent} from './email-verification/email-verification.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    EmailVerificationComponent,
    CardDialog,
    ProfileComponent,
    TrackCapsDirective,
    FocusOnShowDirective,
    DiscoverComponent,
    SettingsComponent,
    AlertComponent,
    CardCreationDialog,
    CardComponent,
    DialogEntryComponent,
    AccountDeletionConfirmationDialog,
    CardDeletionConfirmationDialog,
    GamesComponent,
    AboutComponent,
    TileComponent,
    EmailVerificationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MatSnackBarModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatListModule,
    MatMenuModule,
    MatTooltipModule,
    MatTabsModule,
    MatDialogModule,
    MatCheckboxModule,
    MatTableModule,
    MatRadioModule,
    MatSidenavModule,
    MatExpansionModule,
    MatSlideToggleModule,
    MatChipsModule,
    MatSliderModule,
    ClipboardModule,
    DragDropModule
  ],
  providers: [
    authInterceptorProviders,
    {
      provide: LOCALE_ID,
      useValue: 'en',

    },
    {
      provide: MatDialogRef,
      useValue: {}
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
