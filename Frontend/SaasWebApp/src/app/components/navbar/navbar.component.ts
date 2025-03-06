import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../Services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  isLoggedIn = false;
  name = '';
  private loginSubscription!: Subscription;
  private userProfileSubscription!: Subscription;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.loginSubscription = this.authService.getLoginStatus().subscribe(
      (isLoggedIn) => {
        this.isLoggedIn = isLoggedIn;
      }
    );

    this.userProfileSubscription = this.authService.getUserProfile$().subscribe(
      (user) => {
        if (user && user.name && user.lastName) {
          this.name = `${user.name} ${user.lastName}`;
        } else {
          this.name = 'Usuario';
        }
      }
    );
  }

  ngOnDestroy() {
    this.loginSubscription.unsubscribe();
    this.userProfileSubscription.unsubscribe();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  register(): void {
    this.router.navigate(['/register']);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  
}
