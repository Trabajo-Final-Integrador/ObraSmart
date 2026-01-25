export interface DashboardReporteDto {
  fechaGeneracion: string;

  estadoGeneral: {
    equiposCriticos: number;
    repuestosCriticos: number;
    reparacionesAbiertas: number;
    nivelRiesgo: string;
    mensaje: string;
  };

  equipos: {
    totalEquipos: number;
    equiposPorTipo?: { [key: string]: number };
    equiposPorMarca?: { [key: string]: number };
    equiposPorModelo?: { [key: string]: number };
    equiposPorCombustible?: { [key: string]: number };
    equiposCriticos: EquipoCriticoDto[];
  };

  reparaciones: {
    totalReparaciones: number;
    reparacionesPorEstado: { [key: string]: number };
    reparacionesPorTipoMantenimiento?: { [key: string]: number };
    tiempoPromedioHoras: number;
    reparacionesPorEquipo?: { [key: number]: number };
    reparacionesPorMes?: { [key: string]: number };
    topEquiposConMasFallas?: EquipoCriticoDto[];
  };

  stock: {
    totalRepuestos: number;
    repuestosBajoMinimo: number;
    repuestosCriticos: RepuestoCriticoDto[];
    repuestosPorCategoria?: { [key: string]: number };
    movimientosPorTipo?: { [key: string]: number };
    movimientosPorMes?: { [key: string]: number };
    repuestosMasUsados?: RepuestoUsoDto[];
  };
}

export interface EquipoCriticoDto {
  equipoId: number;
  nombreEquipo: string;
  cantidadReparaciones: number;
}

export interface RepuestoCriticoDto {
  repuestoId: number;
  nombre: string;
  categoria: string;
  stockActual: number;
  stockMinimo: number;
}

export interface RepuestoUsoDto {
  repuestoId: number;
  nombre: string;
  categoria: string;
  cantidadMovimientosSalida: number;
}
