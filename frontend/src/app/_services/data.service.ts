import {Injectable} from '@angular/core';
import {User} from '../models/user.model';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  _wordlist: string[];
  private _current_language: string;
  _user: User;
  private userInfoKey = 'userInfo';
  private curLangKey = 'curLang';
  private wordlistKey = 'wordlist';

  get currentLang(): string {
    if (!this._current_language) {
      return JSON.parse(localStorage.getItem(this.curLangKey));
    }
    return this._current_language;
  }

  // get user() {
  //   if (!this._user) {
  //     try {
  //       let user = JSON.parse(localStorage.getItem(this.userInfoKey));
  //       if (!user) {
  //         this.saveUserInfo();
  //       } else {
  //         this._user = user;
  //       }
  //     } catch (e) {
  //       this.saveUserInfo();
  //     }
  //   }
  //   return this._user;
  // }


  set user(user) {
    this._user = user;
  }

  set currentLang(language: string) {
    this._current_language = language;

  }

  constructor(private userService: UserService) {
    this.requestWordlist().then(r => {
      this._wordlist = r;
    });
  }

  async requestWordlist(): Promise<string[]> {
    return fetch('assets/txt/wordlist.txt')
      .then(response => response.text())
      .then(data => {
        let result = data.toString().replace(/\r\n/g, '').split('\n');
        this._wordlist = result;
        console.log('REQUESTED WORDLIST');//storage limit hit
        // localStorage.setItem(this.wordlistKey, JSON.stringify(result));
        return result;
      });
  }

  get wordlist() {
    if (!this._wordlist) {
      try {
        let wordlist = JSON.parse(localStorage.getItem(this.wordlistKey));
        if (!wordlist) {
          this.requestWordlist().then(r => {
              return r;
            }
          );
        } else {
          console.log('WORDLIST RESTORED FROM LOCAL STORAGE');
        }
      } catch (e) {
        this.requestWordlist().then(r => {
            return r;
          }
        );
      }
    }
    return this._wordlist;
  }

  // saveUserInfo() {
  //   this.userService.getMe().subscribe(data => {
  //     this._user = data;
  //     console.log(this._user);
  //     localStorage.removeItem(this.userInfoKey);
  //     localStorage.setItem(this.userInfoKey, JSON.stringify(data));
  //   }, error => {
  //     console.log(error);
  //   });
  // }

  saveCurrentLang(language: string) {
    localStorage.removeItem(this.curLangKey);
    localStorage.setItem(this.curLangKey, JSON.stringify(language));
  }

  deleteUserInfo() {
    localStorage.removeItem(this.userInfoKey);
  }
}
