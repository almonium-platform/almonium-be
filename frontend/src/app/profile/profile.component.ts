import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../_services/token-storage.service';
import {UserService} from '../_services/user.service';
import {Friend} from '../models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: any;
  content: string;
  friends: Friend[];

  constructor(private token: TokenStorageService,
              private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.userService.getFriends().subscribe(
      data => {
        this.content = data;
      },
      err => {
        this.content = JSON.parse(err.error).message;
      }
    );

  }

}
