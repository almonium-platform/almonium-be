import {Injectable} from '@angular/core';

function load_words(): string[] {
  let wordlist = [];
  fetch('assets/txt/wordlist.txt')
    .then(response => response.text())
    .then(data => {
      wordlist = data.toString().replace(/\r\n/g, '').split('\n');
    });
  return wordlist;
}

@Injectable()
export class Globals {
  wordlist: string[] = load_words();
}
