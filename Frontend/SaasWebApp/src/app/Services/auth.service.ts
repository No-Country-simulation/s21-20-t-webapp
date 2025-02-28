import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'https://inventory-lrkf.onrender.com';
  private isAuthenticated = false;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http
      .post<any>(`${this.apiUrl}/auth/login`, { email, password }, { observe: 'response' })
      .pipe(
        tap((response: HttpResponse<any>) => {
          console.log('Respuesta del login:', response);
          if (response.body && response.body.token && response.body.id) {
            localStorage.setItem('token', response.body.token);
            localStorage.setItem('userId', response.body.id.toString()); // Convertir el id a string
            console.log('Token almacenado:', response.body.token);
            console.log('UserId almacenado:', response.body.id);
          }
        })
      );
  }


  register(
    companyName: string,
    name: string,
    lastName: string,
    email: string,
    password: string,
    phoneNumber: string,
    country: string,
    birthDate: string,
    roles: string[]
  ): Observable<any> {
    const phone = Number(phoneNumber);
    if (isNaN(phone)) {
      throw new Error('El número de teléfono no es válido');
    }

    // Convertir la fecha a formato YYYY-MM-DD si es necesario
    const date = new Date(birthDate);
    const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;

    const userData = {
      tenant: {
        name: companyName,
        configuration: {
          key1: 'value1',
          key2: 'value2'
        }
      },
      user: {
        name,
        lastName,
        email,
        password,
        phoneNumber: phone,
        country,
        birthDate: formattedDate,
        roleDto: {
          "roles": [
        "USER"  
      ] 
        }
      }
    };
    
    return this.http.post(`${this.apiUrl}/registration`, userData);
  }

  logout(): void {
    localStorage.removeItem('token');
    this.isAuthenticated = false;
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getUserProfile(): Observable<any> {
    const token = localStorage.getItem('token');
    const userId = localStorage.getItem('userId');

    if (!token || !userId) return of(null);

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    const url = `${this.apiUrl}/user/${userId}`; // URL CORRECTA

    return this.http.get<any>(url, { headers }).pipe(
      catchError((error) => {
        console.error('Error obteniendo perfil del usuario:', error);
        return throwError(error);
      })
    );
  }
  private decodeToken(token: string): any {
    try {
      const decoded = JSON.parse(atob(token.split('.')[1])); // Decodifica el payload del JWT
      console.log("Decoded Token:", decoded); // <-- Revisa si tiene el 'id'
      return decoded;
    } catch (e) {
      console.error("Error decodificando token:", e);
      return null;
    }
  }
  
  
  
  // Función para obtener el email desde el token JWT
  private getEmailFromToken(token: string): string | null {
    try {
      const decoded = JSON.parse(atob(token.split('.')[1]));
      return decoded.email || null;
    } catch (e) {
      return null;
    }
  }}
