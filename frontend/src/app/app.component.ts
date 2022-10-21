import {Component, HostListener, OnInit} from '@angular/core';
import {TokenStorageService} from './_services/token-storage.service';
import {DataService} from './_services/data.service';
import {User} from "./models/user.model";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  private roles: string[];
  isLoggedIn = false;
  showTestLabel = false;
  username: string;
  color: string;
  backgroundImgUrl: string = `url('../assets/img/background/4.svg')`;
  user: User;
  languages: string[] = [];
  ui_langs: string[] = ['UK', 'RU', 'EN'];
  language: string = '';
  ui_lang: string = 'EN';

  constructor(private tokenStorageService: TokenStorageService,
              private dataService: DataService,
  ) {
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

    if (this.isLoggedIn) {
      this.user = this.tokenStorageService.getUser();
      this.backgroundImgUrl = `url('../assets/img/background/${this.user.background}.svg')`
      this.ui_lang = this.user.uiLang || 'EN';
      this.languages = this.user.targetLangs;
      this.language = this.tokenStorageService.getCurLang();
      this.color = this.langBtnColor();
      this.roles = this.user.roles;
      this.username = this.user.username;
      console.log(this.backgroundImgUrl)
    }
  }

  getBackgroundImg() {
    return this.tokenStorageService.getBackground();
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
    this.color = this.langBtnColor();
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
