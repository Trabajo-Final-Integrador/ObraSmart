import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RepuestoService, RepuestoDTO, CategoriaRepuestoDTO } from 'src/app/service/repuesto.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-repuesto',
  templateUrl: './editar-repuesto.component.html',
  styleUrls: ['./editar-repuesto.component.scss']
})
export class EditarRepuestoComponent implements OnInit {

  categorias: CategoriaRepuestoDTO[] = [];
  repuesto: RepuestoDTO = {
    id: 0,
    codigo: '',
    nombre: '',
    idCategoria: 0,
    stock: 0,
    stockMinimo: 0,
    unidadMedida: ''
  };

  // Estados para crear categoría
  isCategoryModalOpen = false;
  newCategoriaNombre = '';

  isSubmitting = false;
  loading = true;
  repuestoId: number = 0;

  constructor(
    private repuestoService: RepuestoService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.repuestoId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.repuestoId) {
      this.cargarRepuesto();
    }
    this.cargarCategorias();
  }

  cargarRepuesto() {
    this.repuestoService.obtenerPorId(this.repuestoId).subscribe({
      next: (data) => {
        this.repuesto = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando repuesto:', err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo cargar el repuesto',
          confirmButtonColor: '#00796b'
        }).then(() => {
          this.router.navigate(['/stock/repuestos']);
        });
      }
    });
  }

  cargarCategorias() {
    this.repuestoService.listarCategorias().subscribe({
      next: data => this.categorias = data,
      error: () => Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudieron cargar las categorías',
        confirmButtonColor: '#00796b'
      })
    });
  }

  abrirModalCategoria(): void {
    this.newCategoriaNombre = '';
    this.isCategoryModalOpen = true;
  }

  cerrarModalCategoria(): void {
    this.isCategoryModalOpen = false;
  }

  guardarCategoria(): void {
    if (!this.newCategoriaNombre.trim()) {
      Swal.fire({
        icon: 'warning',
        title: 'Campo requerido',
        text: 'Debes ingresar un nombre de categoría',
        confirmButtonColor: '#00796b'
      });
      return;
    }

    this.repuestoService.crearCategoria(this.newCategoriaNombre).subscribe({
      next: (catCreada) => {
        this.categorias.push(catCreada);
        this.repuesto.idCategoria = catCreada.id;
        this.cerrarModalCategoria();
        Swal.fire({
          icon: 'success',
          title: 'Categoría creada',
          text: 'La categoría fue creada exitosamente',
          timer: 1500,
          showConfirmButton: false
        });
      },
      error: (err) => {
        console.error('Error creando categoría', err);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo crear la categoría',
          confirmButtonColor: '#00796b'
        });
      }
    });
  }

  actualizarRepuesto() {
    if (!this.repuesto.codigo || !this.repuesto.nombre || !this.repuesto.unidadMedida) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        text: 'Complete todos los campos requeridos',
        confirmButtonColor: '#00796b'
      });
      return;
    }
    if (this.repuesto.idCategoria === 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Categoría requerida',
        text: 'Debe seleccionar una categoría',
        confirmButtonColor: '#00796b'
      });
      return;
    }

    this.isSubmitting = true;
    this.repuestoService.actualizar(this.repuestoId, this.repuesto).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Repuesto actualizado',
          text: 'El repuesto fue actualizado exitosamente',
          timer: 1500,
          showConfirmButton: false
        });
        this.router.navigate(['/stock/repuestos']);
      },
      error: (err) => {
        console.error('Error al actualizar repuesto:', err);
        this.isSubmitting = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo actualizar el repuesto',
          confirmButtonColor: '#00796b'
        });
      }
    });
  }

  cancelar() {
    this.router.navigate(['/stock/repuestos']);
  }
}
