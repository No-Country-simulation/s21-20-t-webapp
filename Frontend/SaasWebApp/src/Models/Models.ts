export interface Tenant {
    id: number;
    nombre: string;
    configuracion: any; // JSONB puede ser cualquier estructura
    creadoEn?: Date;
    actualizadoEn?: Date;
  }
  
  export interface Usuario {
    id: number;
    tenantId: number;
    email: string;
    password: string;
    rol?: string; // 'admin', 'manager', 'viewer', etc.
    creadoEn?: Date;
    ultimoLogin?: Date;
  }
  
  export interface CategoriaProducto {
    id: number;
    tenantId: number;
    nombre: string;
    camposPersonalizados: any; // JSONB para datos dinámicos
    creadoEn?: Date;
    actualizadoEn?: Date;
  }
  export interface AuthResponse {
    token: string;
  }
  
  export interface Producto {
    id: number;
    tenantId: number;
    categoriaId?: number;
    nombre: string;
    sku: string;
    camposPersonalizados: any; // JSONB para datos adicionales
    stock: number;
    creadoEn?: Date;
    actualizadoEn?: Date;
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
  