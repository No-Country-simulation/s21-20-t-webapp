import { Component, OnInit, signal, effect } from '@angular/core';
import { TransactionService } from '../../Services/transaction.service';
import { ProductService } from '../../Services/producto.service';
import { AuthService } from '../../Services/auth.service';
import { Transaction, TransactionType, Producto, Inventory } from '../../../Models/Models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css'],
})
export class TransactionsComponent implements OnInit {
  TransactionType = TransactionType;
  transactions = signal<Transaction[]>([]);
  filteredTransactions = signal<Transaction[]>([]);
  filteredProducts = signal<Producto[]>([]);
  startDate = signal<string>('');
  endDate = signal<string>('');
  editingTransaction = signal<Transaction | null>(null);
  newTransaction = signal<Transaction>({
    tenantId: 0,
    productId: 0,
    quantity: 0,
    type: TransactionType.ENTRADA,
    reference: '',
    notes: '',
    createdById: 0,
  });
  showOperationForm = signal<boolean>(true);
  selectedOperation = signal<TransactionType | null>(null);
  products = signal<Producto[]>([]);
  selectedProducts = signal<Producto[]>([]);
  searchQuery = signal<string>('');
  inventory = signal<Inventory | null>(null);
  currentPage = signal(0);
  pageSize = signal(10);
  totalPages = signal(0);
  totalElements = signal(0);
  locationFilter = signal<string>('');

  constructor(
    private transactionService: TransactionService,
    private productService: ProductService,
    private authService: AuthService,
    private router: Router
  ) {
    effect(() => {
      this.authService.getUserProfile().subscribe(user => {
        this.loadTransactions(user.tenantId);
        this.loadProducts(user.tenantId);
      });
    });
  }

  ngOnInit(): void {
    this.filterTransactions();
  }

  loadTransactions(tenantId: number): void {
    this.transactionService.getTransactions(this.currentPage(), this.pageSize()).subscribe({
      next: (response) => {
        this.transactions.set(response.content);
        this.totalPages.set(response.pageable.totalElements / this.pageSize());
        this.totalElements.set(response.pageable.totalElements);
        this.filterTransactions();
      },
      error: (error) => {
        console.error('Error cargando transacciones:', error);
      },
    });
  }

  loadProducts(tenantId: number): void {
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products.set(products.content.filter(product => product.tenantId === tenantId));
      },
      error: (error) => {
        console.error('Error cargando productos:', error);
      },
    });
  }

  searchProducts(): void {
    const searchText = this.searchQuery().toLowerCase();
    this.filteredProducts.set(this.products().filter(product => {
      return product.nombre.toLowerCase().includes(searchText);
    }));
  }

  filterTransactions(): void {
    const location = this.locationFilter().toLowerCase();
    this.filteredTransactions.set(this.transactions().filter(transaction => {
      return transaction.reference.toLowerCase().includes(location);
    }));
  }

  editTransaction(transaction: Transaction): void {
    this.editingTransaction.set({ ...transaction });
  }

  saveTransaction(): void {
    this.authService.getUserProfile().subscribe(user => {
      const selectedType = this.selectedOperation();
      
  
      if (!selectedType) {
        console.error('Debe seleccionar un tipo de operación');
        return;
      }
  
      const transactions: Transaction[] = this.selectedProducts().map(product => ({
        tenantId: user.tenantId,
        productId: product.id!,
        quantity: product.stock!,
        type: selectedType, 
        reference: '',
        notes: '',
        createdById: user.id,
      }));
  
      
  
      this.transactionService.createTransaction(transactions[0]).subscribe({
        next: () => {
          this.selectedProducts.set([]);
          this.showOperationForm.set(false);
          this.loadTransactions(user.tenantId);
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          console.error('Error creando transacción:', error);
        },
      });
    });
  }
  

  cancelEdit(): void {
    this.editingTransaction.set(null);
  }

  selectOperation(operation: TransactionType): void {
    console.log('Intentando asignar operación:', operation);
    
    if (!Object.values(TransactionType).includes(operation)) {
      console.error('Operación inválida:', operation);
      return;
    }
  
    this.selectedOperation.set(operation);
    console.log('Operación seleccionada después de set:', this.selectedOperation());
  }
  addProduct(product: Producto): void {
    if (!this.selectedProducts().some(p => p.id === product.id)) {
      this.selectedProducts.update(products => [...products, { ...product, stock: 1 }]);
    }
  }

  removeProduct(product: Producto): void {
    this.selectedProducts.update(products => products.filter(p => p.id !== product.id));
  }

  saveTransactions(): void {
    this.authService.getUserProfile().subscribe(user => {
      const selectedType = this.selectedOperation();
      
      if (!selectedType) {
        console.error('Error: Debe seleccionar un tipo de operación');
        return;
      }
  
      
  
      const transactions: Transaction[] = this.selectedProducts().map(product => ({
        tenantId: user.tenantId,
        productId: product.id!,
        quantity: product.stock!,
        type: selectedType as TransactionType, // <-- Asegurar que es del tipo correcto
        reference: '',
        notes: '',
        createdById: user.id,
      }));
  
      
  
      if (!transactions[0].type) {
        console.error('Error: El campo "type" sigue siendo null antes de enviarlo.');
        return;
      }
  
      this.transactionService.createTransaction(transactions[0]).subscribe({
        next: () => {
          this.selectedProducts.set([]);
          this.showOperationForm.set(false);
          this.loadTransactions(user.tenantId);
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          console.error('Error creando transacción:', error);
        },
      });
    });
  }
  

  changePage(page: number): void {
    this.currentPage.set(page);
    this.authService.getUserProfile().subscribe(profile => {
      this.loadTransactions(profile.tenantId);
    });
  }

  getPages(): number[] {
    const pageCount = Math.ceil(this.totalElements() / this.pageSize());
    return Array(pageCount).fill(0).map((_, index) => index);
  }

  getProductName(productId: number): string {
    const product = this.products().find(p => p.id === productId);
    return product ? product.nombre : '';
  }

  deleteTransaction(id: number): void {
    this.transactionService.deleteTransaction(id).subscribe({
      next: () => {
        this.authService.getUserProfile().subscribe(profile => {
          this.loadTransactions(profile.tenantId);
        });
      },
      error: (error) => {
        console.error('Error eliminando transacción:', error);
      },
    });
  }
}