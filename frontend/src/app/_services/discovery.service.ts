import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {FDEntry} from '../models/fd.model';
import {FormGroup} from '@angular/forms';
import {CardDto} from '../models/card.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class DiscoveryService {

  constructor(private http: HttpClient) {
  }

  searchInMyStack(text: string): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.LANG_API + 'search/' + text);
  }

  analyze(text: string): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.LANG_API + 'search/' + text);
  }

  fdSearch(text: string): Observable<HttpResponse<FDEntry[]>> {
    return this.http.get<HttpResponse<FDEntry[]>>(AppConstants.FD_BASE_URL + AppConstants.FD_ENDPOINT + AppConstants.FD_LANG_CODE + text);
  }

  urbanSearch(text: string): Observable<any> {
    return this.http.get<FDEntry[]>(AppConstants.FD_BASE_URL + AppConstants.FD_ENDPOINT + AppConstants.FD_LANG_CODE + text);
  }

  random(): Observable<any> {
    return this.http.get(AppConstants.LANG_API + 'random');
  }

  getAudioFile(word: string): Observable<any> {
    return this.http.get(AppConstants.LANG_API + 'audio/' + word);
  }

  createCard(formGroup: FormGroup, filteredExamples: any, filteredTranslations: any, language: string): Observable<any> {
    return this.http.post(AppConstants.LANG_API + 'create', {
      entry: formGroup.controls.entry.value,
      language: language,
      priority: 2,
      examples: filteredExamples,
      translations: filteredTranslations,
      irregularPlural: formGroup.controls.irregularPlural.value,
      falseFriend: formGroup.controls.falseFriend.value,
      irregularSpelling: formGroup.controls.irregularSpelling.value,
      notes: formGroup.controls.notes.value,
      tags: Array.from(formGroup.controls.tags.value),
      activeLearning: formGroup.controls.activeLearning.value,
    }, httpOptions);

  }
}
