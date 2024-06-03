import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConstants} from '../common/app.constants';
import {environment} from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth/public`;
  constructor(private http: HttpClient) {
  }

  login(credentials): Observable<any> {
    return this.http.post(AppConstants.AUTH_API + '/login', {
      email: credentials.email,
      password: credentials.password
    }, httpOptions);
  }

  register(user): Observable<any> {
    return this.http.post(AppConstants.AUTH_API + '/register', {
      username: user.username,
      email: user.email,
      password: user.password,
      socialProvider: 'LOCAL'
    }, httpOptions);
  }

  verifyEmail(token: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/verify-email?token=${token}`, {});
  }
}
