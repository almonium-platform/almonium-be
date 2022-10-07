import {Component, Inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {FormGroup} from '@angular/forms';
import {CardDto} from '../models/card.model';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {stringify} from "querystring";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};
const httpOptionsText = {
  headers: new HttpHeaders({'Content-Type': 'text/html'})
};

@Injectable({
  providedIn: 'root'
})
export class CardService {

  constructor(private http: HttpClient,
              public dialog: MatDialog) {
  }

  openDialog(card: CardDto, mode: string, component?: any): void {
    let dialogRef = this.dialog.open(CardDialog, {
        data: {
          card: card,
          mode: mode,
        },
        panelClass: 'thin-dialog'
      }
    );
    if ((typeof component !== 'undefined'))
      dialogRef.afterClosed().subscribe(data => {
        component.ngOnInit();
      }, error => {
      })
  }

  deleteCard(id: number): Observable<any> {
    return this.http.delete(AppConstants.CARD_API + id);
  }

  updateCard(changes: any): Observable<any> {
    let body: string = JSON.stringify(changes);
    return this.http.patch(AppConstants.CARD_API + "update/", body, httpOptions);
  }

  getCards(): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.CARD_API + 'all');
  }

  getCardsOfLang(code: string): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.CARD_API + 'all/' + code);
  }

  getSuggestedCards(): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.CARD_API + 'suggested');
  }

  getCard(id: number): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.CARD_API + id);
  }

  getCardByHash(hash: string): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.CARD_API + 'hash/' + hash);
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

  declineCard(cardId: number, senderId: number): Observable<any> {
    return this.http.post(AppConstants.FRIEND_API + 'decline/', {
      cardId: cardId,
      senderId: senderId,
    }, httpOptions);
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
      source: formGroup.controls.source.value,
      tags: Array.from(formGroup.controls.tags.value),
      activeLearning: formGroup.controls.activeLearning.value,
    }, httpOptions);
  }
}

@Component({
  selector: 'card-view',
  template: '<app-card [mode]="this.mode" [card]="this.card"></app-card>',
})
export class CardDialog {
  card: CardDto;
  mode: string;
  dialog: MatDialogRef<CardDialog>

  constructor(
    public dialogRef: MatDialogRef<CardDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.card = data.card;
    this.mode = data.mode;
  }
}

