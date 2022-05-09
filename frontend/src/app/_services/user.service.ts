import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {Friend} from '../models/user.model';
import {map} from 'rxjs/operators';

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

  getUserBoard(): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'user', {responseType: 'text'});
  }

  getModeratorBoard(): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'mod', {responseType: 'text'});
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'admin', {responseType: 'text'});
  }

  getFriends(): Observable<any> {
    return this.http.get(AppConstants.API_URL + 'friends', {responseType: 'text'});
  }

  // findAllShows(): Observable<Friend[]> {
  //   return this.http
  //     .get(`/shows`)
  //     .pipe(map(result => result.friend);
  // }

  getCurrentUser(): Observable<Friend[]> {
    return this.http.get<Friend[]>(AppConstants.API_URL + 'friends');
  }
}
