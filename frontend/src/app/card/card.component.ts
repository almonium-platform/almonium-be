import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {Observable, ReplaySubject} from "rxjs";
import {AbstractControl, FormArray, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {TokenStorageService} from "../_services/token-storage.service";
import {map, startWith} from "rxjs/operators";
import {MatChipInputEvent} from "@angular/material/chips";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {CardService} from "../_services/card.service";
import {FormIntactChecker} from "../_helpers/form-intact-checker";
import {UserService} from "../_services/user.service";
import {Friend} from "../models/user.model";
import {FriendshipService} from "../_services/friendship.service";
import {CardDto} from "../models/card.model";
import {EntryInfo} from "../models/entry.model";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {

  @Input() card: CardDto;
  @Input() entryInfo: EntryInfo;
  @Input() suggestion: CardDto;
  formIntact: boolean = true;
  learningTypeLabel: string;
  spellingTypeLabel: string;
  pluralTypeLabel: string;
  filteredOptions: Observable<string[]>;
  friends: Friend[];
  friendList: string[];
  searchText: string;
  oldValue: string;
  mode: string;
  sender: string;
  friendSelected: boolean;
  formControl = new FormControl();
  separatorKeysCodes: number[] = [ENTER, COMMA];
  filteredTags: Observable<string[]>;
  tags: Set<String> = new Set();
  allTags: string[] = ['Books', 'Netflix', 'Magazines', 'News', 'Social media'];
  @ViewChild('tagInput') tagInput: ElementRef<HTMLInputElement>;


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


  constructor(
    private tokenStorageService: TokenStorageService,
    private cardService: CardService,
    private userService: UserService,
    private friendshipService: FriendshipService,
  ) {

    const rs = new ReplaySubject<boolean>();

    rs.subscribe((isIntact: boolean) => {
      this.formIntact = isIntact;
    })

    this._formIntactChecker = new FormIntactChecker(this.cardFormGroup, rs);

    this.filteredTags = this.cardFormGroup.controls.tags.valueChanges.pipe(
      startWith(''),
      map((tag: string | null) => (tag ? this._filter(tag) : this.allTags.slice())),
    );
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      this.tags.add(value);
    }

    event.input.value = '';
    this.cardFormGroup.controls.tags.setValue(null);
  }

  remove(fruit: string): void {
    this.tags.delete(fruit);
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.tags.add(event.option.viewValue);
    this.tagInput.nativeElement.value = '';
    this.cardFormGroup.controls.tags.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.allTags.filter(fruit => fruit.toLowerCase().includes(filterValue));
  }


  ngOnInit(): void {
    if (!!this.entryInfo) {
      this.mode = 'create';

      this.cardFormGroup.patchValue({
        entry: this.entryInfo.entry,
        activeLearning: true,
      });
    }


    if (!!this.card) {
      this.mode = 'edit';
      this.card.created = new Date(this.card.created).toLocaleDateString()

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
      this.cardFormGroup.patchValue({
        entry: this.card.entry,
        translations: this.card.translations,
        examples: this.card.examples,
        activeLearning: this.card.activeLearning,
      });
    }
    if (!!this.suggestion) {
      this.mode = 'suggest';
      this.card = this.suggestion;
      // this.userService.
    }

    console.log(this.mode)
    console.log(this.suggestion)
    this.learningTypeLabel = 'Active';
    this.pluralTypeLabel = 'Regular';
    this.spellingTypeLabel = 'Regular';
  }


  private filterValues(value: string): string[] {
    const filterValue = value.toLowerCase().split(' ').pop();
    return this.friendList.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }

  deleteCard(): void {
    this.cardService.deleteCard(this.card.id).subscribe(
      data => {
        window.location.reload();
      },
      err => {
        console.log(err.error.message);
      }
    )
  }

  saveCard(): void {
    //TODO
    this.cardFormGroup.controls.tags.patchValue(this.tags);

    let filteredExamples = this.cardFormGroup.controls.examples.value.filter(o => {
      return o.example;
    });

    let filteredTranslations = this.cardFormGroup.controls.translations.value.filter(o => {
      return o.translation;
    });

    console.log("this.dataService.languageCode");
    // this.discoveryService.createCard(
    //   this.cardFormGroup,
    //   filteredExamples,
    //   filteredTranslations,
    //   this.tokenStorageService.getCurLang()
    // ).subscribe(
    //   data => {
    //     console.log('SUCCESS');
    //     window.location.reload();
    //   },
    //   err => {
    //     console.log(err.error.message);
    //   }
    // );
  }

  createCard(): void {
    this.cardFormGroup.controls.tags.patchValue(this.tags);

    let filteredExamples = this.cardFormGroup.controls.examples.value.filter(o => {
      return o.example;
    });

    let filteredTranslations = this.cardFormGroup.controls.translations.value.filter(o => {
      return o.translation;
    });

    console.log("this.dataService.languageCode");
    this.cardService.createCard(
      this.cardFormGroup,
      filteredExamples,
      filteredTranslations,
      this.tokenStorageService.getCurLang()
    ).subscribe(
      data => {
        console.log('SUCCESS');
        window.location.reload();
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
    this.examples.removeAt(index);
  }

  deleteTranslation(index: number) {
    this.translations.removeAt(index);
  }

  get examples() {
    return this.cardFormGroup.controls['examples'] as FormArray;
  }

  get translations() {
    return this.cardFormGroup.controls['translations'] as FormArray;
  }

  deleteTranslationConditional(i: number) {
    if (this.translations.controls.length > 1) {
      this.deleteTranslation(i);
    }
  }

  deleteExampleConditional(i: number) {
    if (this.examples.controls.length > 1) {
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

  changeLearningType($e: MatSlideToggleChange) {
    if ($e.checked) {
      this.learningTypeLabel = 'Active';
    } else {
      this.learningTypeLabel = 'Passive';
    }
  }

  share() {
    console.log("SHARED")
  }

  selectFriend() {
    console.log("enter pressed")
    console.log(1)
    if (!!this.friendSelected) {
      console.log("inside")
      let friend: Friend = this.friends.find(f => f.username === this.searchText)
      console.log(friend)
      console.log(this.card)
      this.cardService.suggestCard(friend.id, this.card.id).subscribe(data => {
          console.log(data);
        },
        err => {
        }
      );
    }
  }

  optionSelectedHandler(value: any) {
    console.log("SELECTED FRIENDS")
    let before = this.oldValue.substr(0, this.oldValue.lastIndexOf(' ') + 1);
    this.searchText = (before + ' ' + value).replace(/\s+/g, ' ').trim();
    this.friendSelected = true;
  }

  get exampleFG() {
    return new FormGroup({
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
      translation: new FormControl(''),
    });
  }

  static validateExampleGroup(): ValidatorFn {
    return (c: AbstractControl) => {
      const example = c.get('example').value;
      const translation = c.get('translation').value;
      if (!!translation && !example) {
        return {
          'translation_without_example': true
        }
      }
      return null;
    };
  }

  acceptCard() {
    // console.log("ACCEPT")
    this.cardService.acceptCard(this.card.id, this.card.userId).subscribe(data => {
      // console.log(data)
    }, error => {
      console.log(error)
    });
  }

  rejectCard() {
    this.cardService.rejectCard(this.card.id).subscribe(data => {
    }, error => {
      console.log(error)
    });
  }

}
