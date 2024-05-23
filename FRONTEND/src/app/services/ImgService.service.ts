import { HttpClient } from '@angular/common/http';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class ImgService {
  private readonly apiKey: string = 'bc683f1a31f593c05144a4f985dcb3ed';

  constructor(private readonly httpClient: HttpClient) {}

  upload(file: File): Observable<string> {
    const fordata = new FormData();

    fordata.append('image', file);

    return this.httpClient
      .post('/upload', fordata, { params: { key: this.apiKey } })
      .pipe(map((response: any) => response['data']['url']));
  }
}