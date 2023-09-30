import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {Observable, ReplaySubject} from 'rxjs';
import {AbstractControl, FormArray, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {TokenStorageService} from '../_services/token-storage.service';
import {filter, map, pairwise, startWith} from 'rxjs/operators';
import {MatChipInputEvent} from '@angular/material/chips';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {CardService} from '../_services/card.service';
import {FormIntactChecker} from '../_helpers/form-intact-checker';
import {UserService} from '../_services/user.service';
import {Friend} from '../models/user.model';
import {FriendshipService} from '../_services/friendship.service';
import {CardDto} from '../models/card.model';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {DataService} from '../_services/data.service';


@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {


  constructor(
    private tokenStorageService: TokenStorageService,
    private cardService: CardService,
    private userService: UserService,
    private friendshipService: FriendshipService,
    private _snackBar: MatSnackBar,
    private router: Router,
    private dataService: DataService,
    public dialog: MatDialog
  ) {

    const rs = new ReplaySubject<boolean>();

    rs.subscribe((isIntact: boolean) => {
      this.formIntact = isIntact;
    });

    this._formIntactChecker = new FormIntactChecker(this.cardFormGroup, rs);

    this.filteredTags = this.cardFormGroup.controls.tags.valueChanges.pipe(
      startWith(''),
      map((tag: string | null) => (tag ? this._filter(tag) : this.allTags.slice())),
    );


    this.changes = {};
    this.cardFormGroup.valueChanges.pipe(
      pairwise(),
      map(([oldState, newState]) => {
        const changes = {};
        for (const key in newState) {
          if (
            key !== 'tags' &&
            key !== 'examples' &&
            key !== 'translations' &&
            JSON.stringify(oldState[key]) !== JSON.stringify(newState[key]) &&
            oldState[key] !== undefined
          ) {
            if (
              this.card[key] !== undefined &&
              JSON.stringify(this.card[key]) === JSON.stringify(newState[key])
            ) {
              console.log('DELETE since nothing new');
              delete oldState[key];
              delete this.changes[key];
            } else {
              console.log('adding/changing value');
              this.changes[key] = newState[key];
              changes[key] = newState[key];
            }
            console.log(this.changes);
          }
        }
        return changes;
      }),
      filter(changes => Object.keys(changes).length !== 0 && !this.cardFormGroup.invalid)
    ).subscribe(
      value => {
        console.log('Form has changed:', value);
        console.log('Form has changed:', this.changes);
      }
    );


  }

  get examples() {
    return this.cardFormGroup.controls.examples as FormArray;
  }

  get translations() {
    return this.cardFormGroup.controls.translations as FormArray;
  }

  get exampleFG() {
    return new FormGroup({
      id: new FormControl(''),
      example: new FormControl(''),
      translation: new FormControl(''),
    }, {validators: CardComponent.validateExampleGroup()});
  }

  get translationFGRequired() {
    return new FormGroup({
      translation: new FormControl('', Validators.required),
    });
  }

  get translationFG() {
    return new FormGroup({
      id: new FormControl(''),
      translation: new FormControl(''),
    });
  }

  @Input() card: CardDto;
  @Input() mode: String;
  formIntact = true;
  learningTypeLabel: string;
  spellingTypeLabel: string;
  friendLabel: string;
  pluralTypeLabel: string;
  filteredOptions: Observable<string[]>;
  friends: Friend[];
  friendList: string[];
  searchText: string;
  tagMap: Map<number, string>;
  oldValue: string;
  sender: string;
  shareWithFriend = false;
  friendSelected: boolean;
  formControl = new FormControl();
  separatorKeysCodes: number[] = [ENTER, COMMA];
  filteredTags: Observable<string[]>;
  tags: Set<String> = new Set();
  allTags: string[] = ['Books', 'Netflix', 'Magazines', 'News', 'Social media'];
  @ViewChild('tagInput') tagInput: ElementRef<HTMLInputElement>;
  changes: {};

  public cardFormGroup: FormGroup = new FormGroup({
    entry: new FormControl('', Validators.required),
    notes: new FormControl(''),
    primaryTranslation: new FormControl(''),
    tags: new FormControl(''),
    priority: new FormControl(''),
    activeLearning: new FormControl(''),
    falseFriend: new FormControl(''),
    irregularPlural: new FormControl(''),
    irregularSpelling: new FormControl(''),
    translations: new FormArray([
      this.translationFGRequired
    ]),
    examples: new FormArray([
      this.exampleFG
    ]),
  });
  private _formIntactChecker: FormIntactChecker;

  static validateExampleGroup(): ValidatorFn {
    return (c: AbstractControl) => {
      const example = c.get('example').value;
      const translation = c.get('translation').value;
      if (!!translation && !example) {
        return {
          translation_without_example: true
        };
      }
      return null;
    };
  }


  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      this.tags.add(value);
    }

    event.input.value = '';
    // this.cardFormGroup.controls.tags.setValue(null);
  }

  remove(tag: string): void {
    this.tags.delete(tag);
    this.formIntact = false;
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.tags.add(event.option.viewValue);
    this.tagInput.nativeElement.value = '';
    // this.cardFormGroup.controls.tags.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toString().toLowerCase();
    return this.allTags.filter(fruit => fruit.toLowerCase().includes(filterValue));
  }


  ngOnInit(): void {
    console.log(this.mode);
    console.log(this.card);
    this.changes = [];
    this.changes['examples'] = new Map();
    this.changes['translations'] = new Map();
    this.changes['tr_del'] = [];
    this.changes['ex_del'] = [];

    // EXAMPLES CHANGES
    this.cardFormGroup.controls.examples.valueChanges.pipe(
      pairwise(),
      map(([oldState, newState]) => {
        const changes = {};
        if (newState.length < oldState.length) {
          console.log('deletetion detected, return');
          return changes;
        }
        for (const key in newState) {
          if (
            JSON.stringify(oldState[key]) !== JSON.stringify(newState[key]) &&
            oldState[key] !== undefined
          ) {
            if (
              this.card.examples[key] !== undefined &&
              JSON.stringify(this.card.examples[key]) === JSON.stringify(newState[key])
            ) {
              console.log('DELTING, it\'s like original');
              delete oldState[key];
              this.changes['examples'].delete(key, newState[key]);
            } else {
              console.log('adding new example');
              this.changes['examples'].set(key, newState[key]);
              changes[key] = newState[key];
            }
            console.log(this.changes);
          }
        }
        return changes;
      }),
      filter(changes => Object.keys(changes).length !== 0 && !this.cardFormGroup.invalid)
    ).subscribe(
      value => {
      }
    );

    this.cardFormGroup.controls.translations.valueChanges.pipe(
      pairwise(),
      map(([oldState, newState]) => {
        const changes = {};
        for (const key in newState) {
          if (
            JSON.stringify(oldState[key]) !== JSON.stringify(newState[key]) &&
            oldState[key] !== undefined
          ) {
            if (
              this.card.translations[key] !== undefined &&
              JSON.stringify(this.card.translations[key]) === JSON.stringify(newState[key])
            ) {
              delete oldState[key];
              console.log('DELTING, it\'s like original');
              this.changes['translations'].delete(key, newState[key]);
            } else {
              console.log('adding new translation');
              this.changes['translations'].set(key, newState[key]);
              changes[key] = newState[key];
            }
            console.log(this.changes);
          }
        }
        return changes;
      }),
      filter(changes => Object.keys(changes).length !== 0 && !this.cardFormGroup.invalid)
    ).subscribe(
      value => {
        console.log(this.changes);
      }
    );

    // this.cardFormGroup.disable()
    // for (var control in this.cardFormGroup.controls) {
    //   this.cardFormGroup.controls[control].re();
    // }

    if (this.mode === 'create') {
      this.cardFormGroup.patchValue({
        entry: this.card.entry,
        priority: 2,
        activeLearning: true,
      });
    }


    if (this.mode === 'edit') {
      this.friendshipService.getMyFriends().subscribe(friends => {
        this.friends = friends;
        this.friendList = friends.map(f => f.username);

        this.formControl.valueChanges.subscribe(() => {
          this.oldValue = this.searchText;
        });

        this.filteredOptions = this.formControl.valueChanges.pipe(
          startWith(''),
          map(val => val.split(' ').pop().length >= 3 ? this.filterValues(val) : [])
        );
      });
    }
    if (this.mode === 'suggest'
      || this.mode === 'edit'
      || this.mode === 'view'
    ) {

      this.tagMap = new Map();
      this.card.tags.forEach((tag) => {
        this.tagMap.set(tag.id, tag.text);
      });

      this.tags = new Set(this.card.tags.map((dto) => dto.text));

      this.card.created = new Date(this.card.created).toLocaleDateString();

      if (!!this.card.updated) {
        this.card.updated = new Date(this.card.updated).toLocaleDateString();
      }

      // EXAMPLES AND TRANSLATIONS HANDLING
      if (this.card.examples.length > 0) {
        this.examples.clear();
      }
      this.card.examples.forEach((element) => {
        const efg = this.exampleFG;
        this.examples.push(efg);
        efg.patchValue(
          element
        );
      });

      if (this.card.translations.length > 0) {
        this.translations.clear();
      }
      this.card.translations.forEach((element) => {
        const tfg = this.translationFG;
        this.translations.push(tfg);
        tfg.patchValue(
          element
        );
      });

      this.cardFormGroup.patchValue({
        entry: this.card.entry,
        notes: this.card.notes,
        priority: this.card.priority,
        activeLearning: this.card.activeLearning,
        falseFriend: this.card.falseFriend,
        irregularPlural: this.card.irregularPlural,
        irregularSpelling: this.card.irregularSpelling
      });
    }
    console.log(this.mode);
    console.log(this.card);

    this.learningTypeLabel = 'Active';
    this.pluralTypeLabel = 'Regular';
    this.friendLabel = 'Ordinary';
    this.spellingTypeLabel = 'Regular';

  }


  private filterValues(value: string): string[] {
    const filterValue = value.toLowerCase().split(' ').pop();
    return this.friendList.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }

  deleteCard(): void {
    this.cardService.deleteCard(this.card.id).subscribe(
      data => {
        this.dataService.showToast('Card successfully deleted');
      },
      err => {
        console.log(err.error.message);
        this.dataService.showToast(err.error.message);
      }
    );
  }

  replacer(key, value) {
    if (value instanceof Map) {
      return {
        dataType: 'Map',
        value: Array.from(value.entries()), // or with spread: value: [...value]
      };
    } else {
      return value;
    }
  }

  updateCard(): void {
    console.log('updated');
    this.changes['translations'] = Array.from(this.changes['translations'].values());
    this.changes['examples'] = Array.from(this.changes['examples'].values());
    console.log(this.changes);
    console.log(JSON.stringify(Object.assign({}, this.changes)));

    this.changes['id'] = this.card.id;
    this.changes['tags'] = Array.from(this.tags);
    if (this.tokenStorageService.getCurLang() !== this.card.language) {
      this.changes['lang'] = this.tokenStorageService.getCurLang();
    }

    this.cardService.updateCard(
      Object.assign({}, this.changes),
    ).subscribe(
      data => {
        this.dialog.closeAll();
        this.showToast('Card changes saved');
      },
      err => {
        // console.log(err.error.message);
      }
    );
  }

  createCard(): void {
    this.cardFormGroup.controls.tags.patchValue(this.tags);

    const filteredExamples = this.cardFormGroup.controls.examples.value.filter(o => {
      return o.example;
    });

    const filteredTranslations = this.cardFormGroup.controls.translations.value.filter(o => {
      return o.translation;
    });

    this.cardService.createCard(
      this.cardFormGroup,
      filteredExamples,
      filteredTranslations,
      this.tokenStorageService.getCurLang()
    ).subscribe(
      data => {
        console.log('SUCCESS');
        this.dataService.showToast('Card successfully created');
        // this.cardDialog.close();
        this.dialog.closeAll();
      },
      err => {
        console.log(err.error.message);
      }
    );
  }

  addExample() {
    this.examples.push(this.exampleFG);
  }

  addTranslation() {
    this.translations.push(this.translationFG);
  }

  deleteExample(index: number) {
    console.log(index);
    console.log(this.changes);
    console.log(this.examples.at(index).value);
    if (this.examples.at(index).value.id !== '') {
      console.log('deleting existing example');
      this.changes['ex_del'].push(this.examples.at(index).value.id);
      this.formIntact = false;
    } else {
      console.log('deleting new, no action');
    }
    this.changes['examples'].delete('' + index);
    this.examples.removeAt(index);
    console.log(this.changes);
  }

  deleteTranslation(index: number) {
    console.log(index);
    console.log(this.changes);
    console.log(this.translations.at(index).value);
    if (this.translations.at(index).value.id !== '') {
      this.changes['tr_del'].push(this.translations.at(index).value.id);
      this.formIntact = false;
    } else {
      console.log('deleting new, no action');
    }
    this.changes['translations'].delete('' + index);
    this.translations.removeAt(index);
  }

  deleteTranslationConditional(i: number) {
    if (this.translations.controls.length > 1) {
      this.deleteTranslation(i);
    }
  }

  deleteExampleConditional(i: number) {
    if (this.examples.controls.length > 1) {
      console.log('conditional ex del');
      this.deleteExample(i);
    }
  }

  changePluralType($e: MatSlideToggleChange) {
    if ($e.checked) {
      this.pluralTypeLabel = 'Irregular';
    } else {
      this.pluralTypeLabel = 'Regular';
    }
  }

  changeSpellingType($e: MatSlideToggleChange) {
    if ($e.checked) {
      this.spellingTypeLabel = 'Irregular';
    } else {
      this.spellingTypeLabel = 'Regular';
    }
  }

  changeFalseFriendType($e: MatSlideToggleChange) {
    if ($e.checked) {
      this.friendLabel = 'Misleading';
    } else {
      this.friendLabel = 'Ordinary';
    }
  }

  changeLearningType($e: MatSlideToggleChange) {
    if ($e.checked) {
      this.learningTypeLabel = 'Active';
    } else {
      this.learningTypeLabel = 'Passive';
    }
  }

  shareLinkToast() {
    this.dataService.showToast('Link copied to the clipboard');
  }

  suggestCardToast() {
    this.dataService.showToast('Card has been sent to your friend');
  }


  showToast(msg: string): void {
    this._snackBar.open(msg, '', {
      duration: 1500,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      direction: 'rtl',
      panelClass: 'simple-snack-bar'
    });
  }

  selectFriend() {
    console.log('enter pressed');
    if (!!this.friendSelected) {
      console.log('inside');
      // TODO clear input
      this.shareWithFriend = false;
      const friend: Friend = this.friends.find(f => f.username === this.searchText);
      console.log(friend);
      console.log(this.card);
      this.cardService.suggestCard(friend.id, this.card.id).subscribe(data => {
          this.suggestCardToast();
        },
        err => {
          console.log(err);
          this.dataService.showToast(err.error);
        }
      );
    }
  }

  optionSelectedHandler(value: any) {
    console.log('SELECTED FRIENDS');
    const before = this.oldValue.substr(0, this.oldValue.lastIndexOf(' ') + 1);
    this.searchText = (before + ' ' + value).replace(/\s+/g, ' ').trim();
    this.friendSelected = true;
  }

  acceptCard() {
    this.cardService.acceptCard(this.card.id, this.card.userId).subscribe(data => {
      this.dialog.closeAll();
      this.dataService.showToast('Card accepted');
    }, error => {
      this.dataService.showToast(error);
      console.log(error);
    });
  }

  declineCard() {
    this.cardService.declineCard(this.card.id, this.card.userId).subscribe(data => {
      this.dataService.showToast('Card declined');
      this.dialog.closeAll();
    }, error => {
      this.dataService.showToast(error);
      console.log(error);
    });
  }

  getLink(): string {
    return window.location.origin + '/discover/dialog/' + this.card.public_id;
  }

  openDeletionDialog(): void {
    let dialogRef = this.dialog.open(CardDeletionConfirmationDialog, {
//       width: '250px',
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteCard();
        this.dialog.closeAll();
      }
      dialogRef = null;
    });
  }

  clickDirectShare() {
    this.shareWithFriend = true;
  }

  showFriendSearch() {
    return this.shareWithFriend;
  }

  viewOnly() {
    return this.mode === 'view';
  }

  guestView() {
    return this.viewOnly() && !this.tokenStorageService.getToken();
  }

  login() {
    window.location.href = '/login';
  }
}

@Component({
  selector: 'card-deletion-confirmation-dialog',
  templateUrl: 'card-deletion-confirmation-dialog.html',
})
export class CardDeletionConfirmationDialog {
  constructor(public dialogRef: MatDialogRef<CardDeletionConfirmationDialog>) {
  }

}
