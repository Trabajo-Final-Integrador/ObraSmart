export const MEDIA_PLACEHOLDER =
  'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="120" height="90" viewBox="0 0 120 90"><rect width="120" height="90" fill="%23e5e7eb"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="%239ca3af" font-family="Arial" font-size="12">Sin imagen</text></svg>';

const MEDIA_BASE_OFFSETS = {
  user: 1_000_000,
  reparacion: 2_000_000,
  repuesto: 3_000_000,
} as const;

export const MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;

export function buildReparacionMediaId(id: number | string): number {
  return MEDIA_BASE_OFFSETS.reparacion + Number(id || 0);
}

export function buildRepuestoMediaId(id: number | string): number {
  return MEDIA_BASE_OFFSETS.repuesto + Number(id || 0);
}

export function appendCacheBust(url: string, cacheBust?: number): string {
  if (!url) return url;
  if (!cacheBust) return url;
  const separator = url.includes('?') ? '&' : '?';
  return `${url}${separator}t=${cacheBust}`;
}

export function validateImageFile(file: File): string | null {
  if (file.size > MAX_IMAGE_SIZE_BYTES) {
    return 'El archivo supera 5MB';
  }
  if (!file.type.startsWith('image/')) {
    return 'Formato inválido: solo imágenes';
  }
  return null;
}
