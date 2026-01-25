export const MEDIA_OFFSETS = {
  EQUIPO: 0,
  USUARIO: 1_000_000,
  REPARACION: 2_000_000,
  REPUESTO: 3_000_000
};

export const MEDIA_PLACEHOLDER = 'assets/img/image-placeholder.svg';

export function appendCacheBust(url?: string, bust?: number | string): string {
  if (!url) return MEDIA_PLACEHOLDER;
  const value = bust ?? Date.now();
  const sep = url.includes('?') ? '&' : '?';
  return `${url}${sep}t=${value}`;
}

export function buildReparacionMediaId(id: number): number {
  return MEDIA_OFFSETS.REPARACION + id;
}

export function buildRepuestoMediaId(id: number): number {
  return MEDIA_OFFSETS.REPUESTO + id;
}

export function validateImageFile(file: File): string | null {
  const allowedTypes = ['image/png', 'image/jpeg', 'image/jpg', 'image/webp'];
  const maxSize = 5 * 1024 * 1024;

  if (!allowedTypes.includes(file.type)) {
    return 'Formato de imagen no válido';
  }

  if (file.size > maxSize) {
    return 'La imagen supera el tamaño máximo de 5MB';
  }

  return null;
}
