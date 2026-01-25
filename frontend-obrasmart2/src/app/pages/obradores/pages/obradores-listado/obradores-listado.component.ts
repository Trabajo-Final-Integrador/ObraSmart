import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ObradorDto, ObradorService } from 'src/app/service/obrador.service';
import { SidebarService } from 'src/app/service/sidebar.service';
import { UsuarioService } from 'src/app/pages/usuarios/usuario.service';
import { Usuario } from 'src/app/pages/usuarios/usuario.model';

@Component({
  selector: 'app-obradores-listado',
  styleUrls: ['./obradores-listado.component.scss'],
  templateUrl: './obradores-listado.component.html',
})
export class ObradoresListadoComponent implements OnInit {
  obradores: ObradorDto[] = [];
  loading = false;
  error?: string;
  filtro = '';
  pageSize = 5;
  pageIndex = 0;
  supervisores: Usuario[] = [];
  detalleVisible = false;
  obradorSeleccionado?: ObradorDto;

  constructor(
    private obradorService: ObradorService,
    private router: Router,
    private sidebarService: SidebarService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    this.fetchObradores();
    this.cargarSupervisores();
  }

  fetchObradores(): void {
    this.loading = true;
    this.error = undefined;
    this.obradorService.listar().subscribe({
      next: (data) => {
        this.obradores = data;
        console.log('[Obradores] loaded', data?.length ?? 0);
        this.loading = false;
      },
      error: (err) => {
        console.error('[Obradores] load failed', err?.status, err?.message || err);
        this.error = 'No se pudieron cargar los obradores';
        this.loading = false;
      },
    });
  }

  get obradoresFiltrados(): ObradorDto[] {
    const term = this.filtro.trim().toLowerCase();
    if (!term) return this.obradores;
    return this.obradores.filter((o) => {
      return (
        o.nombre?.toLowerCase().includes(term) ||
        o.ubicacion?.toLowerCase().includes(term) ||
        (o.supervisorUserId !== undefined && o.supervisorUserId !== null && o.supervisorUserId.toString().includes(term))
      );
    });
  }

  getSupervisorLabel(id?: number): string {
    if (!id) return '-';
    const sup = this.supervisores.find((u) => u.id === id);
    if (!sup) return id.toString();
    const fullName = `${sup.firstname || ''} ${sup.lastname || ''}`.trim();
    return sup.username || sup.email || fullName || id.toString();
  }

  get totalPages(): number {
    const total = Math.ceil(this.obradoresFiltrados.length / this.pageSize);
    return total > 0 ? total : 1;
  }

  get obradoresPaginados(): ObradorDto[] {
    const start = this.pageIndex * this.pageSize;
    return this.obradoresFiltrados.slice(start, start + this.pageSize);
  }

  onPageSizeChange(value: number): void {
    this.pageSize = Number(value) || 5;
    this.pageIndex = 0;
  }

  firstPage(): void {
    this.pageIndex = 0;
  }

  prevPage(): void {
    if (this.pageIndex > 0) this.pageIndex--;
  }

  nextPage(): void {
    if (this.pageIndex + 1 < this.totalPages) this.pageIndex++;
  }

  lastPage(): void {
    this.pageIndex = Math.max(0, this.totalPages - 1);
  }

  onFilterChange(): void {
    this.pageIndex = 0;
  }

  crearNuevo(): void {
    this.router.navigate(['/obradores/nuevo']);
  }

  verDetalle(id: number): void {
    const found = this.obradores.find((o) => o.id === id);
    this.obradorSeleccionado = found;
    this.detalleVisible = !!found;
  }

  editar(id: number): void {
    this.router.navigate(['/obradores', id]);
  }

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

  eliminar(id?: number): void {
    if (!id) return;
    const confirmado = confirm('¿Seguro que deseas eliminar este obrador?');
    if (!confirmado) return;
    this.obradorService.eliminar(id).subscribe({
      next: () => {
        this.fetchObradores();
        if (this.pageIndex >= this.totalPages) {
          this.pageIndex = Math.max(0, this.totalPages - 1);
        }
      },
      error: (err) => {
        console.warn('[Obradores] eliminar falló', err);
      },
    });
  }

  private cargarSupervisores(): void {
    this.usuarioService.listarUsuarios().subscribe({
      next: (usuarios) => {
        this.supervisores = (usuarios || []).filter(
          (u) => (u as any).role === 'ADMINISTRACION' || (u as any).rol === 'ADMINISTRACION'
        );
      },
      error: (err) => {
        console.warn('[Obradores] Error cargando supervisores', err);
        this.supervisores = [];
      },
    });
  }
}
