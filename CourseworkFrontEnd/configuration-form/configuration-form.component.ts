import { Component } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { TicketingSystemService } from '../ticketing-system/ticketing-system.service';

@Component({
    selector: 'app-configuration-form',
    templateUrl: './configuration-form.component.html',
    standalone: true,
    imports: [FormsModule],
    styleUrls: ['./configuration-form.component.css']
})

export class ConfigurationFormComponent {
    totalTickets: number = 0;
    ticketReleaseRate: number = 0;
    customerRetrievalRate: number = 0;
    maxTicketCapacity: number = 0;

    constructor(private ticketingService: TicketingSystemService) { }

    onSubmit() {

        if(this.totalTickets == 0 || this.ticketReleaseRate == 0 ||
            this.customerRetrievalRate == 0 || this.maxTicketCapacity == 0){
            alert("Required valid values.")
        }
        else if(this.ticketReleaseRate < 1000 || this.customerRetrievalRate < 100){
            alert("Enter rates minimum of 1000ms (1s) to perform efficiently.")
        }
        else {
            const config = {
                totalTickets: this.totalTickets,
                ticketReleaseRate: this.ticketReleaseRate,
                customerRetrievalRate: this.customerRetrievalRate,
                maxTicketCapacity: this.maxTicketCapacity,
            };

            this.ticketingService.updateConfiguration(config).subscribe(
                (res) => alert(res.message),
                (error) => alert(error.error.message)
            );
        }
    }

    ngOnInit() {
        this.ticketingService.getConfiguration().subscribe(
            (res) => {
                this.totalTickets = res.data.totalTickets
                this.ticketReleaseRate = res.data.ticketReleaseRate
                this.customerRetrievalRate = res.data.customerRetrievalRate
                this.maxTicketCapacity = res.data.maxTicketCapacity
                // alert(res.message)
            },
            (error) => {}
        );
    }
}
