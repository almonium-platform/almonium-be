import {Component, Input, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {SearchService} from '../_services/search.service';

@Component({
  selector: 'app-card-creation',
  templateUrl: './card-creation.component.html',
  styleUrls: ['./card-creation.component.css']
})
export class CardCreationComponent implements OnInit {

  @Input() entryInfo;
  learningTypeLabel: string;

  constructor(
    private searchService: SearchService
  ) {
  }

  public translationFormGroup: FormGroup = new FormGroup({
    translation: new FormControl('', Validators.required),
  });

  public cardCreationFromGroup: FormGroup = new FormGroup({
    entry: new FormControl(''),
    notes: new FormControl(''),
    primaryTranslation: new FormControl(''),
    activeLearning: new FormControl(''),
    translations: new FormArray([
      this.translationFormGroup
    ]),
    examples: new FormArray([]),
  });

  get examples() {
    return this.cardCreationFromGroup.controls['examples'] as FormArray;
  }

  get translations() {
    return this.cardCreationFromGroup.controls['translations'] as FormArray;
  }

  ngOnInit(): void {
    this.learningTypeLabel = 'Active';
    this.cardCreationFromGroup.patchValue({
      entry: this.entryInfo.entry,
      activeLearning: true,
    });
  }

  submit(): void {
    console.log('submitted');
    console.log(this.cardCreationFromGroup);
    console.log(this.cardCreationFromGroup.controls.primaryTranslation.value);
    this.searchService.createCard(this.cardCreationFromGroup).subscribe(
      data => {
        console.log('SUCCESS');
        window.location.reload();
      },
      err => {
        console.log(err.error.message);
      }
    );

  }

  displayMessage(e) {
    if (e.checked) {
      this.learningTypeLabel = 'Active';
    } else {
      this.learningTypeLabel = 'Passive';
    }
  }

  addExample() {
    let formGroupExample: FormGroup = new FormGroup({
      example: new FormControl('', Validators.required),
      translation: new FormControl(''),
    });
    this.examples.push(formGroupExample);
  }

  addTranslation() {
    let fg: FormGroup = new FormGroup({
      translation: new FormControl('', Validators.required),
    });
    this.translations.push(fg);
  }

  deleteExample(index: number) {
    this.examples.removeAt(index);
  }

  deleteTranslation(index: number) {
    this.translations.removeAt(index);
  }
}
