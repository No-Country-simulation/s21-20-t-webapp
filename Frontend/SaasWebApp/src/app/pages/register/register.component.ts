import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../Services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  companyName = ''; // Nuevo campo para el nombre de la empresa
  name = '';
  lastName = '';
  email = '';
  password = '';
  phoneNumber: number | null = null;
  country = '';
  birthDate = '';
  roles: string[] = ['user']; // Rol por defecto
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    if (!this.companyName || !this.name || !this.lastName || !this.email || !this.password || !this.phoneNumber || !this.country || !this.birthDate) {
      this.errorMessage = 'Todos los campos son obligatorios';
      return;
    }
  
    const phone = Number(this.phoneNumber);
    if (isNaN(phone)) {
      this.errorMessage = 'El teléfono debe ser un número válido';
      return;
    }
  
    const date = new Date(this.birthDate);
    const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
  
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
        phoneNumber: phone,
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
      next: () => this.router.navigate(['/login']),
      error: (err) => {
        console.error('Error en el registro:', err);
        this.errorMessage = 'Error en el registro. Verifica los datos ingresados.';
      },
    });
  }
  
}
