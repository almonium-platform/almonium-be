import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {Friend, FriendshipActionDto, User} from '../models/user.model';
import {CardDto} from "../models/card.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getFriends(id: number): Observable<any> {
    return this.http.get<Friend[]>(AppConstants.HOME_API + 'friends/' + id);
  }

  getCards(): Observable<any> {
    return this.http.get<CardDto[]>(AppConstants.HOME_API + 'cards');
  }

  manageFriendship(dto: FriendshipActionDto): Observable<any> {

    return this.http.post(AppConstants.HOME_API + 'friendship', {
      idInitiator: dto.idInitiator,
      idAcceptor: dto.idAcceptor,
      action: dto.action
    }, httpOptions);
  }

  searchFriends(emailText: string): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'search/' + emailText, {responseType: 'text'});
  }

  getMe(): Observable<any> {
    return this.http.get<User>(AppConstants.API_URL + 'user/me');
  }

  deleteAccount(): Observable<any> {
    return this.http.delete(AppConstants.API_URL + 'user/delete');
  }

}
