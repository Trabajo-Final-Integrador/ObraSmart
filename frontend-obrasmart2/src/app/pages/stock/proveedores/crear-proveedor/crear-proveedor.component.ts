

 import { Component } from '@angular/core';
 import { Router } from '@angular/router';
 import { ProveedorService } from 'src/app/service/proveedor.service';
 import { ProveedorCreateDTO } from 'src/app/service/proveedor.service';
 import Swal from 'sweetalert2';
 import { SidebarService } from 'src/app/service/sidebar.service';

 @Component({
   selector: 'app-crear-proveedor',
   templateUrl: './crear-proveedor.component.html',
   styleUrls: ['./crear-proveedor.component.scss']
 })
export class CrearProveedorComponent {

  
   // Define si es vista de creación o edición
   vista: 'listado'|'nuevo' | 'editar' = 'nuevo';

   // Control de pestañas activas (legacy)
   tabActiva: any  = 'general';

   // Control de pasos (flujo ágil)
   pasoActual: 1 | 2 = 1;

   mostrarComercial = false;
   mostrarBancario = false;

    marcas: string[] = [];
   marcaInput: string = '';

   // Pestañas visibles en la parte superior (legacy, no se usa en alta ágil)
   tabs = [
     { id: 'general', label: 'providers.tabs.general', icon: 'bi bi-info-circle' },
     { id: 'contacto', label: 'providers.tabs.contact', icon: 'bi bi-telephone' },
     { id: 'comercial', label: 'providers.tabs.commercial', icon: 'bi bi-box' },
     { id: 'catalogo', label: 'providers.tabs.catalog', icon: 'bi bi-journal' },
     { id: 'bancario', label: 'providers.tabs.bank', icon: 'bi bi-currency-dollar' },
     { id: 'observaciones', label: 'providers.tabs.notes', icon: 'bi bi-three-dots' }
   ];
   // Lista de provincias argentinas para el selector
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

    nuevoProveedor: ProveedorCreateDTO = {
    
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
    private proveedorService: ProveedorService,
    private router: Router,
    private sidebarService: SidebarService
   ) {
     
 }
  

   // --- Métodos principales ---

   /** Cambia la pestaña activa */
  //  setTabActiva(tabId: string) {
  //    this.tabActiva = tabId;
  //  }

  //  agregarMarca() {
  //    if (this.marcaInput.trim()) {
  //      this.marcas.push(this.marcaInput.trim());
  //      this.marcaInput = '';
  //    }
  //  }


  //  eliminarMarca(index: number) {
  //    this.marcas.splice(index, 1);
  //  }

  //  /** Guarda el proveedor (simulación de envío al backend) */
  //  guardarProveedor() {
  //     if (!this.nuevoProveedor.razonSocial || !this.nuevoProveedor.cuit) {
  //       alert('Por favor completá los campos obligatorios: Razón Social y CUIT.');
  //       return;
  //     }

  //    console.log('📦 Enviando proveedor al backend:', this.nuevoProveedor);
  //     this.proveedorService.crear(this.nuevoProveedor).subscribe({
  //       next: () => {
  //         alert('Proveedor guardado con éxito ✅');
  //         this.router.navigate(['/stock/proveedores']);
  //       },
  //       error: err => {
  //         console.error('❌ Error al guardar proveedor:', err);
  //         alert('Hubo un error al guardar el proveedor.');
  //       }
  //     });
  //   }
  guardarProveedor() {
  if (!this.validarPaso2()) {
    return;
  }

  this.nuevoProveedor.tiempoEntrega =
      this.nuevoProveedor.tiempoEntrega ? Number(this.nuevoProveedor.tiempoEntrega) : undefined;

  this.nuevoProveedor.pedidoMinimo =
      this.nuevoProveedor.pedidoMinimo ? Number(this.nuevoProveedor.pedidoMinimo) : undefined;

  this.nuevoProveedor.descuentoVolumen =
      this.nuevoProveedor.descuentoVolumen ? Number(this.nuevoProveedor.descuentoVolumen) : undefined;

  console.log("Enviando proveedor:", this.nuevoProveedor);

  this.proveedorService.crear(this.nuevoProveedor).subscribe({
    next: () => {
      Swal.fire({
        icon: 'success',
        title: 'Proveedor creado',
        text: 'El proveedor fue guardado correctamente',
        timer: 1500,
        showConfirmButton: false
      });
      this.router.navigate(['/stock/proveedores']);
    },
    error: err => {
      console.error('Error al guardar proveedor', err);
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudo guardar el proveedor',
        confirmButtonColor: '#00796b'
      });
    }
  });
}


  //   cancelar() {
  //    this.router.navigate(['/stock/proveedores']);
  //  }

   setTabActiva(tabId: string): void {
    // legacy
    this.tabActiva = tabId;
  }

  // Eventos de los steps (next / prev)
  irAlSiguiente(tabId: string): void {
    // legacy
    this.tabActiva = tabId;
  }

  irAlAnterior(tabId: string): void {
    // legacy
    this.tabActiva = tabId;
  }

  // --- Navegación entre pasos ---
