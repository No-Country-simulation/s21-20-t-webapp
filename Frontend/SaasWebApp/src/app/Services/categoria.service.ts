// src/app/Services/categoria.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoriaProducto } from '../../Models/Models'; // Asegúrate de que la ruta sea correcta

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  private apiUrl = 'https://inventory-lrkf.onrender.com/category'; // Reemplaza con tu URL

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getCategorias(tenantId: number): Observable<CategoriaProducto[]> {
    return this.http.get<CategoriaProducto[]>(`${this.apiUrl}/${tenantId}`, {
      headers: this.getHeaders(),
    });
  }

  crearCategoria(categoria: CategoriaProducto): Observable<CategoriaProducto> {
    return this.http.post<CategoriaProducto>(this.apiUrl, categoria, {
      headers: this.getHeaders(),
    });
  }
}