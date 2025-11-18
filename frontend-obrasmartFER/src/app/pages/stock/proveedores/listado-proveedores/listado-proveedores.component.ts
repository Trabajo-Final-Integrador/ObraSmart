/* import { Component, OnInit } from '@angular/core';
import { ProveedorService, ProveedorDTO } from 'src/app/service/proveedor.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-listado-proveedores',
  templateUrl: './listado-proveedores.component.html',
  styleUrls: ['./listado-proveedores.component.scss']
})
export class ListadoProveedoresComponent implements OnInit {
  proveedores: ProveedorDTO[] = [];
  cargando = false;
  error = '';
  

  constructor(private service: ProveedorService, private router: Router) {}

  ngOnInit(): void {
    this.refrescar();
  }

  refrescar(): void {
    this.cargando = true;
    this.service.listar().subscribe({
      next: d => {
        this.proveedores = d;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar los proveedores.';
        this.cargando = false;
      }
    });
  }

  crearNuevo(): void {
    this.router.navigate(['/stock/proveedores/crear']);
  }
}
 */

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface Proveedor {
  codigo: string;
  nombre: string;
  ciudad: string;
  especialidad: string;
  telefono: string;
  email: string;
  rating: number;
  estado: 'Activo' | 'Inactivo';
}

@Component({
  selector: 'app-listado-proveedores',
  templateUrl: './listado-proveedores.component.html',
  styleUrls: ['./listado-proveedores.component.scss']
})
export class ListadoProveedoresComponent implements OnInit {

   constructor(private router: Router) {} 
  // Estado actual de la vista (listado / nuevo / editar)
  vista: 'listado' | 'nuevo' | 'editar' = 'listado';

  // Búsqueda y filtro
  searchTerm: string = '';
  filtroEstado: string = 'todos';

  // Datos del proveedor seleccionado (para edición)
  proveedorSeleccionado?: Proveedor;

  // Lista original y filtrada de proveedores
  proveedores: Proveedor[] = [];
  proveedoresFiltrados: Proveedor[] = [];

  ngOnInit(): void {
   

    this.proveedoresFiltrados = [...this.proveedores];
  }

  /** Filtra por texto y estado */
  filtrarProveedores() {
    const termino = this.searchTerm.toLowerCase().trim();

    this.proveedoresFiltrados = this.proveedores.filter(p => {
      const coincideTexto =
        p.nombre.toLowerCase().includes(termino) ||
        p.ciudad.toLowerCase().includes(termino) ||
        p.especialidad.toLowerCase().includes(termino);

      const coincideEstado =
        this.filtroEstado === 'todos' || p.estado === this.filtroEstado;

      return coincideTexto && coincideEstado;
    });
  }

  /** Cambia entre vistas */
  cambiarVista(vista: 'listado' | 'nuevo' | 'editar', proveedor?: Proveedor) {
    this.vista = vista;

    if (vista === 'editar' && proveedor) {
      this.proveedorSeleccionado = proveedor;
    }

    console.log(`📄 Cambiando a vista: ${vista}`);
  }

  /** Elimina un proveedor (solo simulación) */
  eliminarProveedor(codigo: string) {
    if (confirm('¿Seguro que querés eliminar este proveedor?')) {
      this.proveedores = this.proveedores.filter(p => p.codigo !== codigo);
      this.filtrarProveedores();
      alert('Proveedor eliminado ✅');
    }
  }

  /** Obtiene el array de estrellas para el rating */
  getStars(rating: number): number[] {
    return [1, 2, 3, 4, 5];
  }

  crearNuevo(): void {
    this.router.navigate(['/stock/proveedores/crear']);
  }
}
