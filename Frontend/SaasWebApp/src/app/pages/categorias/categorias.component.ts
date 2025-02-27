import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoriaService } from '../../Services/categoria.service';
import { CategoriaProducto, Producto, Inventario } from '../../../Models/Models';
import { AuthService } from '../../Services/auth.service';
import { ProductoService } from '../../Services/producto.service';
import { InventarioService } from '../../Services/inventario.service';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categorias.component.html',
})
export class CategoriasComponent implements OnInit {
  categorias: CategoriaProducto[] = [];
  nuevaCategoria: CategoriaProducto = {
    id: 0, // Cambiado a number
    tenantId: 0, // Cambiado a number
    nombre: '',
    camposPersonalizados: {},
    creadoEn: new Date(),
  };
  errorMessage: string = '';

  productos: Producto[] = [];
  nuevoProducto: Producto = {
    id: 0, // Cambiado a number
    tenantId: 0, // Cambiado a number
    categoriaId: 0, // Cambiado a number
    nombre: '',
    sku: '',
    camposPersonalizados: {},
    stock: 0,
    creadoEn: new Date(),
  };
  productoSeleccionado: Producto | null = null;
  inventario: Inventario[] = [];
  inventarioSeleccionado: Inventario | null = null;

  constructor(
    private categoriaService: CategoriaService,
    private authService: AuthService,
    private productoService: ProductoService,
    private inventarioService: InventarioService
  ) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.authService.getUserProfile().subscribe((userProfile) => {
      if (userProfile && userProfile.tenantId) {
        this.categoriaService.getCategorias(userProfile.tenantId).subscribe({
          next: (categorias) => (this.categorias = categorias),
          error: (err) => (this.errorMessage = 'Error al cargar categorías'),
        });
        this.nuevaCategoria.tenantId = userProfile.tenantId;
      }
    });
  }

  crearCategoria(): void {
    this.categoriaService.crearCategoria(this.nuevaCategoria).subscribe({
      next: (categoria) => {
        this.categorias.push(categoria);
        this.nuevaCategoria.nombre = '';
      },
      error: (err) => (this.errorMessage = 'Error al crear categoría'),
    });
  }

  cargarProductos(categoriaId: number): void { // Cambiado a number
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
            if (this.nuevoProducto.categoriaId) {
              this.cargarProductos(this.nuevoProducto.categoriaId);
            }
          },
          error: (err) => (this.errorMessage = 'Error al editar producto'),
        });
    }
  }

  eliminarProducto(productoId: number): void { // Cambiado a number
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
    this.productoSeleccionado = { ...producto };
  }

  cargarInventario(productoId: number): void { // Cambiado a number
    this.authService.getUserProfile().subscribe((userProfile) => {
      if (userProfile && userProfile.tenantId) {
        this.inventarioService
          .getInventario(userProfile.tenantId, productoId)
          .subscribe({
            next: (inventario) => (this.inventario = inventario),
            error: (err) => (this.errorMessage = 'Error al cargar inventario'),
          });
      }
    });
  }

  editarInventario(): void {
    if (this.inventarioSeleccionado) {
      this.inventarioService
        .actualizarInventario(this.inventarioSeleccionado)
        .subscribe({
          next: () => {
            this.inventarioSeleccionado = null;
            this.cargarInventario(this.inventario[0].productoId);
          },
          error: (err) => (this.errorMessage = 'Error al editar inventario'),
        });
    }
  }

  seleccionarInventarioItem(inventario: Inventario): void {
    this.inventarioSeleccionado = { ...inventario };
  }
}