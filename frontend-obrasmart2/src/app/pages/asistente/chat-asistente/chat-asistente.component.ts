import { Component, OnInit, HostListener } from '@angular/core';
import { AsistenteService } from '../asistente.service';
import { TranslateService } from '@ngx-translate/core';

interface Mensaje {
  emisor: 'usuario' | 'asistente';
  texto: string;
  fecha: Date;
}

@Component({
  selector: 'app-chat-asistente',
  templateUrl: './chat-asistente.component.html',
  styleUrls: ['./chat-asistente.component.scss']
})
export class ChatAsistenteComponent implements OnInit {

  mensaje = '';
  mensajes: Mensaje[] = [];
  cargando = false;

  mostrarChat = false;
  darkMode = false;

  // Sonido
  audio = new Audio('assets/asistente/notificacion.mp3');

  // Drag
  private dragging = false;
  private offsetX = 0;
  private offsetY = 0;

  // Resize
  private resizing = false;
  private startWidth = 0;
  private startHeight = 0;
  private startMouseX = 0;
  private startMouseY = 0;

  constructor(private asistenteService: AsistenteService, private translate: TranslateService) {}

  ngOnInit() {
    const savedMode = localStorage.getItem('asistente-dark');
    if (savedMode === 'true') this.darkMode = true;

    // bajar volumen
    this.audio.volume = 0.25;
  }

  toggleChat() {
    this.mostrarChat = !this.mostrarChat;
  }

  toggleMode() {
    this.darkMode = !this.darkMode;
    localStorage.setItem('asistente-dark', String(this.darkMode));
  }

  enviar() {
    const texto = this.mensaje.trim();
    if (!texto) return;

    this.mensajes.push({ emisor: 'usuario', texto, fecha: new Date() });
    this.cargando = true;

    this.asistenteService.enviarMensaje(texto).subscribe({
      next: resp => {
        this.mensajes.push({ emisor: 'asistente', texto: resp.respuesta, fecha: new Date() });
        this.audio.play().catch(() => {});
        this.cargando = false;
      },
      error: () => {
        this.mensajes.push({
          emisor: 'asistente',
          texto: this.translate.instant('chat.error'),
          fecha: new Date()
        });
        this.cargando = false;
      }
    });

    this.mensaje = '';
  }

  // firma elegante
  formatearRespuesta(texto: string): string {
    const firmaRegex = /Asistente[\s\S]*?Altamirano/gi;
    return texto.replace(firmaRegex,
      `<div class="firma-chica">
        Asistente ObraSmart<br>
        <span>Ferreyra & Castro Altamirano</span>
      </div>`);
  }

  // ================================
  // DRAG
  // ================================
  startDrag(event: MouseEvent, element: HTMLElement) {
    this.dragging = true;
    this.offsetX = event.clientX - element.offsetLeft;
    this.offsetY = event.clientY - element.offsetTop;
  }

  // ================================
  // RESIZE
  // ================================
  startResize(event: MouseEvent, element: HTMLElement) {
    this.resizing = true;

    this.startWidth = element.offsetWidth;
    this.startHeight = element.offsetHeight;
    this.startMouseX = event.clientX;
    this.startMouseY = event.clientY;

    event.stopPropagation();
  }

  // ================================
  // MOVIMIENTO GLOBAL
  // ================================
  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent) {
    const windowEl = document.querySelector('.asistente-container') as HTMLElement;
    if (!windowEl) return;

    if (this.dragging) {
      windowEl.style.left = `${event.clientX - this.offsetX}px`;
      windowEl.style.top = `${event.clientY - this.offsetY}px`;
    }

    if (this.resizing) {
      windowEl.style.width = `${this.startWidth + (event.clientX - this.startMouseX)}px`;
      windowEl.style.height = `${this.startHeight + (event.clientY - this.startMouseY)}px`;
    }
  }

  @HostListener('document:mouseup')
  endActions() {
    this.dragging = false;
    this.resizing = false;
  }
}
