import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'; // Importa HttpParams
import { Observable } from 'rxjs';
import { Producto, ProductoActualizar } from '../../Models/Models';

@Injectable({
    providedIn: 'root',
})
export class ProductService {
    private apiUrl = 'https://inventory-lrkf.onrender.com/products';

    constructor(private http: HttpClient) { }

    getProducts(page: number = 0, size: number = 10): Observable<{ content: Producto; totalPages: number; totalElements: number }> {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });

        let params = new HttpParams() // Usa HttpParams para construir los parámetros
            .set('page', page.toString())
            .set('size', size.toString());

        return this.http.get<{ content: Producto; totalPages: number; totalElements: number }>(this.apiUrl, { headers: headers, params: params });
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

    obtenerProductos(): Observable<Producto> {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });
        return this.http.get<Producto>(this.apiUrl, { headers });
    }
}