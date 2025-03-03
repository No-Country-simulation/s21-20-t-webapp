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
  
  
  export interface Inventario {
    id: number;
    productoId: number;
    tenantId: number;
    cantidad: number;
    ubicacion?: string;
    creadoEn?: Date;
    actualizadoEn?: Date;
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
  