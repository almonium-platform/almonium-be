import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {Observable} from 'rxjs';
import {FormControl} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {DataService} from '../_services/data.service';
import {map, startWith} from 'rxjs/operators';


@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrls: ['./discover.component.css']
})
export class DiscoverComponent implements OnInit, OnDestroy {

  @Input()
  searchText: string;
  oldValue: string;
  wordlist: string[] = ['aaaa', 'bbbb'];
  content: string;
  formControl = new FormControl();
  filteredOptions: Observable<string[]>;

  constructor(private userService: UserService, private dataService: DataService, private readonly http: HttpClient) {
  }

  // private mat_filter(value: string): string[] {
  //   const filterValue = value.toLowerCase().split(' ').pop();
  //   return this.wordlist.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  // }


  getReversoLink(): string {
    return 'https://context.reverso.net/translation/english-russian/' + this.searchText;
  }

  ngOnInit(): void {
    // this.dataService.getWordlist().then(r => {
    //   this.wordlist = r;
    // this.formControl.valueChanges.subscribe(() => {
    //   this.oldValue = this.searchText;
    // });
    //
    // this.filteredOptions = this.formControl.valueChanges.pipe(
    //   startWith(''),
    //   map(val => val.length >= 3 ? this.mat_filter(val) : [])
    // );
    // });
  }

  ngOnDestroy(): void {

  }

  // optionSelectedHandler(value: any) {
    // let before = this.oldValue.substr(0, this.oldValue.lastIndexOf(' ') + 1);
    // this.searchText = (before + ' ' + value).replace(/\s+/g, ' ').trim();
  // }

}
