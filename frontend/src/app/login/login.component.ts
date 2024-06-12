import {Component, Directive, EventEmitter, HostListener, OnInit, Output} from '@angular/core';
import {AuthService} from '../_services/auth.service';
import {UserService} from '../_services/user.service';
import {TokenStorageService} from '../_services/token-storage.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AppConstants} from '../common/app.constants';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  loginForm: any = {};
  capsOn;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  signUpForm: any = {};
  isSignUpSuccessful = false;
  isSignUpFailed = false;
  currentUser: any;
  hide = true;
  googleURL = AppConstants.GOOGLE_AUTH_URL;
  facebookURL = AppConstants.FACEBOOK_AUTH_URL;
  appleURL = AppConstants.APPLE_AUTH_URL;

  constructor(private authService: AuthService,
              private tokenStorage: TokenStorageService,
              private route: ActivatedRoute,
              private userService: UserService,
              public router: Router
  ) {
  }

  ngOnInit(): void {
    const token: string = this.route.snapshot.queryParamMap.get('token');
    const error: string = this.route.snapshot.queryParamMap.get('error');
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.currentUser = this.tokenStorage.getUser();
    } else if (token) {
      this.tokenStorage.saveToken(token);
      this.userService.getCurrentUser().subscribe(
        data => {
          this.login(data);
        },
        err => {
          this.errorMessage = err.error.message;
          this.isLoginFailed = true;
        }
      );
    } else if (error) {
      this.errorMessage = error;
      this.isLoginFailed = true;
    }
    if (this.isLoggedIn) {
      window.location.href = '/home';
    }
  }

  onRegistrationSubmit(): void {
    console.log('registration');
    this.authService.register(this.signUpForm).subscribe(
      data => {
        console.log(data);
        this.isSignUpSuccessful = true;
        this.isSignUpFailed = false;
        this.loginForm.email = this.signUpForm.email;
        this.loginForm.password = this.signUpForm.password;
        this.onLoginSubmit();
      },
      err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    );
  }

  onLoginSubmit(): void {
    this.authService.login(this.loginForm).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.login(data.user);
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  login(user): void {
    this.tokenStorage.saveUser(user);
    this.isLoginFailed = false;
    this.isLoggedIn = true;
    this.currentUser = this.tokenStorage.getUser();
    window.location.reload();
  }

  navigateToProvider(url: string): void {
    window.location.href = url;
  }
}

@Directive({selector: '[capsLock]'})
export class TrackCapsDirective {
  @Output('capsLock') capsLock = new EventEmitter<Boolean>();

  @HostListener('window:keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    this.capsLock.emit(event.getModifierState && event.getModifierState('CapsLock'));
  }

  @HostListener('window:keyup', ['$event'])
  onKeyUp(event: KeyboardEvent): void {
    this.capsLock.emit(event.getModifierState && event.getModifierState('CapsLock'));
  }
}
