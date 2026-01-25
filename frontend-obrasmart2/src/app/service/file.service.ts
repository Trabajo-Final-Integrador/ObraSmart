import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class FileService {
  private baseUrl = `${environment.apiUrl}/files`;

  constructor(private http: HttpClient) {}

  upload(ownerType: string, ownerId: string | number, purpose: string, file: File) {
    const form = new FormData();
    form.append('file', file);
    form.append('ownerType', ownerType);
    form.append('ownerId', String(ownerId));
    form.append('purpose', purpose);
    return this.http.post<any>(this.baseUrl, form);
  }

  getByOwner(ownerType: string, ownerId: string | number, purpose: string) {
    const params = { ownerType, ownerId: String(ownerId), purpose };
    return this.http.get<any[]>(`${this.baseUrl}/by-owner`, { params });
  }

  batchByOwners(ownerType: string, purpose: string, ownerIds: (string | number)[]) {
    return this.http.post<Record<string, any>>(`${this.baseUrl}/by-owners`, {
      ownerType,
      purpose,
      ownerIds: ownerIds.map(String),
    });
  }

  getUrl(fileId: string) {
    return `${this.baseUrl}/${fileId}`;
  }
}
