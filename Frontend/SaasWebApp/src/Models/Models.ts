export interface Tenant {
    id: number;
    name: string;
    configuration: any; // JSONB puede ser cualquier estructura
    createdAt?: Date;
    updateAt?: Date;
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
  
  export interface Transaction {
    id?: number;
    tenantId: number;
    productId: number;
    quantity: number;
    type: TransactionType;
    reference: string;
    notes: string;
    createdById: number;
    createdAt?: string;
  }
  
  export enum TransactionType {
    ENTRADA = 'ENTRADA',
    SALIDA = 'SALIDA',
    AJUSTE = 'AJUSTE',
  }
  
  export interface TransactionFilter {
    startDate?: string;
    endDate?: string;
    type?: TransactionType;
    productId?: number;
    reference?: string;
  }
  
  export interface TransactionResponse {
    content: Transaction[];
    pageable: {
      pageNumber: number;
      pageSize: number;
      totalElements: number;
    };
  }
  