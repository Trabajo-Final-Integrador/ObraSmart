import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SidebarService } from 'src/app/service/sidebar.service';
import { Usuario } from '../usuario.model';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { environment } from '../../../../../environments/environment';
import { MediaService } from 'src/app/service/media.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-listado-usuario',
  templateUrl: './listado-usuario.component.html',
  styleUrls: ['./listado-usuario.component.scss']
})
export class ListadoUsuariosComponent implements OnInit {
  usuarios: any[] = [];
  private _filtro: string = '';
  private readonly baseUrl = `${(environment as any).gatewayUrl || environment.apiUrl}/users`;
  private photoCache: Record<number, string> = {};
  private photoVersion: Record<number, number> = {};
  readonly placeholderData =
    'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="120" height="90" viewBox="0 0 120 90"><rect width="120" height="90" fill="%23e5e7eb"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%239ca3af" font-family="Arial" font-size="12">Sin imagen</text></svg>';
  readonly defaultUserImg = this.placeholderData;

  get filtro(): string {
    return this._filtro;
  }

  set filtro(value: string) {
    this._filtro = value;
    this.paginaActual = 1;
  }

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private sidebarService: SidebarService,
    private media: MediaService,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.obtenerUsuarios();
  }

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

  obtenerUsuarios() {
    this.http
      .get<any[]>(this.baseUrl, { withCredentials: true })
      .subscribe({
        next: (data) => {
          this.usuarios = data || [];
          this.usuarios.forEach(u => {
            if (u?.id) {
              this.photoVersion[u.id] ??= 0;
              this.photoCache[u.id] = this.getUserPhotoUrl(u.id);
            }
          });
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
        title: this.translate.instant('common.warning'),
        text: this.translate.instant('users.list.errors.invalidId')
      });
      return;
    }

    Swal.fire({
      title: this.translate.instant('users.list.delete.title'),
      text: this.translate.instant('users.list.delete.text'),
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: this.translate.instant('users.list.delete.confirm'),
      cancelButtonText: this.translate.instant('common.cancel')
    }).then((result) => {
      if (result.isConfirmed) {
        this.http
          .delete(`${this.baseUrl}/${id}`, { withCredentials: true })
          .subscribe({
            next: () => {
              Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'success',
                title: this.translate.instant('users.list.delete.success'),
                showConfirmButton: false,
                timer: 2000
              });
              this.obtenerUsuarios();
            },
            error: (err) => {
              console.error('[DELETE USER ERROR]', err);
              Swal.fire({
                icon: 'error',
                title: this.translate.instant('common.error'),
                text: this.translate.instant('users.list.delete.error')
              });
            }
          });
      }
    });
  }

  get usuariosFiltrados(): Usuario[] {
    if (!this.filtro.trim()) {
      return this.usuarios;
    }

    const filtroLower = this.filtro.toLowerCase().trim();

    return this.usuarios.filter(
      (u) =>
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
  public soloLectura = false;

  editar(usuario: any): void {
    console.log('📝 Editar ejecutado', usuario);
    this.usuarioEdit = { ...usuario };
    this.soloLectura = false;
    this.mostrarModal = true;
    console.log('👁️ mostrarModal =', this.mostrarModal);
    this.cdr.detectChanges();
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    console.log('🔴 cerrarModal ejecutado');
  }

  onUsuarioActualizado() {
    this.mostrarModal = false;
    this.obtenerUsuarios();
  }

  imgFallback(event: Event) {
    const img = event.target as HTMLImageElement;
    if (img) img.src = this.placeholderData;
  }

  verUsuario(usuario: any): void {
    this.usuarioEdit = { ...usuario };
    this.soloLectura = true;
    this.mostrarModal = true;
    this.cdr.detectChanges();
  }

  fotoDe(usuario: any): string {
    if (!usuario?.id) return this.placeholderData;
    const src = this.photoCache[usuario.id];
    return src || this.placeholderData;
  }

  onFotoError(usuario: any, event?: Event) {
    if (event) {
      const img = event.target as HTMLImageElement;
      if (img) {
        img.onerror = null as any;
        img.src = this.defaultUserImg;
      }
    }
    if (usuario?.id) this.photoCache[usuario.id] = this.defaultUserImg;
  }

  getUserPhotoUrl(userId: number): string {
    if (!userId) return this.defaultUserImg;
    const v = this.photoVersion[userId] ?? 0;
    const base = this.media.getEquipoImageUrl(this.toUserSlotMediaId(userId, 'profile'));
    return v ? `${base}?v=${v}` : base;
  }

  private toUserSlotMediaId(userId: number, slot: 'profile' | 'cred1' | 'cred2' | 'cred3' | 'cred4'): number {
    const base = 1_000_000 + Number(userId || 0);
    const offsets: Record<typeof slot, number> = {
      profile: 0,
      cred1: 100_000,
      cred2: 200_000,
      cred3: 300_000,
      cred4: 400_000
    };
    return base + offsets[slot];
  }

  onPhotoUploaded(userId: number): void {
    if (!userId) return;
    this.photoVersion[userId] = Date.now();
    this.photoCache[userId] = this.getUserPhotoUrl(userId);
  }
}
