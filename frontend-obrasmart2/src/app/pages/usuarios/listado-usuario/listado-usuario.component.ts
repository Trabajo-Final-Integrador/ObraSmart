import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient  } from '@angular/common/http';
import { Usuario } from '../usuario.model';
import { Router } from '@angular/router';
import Swal from 'sweetalert2'; 




@Component({
  selector: 'app-listado-usuario',
  templateUrl: './listado-usuario.component.html',
  styleUrls: ['./listado-usuario.component.scss']
})
export class ListadoUsuariosComponent implements OnInit {
  usuarios: any[] = [];
  private _filtro: string = '';
  menuAbierto = false;
  submenuUsuariosOpen = false;
  submenuStockOpen = false;
  submenuReparacionOpen = false;

  get filtro(): string {
    return this._filtro;
  }

  set filtro(value: string) {
    this._filtro = value;
    this.paginaActual = 1; // resetear a la primera página cuando se filtra
  }

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef, private router: Router ) {}

  ngOnInit(): void {
    this.obtenerUsuarios();
  }

  toggleSidebar(): void {
    this.menuAbierto = !this.menuAbierto;
  }

  toggleUsuarios(event: Event): void {
    event.preventDefault();
    this.submenuUsuariosOpen = !this.submenuUsuariosOpen;
  }

  toggleStock(event: Event): void {
    event.preventDefault();
    this.submenuStockOpen = !this.submenuStockOpen;
  }

  toggleReparacion(event: Event): void {
    event.preventDefault();
    this.submenuReparacionOpen = !this.submenuReparacionOpen;
  }


  obtenerUsuarios() {
    this.http.get<any[]>('http://localhost:8085/users')
      .subscribe({
        next: (data) => {
          this.usuarios = data;
          console.log('✅ Usuarios cargados:', data);
        },
        error: (err) => {
          console.error('❌ Error al obtener usuarios:', err);
        
        }
      });
  }

 
eliminar(id?: number) {
  if (!id) {
    Swal.fire({
      icon: 'warning',
      title: 'Atención',
      text: 'No se pudo eliminar: el ID es inválido.'
    });
    return;
  }

  Swal.fire({
    title: '¿Está seguro?',
    text: '¿Desea eliminar este usuario?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Sí, eliminar',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {
      this.http.delete(`http://localhost:8085/users/${id}`)
        .subscribe({
          next: () => {
            Swal.fire({
              toast: true,
              position: 'top-end',
              icon: 'success',
              title: 'Usuario eliminado correctamente',
              showConfirmButton: false,
              timer: 2000
            });
            this.obtenerUsuarios();
          },
          error: (err) => {
            console.error('❌ Error al eliminar usuario:', err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo eliminar el usuario'
            });
          }
        });
    }
  });
}

  get usuariosFiltrados():Usuario[] {
    if (!this.filtro.trim()) {
      return this.usuarios;
    }

    const filtroLower = this.filtro.toLowerCase().trim();

    return this.usuarios.filter(u =>
      u.username.toLowerCase().includes(filtroLower) ||
      u.email.toLowerCase().includes(filtroLower) ||
      u.role.toLowerCase().includes(filtroLower)
    );
  }

  paginaActual = 1;
usuariosPorPagina = 5;

get totalPaginas(): number {
  return Math.ceil(this.usuariosFiltrados.length / this.usuariosPorPagina);
}

get usuariosPaginados() {
  const start = (this.paginaActual - 1) * this.usuariosPorPagina;
  return this.usuariosFiltrados.slice(start, start + this.usuariosPorPagina);
}

paginaAnterior() {
  if (this.paginaActual > 1) this.paginaActual--;
}

paginaSiguiente() {
  if (this.paginaActual < this.totalPaginas) this.paginaActual++;
}

primeraPagina() {
  this.paginaActual = 1;
}

ultimaPagina() {
  this.paginaActual = this.totalPaginas;
}
 

public mostrarModal = false;
public usuarioEdit: any = {
  email: '',
  role: '',
  status: ''
};

editar(usuario: any): void {
  console.log('🟢 Editar ejecutado', usuario);
  this.usuarioEdit = { ...usuario }; // clona el usuario
  this.mostrarModal = true;
  console.log('👁 mostrarModal =', this.mostrarModal);
   this.cdr.detectChanges(); 
}

cerrarModal(): void {
  this.mostrarModal = false;
  console.log('🔴 cerrarModal ejecutado');
}

onUsuarioActualizado() {
  this.mostrarModal = false;


  this.obtenerUsuarios(); // refresca lista

  
}



}
