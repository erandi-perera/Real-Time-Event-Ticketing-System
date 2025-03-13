import { Component } from '@angular/core'; // Importing Component decorator from Angular core.
import { TicketingSystemService } from '../ticketing-system/ticketing-system.service'; // Importing the service

@Component({
    selector: 'app-control-panel', // The HTML tag to use this component in the application.
    templateUrl: './control-panel.component.html',
    styleUrls: ['./control-panel.component.css'],
    standalone: true,
})

export class ControlPanelComponent {

    // Injecting the TicketingSystemService to interact with the backend.
    constructor(private ticketingSystemService: TicketingSystemService) { }

    startOperations() {
        this.ticketingSystemService.startOperations().subscribe(
            (res) => alert(res.message),
            (error) => alert(error.error.message)
        );
    }

    stopOperations() {
        this.ticketingSystemService.stopOperations().subscribe(
            (res) => alert(res.message),
            (error) => alert(error.error.message),
        );
    }

    resetOperations() {
        this.ticketingSystemService.resetOperations().subscribe(
            (res) => alert(res.message),
            (error) => alert(error.error.message),
        );
    }
}
