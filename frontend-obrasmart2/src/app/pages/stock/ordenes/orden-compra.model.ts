export interface OrdenCompra {
  id: number;
  idProveedor: number;
  proveedorNombre: string;
  estado: string;
  totalItems: number;
  total: number;
  fecha: string;
}
export interface ItemOrden {
  idRepuesto: number;
  cantidad: number;
  precioUnitario: number;
}

