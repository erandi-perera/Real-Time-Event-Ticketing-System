import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogService } from './LogService';

interface LogEntry {
    dateTime: string;
    message: string;
}

@Component({
    selector: 'app-log-display',
    templateUrl: './log-display.component.html',
    standalone: true,
    styleUrls: ['./log-display.component.css'],
    imports: [CommonModule]
})

export class LogDisplayComponent implements OnInit {
    logs: LogEntry[] = [];
    constructor(private logService: LogService) {}

    clearLogs() {
        this.logService.clearLogs().subscribe(
            (res) => {
                this.logs = [];
                alert(res.message);
            },
            (error) => console.error('Error clearing logs:', error),
        );
    }

    ngOnInit() {
        this.logService.getLogs().subscribe(
            (logArray) => this.logs = logArray,
            (error) => console.error('Error receiving logs:', error)
        );
    }
}
