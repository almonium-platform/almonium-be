import {AfterViewInit, Component, Directive, ElementRef, Input, OnDestroy, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {Observable} from 'rxjs';
import {FormControl} from '@angular/forms';
import {DataService} from '../_services/data.service';
import {map, startWith} from 'rxjs/operators';
import {SearchService} from '../_services/search.service';
import {FDEntry} from '../models/fd.model';
import {EntryInfo} from '../models/entry.model';


@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrls: ['./discover.component.css']
})
export class DiscoverComponent implements OnInit, OnDestroy {

  @Input()
  searchText: string;
  oldValue: string;
  searched: boolean;
  wordlist: string[] = ['aaaa', 'bbbb'];
  content: string;
  fdEntries: FDEntry[];
  formControl = new FormControl();
  filteredOptions: Observable<string[]>;
  entryInfo: EntryInfo;
  creationInvoked: boolean;

  constructor(private userService: UserService,
              private dataService: DataService,
              private searchService: SearchService
  ) {
  }

  private filterValues(value: string): string[] {
    const filterValue = value.toLowerCase().split(' ').pop();
    return this.wordlist.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }


  getReversoLink(): string {
    return 'https://context.reverso.net/translation/english-russian/' + this.searchText;
  }

  ngOnInit(): void {
    this.dataService.getWordlist().then(r => {
      this.wordlist = r;
      this.formControl.valueChanges.subscribe(() => {
        this.oldValue = this.searchText;
      });


      this.filteredOptions = this.formControl.valueChanges.pipe(
        startWith(''),
        map(val => val.split(' ').pop().length >= 3 ? this.filterValues(val) : [])
      );
    });
  }

  ngOnDestroy(): void {
    // this.filteredOptions.
  }

  invokeCreation(): void {
    this.creationInvoked = true;
  }

  optionSelectedHandler(value: any) {
    let before = this.oldValue.substr(0, this.oldValue.lastIndexOf(' ') + 1);
    this.searchText = (before + ' ' + value).replace(/\s+/g, ' ').trim();
  }

  search() {
    console.log(this.searchText);
    this.searched = true;
    let eInfo: EntryInfo = {entry: this.searchText, frequency: 0.5, type: 'noun'};
    this.entryInfo = eInfo;
    console.log('SETTED ' + this.entryInfo);

    this.searchService.fdSearch(this.searchText).subscribe(data => {
      this.fdEntries = data.body;
      console.log(data.status);
      console.log(data.body);
      console.log(this.fdEntries[0].phonetics[0].audio);
      this.searched = true;
    }, error => {
      console.log((error.status));
    });
  }

  urban() {
    console.log('URBAN');
  }
}

@Directive({
  selector: 'input[appFocus]',
})
export class FocusOnShowDirective implements AfterViewInit {
  @Input('appFocus')
  private focused: boolean = false;

  constructor(public element: ElementRef<HTMLElement>) {
  }

  ngAfterViewInit(): void {
    // ExpressionChangedAfterItHasBeenCheckedError: Expression has changed after it was checked.
    if (this.focused) {
      setTimeout(() => this.element.nativeElement.focus(), 0);
    }
  }
}
