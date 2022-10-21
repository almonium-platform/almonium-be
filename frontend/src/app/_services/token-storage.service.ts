import {Injectable} from '@angular/core';
import {User} from '../models/user.model';
import {Router} from "@angular/router";

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const CUR_LANG_KEY = 'cur-lang';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {


  constructor(private router: Router) {
  }

  signOut(): void {
    window.sessionStorage.clear();
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: User): void {
    console.log("SAVED USER ")
    console.log(user)
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    this.saveCurLang(user.targetLangs[0]);
  }

  public saveCurLang(lang: string) {
    console.log("SAVED lang");
    console.log(lang)
    window.sessionStorage.removeItem(CUR_LANG_KEY);
    window.sessionStorage.setItem(CUR_LANG_KEY, (lang));

  }

  public getUser(): User {
    return JSON.parse(sessionStorage.getItem(USER_KEY));
  }

  public getCurLang(): string {
    return (sessionStorage.getItem(CUR_LANG_KEY));
  }

  getBackground() {
    let user = this.getUser();
    let back = '4';
    if (!!user) {
      back = user.background;
    }
    if (this.router.url === '/profile')
      return '';
    else
      return `url('../assets/img/background/${back}.svg')`;
  }
}
