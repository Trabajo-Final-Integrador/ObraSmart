import { Component, OnInit } from '@angular/core';
import { ReportesService } from 'src/app/service/reportes.service';
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
  reparacionesActivas: number = 0;
reparacionesPendientes: number = 0;


  // Gráfico de tendencia de reparaciones (line chart)
  reparacionesChartData!: ChartData<'line'>;
  reparacionesChartOptions: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        display: true,
        position: 'top',
        labels: {
          color: '#333',
          font: { size: 12 }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: { color: '#666' },
        grid: { color: '#e5e5e5' }
      },
      x: {
        ticks: { color: '#666' },
        grid: { color: '#e5e5e5' }
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
  donutLegend: any[] = [];

  constructor(private reportesService: ReportesService) {}

  ngOnInit(): void {
    this.cargarDashboard();
  }

  cargarDashboard() {
    this.isLoading = true;

    this.reportesService.getDashboard().subscribe({
      next: (resp) => {
        console.log('📊 Datos recibidos del dashboard:', resp);
        console.log('🔧 Reparaciones:', resp?.reparaciones);
        console.log('📦 Stock:', resp?.stock);
        console.log('🚜 Equipos:', resp?.equipos);
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
        console.error('❌ Error al cargar reportes:', err);
        this.isLoading = false;
        alert('Error al cargar los reportes. Revisa la consola para más detalles.');
      }
    });
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
          borderColor: '#78866B',
          backgroundColor: 'rgba(120, 134, 107, 0.1)',
          tension: 0.4,
          fill: true
        }]
      };
    }

    // Configurar gráfico donut (equipos por tipo)
    if (this.data?.equipos?.equiposPorTipo) {
      const tipos = Object.keys(this.data.equipos.equiposPorTipo);
      const cantidades = Object.values(this.data.equipos.equiposPorTipo);

      const colores = [
        '#78866B',
        '#A9BA9D',
        '#D0D9CD',
        '#6b7760',
        '#8a9979',
        '#c1cbb8'
      ];

      this.donutData = {
        labels: tipos,
        datasets: [{
          data: cantidades,
          backgroundColor: colores.slice(0, tipos.length),
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
}
