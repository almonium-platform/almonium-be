import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {User} from '../models/user.model';
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

  getMe(): Observable<User> {
    return this.http.get<User>(AppConstants.API_URL + 'user/me');
  }

  deleteAccount(): Observable<any> {
    return this.http.delete(AppConstants.API_URL + 'user/delete', {responseType: "text"});
  }

  getAccount(id: number) {
    return this.http.get<User>(AppConstants.API_URL + 'user/' + id);
  }

  checkUsername(username: string) {
    return this.http.get<boolean>(AppConstants.API_URL + 'user/check/' + username);
  }

  changeUsername(username: string) {
    return this.http.post(AppConstants.API_URL + 'user/change/' + username, {});
  }

  setTargetLangs(codes: string[]): Observable<any> {
    return this.http.post(AppConstants.API_URL + 'user/target/', {
      codes: codes,
    }, httpOptions);
  }
  setFluentLangs(codes: string[]): Observable<any> {
    return this.http.post(AppConstants.API_URL + 'user/fluent/', {
      codes: codes,
    }, httpOptions);
  }
}
