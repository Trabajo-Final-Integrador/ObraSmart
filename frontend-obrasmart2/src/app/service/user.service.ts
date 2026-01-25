import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UserDto {
  id: number;
  username: string;
  nombre?: string;
  apellido?: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = `${(environment as any).gatewayUrl || environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  listar(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(this.baseUrl, { withCredentials: true });
  }
}
