

 import { Component } from '@angular/core';
 import { Router } from '@angular/router';
 import { ProveedorService } from 'src/app/service/proveedor.service';
 import { ProveedorCreateDTO } from 'src/app/service/proveedor.service';

 @Component({
   selector: 'app-crear-proveedor',
   templateUrl: './crear-proveedor.component.html',
   styleUrls: ['./crear-proveedor.component.scss']
 })
 export class CrearProveedorComponent {

  
   // Define si es vista de creación o edición
   vista: 'listado'|'nuevo' | 'editar' = 'nuevo';

   // Control de pestañas activas
   tabActiva: any  = 'general';

    marcas: string[] = [];
   marcaInput: string = '';

   // Pestañas visibles en la parte superior
   tabs = [
     { id: 'general', label: 'General', icon: 'bi bi-info-circle' },
     { id: 'contacto', label: 'Contacto', icon: 'bi bi-telephone' },
     { id: 'comercial', label: 'Comercial', icon: 'bi bi-box' },
     { id: 'catalogo', label: 'Catálogo', icon: 'bi bi-journal' },
     { id: 'bancario', label: 'Datos Bancarios', icon: 'bi bi-currency-dollar' },
     { id: 'observaciones', label: 'Observaciones', icon: 'bi bi-three-dots' }
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

   constructor(private proveedorService: ProveedorService, private router: Router) {
    
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

  this.nuevoProveedor.tiempoEntrega =
      this.nuevoProveedor.tiempoEntrega ? Number(this.nuevoProveedor.tiempoEntrega) : undefined;

  this.nuevoProveedor.pedidoMinimo =
      this.nuevoProveedor.pedidoMinimo ? Number(this.nuevoProveedor.pedidoMinimo) : undefined;

  this.nuevoProveedor.descuentoVolumen =
      this.nuevoProveedor.descuentoVolumen ? Number(this.nuevoProveedor.descuentoVolumen) : undefined;

  console.log("Enviando proveedor:", this.nuevoProveedor);

  this.proveedorService.crear(this.nuevoProveedor).subscribe({
    next: () => {
      alert('Proveedor guardado con éxito');
      this.router.navigate(['/stock/proveedores']);
    },
    error: err => {
      console.error('Error al guardar proveedor', err);
      alert('Error al guardar proveedor');
    }
  });
}


  //   cancelar() {
  //    this.router.navigate(['/stock/proveedores']);
  //  }

   setTabActiva(tabId: string): void {
    this.tabActiva = tabId;
  }

  // Eventos de los steps (next / prev)
  irAlSiguiente(tabId: string): void {
    this.tabActiva = tabId;
  }

  irAlAnterior(tabId: string): void {
    this.tabActiva = tabId;
  }

  // --- Navegación entre pasos ---
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

