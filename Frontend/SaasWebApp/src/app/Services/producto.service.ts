// src/app/Services/producto.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../../Models/Models'; // Asegúrate de que la ruta sea correcta

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  private apiUrl = 'https://inventory-lrkf.onrender.com/product'; // Reemplaza con tu URL

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getProductos(tenantId: number, categoriaId: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/${tenantId}/${categoriaId}`, {
      headers: this.getHeaders(),
    });
  }

  crearProducto(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto, {
      headers: this.getHeaders(),
    });
  }

  actualizarProducto(producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${producto.id}`, producto, {
      headers: this.getHeaders(),
    });
  }

  eliminarProducto(productoId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${productoId}`, {
      headers: this.getHeaders(),
    });
  }


  obtenerProductos(id: string): Observable<Producto[]> {
    // Implementation to fetch products based on the provided id
    return this.http.get<Producto[]>(`/api/productos/${id}`);
  }
}