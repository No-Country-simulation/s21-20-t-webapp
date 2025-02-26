import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../Services/producto.service';
import { Producto } from '../../../Models/Models';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './productos.component.html',
  styleUrls: ['./productos.component.css'],
})
export class ProductosComponent implements OnInit {
  productos: Producto[] = [];

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.productoService.obtenerProductos().subscribe((data) => {
      this.productos = data;
    });
  }

  eliminarProducto(id: string): void {
    this.productoService.eliminarProducto(id).subscribe(() => {
      this.productos = this.productos.filter((producto) => producto.id !== id);
    });
  }
}

