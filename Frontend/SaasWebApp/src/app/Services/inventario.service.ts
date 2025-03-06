// src/app/Services/inventario.service.ts
// inventory.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Inventory, InventoryResponse, InventorySearch } from '../../Models/Models';

@Injectable({
  providedIn: 'root',
})
export class InventoryService {
  private apiUrl = 'https://inventory-lrkf.onrender.com/inventory';

  constructor(private http: HttpClient) {}

  getInventories(page: number = 0, size: number = 10): Observable<InventoryResponse> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<InventoryResponse>(this.apiUrl, { params });
  }

  getInventory(id: number): Observable<Inventory> {
    return this.http.get<Inventory>(`${this.apiUrl}/${id}`);
  }

  createInventory(inventory: Inventory): Observable<Inventory> {
    return this.http.post<Inventory>(this.apiUrl, inventory);
  }

  updateInventory(id: number, inventory: Inventory): Observable<Inventory> {
    return this.http.patch<Inventory>(`${this.apiUrl}/${id}`, inventory);
  }

  deleteInventory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchInventories(searchParams: InventorySearch): Observable<InventoryResponse> {
    let params = new HttpParams();
    if (searchParams.location) params = params.set('location', searchParams.location);
    
    if (searchParams.tenantId) params = params.set('tenantId', searchParams.tenantId.toString());
    if (searchParams.productId) params = params.set('productId', searchParams.productId.toString());
    

    return this.http.get<InventoryResponse>(`${this.apiUrl}/search`, { params });
  }

  getInventoriesByDateRange(startDate: string, endDate: string, page: number = 0, size: number = 10): Observable<InventoryResponse> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<InventoryResponse>(`${this.apiUrl}/date-range`, { params });
  }
}