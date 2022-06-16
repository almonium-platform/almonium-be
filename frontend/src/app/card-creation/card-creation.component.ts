import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {DiscoveryService} from '../_services/discovery.service';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatChipInputEvent} from '@angular/material/chips';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {DataService} from '../_services/data.service';
import {TokenStorageService} from '../_services/token-storage.service';

@Component({
  selector: 'app-card-creation',
  templateUrl: './card-creation.component.html',
  styleUrls: ['./card-creation.component.css']
})
export class CardCreationComponent implements OnInit {

  @Input() entryInfo;
  learningTypeLabel: string;
  spellingTypeLabel: string;
  pluralTypeLabel: string;

  separatorKeysCodes: number[] = [ENTER, COMMA];
  filteredTags: Observable<string[]>;
  tags: Set<String> = new Set();
  allTags: string[] = ['Books', 'Netflix', 'Magazines', 'News', 'Social media'];
  @ViewChild('tagInput') tagInput: ElementRef<HTMLInputElement>;


  get exampleFG() {
    return new FormGroup({
      example: new FormControl('', Validators.required),
      translation: new FormControl(''),
    });
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

  public cardCreationFromGroup: FormGroup = new FormGroup({
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

  constructor(
    private discoveryService: DiscoveryService,
    private tokenStorageService: TokenStorageService,
    public dialog: MatDialog
  ) {
    this.filteredTags = this.cardCreationFromGroup.controls.tags.valueChanges.pipe(
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
    this.cardCreationFromGroup.controls.tags.setValue(null);
  }

  remove(fruit: string): void {
    this.tags.delete(fruit);
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.tags.add(event.option.viewValue);
    this.tagInput.nativeElement.value = '';
    this.cardCreationFromGroup.controls.tags.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.allTags.filter(fruit => fruit.toLowerCase().includes(filterValue));
  }


  ngOnInit(): void {
    this.learningTypeLabel = 'Active';
    this.pluralTypeLabel = 'Regular';
    this.spellingTypeLabel = 'Regular';
    this.cardCreationFromGroup.patchValue({
      entry: this.entryInfo.entry,
      activeLearning: true,
    });
  }

  createCard(): void {
    this.cardCreationFromGroup.controls.tags.patchValue(this.tags);

    let filteredExamples = this.cardCreationFromGroup.controls.examples.value.filter(o => {
      return o.example;
    });

    let filteredTranslations = this.cardCreationFromGroup.controls.translations.value.filter(o => {
      return o.translation;
    });

    console.log("this.dataService.languageCode");
    this.discoveryService.createCard(
      this.cardCreationFromGroup,
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
    return this.cardCreationFromGroup.controls['examples'] as FormArray;
  }

  get translations() {
    return this.cardCreationFromGroup.controls['translations'] as FormArray;
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

}


