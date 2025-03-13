import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {HttpClient} from "@angular/common/http";

interface LogEntry {
    dateTime: string;
    message: string;
}

@Injectable({
    providedIn: 'root',
})
export class LogService {
    private eventSource!: EventSource;

    constructor(private http: HttpClient) {}

    getLogs(): Observable<LogEntry[]> {
        return new Observable<LogEntry[]>((observer) => {
            this.eventSource = new EventSource('http://localhost:8080/logs') as EventSource;

            this.eventSource.onmessage = (event) => {
                const logs: LogEntry[] = JSON.parse(event.data);
                observer.next(logs);
            };

            this.eventSource.onerror = (error) => {
                observer.error(error);
                this.eventSource.close();
            };

            return () => this.eventSource.close();
        });
    }

    clearLogs(): Observable<any> {
        return this.http.get('http://localhost:8080/logs/clear', {});
    }
}
