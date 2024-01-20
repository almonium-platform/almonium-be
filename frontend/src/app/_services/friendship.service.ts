import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {Friend, FriendshipActionDto, FriendshipDto} from '../models/user.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};


@Injectable({
  providedIn: 'root'
})
export class FriendshipService {

  constructor(private http: HttpClient) {
  }

  getMyFriends(): Observable<Friend[]> {
    return this.http.get<Friend[]>(`${AppConstants.FRIEND_API}`);
  }

  manageFriendship(dto: FriendshipActionDto): Observable<FriendshipDto> {
    return this.http.post<FriendshipDto>(`${AppConstants.FRIEND_API}/friendships`, dto, httpOptions);
  }

  searchFriends(email: string): Observable<Friend> {
    return this.http.get<Friend>(`${AppConstants.FRIEND_API}/search`, {params: {email}});
  }

}
