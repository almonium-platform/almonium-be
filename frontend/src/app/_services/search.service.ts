import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Friend} from '../models/user.model';
import {AppConstants} from '../common/app.constants';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private http: HttpClient) {
  }

  search(text: string): Observable<any> {
    return this.http.get(AppConstants.LANG_API + 'search/' + text);
  }

}
