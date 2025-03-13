import { Injectable } from '@angular/core'; // Importing Injectable decorator from Angular core to define a service.
import {HttpClient} from '@angular/common/http'; // Importing HttpClient to make HTTP requests.
import { Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class TicketingSystemService {

    private apiUrl = 'http://localhost:8080/api/ticket';

    // Injecting HttpClient to handle HTTP requests to the backend.
    constructor(private http: HttpClient) { }

    startOperations():  Observable<any> {
        return this.http.get(`${this.apiUrl}/start-operations`, {});
    }


    stopOperations():  Observable<any> {
        return this.http.get(`${this.apiUrl}/stop-operations`, {});
    }


    resetOperations():  Observable<any> {
        return this.http.get<string>(`${this.apiUrl}/reset-operations`, {});
    }


    updateConfiguration(config: any):  Observable<any> {
        return this.http.put(`${this.apiUrl}/update-configuration`, config);
    }


    getTicketStatus():  Observable<any> {
        return this.http.get(`${this.apiUrl}/status`, {});
    }


    getConfiguration():  Observable<any> {
        return this.http.get(`${this.apiUrl}/configuration`, {});
    }
}
