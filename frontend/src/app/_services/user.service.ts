import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {Friend, FriendshipActionDto, User} from '../models/user.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getPublicContent(): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'all', {responseType: 'text'});
  }

  getUserBoard(text: string): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'user', {responseType: 'text'});
  }

  getModeratorBoard(): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'mod', {responseType: 'text'});
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'admin', {responseType: 'text'});
  }

  getFriends(id: number): Observable<any> {
    return this.http.get<Friend[]>(AppConstants.API_URL + 'friends/' + id);
  }

  manageFriendship(dto: FriendshipActionDto): Observable<any> {

    return this.http.post(AppConstants.API_URL + 'friendship', {
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

  // findAllShows(): Observable<Friend[]> {
  //   return this.http
  //     .get(`/shows`)
  //     .pipe(map(result => result.friend);
  // }

  getMyFriends(): Observable<Friend[]> {
    return this.http.get<Friend[]>(AppConstants.API_URL + 'friends');
  }
}
