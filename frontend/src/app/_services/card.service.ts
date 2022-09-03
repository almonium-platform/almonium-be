import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {FormGroup} from '@angular/forms';
import {CardDto} from '../models/card.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class CardService {

  constructor(private http: HttpClient) {
  }

  deleteCard(id: number): Observable<any> {
    return this.http.delete(AppConstants.CARD_API + id);
  }

  updateCard(id: number): Observable<any> {
    return this.http.patch(AppConstants.CARD_API + id, {});
  }

  getCards(): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.CARD_API + 'all');
  }

  getSuggestedCards(): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.CARD_API + 'suggested');
  }

  getCard(id: number): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.CARD_API + id);
  }

  suggestCard(userId: number, cardId: number): Observable<any> {
    return this.http.post(AppConstants.FRIEND_API + 'suggest/', {
      recipientId: userId,
      cardId: cardId,
    }, httpOptions);
  }

  acceptCard(cardId: number, senderId: number): Observable<any> {
    return this.http.post(AppConstants.FRIEND_API + 'accept/', {
      cardId: cardId,
      senderId: senderId,
    }, httpOptions);
  }

  rejectCard(id: number): Observable<any> {
    return this.http.post(AppConstants.FRIEND_API + 'reject/' + '?id=' + id, {}, httpOptions);
  }


  createCard(formGroup: FormGroup, filteredExamples: any, filteredTranslations: any, language: string): Observable<any> {
    return this.http.post(AppConstants.CARD_API + 'create', {
      entry: formGroup.controls.entry.value,
      language: language,
      priority: formGroup.controls.priority.value,
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