irSiguienteStep(): void {
  // legacy
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
  // legacy
  switch (this.tabActiva) {
    case 'contacto': this.tabActiva = 'general'; break;
    case 'comercial': this.tabActiva = 'contacto'; break;
    case 'catalogo': this.tabActiva = 'comercial'; break;
    case 'bancario': this.tabActiva = 'catalogo'; break;
    case 'observaciones': this.tabActiva = 'bancario'; break;
    default: break;
  }
 }

  irAPaso2(): void {
    if (!this.validarPaso1()) {
      return;
    }
    this.pasoActual = 2;
  }

  volverAPaso1(): void {
    this.pasoActual = 1;
  }

  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }

  private validarPaso1(): boolean {
    const requerido = [
      this.nuevoProveedor.razonSocial,
      this.nuevoProveedor.cuit,
      this.nuevoProveedor.condicionIVA,
      this.nuevoProveedor.telefono,
      this.nuevoProveedor.email
    ];

    if (requerido.some((v) => !String(v || '').trim())) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos obligatorios',
        text: 'Completá Razón Social, CUIT, Condición IVA, Teléfono y Email.',
        confirmButtonColor: '#00796b'
      });
      return false;
    }
    return true;
  }

  private validarPaso2(): boolean {
    if (!this.nuevoProveedor.direccion?.trim() ||
        !this.nuevoProveedor.ciudad?.trim() ||
        !this.nuevoProveedor.provincia?.trim()) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos obligatorios',
        text: 'Completá Dirección, Ciudad y Provincia.',
        confirmButtonColor: '#00796b'
      });
      return false;
    }

    if (this.nuevoProveedor.tieneCatalogo && !this.nuevoProveedor.urlCatalogo?.trim()) {
      Swal.fire({
        icon: 'warning',
        title: 'Falta la URL del catálogo',
        text: 'Si tiene catálogo, completá la URL.',
        confirmButtonColor: '#00796b'
      });
      return false;
    }

    return true;
  }
}

// import { Component } from '@angular/core';
// import { Router } from '@angular/router';
// import { ProveedorService } from 'src/app/service/proveedor.service';
// import { ProveedorCreateDTO } from 'src/app/service/proveedor.service';

// @Component({
//   selector: 'app-crear-proveedor',
//   templateUrl: './crear-proveedor.component.html',
//   styleUrls: ['./crear-proveedor.component.scss']
// })
// export class CrearProveedorComponent {

//   // 🔸 Paso actual del wizard (1 a 4)
//   pasoActual: number = 1;

//   // 🔸 Objeto de proveedor que se va completando en los steps
//   nuevoProveedor: ProveedorCreateDTO = {
//     razonSocial: '',
//     nombreComercial: '',
//     cuit: '',
//     condicionIVA: '',
//     estado: 'Activo',
//     telefono: '',
//     email: '',
//     horarioAtencion: '',
//     direccion: '',
//     ciudad: '',
//     provincia: '',
//     codigoPostal: '',
//     especialidad: '',
//     tiempoEntrega: '',
//     pedidoMinimo: '',
//     descuentoVolumen: '',
//     condicionesPago: '',
//     tieneStock: false,
//     atieneUrgencias: false,
//     haceEnvios: false,
//     aceptaDevoluciones: false,
//     zonaCobertura: '',
//     tieneCatalogo: false,
//     urlCatalogo: '',
//     codigoCliente: '',
//     banco: '',
//     tipoCuenta: '',
//     cbu: '',
//     observaciones: ''
//   };

//   constructor(private proveedorService: ProveedorService, private router: Router) {}

//   // 🔹 Avanza al siguiente paso
//   avanzar() {
//     if (this.pasoActual < 4) {
//       this.pasoActual++;
//     }
//   }

//   // 🔹 Retrocede al paso anterior
//   retroceder() {
//     if (this.pasoActual > 1) {
//       this.pasoActual--;
//     }
//   }

//   // 🔹 Guarda el proveedor final
//   guardarProveedor() {
//     console.log('📦 Enviando proveedor al backend:', this.nuevoProveedor);

//     this.proveedorService.crear(this.nuevoProveedor).subscribe({
//       next: () => {
//         alert('✅ Proveedor guardado con éxito');
//         this.router.navigate(['/stock/proveedores']);
//       },
//       error: err => {
//         console.error('❌ Error al guardar proveedor:', err);
//         alert('Hubo un error al guardar el proveedor.');
//       }
//     });
//   }

