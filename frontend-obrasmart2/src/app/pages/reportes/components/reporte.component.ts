import { Component, OnInit } from '@angular/core';
import { ReportesService } from 'src/app/service/reportes.service';
import { SidebarService } from 'src/app/service/sidebar.service';
import { DashboardReporteDto } from 'src/app/pages/reportes/reporte.model';
import { ChartData, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-components',
  templateUrl: './reporte.component.html',
  styleUrls: ['./reporte.component.scss']
})
export class ReportesComponent implements OnInit {
  data!: DashboardReporteDto;
  isLoading = true;
  reparacionesActivas = 0;
  reparacionesPendientes = 0;

  // Filtros de fecha
  startDate = '';
  endDate = '';
  preset = 'LAST_30_DAYS';
  readonly hoy = new Date();

  // Gráfico de tendencia de reparaciones (line chart)
  reparacionesChartData!: ChartData<'line'>;
  reparacionesChartOptions: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        display: true,
        position: 'top',
        align: 'start',
        labels: {
          color: '#2f5f5b',
          font: { size: 11, weight: '600' },
          boxWidth: 12,
          padding: 12,
        }
      },
      tooltip: {
        backgroundColor: '#2f5f5b',
        titleFont: { size: 12, weight: '600' },
        bodyFont: { size: 12 },
        cornerRadius: 6
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: { color: '#4a4a4a' },
        grid: { color: '#e5dfd5', borderDash: [4, 4] }
      },
      x: {
        ticks: { color: '#4a4a4a' },
        grid: { display: false }
      }
    }
  };

  // Gráfico donut (distribución de flota)
  donutData!: ChartData<'doughnut'>;
  donutOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        display: false
      }
    }
  };
  donutLegend: { label: string; color: string }[] = [];

  constructor(
    private reportesService: ReportesService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit(): void {
    this.aplicarPreset('LAST_30_DAYS');
  }

  cargarDashboard() {
    this.isLoading = true;

    this.reportesService.getDashboard({
      startDate: this.startDate,
      endDate: this.endDate,
      preset: this.preset
    }).subscribe({
      next: (resp) => {
        console.log('Datos recibidos del dashboard:', resp);
        this.data = resp;

        const estados = resp.reparaciones?.reparacionesPorEstado || {};
        this.reparacionesActivas =
          (estados['ABIERTA'] || 0) +
          (estados['EN_PROCESO'] || 0) +
          (estados['PENDIENTE'] || 0);

        this.reparacionesPendientes = (estados['PENDIENTE'] || 0);

        this.configurarGraficos();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar reportes:', err);
        this.isLoading = false;
        alert('Error al cargar los reportes. Revisa la consola para más detalles.');
      }
    });
  }

  aplicarPreset(preset: string) {
    this.preset = preset;
    const hoy = new Date();
    let inicio = new Date(hoy);

    switch (preset) {
      case 'LAST_7_DAYS':
        inicio.setDate(hoy.getDate() - 6);
        break;
      case 'LAST_30_DAYS':
        inicio.setDate(hoy.getDate() - 29);
        break;
      case 'THIS_YEAR':
        inicio = new Date(hoy.getFullYear(), 0, 1);
        break;
      case 'LAST_YEAR':
        inicio = new Date(hoy.getFullYear() - 1, hoy.getMonth(), hoy.getDate());
        break;
      default:
        inicio.setDate(hoy.getDate() - 29);
        break;
    }

    this.startDate = this.formatearFecha(inicio);
    this.endDate = this.formatearFecha(hoy);
    this.cargarDashboard();
  }

  onDateChange() {
    this.preset = 'CUSTOM';
    if (this.startDate && this.endDate && this.startDate > this.endDate) {
      this.endDate = this.startDate;
    }
    this.cargarDashboard();
  }

  private formatearFecha(fecha: Date): string {
    return fecha.toISOString().split('T')[0];
  }

  configurarGraficos() {
    // Configurar gráfico de línea (reparaciones por mes)
    if (this.data?.reparaciones?.reparacionesPorMes) {
      const meses = Object.keys(this.data.reparaciones.reparacionesPorMes);
      const valores = Object.values(this.data.reparaciones.reparacionesPorMes);

      this.reparacionesChartData = {
        labels: meses,
        datasets: [{
          label: 'Reparaciones',
          data: valores,
          borderColor: '#5c4e8f',
          backgroundColor: 'rgba(92, 78, 143, 0.18)',
          tension: 0.4,
          fill: true,
          borderWidth: 3.2,
          pointRadius: 5,
          pointBackgroundColor: '#f29e4c',
          pointBorderColor: '#5c4e8f',
          pointHoverRadius: 8,
          pointHoverBackgroundColor: '#f29e4c',
          pointHoverBorderColor: '#5c4e8f',
          pointHoverBorderWidth: 2
        }]
      };
    }

    // Configurar gráfico donut (equipos por tipo)
    if (this.data?.equipos?.equiposPorTipo) {
      const tipos = Object.keys(this.data.equipos.equiposPorTipo);
      const cantidades = Object.values(this.data.equipos.equiposPorTipo);

      const baseColores = [
        '#2f5f5b',
        '#658c88',
        '#9abcb7',
        '#e8c07d',
        '#f2a65a',
        '#d64550',
        '#b2c7c0',
        '#f7d7b4'
      ];

      // Generamos colores alternando la paleta para reducir repeticiones cercanas
      const colores = tipos.map((_, idx) => baseColores[(idx * 3) % baseColores.length]);

      this.donutData = {
        labels: tipos,
        datasets: [{
          data: cantidades,
          backgroundColor: colores,
          borderWidth: 2,
          borderColor: '#fff'
        }]
      };

      // Crear leyenda personalizada
      this.donutLegend = tipos.map((tipo, index) => ({
        label: `${tipo} (${cantidades[index]})`,
        color: colores[index]
      }));
    }
  }

  handleRefresh() {
    this.cargarDashboard();
  }

  toggleMenu() {
    this.sidebarService.toggleSidebar();
  }

  exportar() {
    // Export simple: imprime la vista actual (puedes reemplazar por jsPDF/html2canvas si quieres PDF directo)
    window.print();
  }
}
