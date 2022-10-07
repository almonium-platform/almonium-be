import {Component, HostListener, OnInit} from '@angular/core';
import {TokenStorageService} from './_services/token-storage.service';
import {DataService} from './_services/data.service';
import {UserService} from "./_services/user.service";
import {AuthService} from "./_services/auth.service";
import {User} from "./models/user.model";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  private roles: string[];
  isLoggedIn = false;
  showAdminBoard = false;
  showTestLabel = false;
  showModeratorBoard = false;
  username: string;
  color: string;
  currentUser: User;
  languages: string[] = [];
  ui_langs: string[] = ['UK', 'RU', 'EN'];
  language: string = '';
  ui_lang: string = 'EN';

  constructor(private tokenStorageService: TokenStorageService,
              private dataService: DataService,
  ) {
    this.currentUser = this.tokenStorageService.getUser();
  }

  testEnvDisclaimer() {
    this.dataService.getProfile().subscribe(data => {
      if (data.includes('test')) {
        this.showTestLabel = true;
      }
    }, error => {
      console.log("couldn't get profile")
    });
  }

  ngOnInit(): void {
    this.testEnvDisclaimer();
    this.isLoggedIn = !!this.tokenStorageService.getUser();
    console.log(this.tokenStorageService.getUser())

    if (this.isLoggedIn) {
      const user: User = this.tokenStorageService.getUser();
      this.ui_lang = user.uiLang || 'EN';
      this.languages = user.targetLangs;
      this.language = this.tokenStorageService.getCurLang();

      this.roles = user.roles;

      this.username = user.username;
    }
  }

  connect() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.showPosition)
    } else {
      // I believe it may also mean geolocation isn't supported
      alert('Geolocation denied')
    }
  }

  showPosition(position) {
    alert(`${position.coords.longitude} - ${position.coords.latitude}`)
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
      this.color = this.langBtnColor();
      this.tokenStorageService.saveCurLang(this.language);
    }
  }

  langBtnColor() {
    console.log(this.language)
    if (this.language.toLowerCase() === 'en')
      return "#252552";
    if (this.language.toLowerCase() === 'de')
      return "#561015";
  }
}
