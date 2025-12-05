export interface Movimiento {
  id: number;
  idRepuesto: number;
  repuestoNombre: string;
  repuestoCodigo: string;
  tipo: 'ENTRADA' | 'SALIDA' | 'AJUSTE';
  cantidad: number;
  observacion?: string;
  fecha: string; // ISO string desde Instant
}

