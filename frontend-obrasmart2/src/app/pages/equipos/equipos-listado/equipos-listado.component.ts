import { Component, OnInit } from '@angular/core';
import { EquipoDTO, EquipoService } from 'src/app/service/equipo.service';
import { MarcaService, MarcaDTO } from 'src/app/service/marca.service';
import { ModeloService, ModeloDTO } from 'src/app/service/modelo.service';
import { TipoEquipoService, TipoEquipoDTO } from 'src/app/service/tipo-equipo.service';
import { Router } from '@angular/router';
import { SidebarService } from 'src/app/service/sidebar.service';
import Swal from 'sweetalert2';



@Component({
  selector: 'app-equipos-listado',
  templateUrl: './equipos-listado.component.html',
  styleUrls: ['./equipos-listado.component.scss']
})
export class EquiposListadoComponent implements OnInit {


  equipos: EquipoDTO[] = [];
  marcas: MarcaDTO[] = [];
  modelos: ModeloDTO[] = [];
  tipos: TipoEquipoDTO[] = [];

  // Variables para el filtro y paginación
  private _filtro: string = '';
  paginaActual = 1;
  equiposPorPagina = 5;

  get filtro(): string {
    return this._filtro;
  }

  set filtro(value: string) {
    this._filtro = value;
    this.paginaActual = 1; // resetear a la primera página cuando se filtra
  }

  constructor(
    private equipoService: EquipoService,
    private marcaService: MarcaService,
    private modeloService: ModeloService,
    private tipoService: TipoEquipoService,
    private router: Router,
    private sidebarService: SidebarService
  ) {}

 ngOnInit(): void {
    this.equipoService.listar().subscribe({
      next: (data) => this.equipos = data,
      error: (err) => console.error(err)
    });
    this.marcaService.listar().subscribe(r => this.marcas = r);
    this.modeloService.listar().subscribe(r => this.modelos = r);
    this.tipoService.listar().subscribe(r => this.tipos = r);

  }
  nuevoEquipo() {
    this.router.navigate(['/equipos/nuevo']);
  }

  editarEquipo(id: number) {
    this.router.navigate(['/equipos/editar', id]);
  }

  eliminarEquipo(equipo: EquipoDTO) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `Se eliminará el equipo "${equipo.nombre}" (${equipo.codigoInterno})`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed && equipo.id) {
        this.equipoService.eliminar(equipo.id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Equipo eliminado',
              text: 'El equipo fue eliminado correctamente',
              timer: 1500,
              showConfirmButton: false
            });

            // Recargar la lista
            this.equipoService.listar().subscribe({
              next: (data) => this.equipos = data,
              error: (err) => console.error(err)
            });
          },
          error: (err) => {
            console.error('Error al eliminar:', err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo eliminar el equipo. Puede estar relacionado con otros registros.',
              confirmButtonColor: '#00796b'
            });
          }
        });
      }
    });
  }

  getMarcaNombre(idMarca: number): string {
  const marca = this.marcas.find(m => m.id === idMarca);
  return marca ? marca.nombre : '';
}

getModeloNombre(idModelo: number): string {
  const modelo = this.modelos.find(m => m.id === idModelo);
  return modelo ? modelo.nombre : '';
}

getTipoEquipoNombre(idTipo: number): string {
  const tipo = this.tipos.find(t => t.id === idTipo);
  return tipo ? tipo.nombre : '';
}

  // Métodos para el menú
  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

  // Filtro de equipos
  get equiposFiltrados(): EquipoDTO[] {
    if (!this.filtro.trim()) {
      return this.equipos;
    }

    const filtroLower = this.filtro.toLowerCase().trim();

    return this.equipos.filter(e =>
      e.codigoInterno?.toLowerCase().includes(filtroLower) ||
      e.nombre?.toLowerCase().includes(filtroLower) ||
      this.getMarcaNombre(e.idMarca).toLowerCase().includes(filtroLower) ||
      this.getModeloNombre(e.idModelo).toLowerCase().includes(filtroLower) ||
      this.getTipoEquipoNombre(e.idTipoEquipo).toLowerCase().includes(filtroLower)
    );
  }

  // Paginación
  get totalPaginas(): number {
    return Math.ceil(this.equiposFiltrados.length / this.equiposPorPagina);
  }

  get equiposPaginados(): EquipoDTO[] {
    const start = (this.paginaActual - 1) * this.equiposPorPagina;
    return this.equiposFiltrados.slice(start, start + this.equiposPorPagina);
  }

  paginaAnterior(): void {
    if (this.paginaActual > 1) this.paginaActual--;
  }

  paginaSiguiente(): void {
    if (this.paginaActual < this.totalPaginas) this.paginaActual++;
  }

  primeraPagina(): void {
    this.paginaActual = 1;
  }

  ultimaPagina(): void {
    this.paginaActual = this.totalPaginas;
  }

  // Método para obtener la clase del badge según el estado
  getBadgeClass(estado: string): string {
    if (!estado) return 'badge';

    switch (estado.toUpperCase()) {
      case 'OPERATIVO':
        return 'badge badge-success';
      case 'MANTENIMIENTO':
        return 'badge badge-warning';
      case 'FUERA_SERVICIO':
      case 'FUERA DE SERVICIO':
        return 'badge badge-danger';
      default:
        return 'badge';
    }
  }
}
