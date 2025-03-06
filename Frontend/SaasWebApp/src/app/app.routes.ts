import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ProductComponent } from './pages/productos/productos.component';
import { CategoryComponent } from './pages/categorias/categorias.component';
import { UsuariosComponent } from './pages/usuarios/usuarios.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { AuthGuard } from './guards/auth.guard';
import { InventoryComponent } from './pages/inventario/inventario.component'; // Importa InventoryComponent

export const routes: Routes = [
  { path: '', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'productos', component: ProductComponent, canActivate: [AuthGuard] },
  { path: 'categorias', component: CategoryComponent, canActivate: [AuthGuard] },
  { path: 'usuarios', component: UsuariosComponent, canActivate: [AuthGuard] },
  { path: 'inventario', component: InventoryComponent, canActivate: [AuthGuard] }, // Agrega la ruta del inventario
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '**', redirectTo: '' },
];

