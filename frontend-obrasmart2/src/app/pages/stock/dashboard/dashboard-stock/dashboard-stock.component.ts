import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RepuestoService } from 'src/app/service/repuesto.service';
import { MovimientosService } from 'src/app/service/movimiento-stock.service';
import { OrdenCompraService } from 'src/app/service/orden-compra.service';
import { SidebarService } from 'src/app/service/sidebar.service';
import { ThemeService } from 'src/app/service/theme.service';

@Component({
  selector: 'app-dashboard-stock',
  templateUrl: './dashboard-stock.component.html',
  styleUrls: ['./dashboard-stock.component.scss']
})
export class DashboardStockComponent implements OnInit {

  theme: 'light' | 'dark' = 'light';

  totalRepuestos = 0;
  stockBajo = 0;
  movimientosHoy = 0;
  ordenesPendientes = 0;

  alertasStock: any[] = [];
  alertasPaginadas: any[] = [];
  alertasPage = 1;
  alertasPageSize = 8;

  loading = true;

  constructor(
    private repSrv: RepuestoService,
    private movSrv: MovimientosService,
    private ordSrv: OrdenCompraService,
    private router: Router,
    private themeService: ThemeService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit(): void {
    this.cargarDashboard();
  }

  cargarDashboard() {
    this.loading = true;

    // Total repuestos
    this.repSrv.listar().subscribe(data => {
      this.totalRepuestos = data.length;
      this.stockBajo = data.filter(r => r.stock <= r.stockMinimo).length;
    });

    // Movimientos hoy
    this.movSrv.listar().subscribe(mov => {
      const hoy = new Date().toISOString().split('T')[0];
      this.movimientosHoy = mov.filter(x => x.fecha.startsWith(hoy)).length;
    });

    // Órdenes pendientes
    this.ordSrv.listar().subscribe(ords => {
      this.ordenesPendientes = ords.filter(x => x.estado === 'PENDIENTE').length;
    });

    // Alertas de stock
    this.repSrv.listar().subscribe(reps => {
      this.alertasStock = reps
        .filter(r => r.stock <= r.stockMinimo)
        .map(r => ({
          nombre: `${r.nombre} #${r.codigo}`,
          stock: r.stock,
          minimo: r.stockMinimo
        }));
      this.alertasPage = 1;
      this.actualizarPaginacion();

      this.loading = false;
    });
  }

  get totalPaginasAlertas(): number {
    return Math.max(1, Math.ceil(this.alertasStock.length / this.alertasPageSize));
  }

  actualizarPaginacion() {
    const inicio = (this.alertasPage - 1) * this.alertasPageSize;
    const fin = inicio + this.alertasPageSize;
    this.alertasPaginadas = this.alertasStock.slice(inicio, fin);
  }

  avanzarAlertas(delta: number) {
    const nueva = this.alertasPage + delta;
    if (nueva < 1 || nueva > this.totalPaginasAlertas) return;
    this.alertasPage = nueva;
    this.actualizarPaginacion();
  }

  ir(path: string) {
    this.router.navigate([path]);
  }

  regresar() {
    this.router.navigate(['/principal']);
  }

  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }

  toggleTheme() {
    this.themeService.toggleTheme();
    this.theme = this.themeService.getMode();
  }

  cambiarTema() {
    this.themeService.toggleTheme();
    this.theme = this.themeService.getMode();
  }

}
