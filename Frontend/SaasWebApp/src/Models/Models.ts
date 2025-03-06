export interface Tenant {
    id: number;
    nombre: string;
    configuracion: any; // JSONB puede ser cualquier estructura
    creadoEn?: Date;
    actualizadoEn?: Date;
  }
  
  export interface Usuario {
    id: number;
    email: string;
    name: string;
    lastName: string;
    phoneNumber: number;
    birthDate: string;
    tenantId: number;
    registerDate: string;
    lastLogin: string;
  }
  
  export interface CategoriaProducto {
    id?: number;
    tenantId: number;
    categoriaId: number;
    nombre: string;
    sku: string;
    camposPersonalizados: { [key: string]: string };
    creadoEn?: string;
    actualizadoEn?: string;
  }
  export interface AuthResponse {
    token: string;
  }
  
  export interface Producto {
    id?: number; // Agregado y opcional
    tenantId: number;
    categoriaId: number;
    nombre: string;
    sku: string;
    camposPersonalizados: {
    [key: string]: string;
  };
    stock?: number;
  }

  export interface ProductoActualizar {
    id: number;
    tenantId: number;
    categoriaId: number;
    nombre: string;
    sku: string;
    camposPersonalizados: { [key: string]: string };
  }
  
  
  export interface Inventory {
    id: number;
    productId: number;
    tenantId: number;
    quantity: number;
    location: string;
  }
  
  export interface InventoryResponse {
    content: Inventory[];
    totalPages: number;
    totalElements: number;
  }
  
  export interface InventorySearch {
    id?: number;
    location?: string;                          
    tenantId?: number;
    productId?: number;
    quantity?: number;
  }
  
  export interface Transaccion {
    id: number;
    tenantId: number;
    productoId: number;
    cantidad: number;
    tipo: 'entrada' | 'salida' | 'ajuste';
    referencia?: string;
    notas?: string;
    creadoPor: string; // Usuario que creó la transacción
    creadoEn?: Date;
  }
  