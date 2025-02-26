import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto, Inventario } from '../../Models/Models';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  private apiUrl = 'https://tu-api.com/productos';
  private inventarioUrl = 'https://tu-api.com/inventarios';

  constructor(private http: HttpClient) {}

  // ✅ Este método debe ser usado en ProductosComponent
  obtenerProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }

  obtenerInventarios(): Observable<Inventario[]> {
    return this.http.get<Inventario[]>(this.inventarioUrl);
  }

  agregarProducto(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  actualizarProducto(id: string, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }

  eliminarProducto(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
