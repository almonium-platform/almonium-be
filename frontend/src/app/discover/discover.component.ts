import {AfterViewInit, Component, Directive, ElementRef, Inject, Input, OnDestroy, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {Observable} from 'rxjs';
import {FormControl} from '@angular/forms';
import {DataService} from '../_services/data.service';
import {map, startWith} from 'rxjs/operators';
import {DiscoveryService} from '../_services/discovery.service';
import {FDEntry} from '../models/fd.model';
import {EntryInfo} from '../models/entry.model';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {TokenStorageService} from "../_services/token-storage.service";
import {TranslationCard} from "../models/translation.model";
import {User} from "../models/user.model";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {CardDialog, CardService} from "../_services/card.service";
import {CardDto} from "../models/card.model";


@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrls: ['./discover.component.css']
})
export class DiscoverComponent implements OnInit, OnDestroy {
  searched: boolean;
  translated: boolean;
  audioAvailable: boolean;
  creationInvoked: boolean;

  @Input()
  searchText: string;
  oldValue: string;
  wordlist: string[] = ['aaaa', 'bbbb'];
  content: string;
  cardSearchLabel: string;
  frenchRegex: string;
  germanRegex: string;
  spanishRegex: string;
  englishRegex: string;
  russianRegex: string;
  ukrainianRegex: string;

  audioLink: string;
  user: User;
  fdEntries: FDEntry[];
  foundCards: CardDto[];
  translationCards: TranslationCard[] = [];
  formControl = new FormControl();
  filteredOptions: Observable<string[]>;
  entryInfo: EntryInfo;

  ukrRusRegex: '[А-ЯҐЄІЇ]';
  ukrRegex: '[А-ЩЬЮЯҐЄІЇа-щьюяґєії]';
  engines: boolean;

  constructor(private userService: UserService,
              private dataService: DataService,
              private tokenStorageService: TokenStorageService,
              private discoveryService: DiscoveryService,
              private cardService: CardService,
              private router: Router,
              public dialog: MatDialog
  ) {
    userService.getMe();
    this.user = this.tokenStorageService.getUser();
  }

  showEngines() {
    this.engines = !this.engines;
  }

  openCreationDialog(): void {
    var cardDto = <CardDto>{};
    cardDto.entry = this.entryInfo.entry;
    let dialogRef = this.dialog.open(CardDialog, {
      data: {
        card: cardDto,
        mode: 'create',
      },
      panelClass: 'thin-dialog'
    });
    // dialogRef.afterClosed().subscribe(() => {
    //   this.router.navigate([''], {}).then(r => {
    //     console.log("success")
    //   });
    // });
    //
  }

  private filterValues(value: string): string[] {
    const filterValue = value.toLowerCase().split(' ').pop();
    return this.wordlist.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }


  getRandomWord() {
    this.discoveryService.random().subscribe((data) => {
      this.clearScreen();
      this.searchText = data.word;
      this.search();
    });
  }

  clearScreen() {
    this.audioAvailable = false;
    this.searched = false;
  }

  getOxfordLink() {
    return 'https://www.oxfordlearnersdictionaries.com/definition/english/' + this.searchText.replace(' ', '-');
  }

  getMacmillianLink() {
    return 'https://www.macmillandictionary.com/dictionary/american/' + this.searchText.replace(' ', '-');
  }

  getCambridgeLink() {
    return 'https://dictionary.cambridge.org/us/dictionary/english/' + this.searchText.replace(' ', '-');
  }

  getReversoLink(): string {
    return 'https://context.reverso.net/translation/english-russian/' + this.searchText;
  }

  getUrbanLink(): string {
    return 'https://www.urbandictionary.com/define.php?term=' + this.searchText;
  }

  getMerriamWebsterLink(): string {
    return 'https://www.merriam-webster.com/dictionary/' + this.searchText;
  }

  getCollinsLink(): string {
    return 'https://www.collinsdictionary.com/dictionary/english/' + this.searchText.replace(' ', '-');
  }

  ngOnInit(): void {
    this.entryInfo = {entry: this.searchText, frequency: 0.5, type: 'noun'};
    this.dataService.requestWordlist().then(r => {
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

  prettify(text: string) {
    return text
      .replace(/\.$/g, '')
      .replace(/\s\s+/g, ' ')
      .trim();
  }

  openDialogLocal(card: CardDto, mode: string): void {
    this.cardService.openDialog(card, mode)
  }

  search() {
    console.log(this.searchText);
    this.searched = true;
    this.translated = true;
    this.audioAvailable = false;
    this.audioLink = '';
    this.fdEntries = [];
    this.searchText = this.searchText
      .replace(/[^A-Za\s-z\d'.,\-!?–äöüßàâçéèêëîïôûùÿñæœ]/gi, '')
      .replace(/\s\s+/g, ' ')
      .trim();
    this.entryInfo = {entry: this.searchText, frequency: 0.5, type: 'noun'};
    console.log('this.entryInfo');
    console.log(this.entryInfo);

    this.discoveryService.searchInMyStack(this.searchText).subscribe(data => {
      if (data.length === 0) {
        this.cardSearchLabel = 'No cards like this in your stack so far';
      } else {
        this.cardSearchLabel = 'We`ve found : ' + data.length;
        this.foundCards = data;
      }
    }, error => {
      console.log((error.status));
    });

    this.discoveryService.fdSearch(this.searchText).subscribe(data => {
      // @ts-ignore
      this.fdEntries = data;
      this.searched = true;
    }, error => {
      console.log((error.status));
    });
  }

  urban() {
  }

  geolocate() {
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

  getAudio() {
    this.discoveryService.getAudioFile(this.searchText).subscribe(data => {
      this.audioAvailable = true;
      this.audioLink = data[0];
    });
  }


  showTranslation(lang: string) {
    this.discoveryService.translate(this.searchText, this.tokenStorageService.getCurLang(), lang).subscribe(data => {
      this.translated = true;
      this.translationCards[0] = data.body;
      console.log(data.body);
    }, error => {
      if (error.status === 403) {
        console.log("Limit exceeded")
      } else {
        console.log("ISE 500")
      }
    })
  }

  getIt() {
    if (this.searched) {
      return '1em';
    } else return '15em';
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

@Component({
  selector: 'card-dialog',
  templateUrl: 'card-dialog.html',
})
export class CardCreationDialog {
  card: CardDto;
  mode: string;

  constructor(
    public dialogRef: MatDialogRef<CardCreationDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.card = data.card;
    this.mode = data.mode;
  }
}


@Component({
  template: ''
})
export class DialogEntryComponent {
  constructor(public dialog: MatDialog,
              private router: Router,
              private cardService: CardService,
              private route: ActivatedRoute) {
    this.openDialog();
  }

  openDialog(): void {
    this.route.params.subscribe((params: Params) => {
      this.cardService.getCardByHash(params['id']).subscribe(card => {
        console.log(card)
        const dialogRef = this.dialog.open(CardDialog, {
          data: {
            card: card,
            mode: 'view'
          },
          panelClass: 'thin-dialog'
        });
        dialogRef.afterClosed().subscribe(() => {
          this.router.navigate(['../../'], {relativeTo: this.route}).then(r => {
            console.log("success")
          });
        });
      }, error => {
      })
    })
  }
}
