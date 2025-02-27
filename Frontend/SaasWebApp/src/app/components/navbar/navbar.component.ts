import { Component } from '@angular/core';
import { AuthService } from '../../Services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule], 
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  isLoggedIn = false;
  name = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.checkLoginStatus();
  }

  checkLoginStatus() {
    this.isLoggedIn = this.authService.isLoggedIn();
  
    if (this.isLoggedIn) {
      this.authService.getUserProfile().subscribe(
        (user) => {
          console.log("Usuario autenticado:", user); // <-- Verifica que reciba los datos correctos
          if (user) {
            this.name = `${user.name} ${user.lastName}`;
          } else {
            this.name = 'Usuario';
          }
        },
        (error) => {
          console.error("Error obteniendo perfil:", error);
          this.name = 'Usuario';
        }
      );
    } else {
      this.name = '';
    }
  }
  
  
  login(): void {
    this.router.navigate(['/login']);
  }

  register(): void {
    this.router.navigate(['/register']);
  }

  logout(): void {
    this.authService.logout();
    this.checkLoginStatus();  // Actualiza el estado al hacer logout
    this.router.navigate(['/login']);
  }

  private decodeToken(token: string): any {
    try {
      return JSON.parse(atob(token.split('.')[1])); // Decodifica el payload del JWT
    } catch (e) {
      return null;
    }
  }

  
}
