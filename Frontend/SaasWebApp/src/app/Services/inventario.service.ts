// src/app/Services/inventario.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Inventario } from '../../Models/Models'; // Asegúrate de que la ruta sea correcta

@Injectable({
  providedIn: 'root',
})
export class InventarioService {
  private apiUrl = 'https://inventory-lrkf.onrender.com/inventory'; // Reemplaza con tu URL

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getInventario(tenantId: number, productoId: number): Observable<Inventario[]> {
    return this.http.get<Inventario[]>(`${this.apiUrl}/${tenantId}/${productoId}`, {
      headers: this.getHeaders(),
    });
  }

  actualizarInventario(inventario: Inventario): Observable<Inventario> {
    return this.http.put<Inventario>(`${this.apiUrl}/${inventario.id}`, inventario, {
      headers: this.getHeaders(),
    });
  }
}