import {Component, Input, OnInit} from '@angular/core';
import {TokenStorageService} from '../_services/token-storage.service';
import {UserService} from '../_services/user.service';
import {Friend, FriendshipActionDto, User} from '../models/user.model';
import {MatMenuTrigger} from '@angular/material/menu';
import {FormControl, FormGroup} from '@angular/forms';

enum Action {
  REQUEST,
  ACCEPT,
  CANCEL,
  UNFRIEND,
  REJECT,
  BLOCK,
  UNBLOCK
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: any;
  user: User;
  content: any;
  friends: Friend[];
  friendsBlocked: Friend[] = [];
  friendsBlockedMe: Friend[] = [];
  friendsMutual: Friend[] = [];
  friendsAsked: Friend[] = [];
  friendsAskedMe: Friend[] = [];
  visibleFriends: Friend[] = [];
  avatarColors = {};
  dto: FriendshipActionDto;
  selectedUserId: number;
  searchFriend: Friend;

  colors = [
    '#EB7181', // red
    '#468547', // green
    '#FFD558', // yellow
    '#3670B2', // blue
    '#7d3585', // blue
    '#814033', // blue
  ];
  private isLoggedIn: boolean;
  searchText: string;
  notFound: boolean;
  placeholder: any;
  searchYourself: boolean = false;
  searchAlreadyFriend: boolean = false;

  constructor(private token: TokenStorageService,
              private userService: UserService
  ) {
    this.currentUser = this.token.getUser(
    );
    this.placeholder = "Enter your friend's email"
  }

  groupBy(arr, property) {
    return arr.reduce(function(memo, x) {
      if (!memo[x[property]]) {
        memo[x[property]] = [];
      }
      memo[x[property]].push(x);
      return memo;
    }, {});
  }

  public get action(): typeof Action {
    return Action;
  }

  ngOnInit(): void {

    this.dto = <FriendshipActionDto> {};
    this.isLoggedIn = !!this.token.getToken();
    if (!this.isLoggedIn) {
      window.location.href = '/login';
    }

    this.userService.getFriends(this.currentUser.id).subscribe(
      data => {
        this.friends = data;
        let sortedFriends = this.groupBy(data, 'status');
        console.log(this.friends);
        for (let i = 0; i < this.friends.length; i++) {
          this.avatarColors[this.friends[i].email] = this.getColor();
        }

        this.friendsMutual = (sortedFriends.FRIENDS);
        this.friendsAskedMe = (sortedFriends.ASKED_ME);
        this.friendsAsked = (sortedFriends.ASKED);
        this.friendsBlocked = (sortedFriends.BLOCKED);
        this.friendsBlockedMe = (sortedFriends.BLOCKED_ME);

        console.log(this.visibleFriends);
        console.log(this.friendsMutual);
        console.log(this.friendsAsked);
        console.log(this.friendsAskedMe);
        console.log(this.friendsBlocked);
      },
      err => {
        this.user = JSON.parse(err.error).message;
      }
    );
  }

  createInitials(name: string): string {
    return name.substring(0, 2).toUpperCase();
  }

  getColor(): string {
    const randomIndex = Math.floor(Math.random() * Math.floor(this.colors.length));
    return this.colors[randomIndex];
  }

  openContextMenu(
    event: MouseEvent,
    trigger: MatMenuTrigger,
    triggerElement: HTMLElement,
    friend: Friend
  ) {
    console.log(friend.email);
    this.selectedUserId = friend.id;
    triggerElement.style.left = event.clientX + 5 + 'px';
    triggerElement.style.top = event.clientY + 5 + 'px';
    if (trigger.menuOpen) {
      trigger.closeMenu();
      trigger.openMenu();
    } else {
      trigger.openMenu();
    }
    event.preventDefault();
  }

  friendshipAction(action: Action): void {
    console.log('HERERE' + Action[action].toString() + '  ' + this.currentUser.id + ' ' + this.selectedUserId);
    this.dto.action = Action[action].toString();
    this.dto.idAcceptor = this.selectedUserId;
    this.dto.idInitiator = this.currentUser.id;
    this.userService.manageFriendship(this.dto).subscribe(
      data => {
        console.log(data);
      },
      err => {
      }
    );
    window.location.reload();
  }

  searchPeople() {
    this.notFound = false;
    this.searchYourself = false;
    this.searchAlreadyFriend = false;
    // this.userService.getUserBoard(this.searchText).subscribe(
    //   data => {
    //     console.log('OLD' + data);
    //   },
    //   err => {
    //     this.content = JSON.parse(err.error).message;
    //   }
    // );

    if (this.friends.some(e => e.email === this.searchText)) {
      this.searchAlreadyFriend = true;
    } else if (this.searchText === this.currentUser.email) {
      this.searchYourself = true;
    } else {
      this.userService.searchFriends(this.searchText).subscribe(data => {
          this.searchFriend = JSON.parse(data);
          console.log(this.searchFriend);
          console.log(this.searchFriend.email);
          console.log(this.searchFriend.username);
        },
        err => {
          this.notFound = true;
          console.log('NOT FOUND');
          // this.content = JSON.parse(err.error).message;
        });
    }
  }
}
