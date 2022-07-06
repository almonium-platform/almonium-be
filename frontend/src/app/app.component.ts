import {Component, HostListener, OnInit} from '@angular/core';
import {TokenStorageService} from './_services/token-storage.service';
import {DataService} from './_services/data.service';

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
  languages: string[] = [];
  ui_langs: string[] = ['UK', 'RU', 'EN'];
  language: string = '';
  ui_lang: string = 'EN';

  constructor(private tokenStorageService: TokenStorageService,
              private dataService: DataService) {
    this.currentUser = this.tokenStorageService.getUser();
  }

  ngOnInit(): void {
    console.log(this.tokenStorageService.getCurLang());
    this.isLoggedIn = !!this.tokenStorageService.getToken();
    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.ui_lang = user.ui_lang || 'EN';
      this.languages = user.targetLangs;
      this.language = this.tokenStorageService.getCurLang();

      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');

      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    this.dataService.deleteUserInfo();
    window.location.href = '/login';
  }

  changeLearningLanguage(language: string) {
    this.language = language.toLowerCase();
    this.tokenStorageService.saveCurLang(this.language);
  }

  changeUiLanguage(value: any) {
    console.log('HOHO' + value);
  }

  @HostListener('window:keydown.Alt.a', ['$event'])
  onKeyDownAltA(e) {
    e.preventDefault();
    if (this.isLoggedIn) {
      let currentIndex = this.languages.indexOf(this.language);
      const nextIndex = ++currentIndex % this.languages.length;
      this.language = this.languages[nextIndex];
      this.tokenStorageService.saveCurLang(this.language);
    }
  }

}
