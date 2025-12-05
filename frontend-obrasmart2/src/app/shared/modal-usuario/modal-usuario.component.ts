import { Component, Input, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-modal-usuario',
  templateUrl: './modal-usuario.component.html',
  styleUrls: ['./modal-usuario.component.scss']
})
export class ModalUsuarioComponent {

  @Input() usuario: any = {};
  @Output() cerrar = new EventEmitter<void>();
  @Output() guardar = new EventEmitter<void>();

  constructor(private http: HttpClient) {}

 guardarCambios() {
  const id = this.usuario.id;

  this.http.put(`http://localhost:8085/users/${id}`, this.usuario)
  .subscribe({
    next: () => {
      // Primero emitimos el evento guardar para refrescar la tabla
      this.guardar.emit();

      // Luego cerramos el modal
      this.cerrar.emit();

      // Finalmente mostramos el toast
      setTimeout(() => {
        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'success',
          title: 'Cambios guardados correctamente',
          showConfirmButton: false,
          timer: 2000,
           customClass: {
            container: 'swal-no-backdrop'
          },
          didOpen: () => {
            const container = document.querySelector('.swal2-container') as HTMLElement;
            if (container) {
              container.style.zIndex = '99999';
            }
          }
        });
      }, 100);
    },
    error: (err) => {
      console.error("Error al actualizar", err);
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudo actualizar el usuario'
      });
    }
  });
}

  cancelar() {
    this.cerrar.emit();
  }

}
