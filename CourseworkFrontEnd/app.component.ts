import { Component } from '@angular/core';
import { ControlPanelComponent } from "./control-panel/control-panel.component";
import { ConfigurationFormComponent } from "./configuration-form/configuration-form.component";
import { TicketStatusComponent } from "./ticket-status/ticket-status.component";
import { LogDisplayComponent } from "./log-display/log-display.component";

@Component({
    selector: 'app-root', // Defines the custom HTML tag <app-root> used in index.html to bootstrap the application.
    templateUrl: './app.component.html',
    standalone: true,
    imports: [
        ControlPanelComponent,
        ConfigurationFormComponent,
        TicketStatusComponent,
        LogDisplayComponent,
    ],
    styleUrls: ['./app.component.css']
})
export class AppComponent {}

