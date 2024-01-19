import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {UserInfoDto} from '../models/user-info.dto';
import {UsernameAvailability} from '../models/username-availability.dto';

@Injectable({providedIn: 'root'})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {
  }

  getCurrentUser(): Observable<UserInfoDto> {
    return this.http.get<UserInfoDto>(`${this.apiUrl}/me`);
  }

  deleteAccount(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/me/account`);
  }

  checkUsernameAvailability(username: string): Observable<UsernameAvailability> {
    return this.http.get<UsernameAvailability>(`${this.apiUrl}/${username}/availability`);
  }

  changeUsername(request: { newUsername: string }): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/me/username`, request);
  }

  updateTargetLanguages(langCodes: string[]): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/me/target-languages`, {langCodes});
  }

  updateFluentLanguages(langCodes: string[]): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/me/fluent-languages`, {langCodes});
  }
}
