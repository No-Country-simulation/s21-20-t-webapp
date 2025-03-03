import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto, ProductoActualizar } from '../../Models/Models'; // Asegúrate de la ruta correcta

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'https://inventory-lrkf.onrender.com/products';

  constructor(private http: HttpClient) {}

  getProducts(): Observable<{ content: Producto[]; totalPages: number; totalElements: number }> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<{ content: Producto[]; totalPages: number; totalElements: number }>(this.apiUrl, { headers });
  }

  getProductById(id: number): Observable<Producto> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<Producto>(`${this.apiUrl}/${id}`, { headers });
  }

  createProduct(product: Producto): Observable<Producto> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.post<Producto>(this.apiUrl, product, { headers });
  }

  updateProduct(id: number, product: ProductoActualizar): Observable<ProductoActualizar> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.patch<ProductoActualizar>(`${this.apiUrl}/${id}`, product, { headers });
  }

  deleteProduct(id: number): Observable<void> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }

  obtenerProductos(): Observable<Producto[]> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<Producto[]>(this.apiUrl, { headers });
  }
}