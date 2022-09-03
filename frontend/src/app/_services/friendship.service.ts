import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {Friend, FriendshipActionDto} from '../models/user.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};


@Injectable({
  providedIn: 'root'
})
export class FriendshipService {

  constructor(private http: HttpClient) {
  }

  getMyFriends(): Observable<any> {
    return this.http.get<Friend[]>(AppConstants.FRIEND_API + 'friends/');
  }

  suggestCard(userId: number, cardId: number): Observable<any> {
    console.log("OOPs");
    console.log(userId);
    console.log(cardId);
    return this.http.post(AppConstants.FRIEND_API + 'suggest/', {
      recipientId: userId,
      cardId: cardId,
      lol: "lol"
    }, httpOptions);
  }

  manageFriendship(dto: FriendshipActionDto): Observable<any> {

    return this.http.post(AppConstants.FRIEND_API + 'friendship', {
      idInitiator: dto.idInitiator,
      idAcceptor: dto.idAcceptor,
      action: dto.action
    }, httpOptions);
  }

  searchFriends(emailText: string): Observable<any> {
    return this.http.get(AppConstants.FRIEND_API + 'search/' + emailText, {responseType: 'text'});
  }

}
