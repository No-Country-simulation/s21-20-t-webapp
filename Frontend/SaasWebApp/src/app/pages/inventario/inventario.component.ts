// src/app/dashboard/inventario/inventario.component.ts
import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InventarioService } from '../../Services/inventario.service';
import { Inventario } from '../../../Models/Models';
import { AuthService } from '../../Services/auth.service';

@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventario.component.html',
})
export class InventarioComponent implements OnInit {
  @Input() productoId: number = 0; // Recibe el ID del producto desde el componente padre
  inventario: Inventario[] = [];
  inventarioSeleccionado: Inventario | null = null;
  errorMessage: string = '';

  constructor(
    private inventarioService: InventarioService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarInventario();
  }

  cargarInventario(): void {
    this.authService.getUserProfile().subscribe((userProfile) => {
      if (userProfile && userProfile.tenantId) {
        this.inventarioService
          .getInventario(userProfile.tenantId, this.productoId)
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
            this.cargarInventario(); // Recargar el inventario después de la actualización
          },
          error: (err) => (this.errorMessage = 'Error al editar inventario'),
        });
    }
  }

  seleccionarInventario(inventario: Inventario): void {
    this.inventarioSeleccionado = { ...inventario };
  }
}
