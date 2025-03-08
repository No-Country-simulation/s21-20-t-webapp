import { Component, OnInit } from '@angular/core';
import { CategoriaService } from '../../Services/categoria.service';
import { AuthService } from '../../Services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoriaProducto } from '../../../Models/Models';
import { ProductComponent } from '../productos/productos.component';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule, FormsModule, ProductComponent],
  templateUrl: './categorias.component.html',
  styleUrls: ['./categorias.component.css'],
})
export class CategoryComponent implements OnInit {
  categories: CategoriaProducto[] = [];
  newCategory: CategoriaProducto = {
    tenantId: 0,
    categoriaId: 0,
    nombre: '',
    sku: '',
    camposPersonalizados: {},
  };
  selectedCategory: CategoriaProducto | null = null;
  isEditing = false;
  showProductComponent = false;
  selectedCategoryForProducts: CategoriaProducto | null = null;
  editingCategory: CategoriaProducto | null = null;

  constructor(
    private categoriaService: CategoriaService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  

  loadCategories(): void {
    this.authService.getUserProfile().subscribe(
      (user) => {
        if (user && user.tenantId) {
          this.categoriaService.getCategories().subscribe((response) => { // Cambiado a response
            if (response && response.content && Array.isArray(response.content)) { // Verificar si content existe y es un array
              this.categories = response.content.filter(
                (category) => category.tenantId === user.tenantId
              );
            } else {
              console.error('La respuesta del backend no contiene un array de categorías válido');
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


  createCategory(): void {
    this.authService.getUserProfile().subscribe(
      (user) => {
        if (user && user.tenantId) {
          this.newCategory.tenantId = user.tenantId; // Asegurarse de que el tenantId es correcto
          this.categoriaService.createCategoriaProducto(this.newCategory).subscribe(() => {
            this.loadCategories();
            // Limpiar solo los campos necesarios, manteniendo tenantId
            this.newCategory = {
              tenantId: user.tenantId, // Mantener el tenantId del usuario
              categoriaId: 0,
              nombre: '',
              sku: '',
              camposPersonalizados: {},
            };
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

  selectCategory(category: CategoriaProducto): void {
    this.selectedCategory = { ...category };
    this.isEditing = true;
  }

  updateCategory(): void {
    if (this.selectedCategory && this.selectedCategory.id) {
      this.categoriaService.updateCategoriaProducto(this.selectedCategory.id, this.selectedCategory).subscribe(() => {
        this.loadCategories();
        this.selectedCategory = null;
        this.isEditing = false;
      });
    }
  }

  deleteCategory(id: number): void {
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
        this.categoriaService.deleteCategoriaProducto(id).subscribe(() => {
          this.loadCategories();
          Swal.fire('Eliminado!', 'La categoría ha sido eliminada.', 'success');
        });
      }
    });
  }

  cancelEdit(): void {
    this.selectedCategory = null;
    this.isEditing = false;
  }

  showProducts(category: CategoriaProducto): void {
    this.selectedCategoryForProducts = category;
    this.showProductComponent = true;
  }

  
}