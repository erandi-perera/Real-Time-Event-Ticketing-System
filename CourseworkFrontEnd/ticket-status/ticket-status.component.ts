import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription, interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { TicketingSystemService } from '../ticketing-system/ticketing-system.service';

@Component({
    selector: 'app-ticket-status', // The HTML tag to use this component in the application.
    templateUrl: './ticket-status.component.html',
    standalone: true,
    styleUrls: ['./ticket-status.component.css']
})

export class TicketStatusComponent implements OnInit, OnDestroy {
    totalTickets: number = 0;
    remainingTickets: number = 0;
    private subscription: Subscription = new Subscription();

    constructor(private ticketService: TicketingSystemService) {}

    ngOnInit(): void {
        this.subscription = interval(1000)
            .pipe(
                switchMap(() => this.ticketService.getTicketStatus())
            )
            .subscribe(
                (res: any) => {
                    if (res) {
                        this.totalTickets = res.data.totalTickets;
                        this.remainingTickets = res.data.remainingTickets;
                    }
                }
            );
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }
}
