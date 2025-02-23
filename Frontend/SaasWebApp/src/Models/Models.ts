export interface Tenant {
    id: string;
    nombre: string;
    configuracion: any; // JSONB puede ser cualquier estructura
    creadoEn?: Date;
    actualizadoEn?: Date;
  }
  
  export interface Usuario {
    id: string;
    tenantId: string;
    email: string;
    password: string;
    rol?: string; // 'admin', 'manager', 'viewer', etc.
    creadoEn?: Date;
    ultimoLogin?: Date;
  }
  
  export interface CategoriaProducto {
    id: string;
    tenantId: string;
    nombre: string;
    camposPersonalizados: any; // JSONB para datos dinámicos
    creadoEn?: Date;
    actualizadoEn?: Date;
  }
  
  export interface Producto {
    id: string;
    tenantId: string;
    categoriaId?: string;
    nombre: string;
    sku: string;
    camposPersonalizados: any; // JSONB para datos adicionales
    creadoEn?: Date;
    actualizadoEn?: Date;
  }
  
  export interface Inventario {
    id: string;
    productoId: string;
    tenantId: string;
    cantidad: number;
    ubicacion?: string;
    creadoEn?: Date;
    actualizadoEn?: Date;
  }
  
  export interface Transaccion {
    id: string;
    tenantId: string;
    productoId: string;
    cantidad: number;
    tipo: 'entrada' | 'salida' | 'ajuste';
    referencia?: string;
    notas?: string;
    creadoPor: string; // Usuario que creó la transacción
    creadoEn?: Date;
  }
  