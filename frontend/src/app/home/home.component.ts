import {ChangeDetectorRef, Component, Inject, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {CardDto} from "../models/card.model";
import {APP_BASE_HREF} from "@angular/common";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CardService} from "../_services/card.service";
import {Router} from "@angular/router";
import {DataService} from "../_services/data.service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  cards: CardDto[];
  suggestedCards: CardDto[];
  cardLink: string = APP_BASE_HREF + "";

  constructor(private userService: UserService,
              private cardService: CardService,
              private tokenStorageService: TokenStorageService,
              private changeDetectorRefs: ChangeDetectorRef,
              private router: Router,
              private dataService: DataService,
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

