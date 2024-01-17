import {FormGroup} from '@angular/forms';
import {ReplaySubject} from 'rxjs';

export class FormIntactChecker {

  private _originalValue:any;
  private _lastNotify:boolean;

  constructor(private _form: FormGroup, private _replaySubject?:ReplaySubject<boolean>) {

    // When the form loads, changes are made for each control separately
    // and it is hard to determine when it has actually finished initializing,
    // To solve it, we keep updating the original value, until the form goes
    // dirty. When it does, we no longer update the original value.

    this._form.statusChanges.subscribe(change => {
      if(!this._form.dirty) {
        this._originalValue = JSON.stringify(this._form.value);
      }
    })

    // Every time the form changes, we compare it with the original value.
    // If it is different, we emit a value to the Subject (if one was provided)
    // If it is the same, we emit a value to the Subject (if one was provided), or
    // we mark the form as pristine again.

    this._form.valueChanges.subscribe(changedValue => {

      if(this._form.dirty) {
        var current_value = JSON.stringify(this._form.value);

        if (this._originalValue != current_value) {
          if(this._replaySubject && (this._lastNotify == null || this._lastNotify == true)) {
            this._replaySubject.next(false);
            this._lastNotify = false;
          }
        } else {
          if(this._replaySubject)
            this._replaySubject.next(true);
          else
            this._form.markAsPristine();

          this._lastNotify = true;
        }
      }
    })
  }

  // This method can be call to make the current values of the
  // form, the new "orginal" values. This method is useful when
  // you save the contents of the form but keep it on screen. From
  // now on, the new values are to be considered the original values
  markIntact() {
    this._originalValue = JSON.stringify(this._form.value);

    if(this._replaySubject)
      this._replaySubject.next(true);
    else
      this._form.markAsPristine();

    this._lastNotify = true;
  }
}
