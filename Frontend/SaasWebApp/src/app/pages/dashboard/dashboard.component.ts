import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../Services/producto.service';
import { Producto } from '../../../Models/Models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  totalProductos = 0;
  totalStock = 0;

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void {
    this.productoService.obtenerProductos('someId').subscribe((productos: Producto[]) => {
      this.totalProductos = productos.length;
      this.totalStock = productos.reduce((acc: number, p: Producto) => acc + p.stock, 0);
    });
  }
}

