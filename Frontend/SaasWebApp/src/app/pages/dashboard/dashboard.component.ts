import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../Services/producto.service';
import { CategoriaService } from '../../Services/categoria.service';
import { AuthService } from '../../Services/auth.service';
import { Producto, CategoriaProducto, Usuario } from '../../../Models/Models';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  totalProductos = 0;
  totalStock = 0;
  loading = true;
  error: string | null = null;

  userProfile: Usuario | null = null;
  categories: CategoriaProducto[] = [];
  products: Producto[] = [];
  filteredProducts: { [key: string]: any[] } = {};

  constructor(
    private productService: ProductService,
    private categoriaService: CategoriaService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        this.userProfile = user;
        this.loadCategories(user.tenantId);
        this.loadProducts(user.tenantId); // Carga los productos directamente aquí
      },
      error: (err) => {
        console.error('Error obteniendo perfil del usuario:', err);
        this.error = 'Error al cargar el perfil del usuario. Inténtalo de nuevo.';
        this.loading = false;
      },
    });

    this.productService.obtenerProductos().subscribe({
      next: (productos: Producto[]) => {
        this.totalProductos = productos.length;
        this.totalStock = productos.reduce((acc, product) => acc + (product.stock || 0), 0);
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error obteniendo productos:', err);
        this.error = 'Error al cargar los productos. Inténtalo de nuevo.';
        this.loading = false;
      },
    });
  }

  loadCategories(tenantId: number): void {
    this.categoriaService.getCategories().subscribe({
      next: (response) => {
        if (response && response.content && Array.isArray(response.content)) {
          this.categories = response.content.filter((category) => category.tenantId === tenantId);
        } else {
          console.error('La respuesta del backend no contiene un array de categorías válido');
          this.error = 'Error al cargar las categorías.';
          this.loading = false;
        }
      },
      error: (err) => {
        console.error('Error obteniendo categorías:', err);
        this.error = 'Error al cargar las categorías. Inténtalo de nuevo.';
        this.loading = false;
      },
    });
  }

  loadProducts(tenantId: number): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data.content.filter((product) => product.tenantId === tenantId);
        this.loading = false;
      },
      error: (err) => {
        console.error('Error obteniendo productos:', err);
        this.error = 'Error al cargar los productos. Inténtalo de nuevo.';
        this.loading = false;
      },
    });
  }
}