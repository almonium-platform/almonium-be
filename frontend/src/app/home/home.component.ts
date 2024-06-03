import {Component, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {CardDto} from "../models/card.model";
import {MatDialog} from "@angular/material/dialog";
import {CardService} from "../_services/card.service";
import {User} from "../models/user.model";
import {AppConstants} from "../common/app.constants";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  cards: CardDto[];
  suggestedCards: CardDto[];
  filteredCards: CardDto[]; // Add this property
  filterBy: string;
  learning: number;
  googleURL = AppConstants.GOOGLE_AUTH_URL;
  facebookURL = AppConstants.FACEBOOK_AUTH_URL;


  constructor(private userService: UserService,
              private cardService: CardService,
              public tokenStorageService: TokenStorageService,
              public dialog: MatDialog
  ) {
  }

  navigateToProvider(url: string): void {
    window.location.href = `${url}&intent=link`;
  }

  openDialogLocal(card: CardDto, mode: string): void {
    this.cardService.openDialog(card, mode, this)
  }

  getUser(): User {
    return this.tokenStorageService.getUser();
  }

  getCards(): void {
    this.cardService.getCardsOfLang(this.tokenStorageService.getCurLang()).subscribe(
      data => {
        this.cards = data;
        this.learning = this.count();
        this.filteredCards = this.cards;
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

  sortCards(): void {
    if (this.filterBy === 'created') {
      this.filteredCards = this.filteredCards.sort((a, b) =>
        new Date(a.created).getTime() - new Date(b.created).getTime()
      );
    }
  }

  count(): number {
    return this.cards.filter(card => card.iteration !== null).length;
  }

  ngOnInit(): void {
    if (!this.tokenStorageService.getToken()) {
      window.location.href = '/login';
    }
    this.getCards();
  }

  calculatePercentage(value: number, total: number): number {
    if (total === 0) {
      return 0;
    }

    return Math.round((value / total) * 100);
  }

  getHighestFrequency(): number {
    return this.cards && this.cards.length > 0
      ? Math.max(...this.cards.map(card => card.frequency))
      : 0;
  }

  getLowestFrequency(): number {
    return this.cards && this.cards.length > 0
      ? Math.min(...this.cards.map(card => card.frequency))
      : 0;
  }

  getAverageFrequency(): number {
    return this.cards && this.cards.length > 0
      ? Math.round(this.cards.reduce((sum, card) => sum + card.frequency, 0) / this.cards.length)
      : 0;
  }
}

