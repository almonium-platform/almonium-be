import {Component, Inject, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {CardDto} from "../models/card.model";
import {APP_BASE_HREF} from "@angular/common";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CardService} from "../_services/card.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  cards: CardDto[];
  suggestedCards: CardDto[];
  cardShow: boolean
  cardLink: string = APP_BASE_HREF + "";

  constructor(private userService: UserService,
              private cardService: CardService,
              private tokenStorageService: TokenStorageService,
              public dialog: MatDialog
  ) {
  }

  openDialog(card: CardDto, mode: string): void {
        this.dialog.open(CardView, {
          data: {
            card: card,
            mode: mode,
          }
        });
  }

  ngOnInit(): void {
    if (!this.tokenStorageService.getToken()) {
      window.location.href = '/login';
    }
    this.cardService.getCards().subscribe(
      data => {
        console.log(data);
        this.cardShow = true;
        this.cards = data;
      },
      err => {
      }
    );
    this.cardService.getSuggestedCards().subscribe(
      data => {
        console.log(data);
        this.cardShow = true;
        this.suggestedCards = data;
      },
      err => {
      }
    );

  }

}

@Component({
  selector: 'card-view',
  templateUrl: 'card-view.html',
})
export class CardView {
  card: CardDto;
  mode: string;

  constructor(
    public dialogRef: MatDialogRef<CardView>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.mode = data.mode;
    this.card = data.card;
  }
}

