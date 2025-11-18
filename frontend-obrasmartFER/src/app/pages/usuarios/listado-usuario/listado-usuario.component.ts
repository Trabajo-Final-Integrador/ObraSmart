import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient  } from '@angular/common/http';
import { Usuario } from '../usuario.model';
//import { AlertComponent } from '../../../shared/components/alert/alert.component';
import { AlertTipo } from '../../../shared/models/alert-tipo'; // 

@Component({
  selector: 'app-listado-usuario',
  templateUrl: './listado-usuario.component.html',
  styleUrls: ['./listado-usuario.component.scss']
})
export class ListadoUsuariosComponent implements OnInit {
  usuarios: any[] = [];
  filtro: string = '';

   alertaMensaje: string = '';
   alertaTipo: AlertTipo = 'info';
   alertaVisible: boolean = false;

   mostrarWarning(msg: string) {
    this.alertaMensaje = msg;
    this.alertaTipo = 'warning';
    this.alertaVisible = true;
  }


  constructor(private http: HttpClient, private cdr: ChangeDetectorRef ) {}

  ngOnInit(): void {
    this.obtenerUsuarios();
  }

  toggleSidebar(): void {
    console.log('🔹 Menú lateral pendiente de implementar');
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
          this.alertaMensaje = 'Error al cargar los usuarios.';
        this.alertaTipo = 'error';
        this.alertaVisible = true;
        }
      });
  }

 
eliminar(id?: number) {
  if (!id) {
    alert('⚠️ No se pudo eliminar: el ID es inválido.');
    return;
  }
  if (confirm('¿Está seguro que desea eliminar este usuario?')) {
    this.http.delete(`http://localhost:8085/users/${id}`)
      .subscribe({
        next: () => {
          this.alertaMensaje = 'Usuario eliminado correctamente.';
          this.alertaTipo = 'success';
          this.alertaVisible = true;
          this.obtenerUsuarios();
        },
        error: (err) => {console.error('❌ Error al eliminar usuario:', err);
        this.alertaMensaje = 'Error al eliminar usuario.';
          this.alertaTipo = 'error';
          this.alertaVisible = true;
        }
      });
  }
}

  get usuariosFiltrados():Usuario[] {
    return this.usuarios.filter(u =>
      u.username.toLowerCase().includes(this.filtro.toLowerCase()) ||
      u.email.toLowerCase().includes(this.filtro.toLowerCase())
    );
  }

  paginaActual = 1;
usuariosPorPagina = 5;

get totalPaginas(): number {
  return Math.ceil(this.usuarios.length / this.usuariosPorPagina);
}

get usuariosPaginados() {
  const start = (this.paginaActual - 1) * this.usuariosPorPagina;
  return this.usuarios.slice(start, start + this.usuariosPorPagina);
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
 

mostrarModal = false;
usuarioEdit: any = {
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

guardarCambios(): void {
  const id = this.usuarioEdit.id;
  this.http.put(`http://localhost:8085/users/${id}`, this.usuarioEdit)
    .subscribe({
      next: () => {
        this.alertaMensaje = 'Usuario actualizado correctamente.';
        this.alertaTipo = 'success';
        this.alertaVisible = true;
        this.mostrarModal = false;
        this.obtenerUsuarios(); // refresca la lista
      },
      error: (err) => {
        console.error('❌ Error al actualizar usuario:', err);
        this.alertaMensaje = 'Error al actualizar el usuario.';
        this.alertaTipo = 'error';
        this.alertaVisible = true;
      }
    });
}

}
