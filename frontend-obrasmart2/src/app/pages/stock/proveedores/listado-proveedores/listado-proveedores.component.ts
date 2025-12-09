import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProveedorService, ProveedorListadoDTO } from 'src/app/service/proveedor.service';
import { SidebarService } from 'src/app/service/sidebar.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listado-proveedores',
  templateUrl: './listado-proveedores.component.html',
  styleUrls: ['./listado-proveedores.component.scss']
})
export class ListadoProveedoresComponent implements OnInit {

  // UI States
  loading = true;

  // Búsqueda y filtrado
  searchTerm: string = '';
  filtroEstado: string = 'todos';

  // Datos
  proveedores: ProveedorListadoDTO[] = [];
  proveedoresFiltrados: ProveedorListadoDTO[] = [];

  // Paginación
  currentPage = 1;
  itemsPerPage = 10;
  totalPages = 1;

  constructor(
    private router: Router,
    private proveedorService: ProveedorService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit(): void {
    this.cargarProveedores();
  }

  cargarProveedores(): void {
    this.loading = true;
    this.proveedorService.listar().subscribe({
      next: (data) => {
        this.proveedores = data;
        this.proveedoresFiltrados = [...this.proveedores];
        this.calcularPaginacion();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar proveedores', err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los proveedores',
          confirmButtonColor: '#00796b'
        });
      }
    });
  }

  filtrarProveedores() {
    const termino = this.searchTerm.toLowerCase().trim();

    this.proveedoresFiltrados = this.proveedores.filter(p => {
      const raz = p.razonSocial.toLowerCase();
      const esp = (p.especialidad ?? '').toLowerCase();
      const cont = (p.contacto ?? '').toLowerCase();

      const coincideTexto =
        raz.includes(termino) ||
        esp.includes(termino) ||
        cont.includes(termino);

      const coincideEstado =
        this.filtroEstado === 'todos' || p.estado === this.filtroEstado;

      return coincideTexto && coincideEstado;
    });

    this.currentPage = 1;
    this.calcularPaginacion();
  }

  // Paginación
  calcularPaginacion() {
    this.totalPages = Math.ceil(this.proveedoresFiltrados.length / this.itemsPerPage);
    if (this.totalPages === 0) this.totalPages = 1;
    if (this.currentPage > this.totalPages) this.currentPage = this.totalPages;
  }

  get proveedoresPaginados(): ProveedorListadoDTO[] {
    const inicio = (this.currentPage - 1) * this.itemsPerPage;
    const fin = inicio + this.itemsPerPage;
    return this.proveedoresFiltrados.slice(inicio, fin);
  }

  get startIndex(): number {
    return (this.currentPage - 1) * this.itemsPerPage;
  }

  get endIndex(): number {
    const end = this.currentPage * this.itemsPerPage;
    return end > this.proveedoresFiltrados.length ? this.proveedoresFiltrados.length : end;
  }

  cambiarPagina(pagina: number) {
    if (pagina >= 1 && pagina <= this.totalPages) {
      this.currentPage = pagina;
    }
  }

  primeraPagina() {
    this.currentPage = 1;
  }

  ultimaPagina() {
    this.currentPage = this.totalPages;
  }

  onItemsPerPageChange() {
    this.currentPage = 1;
    this.calcularPaginacion();
  }

  // UI
  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }

  getBadgeClass(estado: string): string {
    return estado === 'Activo' ? 'badge badge-success' : 'badge badge-danger';
  }

  // Acciones
  crearNuevo(): void {
    this.router.navigate(['/stock/proveedores/crear']);
  }

  editarProveedor(proveedor: ProveedorListadoDTO): void {
    this.router.navigate(['/stock/proveedores/editar', proveedor.id]);
  }

  // eliminarProveedor(proveedor: ProveedorListadoDTO): void {
  //   Swal.fire({
  //     title: '¿Estás seguro?',
  //     text: `Se dará de baja el proveedor "${proveedor.razonSocial}". El proveedor se marcará como Inactivo.`,
  //     icon: 'warning',
  //     showCancelButton: true,
  //     confirmButtonColor: '#d33',
  //     cancelButtonColor: '#6c757d',
  //     confirmButtonText: 'Sí, dar de baja',
  //     cancelButtonText: 'Cancelar'
  //   }).then((result) => {
  //     if (result.isConfirmed) {
  //       this.proveedorService.eliminar(proveedor.id).subscribe({
  //         next: () => {
  //           Swal.fire({
  //             icon: 'success',
  //             title: 'Proveedor dado de baja',
  //             text: 'El proveedor fue marcado como Inactivo correctamente',
  //             timer: 1500,
  //             showConfirmButton: false
  //           });
  //           this.cargarProveedores();
  //         },
  //         error: (err) => {
  //           console.error('Error al dar de baja proveedor', err);
  //           Swal.fire({
  //             icon: 'error',
  //             title: 'Error',
  //             text: 'No se pudo dar de baja el proveedor',
  //             confirmButtonColor: '#00796b'
  //           });
  //         }
  //       });
  //     }
  //   });
  eliminarProveedor(proveedor: ProveedorListadoDTO): void {
  Swal.fire({
    title: '¿Estás seguro?',
    text: `Se dará de baja el proveedor "${proveedor.razonSocial}". El proveedor se marcará como Inactivo.`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Sí, dar de baja',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {

      this.proveedorService.cambiarEstado(proveedor.id, 'INACTIVO')
        .subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Proveedor inactivado',
              text: 'El proveedor fue marcado como Inactivo correctamente',
              timer: 1500,
              showConfirmButton: false
            });
            this.cargarProveedores();
          },
          error: (err) => {
            console.error('Error al cambiar estado del proveedor', err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo cambiar el estado del proveedor',
              confirmButtonColor: '#00796b'
            });
          }
        });

    }
  });
}

  }

