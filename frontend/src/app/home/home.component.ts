import {Component, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {CardDto} from "../models/card.model";
import {MatDialog} from "@angular/material/dialog";
import {CardService} from "../_services/card.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  cards: CardDto[];
  suggestedCards: CardDto[];


  constructor(private userService: UserService,
              private cardService: CardService,
              private tokenStorageService: TokenStorageService,
              public dialog: MatDialog
  ) {
  }

  openDialogLocal(card: CardDto, mode: string): void {
    this.cardService.openDialog(card, mode, this)
  }

  getCards(): void {
    this.cardService.getCardsOfLang(this.tokenStorageService.getCurLang()).subscribe(
      data => {
        this.cards = data;
      },
      err => {
      }
    );
    this.cardService.getSuggestedCards().subscribe(
      data => {
        this.suggestedCards = data;
      },
      err => {
      }
    );
  }

  ngOnInit(): void {
    if (!this.tokenStorageService.getToken()) {
      window.location.href = '/login';
    }
    this.getCards();
  }

}

