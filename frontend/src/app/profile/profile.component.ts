import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {TokenStorageService} from '../_services/token-storage.service';
import {UserService} from '../_services/user.service';
import {Friend, FriendshipActionDto, User} from '../models/user.model';
import {MatMenuTrigger} from '@angular/material/menu';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {FriendshipService} from "../_services/friendship.service";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {Observable} from "rxjs";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {MatChipInputEvent} from "@angular/material/chips";
import {map, startWith} from "rxjs/operators";
import {DataService} from "../_services/data.service";

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
  panelOpenState = false;
  filteredTags: Observable<string[]>;
  tags: Set<string> = new Set();
  allTags: string[] = ['EN', 'DE', 'ES', 'FR', 'OTHER'];
  separatorKeysCodes: number[] = [ENTER, COMMA];
  @ViewChild('tagInput') tagInput: ElementRef<HTMLInputElement>;


  colors = [
    '#EB7181', // red
    '#468547', // green
    '#FFD558', // yellow
    '#3670B2', // blue
    '#7d3585', // blue
    '#814033', // blue
  ];
  private isLoggedIn: boolean;
  friendSearchText: string;
  usernameSearchText: string;
  notFound: boolean;
  placeholder: any;
  searchYourself: boolean = false;
  searchAlreadyFriend: boolean = false;
  currentUsername: boolean;
  availableUsername: boolean;
  unavailableUsername: boolean;
  public cardFormGroup: FormGroup = new FormGroup({
    tags: new FormControl(''),
  });
  public usernameFormGroup: FormGroup = new FormGroup({
    username: new FormControl('', Validators.compose(
      [Validators.pattern('^[a-zA-Z0-9_\-]+$'), Validators.required])),
  });

  constructor(private token: TokenStorageService,
              private userService: UserService,
              private dataService: DataService,
              private friendshipService: FriendshipService,
              public dialog: MatDialog
  ) {
    this.currentUser = this.token.getUser();

    // this.tags = this.currentUser.targetLangs;

    this.placeholder = 'Enter your friend\'s email';
    this.filteredTags = this.cardFormGroup.controls.tags.valueChanges.pipe(
      startWith(''),
      map((tag: string | null) => (tag ? this._filter(tag) : this.allTags.slice())),
    );
    this.cardFormGroup.patchValue({
      tags: ['EN', 'DE']
    })

    this.tags = new Set<string>(this.currentUser.targetLangs);
    // this.cardFormGroup.controls.tags.patchValue(this.allTags);

  }

  private _filter(value: string): string[] {
    const filterValue = value.toString().toLowerCase();
    return this.allTags.filter(fruit => fruit.toLowerCase().includes(filterValue));
  }

  remove(fruit: string): void {
    this.tags.delete(fruit);
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.tags.add(event.option.viewValue);
    this.tagInput.nativeElement.value = '';
    this.cardFormGroup.controls.tags.setValue(null);
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value && this.allTags.includes(value)) {
      this.tags.add(value);
    }

    event.input.value = '';
    this.cardFormGroup.controls.tags.setValue(null);
  }

  groupBy(arr, property) {
    return arr.reduce(function (memo, x) {
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
    console.log(this.currentUser.username)
    this.dto = <FriendshipActionDto>{};
    this.isLoggedIn = !!this.token.getToken();
    if (!this.isLoggedIn) {
      window.location.href = '/login';
    }

    this.friendshipService.getMyFriends().subscribe(
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
    this.friendshipService.manageFriendship(this.dto).subscribe(
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

    if (this.friends.some(e => e.email === this.friendSearchText)) {
      this.searchAlreadyFriend = true;
    } else if (this.friendSearchText === this.currentUser.email) {
      this.searchYourself = true;
    } else {
      this.friendshipService.searchFriends(this.friendSearchText).subscribe(data => {
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

  deleteAccount() {
    this.userService.deleteAccount().subscribe(() => {
      this.token.signOut();
      window.location.href = '/login';
    }, error => {
      console.log(error);
    });
  }

  openDialog(): void {
    let dialogRef = this.dialog.open(AccountDeletionConfirmationDialog, {
//       width: '250px',
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteAccount();
      }
      dialogRef = null;
    });
  }

  saveChanges() {
    this.userService.setTargetLangs(Array.from(this.tags)).subscribe(data => {
      this.dataService.showToast("Changes saved");
    }, error => {
    })
  }

  checkUsername() {
    this.availableUsername = false;
    this.unavailableUsername = false;
    this.currentUsername = false;

    if (this.usernameSearchText == this.currentUser.username) {
      this.currentUsername = true;
      return;
    }
    this.userService.checkUsername(this.usernameSearchText).subscribe(data => {
      console.log(data)
      if (data === true) {
        this.availableUsername = true;
      } else {
        this.unavailableUsername = true;
      }
    })
  }

  changeUsername() {
    this.userService.changeUsername(this.usernameSearchText).subscribe(data => {
      this.dataService.showToast("Username changed")
    }, error => {
    })
  }
}

@Component({
  selector: 'account-deletion-confirmation-dialog',
  templateUrl: 'account-deletion-confirmation-dialog.html',
})
export class AccountDeletionConfirmationDialog {
  constructor(public dialogRef: MatDialogRef<AccountDeletionConfirmationDialog>) {
  }

}
