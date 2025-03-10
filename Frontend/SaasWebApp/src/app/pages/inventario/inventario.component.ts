import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Inventory, InventoryResponse, InventorySearch } from '../../../Models/Models';
import { InventoryService } from '../../Services/inventario.service';
import { AuthService } from '../../Services/auth.service';
import { ProductService } from '../../Services/producto.service';
import { CategoriaService } from '../../Services/categoria.service';
import { Producto, CategoriaProducto } from '../../../Models/Models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-inventario',
  imports: [CommonModule, FormsModule],
  templateUrl: './inventario.component.html',
  styleUrls: ['./inventario.component.css']
})
export class InventoryComponent implements OnInit {
  inventories: Inventory[] = [];
  newInventory: Inventory = { id: 0, productId: 0, tenantId: 0, quantity: 0, location: '' };
  selectedInventory: Inventory | null = null;
  isEditing = false;
  searchParams: InventorySearch = {};
  totalPages = 0;
  totalElements = 0;
  currentPage = 0;
  pageSize = 10;
  loading = false;
  error: string | null = null;
  success: string | null = null;
  productos: Producto[] = [];
  categorias: CategoriaProducto[] = [];

  constructor(
    private inventoryService: InventoryService,
    private authService: AuthService,
    private productService: ProductService,
    private categoriaService: CategoriaService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadInventories();
    this.loadProductos();
    this.loadCategorias();
  }

  loadInventories(): void {
    this.loading = true;
    this.inventoryService.getInventories(this.currentPage, this.pageSize).subscribe({
      next: (response: InventoryResponse) => {
        this.inventories = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar inventarios: ' + err.message;
        this.loading = false;
      }
    });
  }

  loadProductos(): void {
    this.authService.getUserProfile().subscribe(
      (user) => {
        if (user && user.tenantId) {
          this.productService.getProducts().subscribe((response) => { 
            if (response && response.content && Array.isArray(response.content)) { 
              this.productos = response.content.filter(
                (producto) => producto.tenantId === user.tenantId
              );
            } else {
              console.error('La respuesta del backend no contiene un array de productos válido');
            }
          });
        } else {
          console.error('tenantId no encontrado en el perfil del usuario');
        }
      },
      (error) => {
        console.error('Error obteniendo perfil del usuario:', error);
      }
    );
  }

  loadCategorias(): void {
    this.categoriaService.getCategories().subscribe({
      next: (categorias) => {
        this.categorias = categorias.content;
      },
      error: (err) => {
        this.error = 'Error al cargar categorías: ' + err.message;
      }
    });
  }

  createInventory(): void {
    this.loading = true;
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        if (user && user.tenantId) {
          this.newInventory.tenantId = user.tenantId;
          this.inventoryService.createInventory(this.newInventory).subscribe({
            next: (inventory) => {
              this.inventories.push(inventory);
              this.newInventory = { id: 0, productId: 0, tenantId: 0, quantity: 0, location: '' };
              this.success = 'Inventario creado exitosamente.';
              this.loading = false;
            },
            error: (err) => {
              this.error = 'Error al crear inventario: ' + err.message;
              this.loading = false;
            }
          });
        } else {
          this.error = 'Error al obtener perfil del usuario.';
          this.loading = false;
        }
      },
      error: (err) => {
        this.error = 'Error al obtener perfil del usuario: ' + err.message;
        this.loading = false;
      }
    });
  }

  selectInventory(inventory: Inventory): void {
    this.selectedInventory = { ...inventory };
    this.isEditing = true;
  }

  updateInventory(): void {
    if (this.selectedInventory) {
      this.loading = true;
      this.inventoryService.updateInventory(this.selectedInventory.id, this.selectedInventory).subscribe({
        next: (inventory) => {
          const index = this.inventories.findIndex(i => i.id === inventory.id);
          if (index !== -1) {
            this.inventories[index] = inventory;
          }
          this.selectedInventory = null;
          this.isEditing = false;
          this.success = 'Inventario actualizado exitosamente.';
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error al actualizar inventario: ' + err.message;
          this.loading = false;
        }
      });
    }
  }

  deleteInventory(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'No podrás revertir esto.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.loading = true;
        this.inventoryService.deleteInventory(id).subscribe({
          next: () => {
            this.inventories = this.inventories.filter(i => i.id !== id);
            this.success = 'Inventario eliminado exitosamente.';
            this.loading = false;
            Swal.fire('Eliminado!', 'El inventario ha sido eliminado.', 'success'); // Mensaje de éxito
          },
          error: (err) => {
            this.error = 'Error al eliminar inventario: ' + err.message;
            this.loading = false;
            Swal.fire('Error', 'Error al eliminar el inventario.', 'error'); // Mensaje de error
          }
        });
      }
    });
  }

  cancelEdit(): void {
    this.selectedInventory = null;
    this.isEditing = false;
  }

  searchInventories(): void {
    this.loading = true;
    this.error = null;
    this.success = null;
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        if (user && user.tenantId) {
          this.searchParams.tenantId = user.tenantId;
          
          this.inventoryService.searchInventories(this.searchParams).subscribe({
            next: (response) => {
              console.log('Search Response:', response);
              this.inventories = response.content;
              this.totalPages = response.totalPages;
              this.totalElements = response.totalElements;
              this.loading = false;
              console.log('Inventories:', this.inventories);
              this.cdr.detectChanges();
            },
            error: (err) => {
              this.error = 'Error al buscar inventarios: ' + err.message;
              this.loading = false;
            },
          });
        } else {
          this.error = 'Error al obtener perfil del usuario.';
          this.loading = false;
        }
      },
      error: (err) => {
        this.error = 'Error al obtener perfil del usuario: ' + err.message;
        this.loading = false;
      },
    });
  }

  
}