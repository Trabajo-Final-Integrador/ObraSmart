export interface Usuario {
  id?: number;
  email: string;
  firstname?: string;
  lastname?: string;
  username: string;
  password?: string;
  role: 'ADMINISTRACION' | 'TECNICO' | 'OPERARIO';
  status: 'ACTIVO' | 'INACTIVO';
  observaciones?: string;
  photoUrl?: string; // foto precalculada para listado/modal
}
