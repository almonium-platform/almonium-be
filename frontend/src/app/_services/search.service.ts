import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Friend} from '../models/user.model';
import {AppConstants} from '../common/app.constants';
import {FDEntry} from '../models/fd.model';
import {FormGroup} from '@angular/forms';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private http: HttpClient) {
  }

  search(text: string): Observable<any> {
    return this.http.get(AppConstants.LANG_API + 'search/' + text);
  }

  fdSearch(text: string): Observable<HttpResponse<FDEntry[]>> {
    return this.http.get<HttpResponse<FDEntry[]>>(AppConstants.FD_BASE_URL + AppConstants.FD_ENDPOINT + AppConstants.FD_LANG_CODE + text);
  }

  urbanSearch(text: string): Observable<any> {
    return this.http.get<FDEntry[]>(AppConstants.FD_BASE_URL + AppConstants.FD_ENDPOINT + AppConstants.FD_LANG_CODE + text);
  }

  createCard(formGroup: FormGroup): Observable<any> {
    return this.http.post(AppConstants.LANG_API + 'create', {
      entry: formGroup.controls.entry.value,
      examples: formGroup.controls.examples.value,
      translations: formGroup.controls.translations.value,
      notes: formGroup.controls.notes.value,
      activeLearning: formGroup.controls.activeLearning.value,
    }, httpOptions);

  }
}
