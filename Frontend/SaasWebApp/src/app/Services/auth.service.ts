import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Tenant } from '../../Models/Models';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'https://inventory-lrkf.onrender.com';
  private loginStatus$ = new BehaviorSubject<boolean>(this.isLoggedIn());
  private userProfile$ = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http
      .post<any>(`${this.apiUrl}/auth/login`, { email, password }, { observe: 'response' })
      .pipe(
        tap((response: HttpResponse<any>) => {
          if (response.body && response.body.token && response.body.id) {
            localStorage.setItem('token', response.body.token);
            localStorage.setItem('userId', response.body.id.toString());
            this.loginStatus$.next(true);
            this.getUserProfile().subscribe((user) => this.userProfile$.next(user));
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

    const date = new Date(birthDate);
    const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;

    const userData = {
      tenant: {
        name: companyName,
        configuration: {
          key1: 'value1',
          key2: 'value2',
        },
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
          roles: ['USER'],
        },
      },
    };

    return this.http.post(`${this.apiUrl}/registration`, userData);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    this.loginStatus$.next(false);
    this.userProfile$.next(null);
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

    const url = `${this.apiUrl}/user/${userId}`;

    return this.http.get<any>(url, { headers }).pipe(
      catchError((error) => {
        console.error('Error obteniendo perfil del usuario:', error);
        return throwError(error);
      })
    );
  }

  getLoginStatus(): Observable<boolean> {
    return this.loginStatus$.asObservable();
  }

  getUserProfile$(): Observable<any> {
    return this.userProfile$.asObservable();
  }

  getTenantById(id: number): Observable<Tenant> {
    return this.http.get<Tenant>(`${this.apiUrl}/tenant/${id}`);
  }
}