import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProveedorService, ProveedorDto, ProveedorUpdateDTO } from 'src/app/service/proveedor.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-proveedor',
  templateUrl: './editar-proveedor.component.html',
  styleUrls: ['./editar-proveedor.component.scss']
})
export class EditarProveedorComponent implements OnInit {

  proveedorId!: number;
  loading = true;
  isSubmitting = false;

  // Control de pestañas activas
  tabActiva: string = 'general';

  // Pestañas visibles en la parte superior
  tabs = [
    { id: 'general', label: 'providers.tabs.general', icon: 'bi bi-info-circle' },
    { id: 'contacto', label: 'providers.tabs.contact', icon: 'bi bi-telephone' },
    { id: 'comercial', label: 'providers.tabs.commercial', icon: 'bi bi-box' },
    { id: 'catalogo', label: 'providers.tabs.catalog', icon: 'bi bi-journal' },
    { id: 'bancario', label: 'providers.tabs.bank', icon: 'bi bi-currency-dollar' },
    { id: 'observaciones', label: 'providers.tabs.notes', icon: 'bi bi-three-dots' }
  ];

  // Lista de provincias argentinas
  provinciasArgentinas: string[] = [
    'Buenos Aires',
    'CABA',
    'Catamarca',
    'Chaco',
    'Chubut',
    'Córdoba',
    'Corrientes',
    'Entre Ríos',
    'Formosa',
    'Jujuy',
    'La Pampa',
    'La Rioja',
    'Mendoza',
    'Misiones',
    'Neuquén',
    'Río Negro',
    'Salta',
    'San Juan',
    'San Luis',
    'Santa Cruz',
    'Santa Fe',
    'Santiago del Estero',
    'Tierra del Fuego',
    'Tucumán'
  ];

  nuevoProveedor: ProveedorUpdateDTO = {
    razonSocial: '',
    nombreComercial: '',
    cuit: '',
    condicionIVA: '',
    estado: 'Activo',
    telefono: '',
    email: '',
    horarioAtencion: '',
    direccion: '',
    ciudad: '',
    provincia: '',
  codigoPostal: '',
  especialidad: '',
  marcas: [],
  tiempoEntrega: undefined,
    pedidoMinimo: undefined,
    descuentoVolumen: undefined,
    condicionesPago: '',
    tieneStock: false,
    haceEnvios: false,
    aceptaDevoluciones: false,
    zonaCobertura: '',
    tieneCatalogo: false,
    urlCatalogo: '',
    codigoCliente: '',
    banco: '',
    tipoCuenta: '',
    cbu: '',
    observaciones: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private proveedorService: ProveedorService
  ) {}

  ngOnInit(): void {
    this.proveedorId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.proveedorId) {
      this.cargarProveedor();
    }
  }

  cargarProveedor(): void {
    this.loading = true;
    this.proveedorService.obtenerPorId(this.proveedorId).subscribe({
      next: (data: ProveedorDto) => {
        // Mapear ProveedorDto a ProveedorUpdateDTO
      
      this.nuevoProveedor = {
  razonSocial: data.razonSocial ?? '',
  nombreComercial: data.nombreComercial ?? '',
  cuit: data.cuit ?? '',
  condicionIVA: data.condicionIVA ?? null,
  
  // 📌 Backend usa enum en MAYÚSCULAS
  estado: (data.estado ?? '').toUpperCase(),

  telefono: data.telefono ?? '',
  email: data.email ?? '',
  
  horarioAtencion: data.horarioAtencion ?? '',
  direccion: data.direccion ?? '',
  ciudad: data.ciudad ?? '',
  provincia: data.provincia ?? '',
  codigoPostal: data.codigoPostal ?? '',
  especialidad: data.especialidad ?? '',
  marcas: data.marcas ?? [],
  
  // 📌 Estos campos existen en el DTO, deben mandarse aunque no estén en formulario
  condicionesPago: data.condicionesPago ?? '',
  observaciones: data.observaciones ?? '',
  
  // 📌 Estos campos existen en la base pero el usuario no los cambia → mantenelos
  tieneStock: data.tieneStock ?? false,
  haceEnvios: data.haceEnvios ?? false,
  aceptaDevoluciones: data.aceptaDevoluciones ?? false,
  tieneCatalogo: data.tieneCatalogo ?? false,
  
  // No enviar nulls en campos que el backend no espera
  tiempoEntrega: data.tiempoEntrega ?? undefined,
  pedidoMinimo: data.pedidoMinimo ?? undefined,
  descuentoVolumen: data.descuentoVolumen ?? undefined,
  
  zonaCobertura: data.zonaCobertura ?? '',
  urlCatalogo: data.urlCatalogo ?? '',
  codigoCliente: data.codigoCliente ?? '',
  banco: data.banco ?? '',
  tipoCuenta: data.tipoCuenta ?? '',
  cbu: data.cbu ?? ''
};


        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar proveedor', err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo cargar el proveedor',
          confirmButtonColor: '#00796b'
        });
        this.router.navigate(['/stock/proveedores']);
      }
    });
  }

  

  actualizarProveedor(): void {
    // Convertir a números los campos numéricos
    this.nuevoProveedor.tiempoEntrega = this.nuevoProveedor.tiempoEntrega ? Number(this.nuevoProveedor.tiempoEntrega) : undefined;
    this.nuevoProveedor.pedidoMinimo = this.nuevoProveedor.pedidoMinimo ? Number(this.nuevoProveedor.pedidoMinimo) : undefined;
    this.nuevoProveedor.descuentoVolumen = this.nuevoProveedor.descuentoVolumen ? Number(this.nuevoProveedor.descuentoVolumen) : undefined;

    this.isSubmitting = true;
    this.proveedorService.actualizar(this.proveedorId, this.nuevoProveedor).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Proveedor actualizado',
          text: 'El proveedor fue actualizado exitosamente',
          timer: 1500,
          showConfirmButton: false
        });
        this.router.navigate(['/stock/proveedores']);
      },
      error: (err) => {
        console.error('Error al actualizar proveedor', err);
        this.isSubmitting = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo actualizar el proveedor',
          confirmButtonColor: '#00796b'
        });
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/stock/proveedores']);
  }

  setTabActiva(tabId: string): void {
    this.tabActiva = tabId;
  }

  irSiguienteStep(): void {
    switch (this.tabActiva) {
      case 'general': this.tabActiva = 'contacto'; break;
      case 'contacto': this.tabActiva = 'comercial'; break;
      case 'comercial': this.tabActiva = 'catalogo'; break;
      case 'catalogo': this.tabActiva = 'bancario'; break;
      case 'bancario': this.tabActiva = 'observaciones'; break;
      default: break;
    }
  }

  volverStep(): void {
    switch (this.tabActiva) {
      case 'contacto': this.tabActiva = 'general'; break;
      case 'comercial': this.tabActiva = 'contacto'; break;
      case 'catalogo': this.tabActiva = 'comercial'; break;
      case 'bancario': this.tabActiva = 'catalogo'; break;
      case 'observaciones': this.tabActiva = 'bancario'; break;
      default: break;
    }
  }

  irAlSiguiente(tabId: string): void {
    this.tabActiva = tabId;
  }

  irAlAnterior(tabId: string): void {
    this.tabActiva = tabId;
  }
}
