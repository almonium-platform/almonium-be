import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../_services/auth.service';

@Component({
  selector: 'app-email-verification',
  template: `
    <div *ngIf="message">{{ message }}</div>`,
  styles: [``]
})
export class EmailVerificationComponent implements OnInit {
  message: string;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (token) {
      this.authService.verifyEmail(token).subscribe(
        response => {
          this.message = response.message;
          setTimeout(() => this.router.navigate(['/login']), 3000);  // Redirect after 3 seconds
        },
        err => {
          this.message = err.error.message;
        }
      );
    } else {
      this.message = 'Invalid verification link.';
    }
  }
}
