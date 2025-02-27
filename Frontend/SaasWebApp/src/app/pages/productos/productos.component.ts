import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoriaService } from '../../Services/categoria.service';
import { ProductoService } from '../../Services/producto.service';
import { AuthService } from '../../Services/auth.service';
import { CategoriaProducto } from '../../../Models/Models';
import { Producto } from '../../../Models/Models';
@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [ CommonModule,
    FormsModule],
  templateUrl: './productos.component.html',
  styleUrls: ['./productos.component.css'],
})
export class ProductosComponent implements OnInit {
  categorias: CategoriaProducto[] = [];
  productos: Producto[] = [];
  nuevaCategoria: CategoriaProducto = {
    id: 0,
    tenantId: 0,
    nombre: '',
    camposPersonalizados: {},
    creadoEn: new Date(),
  };
  nuevoProducto: Producto = {
    id: 0,
    tenantId: 0,
    categoriaId: 0,
    nombre: '',
    sku: '',
    camposPersonalizados: {},
    stock: 0,
    creadoEn: new Date(),
  };
  productoSeleccionado: Producto | null = null;
  errorMessage: string = '';

  constructor(
    private categoriaService: CategoriaService,
    private productoService: ProductoService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.authService.getUserProfile().subscribe((userProfile) => {
      if (userProfile && userProfile.tenantId) {
        this.categoriaService
          .getCategorias(userProfile.tenantId)
          .subscribe({
            next: (categorias) => (this.categorias = categorias),
            error: (err) => (this.errorMessage = 'Error al cargar categorías'),
          });
      }
    });
  }

  cargarProductos(categoriaId: number): void {
    this.authService.getUserProfile().subscribe((userProfile) => {
      if (userProfile && userProfile.tenantId) {
        this.productoService
          .getProductos(userProfile.tenantId, categoriaId)
          .subscribe({
            next: (productos) => (this.productos = productos),
            error: (err) => (this.errorMessage = 'Error al cargar productos'),
          });
        this.nuevoProducto.tenantId = userProfile.tenantId;
        this.nuevoProducto.categoriaId = categoriaId;
      }
    });
  }

  crearProducto(): void {
    this.productoService.crearProducto(this.nuevoProducto).subscribe({
      next: (producto) => {
        this.productos.push(producto);
        this.nuevoProducto.nombre = '';
        this.nuevoProducto.sku = '';
      },
      error: (err) => (this.errorMessage = 'Error al crear producto'),
    });
  }

  editarProducto(): void {
    if (this.productoSeleccionado) {
      this.productoService
        .actualizarProducto(this.productoSeleccionado)
        .subscribe({
          next: () => {
            this.productoSeleccionado = null;
          },
          error: (err) => (this.errorMessage = 'Error al editar producto'),
        });
    }
  }

  eliminarProducto(productoId: number): void {
    this.productoService.eliminarProducto(productoId).subscribe({
      next: () => {
        this.productos = this.productos.filter(
          (producto) => producto.id !== productoId
        );
      },
      error: (err) => (this.errorMessage = 'Error al eliminar producto'),
    });
  }

  seleccionarProducto(producto: Producto): void {
    this.productoSeleccionado = { ...producto }; // Copia el producto para la edición
  }
}