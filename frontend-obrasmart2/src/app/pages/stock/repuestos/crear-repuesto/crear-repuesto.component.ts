import { Component,  OnInit  } from '@angular/core';
import { RepuestoService, RepuestoDTO, CategoriaRepuestoDTO } from 'src/app/service/repuesto.service';
import { Router } from '@angular/router';
import { SidebarService } from 'src/app/service/sidebar.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-crear-repuesto',
  templateUrl: './crear-repuesto.component.html',
  styleUrls: ['./crear-repuesto.component.scss']
})
export class CrearRepuestoComponent implements OnInit{

  
  categorias: CategoriaRepuestoDTO[] = [];


  nuevoRepuesto: RepuestoDTO = {
    id: 0,
    codigo: '',
    nombre: '',
    idCategoria: 0,
    stock: 0,
    stockMinimo: 0,
    unidadMedida: ''
  };

  
  // 🔹 Estados para crear categoría
  isCategoryModalOpen = false;
  newCategoriaNombre = '';

  isSubmitting = false;

  constructor(
    private repuestoService: RepuestoService,
    private router: Router,
    private sidebarService: SidebarService
  ) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(selectId?: number) {
    this.repuestoService.listarCategorias().subscribe({
      next: data => {
        this.categorias = data;
        if (selectId != null) {
          this.nuevoRepuesto.idCategoria = selectId;
        }
      },
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
        this.cargarCategorias(catCreada.id);
        this.cerrarModalCategoria();
        this.newCategoriaNombre = '';
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
        const serverMessage = err?.error?.message;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: serverMessage || 'No se pudo crear la categoría',
          confirmButtonColor: '#00796b'
        });
      }
    });
  }

  guardarRepuesto() {
    if (!this.nuevoRepuesto.codigo || !this.nuevoRepuesto.nombre || !this.nuevoRepuesto.unidadMedida) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        text: 'Complete todos los campos requeridos',
        confirmButtonColor: '#00796b'
      });
      return;
    }
    if (this.nuevoRepuesto.idCategoria === 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Categoría requerida',
        text: 'Debe seleccionar una categoría',
        confirmButtonColor: '#00796b'
      });
      return;
    }

    this.isSubmitting = true;
    this.repuestoService.crear(this.nuevoRepuesto).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Repuesto creado',
          text: 'El repuesto fue creado exitosamente',
          timer: 1500,
          showConfirmButton: false
        });
        this.router.navigate(['/stock/repuestos']);
      },
      error: (err) => {
        console.error('Error al crear repuesto:', err);
        this.isSubmitting = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo crear el repuesto',
          confirmButtonColor: '#00796b'
        });
      }
    });
  }

  cancelar() {
    this.router.navigate(['/stock/repuestos']);
  }

  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }

}




