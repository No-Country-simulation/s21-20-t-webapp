import { Component, OnInit, Input } from '@angular/core';
import { ProductService } from '../../Services/producto.service';
import { AuthService } from '../../Services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Producto, ProductoActualizar } from '../../../Models/Models';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './productos.component.html',
  styleUrls: ['./productos.component.css'],
})
export class ProductComponent implements OnInit {
  @Input() categoryId: number | undefined; // Recibe categoryId como entrada
  products: Producto[] = [];
  newProduct: Producto = {
    tenantId: 1,
    categoriaId: 1,
    nombre: '',
    sku: '',
    camposPersonalizados: {},
  };
  selectedProduct: ProductoActualizar | null = null;
  isEditing = false;

  constructor(private productService: ProductService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    if (this.categoryId) {
      this.productService.getProducts().subscribe((data) => {
        this.products = data.content.filter((product) => product.categoriaId === this.categoryId);
      });
    }
  }

  createProduct(): void {
    this.authService.getUserProfile().subscribe({
      next: (user: { tenantId: number } | null) => {
      if (user && user.tenantId && this.categoryId) {
        this.newProduct.tenantId = user.tenantId;
        this.newProduct.categoriaId = this.categoryId; // Asegura que categoriaId se mantiene
        this.productService.createProduct(this.newProduct).subscribe(() => {
        this.loadProducts();
        this.newProduct = {
          tenantId: user.tenantId,
          categoriaId: this.categoryId!,
          nombre: '',
          sku: '',
          camposPersonalizados: {},
        };
        });
      } else {
        console.error('tenantId no encontrado en el perfil del usuario o categoryId no definido');
      }
      },
      error: (error: any) => {
      console.error('Error obteniendo perfil del usuario:', error);
      },
    });
  }

  selectProduct(product: Producto): void {
    this.selectedProduct = { ...product, id: product.id! };
    this.isEditing = true;
  }

  updateProduct(): void {
    if (this.selectedProduct && this.selectedProduct.nombre && this.selectedProduct.id) { // Asegúrate de que selectedProduct tenga id
      const updatedProduct: ProductoActualizar = {
        id: this.selectedProduct.id,
        tenantId: this.selectedProduct.tenantId,
        categoriaId: this.selectedProduct.categoriaId,
        nombre: this.selectedProduct.nombre,
        sku: this.selectedProduct.sku,
        camposPersonalizados: this.selectedProduct.camposPersonalizados,
      };
      
      this.productService.updateProduct(this.selectedProduct.id, updatedProduct).subscribe(() => { // Pasa el id y el objeto
        this.loadProducts();
        this.selectedProduct = null;
        this.isEditing = false;
      });
    }
  }

  deleteProduct(id: number): void {
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
        this.productService.deleteProduct(id).subscribe(() => {
          this.loadProducts();
          Swal.fire('Eliminado!', 'El producto ha sido eliminado.', 'success'); // Mensaje de éxito
        });
      }
    });
  }

  cancelEdit(): void {
    this.selectedProduct = null;
    this.isEditing = false;
  }
}