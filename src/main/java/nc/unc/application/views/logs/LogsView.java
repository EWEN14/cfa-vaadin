package nc.unc.application.views.logs;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import nc.unc.application.data.entity.LogEnregistrement;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "logs", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Logs") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class LogsView extends VerticalLayout {
    Grid<LogEnregistrement> grid = new Grid<>(LogEnregistrement.class);
    LogEnregistrmentService logEnregistrmentService;
    public LogsView(LogEnregistrmentService logEnregistrmentService){
        this.logEnregistrmentService = logEnregistrmentService;
        addClassName("list-view");
        setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
        add(new H2("Registre des dernières activités"));
        grid.addClassNames("logs-grid");
        grid.setSizeFull();
        grid.setColumns("typeCrud","description_log","executant");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(grid);
        updateList();
    }
    private void updateList() {
        grid.setItems(logEnregistrmentService.findAllLogs(""));
    }

}
