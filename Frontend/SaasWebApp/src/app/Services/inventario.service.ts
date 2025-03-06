import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Inventory, InventoryResponse, InventorySearch } from '../../Models/Models';

@Injectable({
  providedIn: 'root',
})
export class InventoryService {
  private apiUrl = 'https://inventory-lrkf.onrender.com/inventory';
  private searchUrl = 'https://inventory-lrkf.onrender.com/inventory/search';

  constructor(private http: HttpClient) {}

  getInventories(page: number = 0, size: number = 10): Observable<InventoryResponse> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<InventoryResponse>(this.apiUrl, { headers, params });
  }

  getInventory(id: number): Observable<Inventory> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<Inventory>(`${this.apiUrl}/${id}`, { headers });
  }

  createInventory(inventory: Inventory): Observable<Inventory> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.post<Inventory>(this.apiUrl, inventory, { headers });
  }

  updateInventory(id: number, inventory: Inventory): Observable<Inventory> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.patch<Inventory>(`${this.apiUrl}/${id}`, inventory, { headers });
  }

  deleteInventory(id: number): Observable<void> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }

  searchInventories(params: InventorySearch & { startDate?: string | Date; endDate?: string | Date }): Observable<InventoryResponse> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    let httpParams = new HttpParams();
    if (params.location) httpParams = httpParams.set('location', params.location);
    if (params.startDate) {
      if(typeof params.startDate === 'string'){
        httpParams = httpParams.set('startDate', new Date(params.startDate).toISOString());
      } else {
        httpParams = httpParams.set('startDate', params.startDate.toISOString());
      }
    }
    if (params.endDate) {
      if(typeof params.endDate === 'string'){
        httpParams = httpParams.set('endDate', new Date(params.endDate).toISOString());
      } else {
        httpParams = httpParams.set('endDate', params.endDate.toISOString());
      }
    }
    if (params.id) httpParams = httpParams.set('id', params.id.toString());
    if (params.tenantId) httpParams = httpParams.set('tenantId', params.tenantId.toString());
    if (params.productId) httpParams = httpParams.set('productId', params.productId.toString());
    return this.http.get<InventoryResponse>(this.searchUrl, { headers, params: httpParams });
  }

  getInventoriesByDateRange(startDate: Date, endDate: Date): Observable<InventoryResponse> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    const params = new HttpParams()
      .set('startDate', startDate.toISOString())
      .set('endDate', endDate.toISOString());
    return this.http.get<InventoryResponse>(this.searchUrl, { headers, params });
  }
}