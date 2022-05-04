import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  wordlist: string[];
  private wordlistOble: BehaviorSubject<string[]>;

  constructor() {
    console.log('data service constructor');
    this.getWordlist().then(r => {
      this.wordlist = r;
      this.wordlistOble = new BehaviorSubject<string[]>(r);
    });
  }

  async getWordlist(): Promise<string[]> {
    return fetch('assets/txt/wordlist.txt')
      .then(response => response.text())
      .then(data => {
        console.log('data wordlist updated');
        return data.toString().replace(/\r\n/g, '').split('\n');
      });
  }

  getValue(): Observable<string[]> {
    return this.wordlistOble.asObservable();
  }

  setValue(newValue): void {
    this.wordlistOble.next(newValue);
  }

}
