import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ControlPanelComponent } from './control-panel/control-panel.component';

const routes: Routes = [
    { path: '', component: ControlPanelComponent },

];

@NgModule({
    imports: [RouterModule.forRoot(routes)],

    exports: [RouterModule]

})
export class AppRoutingModule { }
