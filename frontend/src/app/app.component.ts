import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from './_services/token-storage.service';
import {DataService} from './_services/data.service';
import {MatIconRegistry} from '@angular/material/icon';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  private roles: string[];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username: string;
  currentUser: any;
  languages: string[];
  language: string;

  constructor(private tokenStorageService: TokenStorageService) {
    this.currentUser = this.tokenStorageService.getUser();
    this.languages = ['DE', 'RU', 'UA'];
    this.language = 'de';
    // this.matIconRegistry.addSvgIcon(
    //   `avatar`,
    //   this.domSanitizer.bypassSecurityTrustResourceUrl(`../assets/img/icons/avatar.svg`)
    // );

  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');

      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    window.location.href = '/login' +
      '';
  }

  setRole(admin: string) {

  }

  changeLanguage(language: string) {
    this.language = language.toLowerCase();
  }

  passLang(value: any) {

  }
}
