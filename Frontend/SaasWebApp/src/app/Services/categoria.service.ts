import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoriaProducto } from '../../Models/Models';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  private apiUrl = 'https://inventory-lrkf.onrender.com/categorias_productos';

  constructor(private http: HttpClient) {}

  getCategories(page: number = 0, size: number = 10): Observable<{ content: CategoriaProducto[]; totalPages: number; totalElements: number }> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
        Authorization: `Bearer ${token}`,
    });

    let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());

    return this.http.get<{ content: CategoriaProducto[]; totalPages: number; totalElements: number }>(this.apiUrl, { headers: headers, params: params });
}


  getCategoriaProductoById(id: number): Observable<CategoriaProducto> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<CategoriaProducto>(`${this.apiUrl}/${id}`, { headers });
  }

  createCategoriaProducto(categoria: CategoriaProducto): Observable<CategoriaProducto> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.post<CategoriaProducto>(this.apiUrl, categoria, { headers });
  }

  updateCategoriaProducto(id: number, categoria: CategoriaProducto): Observable<CategoriaProducto> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.patch<CategoriaProducto>(`${this.apiUrl}/${id}`, categoria, { headers });
  }

  deleteCategoriaProducto(id: number): Observable<void> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }
}