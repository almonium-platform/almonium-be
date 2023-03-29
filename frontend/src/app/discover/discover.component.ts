import {
  AfterViewInit,
  Component,
  Directive,
  ElementRef,
  Inject,
  Input,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {UserService} from '../_services/user.service';
import {Observable} from 'rxjs';
import {FormControl} from '@angular/forms';
import {DataService} from '../_services/data.service';
import {map, startWith} from 'rxjs/operators';
import {DiscoveryService} from '../_services/discovery.service';
import {FDEntry} from '../models/fd.model';
import {EntryInfo} from '../models/entry.model';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {TokenStorageService} from '../_services/token-storage.service';
import {MachineTranslationDto, TranslationCard} from '../models/translation.model';
import {User} from '../models/user.model';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {CardDialog, CardService} from '../_services/card.service';
import {CardDto, ReportDto} from '../models/card.model';
import {SafeUrl} from '@angular/platform-browser';

declare var Essential_Audio;

@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrls: ['./discover.component.css']
})
export class DiscoverComponent implements OnInit, OnDestroy, AfterViewInit {
  searched: boolean;
  parallelMode: boolean;
  audioAvailable: boolean;
  creationInvoked: boolean;
  ttsReceived: boolean;
  chunkInserted: boolean;

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
  savedChunk: string;
  ukrainianRegex: string;
  audioLink: SafeUrl;
  audioLinkUnsafe: string;
  user: User;
  fdEntries: FDEntry[];
  report: ReportDto;
  foundCards: CardDto[];
  translationCards: TranslationCard;
  formControl = new FormControl();
  filteredOptions: Observable<string[]>;
  entryInfo: EntryInfo;

  ukrRusRegex: '[А-ЯҐЄІЇ]';
  ukrRegex: '[А-ЩЬЮЯҐЄІЇа-щьюяґєії]';
  engines: boolean;

  machineTranslationDto: MachineTranslationDto;

  @ViewChild('audioPlayer') audioPlayer: ElementRef;

  constructor(
    private userService: UserService,
    private dataService: DataService,
    private tokenStorageService: TokenStorageService,
    private discoveryService: DiscoveryService,
    private cardService: CardService,
    public dialog: MatDialog,
  ) {
    this.user = this.tokenStorageService.getUser();
  }

  ngAfterViewInit(): void {
    Essential_Audio.init();
  }


  getFluentLanguages(): string[] {
    return this.user.fluentLangs.filter(e => e !== this.tokenStorageService.getCurLang().toUpperCase());
  }

  showEngines(): void {
    this.engines = !this.engines;
  }

  openCreationDialog(): void {
    const cardDto = {} as CardDto;
    cardDto.entry = this.entryInfo.entry;
    const dialogRef = this.dialog.open(CardDialog, {
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


  getRandomWord(): void {
    this.discoveryService.random().subscribe((data) => {
      this.clearScreen();
      this.searchText = data.word;
      this.search();
    });
  }

  clearScreen(): void {
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

    this.formControl.valueChanges.subscribe(() => {
      console.log(document.getElementById('text-area').clientHeight);
      if (document.getElementById('text-area').clientHeight > 60) {
        console.log('resized');
        this.chunkInserted = true;
      }
    });

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
    const before = this.oldValue.substr(0, this.oldValue.lastIndexOf(' ') + 1);
    this.searchText = (before + ' ' + value).replace(/\s+/g, ' ').trim();
  }

  prettify(text: string) {
    return text
      .replace(/\.$/g, '')
      .replace(/\s\s+/g, ' ')
      .trim();
  }

  openDialogLocal(card: CardDto, mode: string): void {
    this.cardService.openDialog(card, mode);
  }

  search(): void {
    this.searched = true;
    this.audioAvailable = false;
    this.audioLink = '';
    this.parallelMode = false;
    this.ttsReceived = false;
    this.chunkInserted = false;
    this.audioLinkUnsafe = '';
    this.fdEntries = [];
    this.searchText = this.searchText
      .replace(/[^A-Za\s-z\d'.,!?–äöüßàâçéèêëîïôûùÿñæœ]/gi, '')
      .replace(/\s\s+/g, ' ')
      .trim();
    this.entryInfo = {entry: this.searchText, frequency: 0.5, type: 'noun'};
    if (this.searchText.split(' ').length > 5) {
      console.log('big chunk');
      // todo fluent priority
      this.discoveryService.bulkTranslate(this.searchText, this.user.fluentLangs[0])
        .subscribe((data) => {
          console.log(data);
          this.parallelMode = true;
          this.machineTranslationDto = data;
          Essential_Audio.init();
        });
      this.savedChunk = this.searchText;
      this.searchText = '';
    } else {
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
      }, error => {
        console.log((error.status));
      });
      this.discoveryService.getReport(this.searchText, this.tokenStorageService.getCurLang())
        .subscribe(data => {
          this.report = data;
          console.log(this.report);
          console.log(data);
          // this.translated = true;
          this.translationCards = this.report.translationCards;
        }, error => {
          console.log((error.status));
        });
    }
  }

  urban() {
  }

  geolocate() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.showPosition);
    } else {
      // I believe it may also mean geolocation isn't supported
      alert('Geolocation denied');
    }
  }

  showPosition(position) {
    alert(`${position.coords.longitude} - ${position.coords.latitude}`);
  }

  getTTS() {
    this.ttsReceived = true;
    const url = 'http://localhost:9998/api/lang/audio/'
      + this.tokenStorageService.getCurLang() + '/'
      + encodeURIComponent(this.savedChunk) + '/file.mp3';
    console.log(url);
    this.audioPlayer.nativeElement.setAttribute(
      'data-url', url);
    Essential_Audio.init();
  }

  getAudio() {
    if (!this.audioLinkUnsafe) {
      this.discoveryService.getPronunciation(this.tokenStorageService.getCurLang(), this.searchText)
        .subscribe(data => {
          this.audioAvailable = true;
          this.audioLink = data;
          this.audioLinkUnsafe = data;
          new Audio(this.audioLinkUnsafe).play();
        });
    } else {
      new Audio(this.audioLinkUnsafe).play();
    }
  }

  showTranslation(lang: string) {
    this.discoveryService.translate(this.searchText, this.tokenStorageService.getCurLang(), lang).subscribe(data => {
      this.parallelMode = true;
      this.translationCards[0] = data.body;
      console.log(data.body);
    }, error => {
      if (error.status === 403) {
        console.log('Limit exceeded');
      } else {
        console.log('ISE 500');
      }
    });
  }

  positionSearch() {
    if (this.searched || this.chunkInserted) {
      return '1em';
    } else {
      return '15em';
    }
  }

  translateTo(value: string) {
    this.discoveryService.bulkTranslate(this.machineTranslationDto.text, value.toLowerCase())
      .subscribe((data) => {
        this.machineTranslationDto = data;
      });
  }

}

@Directive({
  selector: 'textarea[appFocus]',
})
export class FocusOnShowDirective
  implements AfterViewInit {
  @Input('appFocus')
  private focused = false;

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
      this.cardService.getCardByHash(params.id).subscribe(card => {
        console.log(card);
        const dialogRef = this.dialog.open(CardDialog, {
          data: {
            card,
            mode: 'view'
          },
          panelClass: 'thin-dialog'
        });
        dialogRef.afterClosed().subscribe(() => {
          this.router.navigate(['../../'], {relativeTo: this.route}).then(r => {
            console.log('success');
          });
        });
      }, error => {
      });
    });
  }
}
