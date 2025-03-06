import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../Services/producto.service';
import { CategoriaService } from '../../Services/categoria.service';
import { AuthService } from '../../Services/auth.service';
import { InventoryService } from '../../Services/inventario.service';
import { Producto, CategoriaProducto, Usuario, ProductoActualizar, Inventory } from '../../../Models/Models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, FormsModule],
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
    filteredProducts: { [key: number]: Producto[] } = {};

    editingProductId: number | null = null;
    editedStock: number | null = null;

    constructor(
        private productService: ProductService,
        private categoriaService: CategoriaService,
        private authService: AuthService,
        private inventoryService: InventoryService // Inyecta el servicio de inventario
    ) { }

    ngOnInit(): void {
        this.authService.getUserProfile().subscribe({
            next: (user) => {
                this.userProfile = user;
                this.loadData(user.tenantId);
            },
            error: (err) => {
                console.error('Error obteniendo perfil del usuario:', err);
                this.error = 'Error al cargar el perfil del usuario. Inténtalo de nuevo.';
                this.loading = false;
            },
        });
    }

    loadData(tenantId: number): void {
        this.loadCategories(tenantId);
        this.loadProducts(tenantId);
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
                if (data && data.content && Array.isArray(data.content)) {
                    this.products = data.content.filter((product) => product.tenantId === tenantId);
                    this.getInventoryForProducts(tenantId); // Obtén el inventario para los productos
                } else {
                    console.error('La respuesta del backend no contiene un array de productos válido');
                    this.error = 'Error al cargar los productos.';
                    this.loading = false;
                }
            },
            error: (err) => {
                console.error('Error obteniendo productos:', err);
                this.error = 'Error al cargar los productos. Inténtalo de nuevo.';
                this.loading = false;
            },
        });
    }

    getInventoryForProducts(tenantId: number): void {
        this.inventoryService.getInventories().subscribe({
            next: (inventoryData) => {
                if (inventoryData && inventoryData.content && Array.isArray(inventoryData.content)) {
                    const inventoryMap: { [productId: number]: number } = {};
                    inventoryData.content.forEach((inventory: Inventory) => {
                        inventoryMap[inventory.productId] = inventory.quantity;
                    });

                    this.products.forEach((product) => {
                        product.stock = inventoryMap[product.id!] || 0;
                    });

                    this.filterProductsByCategory();
                    this.loadProductTotals(tenantId);
                } else {
                    console.error('La respuesta del backend no contiene un array de inventario válido');
                    this.error = 'Error al cargar el inventario.';
                }
                this.loading = false;
            },
            error: (err) => {
                console.error('Error obteniendo inventario:', err);
                this.error = 'Error al cargar el inventario. Inténtalo de nuevo.';
                this.loading = false;
            },
        });
    }

    loadProductTotals(tenantId: number): void {
        if (this.products && Array.isArray(this.products)) {
            const tenantProducts = this.products.filter((product) => product.tenantId === tenantId);
            this.totalProductos = tenantProducts.length;
            this.totalStock = tenantProducts.reduce((acc, product) => acc + (product.stock || 0), 0);
        } else {
            console.error('this.products no es un array o es undefined/null.');
        }
    }

    filterProductsByCategory(): void {
        this.filteredProducts = {};
        this.products.forEach((product) => {
            if (product.categoriaId) {
                if (!this.filteredProducts[product.categoriaId]) {
                    this.filteredProducts[product.categoriaId] = [];
                }
                this.filteredProducts[product.categoriaId].push(product);
            }
        });
    }

    editStock(productId: number, stock: number): void {
        this.editingProductId = productId;
        this.editedStock = stock;
    }

    saveStock(product: Producto): void {
        if (this.editedStock !== null && product.id) {
            const updatedProduct: ProductoActualizar = {
                id: product.id,
                tenantId: product.tenantId,
                categoriaId: product.categoriaId,
                nombre: product.nombre,
                sku: product.sku,
                camposPersonalizados: product.camposPersonalizados,
            };

            this.productService.updateProduct(product.id, updatedProduct).subscribe({
                next: () => {
                    this.editingProductId = null;
                    this.editedStock = null;
                    this.loadProducts(this.userProfile!.tenantId);
                },
                error: (err) => {
                    console.error('Error actualizando stock:', err);
                    this.error = 'Error al actualizar el stock. Inténtalo de nuevo.';
                },
            });
        }
    }

    cancelEdit(): void {
        this.editingProductId = null;
        this.editedStock = null;
    }
}