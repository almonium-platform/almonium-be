import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {
  resetForm: any = {};
  token: string;
  isSuccessful = false;
  isResetFailed = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
    if (!this.token) {
      this.errorMessage = 'Invalid or missing token';
      this.isResetFailed = true;
    }
  }

  onResetSubmit(): void {
    this.authService.resetPassword(this.token, this.resetForm.password).subscribe(
      data => {
        this.isSuccessful = true;
        this.isResetFailed = false;
        // Optionally, redirect to login page after reset
        this.router.navigate(['/login']);
      },
      err => {
        this.errorMessage = err.error.message;
        this.isResetFailed = true;
      }
    );
  }
}
