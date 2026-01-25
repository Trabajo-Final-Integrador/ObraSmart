import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrdenCompraService } from 'src/app/service/orden-compra.service';
import { OrdenCompraDTO } from 'src/app/service/orden-compra.service';
import { ProveedorService, ProveedorDto } from 'src/app/service/proveedor.service';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-detalle-orden',
  templateUrl: './detalle-orden.component.html',
  styleUrls: ['./detalle-orden.component.scss']
})
export class DetalleOrdenComponent implements OnInit {

  @ViewChild('ordenContent') ordenContent!: ElementRef;

  orden?: OrdenCompraDTO;
  proveedor?: ProveedorDto;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private ordenSrv: OrdenCompraService,
    private proveedorSrv: ProveedorService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.ordenSrv.obtener(id).subscribe({
      next: data => {
        this.orden = data;
        // Obtener información completa del proveedor
        if (data.idProveedor) {
          this.proveedorSrv.obtenerPorId(data.idProveedor).subscribe({
            next: proveedor => {
              this.proveedor = proveedor;
              this.loading = false;
            },
            error: err => {
              console.error("Error obteniendo proveedor:", err);
              this.loading = false;
            }
          });
        } else {
          this.loading = false;
        }
      },
      error: err => {
        console.error("Error obteniendo detalle:", err);
        this.loading = false;
      }
    });
  }

  calcularTotal(): number {
    if (!this.orden || !this.orden.items) {
      return 0;
    }
    return this.orden.items.reduce((total, item) => {
      return total + ((item?.cantidad || 0) * (item?.precioUnitario || 0));
    }, 0);
  }

  crearFilasVacias(): number[] {
  if (!this.orden?.items) return [];
  return Array(Math.max(0, 6 - this.orden.items.length)).fill(0);
}


  async exportarPDF(): Promise<void> {
    if (!this.ordenContent) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudo generar el PDF'
      });
      return;
    }

    try {
      Swal.fire({
        title: 'Generando PDF...',
        text: 'Por favor espera',
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });

      const element = this.ordenContent.nativeElement;
      const canvas = await html2canvas(element, {
        scale: 2,
        useCORS: true,
        logging: false
      });

      const imgData = canvas.toDataURL('image/png');
      const pdf = new jsPDF({
        orientation: 'portrait',
        unit: 'mm',
        format: 'a4'
      });

      const imgWidth = 210; // A4 width in mm
      const pageHeight = 297; // A4 height in mm
      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      let heightLeft = imgHeight;
      let position = 0;

      pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
      heightLeft -= pageHeight;

      while (heightLeft >= 0) {
        position = heightLeft - imgHeight;
        pdf.addPage();
        pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
        heightLeft -= pageHeight;
      }

      pdf.save(`Orden_${this.orden?.id || 'detalle'}.pdf`);

      Swal.fire({
        icon: 'success',
        title: 'PDF generado',
        text: 'El archivo se ha descargado correctamente',
        timer: 2000,
        showConfirmButton: false
      });
    } catch (error) {
      console.error('Error al generar PDF:', error);
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudo generar el PDF'
      });
    }
  }

  getStatusKey(status: string | null | undefined): string {
    const value = (status || '').toString().trim().toUpperCase();
    switch (value) {
      case 'PENDIENTE':
      case 'PENDING':
        return 'orders.status.pending';
      case 'APROBADA':
      case 'APPROVED':
        return 'orders.status.approved';
      case 'RECIBIDA':
      case 'RECEIVED':
        return 'orders.status.received';
      case 'CANCELADA':
      case 'CANCELED':
      case 'CANCELLED':
        return 'orders.status.canceled';
      default:
        return 'orders.status.pending';
    }
  }
}
