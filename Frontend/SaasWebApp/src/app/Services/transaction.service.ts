import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction, TransactionResponse, TransactionFilter } from '../../Models/Models';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private apiUrl = 'https://inventory-lrkf.onrender.com/transactional';

  constructor(private http: HttpClient) {}

  getTransactions(page: number = 0, size: number = 10): Observable<TransactionResponse> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get<TransactionResponse>(this.apiUrl, { headers, params });
  }

  createTransaction(transaction: Transaction): Observable<Transaction> {
    console.log('Enviando transacción al backend:', transaction);
    
  
    if (!transaction.type) {
      console.error('Error: El campo "type" es obligatorio y no puede ser null.');
      throw new Error('El campo "type" es obligatorio y no puede ser null.');
    }
  
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.post<Transaction>(this.apiUrl, transaction, { headers });
  }
  

  getTransactionById(id: number): Observable<Transaction> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<Transaction>(`${this.apiUrl}/${id}`, { headers });
  }

  updateTransaction(id: number, transaction: Partial<Transaction>): Observable<Transaction> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.patch<Transaction>(`${this.apiUrl}/${id}`, transaction, { headers });
  }

  deleteTransaction(id: number): Observable<void> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }

  searchTransactions(filters: TransactionFilter, page: number = 0, size: number = 10): Observable<TransactionResponse> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    let params = new HttpParams({ fromObject: { ...filters, page: page.toString(), size: size.toString() } });
    return this.http.get<TransactionResponse>(`${this.apiUrl}/search`, { headers, params });
  }

  getTransactionsByDateRange(startDate: string, endDate: string, page: number = 0, size: number = 10): Observable<TransactionResponse> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    let params = new HttpParams().set('startDate', startDate).set('endDate', endDate).set('page', page.toString()).set('size', size.toString());
    return this.http.get<TransactionResponse>(`${this.apiUrl}/date-range`, { headers, params });
  }
}