export interface Usuario {
  id?: number;
  email: string;
  firstname: string;
  lastname: string;
  username: string;
  password: string;
  role: 'ADMINISTRADOR' | 'TECNICO' | 'OPERARIO';
  status: 'ACTIVO' | 'INACTIVO';
}
