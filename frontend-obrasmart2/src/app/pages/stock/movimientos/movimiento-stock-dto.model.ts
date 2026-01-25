export interface MovimientoStockDto {
  idRepuesto: number;
  tipo: 'ENTRADA' | 'SALIDA' | 'AJUSTE';
  cantidad: number;
  observacion?: string;
}
