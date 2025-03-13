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
    @Input() categoryId: number | undefined;

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

    // Propiedades para la paginación
    currentPage: number = 0;
    pageSize: number = 10; // Ajusta el tamaño de página según tus necesidades
    totalPages: number = 0; // Para almacenar el total de páginas

    constructor(private productService: ProductService, private authService: AuthService) { }

    ngOnInit(): void {
        this.loadProducts();
    }

    loadProducts(): void {
        if (this.categoryId) {
            console.log('categoryId:', this.categoryId);
            this.productService.getProducts(this.currentPage, this.pageSize).subscribe({ // Pasa currentPage y pageSize
                next: (data) => {
                    console.log('Data from service:', data);
                    if (data && data.content && Array.isArray(data.content)) {
                        console.log('data.content before filter:', data.content);
                        this.products = data.content.filter((product) => product.categoriaId === this.categoryId);
                        console.log('data.content after filter:', this.products);
                        this.totalPages = data.totalPages; // Almacena el total de páginas
                    } else {
                        console.error('Invalid data structure received from service.');
                        this.products = [];
                    }
                },
                error: (error) => {
                    console.error('Error fetching products:', error);
                    this.products = [];
                }
            });
        }
    }

    createProduct(): void {
        this.authService.getUserProfile().subscribe({
            next: (user: { tenantId: number } | null) => {
                if (user && user.tenantId && this.categoryId) {
                    this.newProduct.tenantId = user.tenantId;
                    this.newProduct.categoriaId = this.categoryId;
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
        if (this.selectedProduct && this.selectedProduct.nombre && this.selectedProduct.id) {
            const updatedProduct: ProductoActualizar = {
                id: this.selectedProduct.id,
                tenantId: this.selectedProduct.tenantId,
                categoriaId: this.selectedProduct.categoriaId,
                nombre: this.selectedProduct.nombre,
                sku: this.selectedProduct.sku,
                camposPersonalizados: this.selectedProduct.camposPersonalizados,
            };

            this.productService.updateProduct(this.selectedProduct.id, updatedProduct).subscribe(() => {
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
                    Swal.fire('Eliminado!', 'El producto ha sido eliminado.', 'success');
                });
            }
        });
    }

    cancelEdit(): void {
        this.selectedProduct = null;
        this.isEditing = false;
    }

    // Métodos para la paginación
    goToPreviousPage(): void {
        if (this.currentPage > 0) {
            this.currentPage--;
            this.loadProducts();
        }
    }

    goToNextPage(): void {
        if (this.currentPage < this.totalPages - 1) {
            this.currentPage++;
            this.loadProducts();
        }
    }

    goToPage(pageNumber: number): void {
        if (pageNumber >= 0 && pageNumber < this.totalPages) {
            this.currentPage = pageNumber;
            this.loadProducts();
        }
    }

    getPagesArray(): number[]{
        const pagesArray: number[] = [];
        for (let i = 0; i < this.totalPages; i++) {
            pagesArray.push(i);
        }
        return pagesArray;
    }
}