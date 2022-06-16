import {Injectable} from '@angular/core';
import {UserInfo} from 'os';
import {User} from '../models/user.model';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const CUR_LANG_KEY = 'cur-lang';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {


  constructor() {
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
    window.sessionStorage.removeItem(USER_KEY);
    this.saveCurLang(user.langs[0]);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public saveCurLang(lang: string) {
    console.log("SAVED" + lang);
    window.sessionStorage.removeItem(CUR_LANG_KEY);
    window.sessionStorage.setItem(CUR_LANG_KEY, (lang));

  }

  public getUser(): any {
    return JSON.parse(sessionStorage.getItem(USER_KEY));
  }

  public getCurLang(): any {
    return (sessionStorage.getItem(CUR_LANG_KEY));
  }
}
