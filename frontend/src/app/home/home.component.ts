import {Component, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {CardDto} from "../models/card.model";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  cards: CardDto[];
  cardShow: boolean

  constructor(private userService: UserService, private tokenStorageService: TokenStorageService) {
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
