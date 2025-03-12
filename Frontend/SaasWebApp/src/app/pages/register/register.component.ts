import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../Services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  companyName = '';
  name = '';
  lastName = '';
  email = '';
  password = '';
  phoneNumber: number | null = null;
  country = '';
  birthDate = '';
  roles: string[]= ['user']; 
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    this.errorMessage = ''; // Limpiar mensaje de error

    // Validaciones
    if (!this.companyName) {
      this.showError('El nombre de la empresa es obligatorio.');
      return;
    }
    if (!this.name) {
      this.showError('El nombre es obligatorio.');
      return;
    }
    if (!this.lastName) {
      this.showError('El apellido es obligatorio.');
      return;
    }
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
    if (!this.phoneNumber) {
      this.showError('El teléfono es obligatorio.');
      return;
    }
    if (isNaN(this.phoneNumber)) {
      this.showError('El teléfono debe ser un número válido.');
      return;
    }
    if (!this.country) {
      this.showError('El país es obligatorio.');
      return;
    }
    if (!this.birthDate) {
      this.showError('La fecha de nacimiento es obligatoria.');
      return;
    }

    const formattedDate = this.formatDate(this.birthDate); // Formatear fecha

    const requestData = {
      tenant: {
        name: this.companyName,
        configuration: {
          key1: 'value1',
          key2: 'value2'
        }
      },
      user: {
        name: this.name,
        lastName: this.lastName,
        email: this.email,
        password: this.password,
        phoneNumber: this.phoneNumber,
        country: this.country,
        birthDate: formattedDate,
        roleDto: {
          roles: ['USER'] 
        }
      }
    };

    this.authService.register(
      this.companyName,
      this.name,
      this.lastName,
      this.email,
      this.password,
      this.phoneNumber.toString(),
      this.country,
      formattedDate,
      this.roles
    ).subscribe({
      next: () => {
        Swal.fire({
          title: '¡Registro Exitoso!',
          text: 'Tu cuenta ha sido creada correctamente.',
          icon: 'success',
          confirmButtonText: 'Iniciar Sesión',
        }).then(() => {
          this.router.navigate(['/login']);
        });
      },
      error: (err) => {
        console.error('Error en el registro:', err);
        this.showError('Error en el registro. Verifica los datos ingresados.');
      },
    });
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

  // Función para formatear la fecha
  private formatDate(date: string): string {
    const d = new Date(date);
    return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;
  }
}