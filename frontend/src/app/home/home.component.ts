import {Component, Inject, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {CardDto} from "../models/card.model";
import {APP_BASE_HREF} from "@angular/common";
import {Observable} from "rxjs";
import {EntryInfo} from "../models/entry.model";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {DialogAnimationsExampleDialog} from "../discover/discover.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  cards: CardDto[];
  cardShow: boolean
  cardLink: string = APP_BASE_HREF + "";

  constructor(private userService: UserService,
              private tokenStorageService: TokenStorageService,
              public dialog: MatDialog
  ) {
  }

  openDialog(id: any): void {
    this.userService.getCard(id).subscribe(
      data => {
        this.dialog.open(DialogAnimationsExampleDialog, {
          data: {
            entryInfo: data,
          }
        });
      },
      err => {
      }
    );
  }

  ngOnInit(): void {
    if (!this.tokenStorageService.getToken()) {
      window.location.href = '/login';
    }
    this.userService.getCards().subscribe(
      data => {
        console.log(data);
        this.cardShow = true;
        this.cards = data;
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

  constructor(
    public dialogRef: MatDialogRef<CardView>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.card = data;
  }
}

