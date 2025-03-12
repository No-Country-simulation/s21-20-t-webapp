import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../Services/auth.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  private loginSubscription!: Subscription;
  isLoggedIn = false;
  email = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.loginSubscription = this.authService.getLoginStatus().subscribe(
      (isLoggedIn) => {
        this.isLoggedIn = isLoggedIn;
      }
    );
  }

  login() {
    // Validaciones
    if (!this.email) {
      this.showError('El correo electrónico es obligatorio.');
      return;
    }
    if (!this.isValidEmail(this.email)) {
      this.showError('El correo electrónico no es válido.');
      return;
    }
    if (!this.password) {
      this.showError('La contraseña es obligatoria.');
      return;
    }

    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        Swal.fire({
          title: '¡Bienvenido a MagnaWeb Inventario!',
          text: 'Has iniciado sesión correctamente.',
          icon: 'success',
          confirmButtonText: 'Continuar',
        }).then(() => {
          this.router.navigate(['/dashboard']);
        });
      },
      error: (err) => {
        console.error('Error en el inicio de sesión:', err);
        this.showError('Error en el inicio de sesión. Verifica tus credenciales.');
      },
    });
  }

  register(): void {
    this.router.navigate(['/register']);
  }

  // Función para mostrar errores con SweetAlert2
  private showError(message: string) {
    Swal.fire({
      title: 'Error',
      text: message,
      icon: 'error',
      confirmButtonText: 'Aceptar'
    });
  }

  // Función para validar formato de correo electrónico
  private isValidEmail(email: string): boolean {
    // Expresión regular para validar el formato del correo electrónico
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
}
