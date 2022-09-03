import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {FDEntry} from '../models/fd.model';
import {FormGroup} from '@angular/forms';
import {CardDto} from '../models/card.model';
import {TranslationCard} from "../models/translation.model";

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

  translate(text: string, from: string, to: string): Observable<HttpResponse<TranslationCard>> {
    return this.http.get<HttpResponse<TranslationCard>>(AppConstants.LANG_API + 'translate/' + from + '/' + to + '/' + text);
  }

  fdSearch(text: string): Observable<HttpResponse<FDEntry[]>> {
    return this.http.get<HttpResponse<FDEntry[]>>(AppConstants.FD_BASE_URL + AppConstants.FD_ENDPOINT + AppConstants.FD_LANG_CODE + text);
  }

  yandexTranslate(text: string): Observable<any> {
    return this.http.get(AppConstants.LANG_API + 'yandex');
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

}
