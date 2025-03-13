import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { ControlPanelComponent } from './control-panel/control-panel.component';
import { LogDisplayComponent } from './log-display/log-display.component';
import { ConfigurationFormComponent } from './configuration-form/configuration-form.component';

@NgModule({
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        AppComponent,
        ControlPanelComponent,
        LogDisplayComponent,
        ConfigurationFormComponent,
    ],
    bootstrap: [AppComponent]
})
export class AppModule {}
