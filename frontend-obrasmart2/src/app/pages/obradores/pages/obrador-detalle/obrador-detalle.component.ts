import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ObradorDto, ObradorService } from 'src/app/service/obrador.service';
import { UsuarioService } from 'src/app/pages/usuarios/usuario.service';
import { Usuario } from 'src/app/pages/usuarios/usuario.model';

@Component({
  selector: 'app-obrador-detalle',
  templateUrl: './obrador-detalle.component.html',
  styleUrls: ['./obrador-detalle.component.scss'],
})
export class ObradorDetalleComponent implements OnInit {
  obrador?: ObradorDto;
  supervisores: Usuario[] = [];
  loading = false;
  error?: string;
  estadoLabel = 'ACTIVO';
  equipoIdsSafe: number[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private obradorService: ObradorService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.cargarSupervisores();
      this.cargarObrador(id);
    } else {
      this.error = 'Obrador no encontrado';
    }
  }

  private cargarObrador(id: number): void {
    this.loading = true;
    this.obradorService.obtener(id).subscribe({
      next: (o) => {
        this.obrador = o;
        this.estadoLabel = (o as any).estado || 'ACTIVO';
        this.equipoIdsSafe = o.equipoIds || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('[Obrador detalle] load failed', err);
        this.error = 'No se pudo cargar el obrador';
        this.loading = false;
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
      error: (err) => console.warn('[Obrador detalle] no se pudieron cargar supervisores', err),
    });
  }

  getSupervisorLabel(id?: number): string {
    if (!id) return '-';
    const sup = this.supervisores.find((u) => u.id === id);
    if (!sup) return id.toString();
    const fullName = `${sup.firstname || ''} ${sup.lastname || ''}`.trim();
    return sup.username || sup.email || fullName || id.toString();
  }

  irAEditar(): void {
    if (this.obrador?.id) {
      this.router.navigate(['/obradores', this.obrador.id]);
    }
  }

  volver(): void {
    this.router.navigate(['/obradores']);
  }
}
