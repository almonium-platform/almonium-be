import {AfterViewInit, Component, Directive, ElementRef, Input, OnDestroy, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {Observable} from 'rxjs';
import {FormControl} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../app.component';
import {DataService} from '../_services/data.service';
import {map, startWith} from 'rxjs/operators';
import {SearchService} from '../_services/search.service';


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

  optionSelectedHandler(value: any) {
    let before = this.oldValue.substr(0, this.oldValue.lastIndexOf(' ') + 1);
    this.searchText = (before + ' ' + value).replace(/\s+/g, ' ').trim();
  }

  search() {
    console.log(this.searchText);
    this.searchService.search(this.searchText).subscribe(data => {
      console.log(data + "FF");
    }, error => {

    });
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
