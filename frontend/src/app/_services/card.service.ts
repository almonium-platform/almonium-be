import {Component, Inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {CardCreationDto, CardDto, CardSuggestionDto, CardUpdateDto} from '../models/card.model';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";

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
  private apiURL = AppConstants.CARD_API; // Assuming this is something like 'http://localhost:8080/api/cards'

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

  createCard(cardCreationDto: CardCreationDto): Observable<CardDto> {
    return this.http.post<CardDto>(this.apiURL, cardCreationDto, httpOptions);
  }

  getCard(id: number): Observable<CardDto> {
    return this.http.get<CardDto>(`${this.apiURL}/${id}`);
  }

  updateCard(id: number, cardUpdateDto: CardUpdateDto): Observable<void> {
    return this.http.put<void>(`${this.apiURL}/${id}`, cardUpdateDto, httpOptions);
  }

  deleteCard(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }

  getCards(): Observable<CardDto[]> {
    return this.http.get<CardDto[]>(this.apiURL);
  }

  getCardsOfLang(code: string): Observable<CardDto[]> {
    return this.http.get<CardDto[]>(`${this.apiURL}/lang/${code}`);
  }

  getSuggestedCards(): Observable<CardDto[]> {
    return this.http.get<CardDto[]>(`${this.apiURL}/suggested`);
  }

  getCardByHash(hash: string): Observable<CardDto> {
    return this.http.get<CardDto>(`${this.apiURL}/public/${hash}`);
  }

  suggestCard(suggestion: CardSuggestionDto): Observable<any> {
    return this.http.post(AppConstants.SUGGESTION_API, suggestion, httpOptions);
  }

  acceptCard(suggestionId: number): Observable<any> {
    return this.http.put(`${AppConstants.SUGGESTION_API}/${suggestionId}/accept`, null, httpOptions);
  }

  declineCard(suggestionId: number): Observable<any> {
    return this.http.put(`${AppConstants.SUGGESTION_API}/${suggestionId}/decline`, null, httpOptions);
  }}

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

