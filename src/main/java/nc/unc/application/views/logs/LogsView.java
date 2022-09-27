package nc.unc.application.views.logs;


import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import nc.unc.application.data.entity.LogEnregistrement;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import java.util.stream.Stream;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "logs", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Logs") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class LogsView extends VerticalLayout {
    Grid<LogEnregistrement> grid = new Grid<>(LogEnregistrement.class);
    LogEnregistrmentService logEnregistrmentService;
    TextField filtertext = new TextField();
    Button deleteAncienLogs;

    private HorizontalLayout getToolbar(){
        filtertext.setHelperText("Recherche par description...");
        filtertext.setPrefixComponent(VaadinIcon.SEARCH.create());
        filtertext.setClearButtonVisible(true);
        filtertext.setValueChangeMode(ValueChangeMode.LAZY);
        filtertext.addValueChangeListener(e -> updateList());

        deleteAncienLogs = new Button("Supprimer les anciens logs");
        deleteAncienLogs.addClickListener(click -> deleteAncienLogs());

        HorizontalLayout toolbar = new HorizontalLayout(filtertext, deleteAncienLogs);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public LogsView(LogEnregistrmentService logEnregistrmentService){
        this.logEnregistrmentService = logEnregistrmentService;
        addClassName("list-view");
        setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
        add(new H2("Registre des dernières activités"));

        add(getToolbar());

        // Ajout de la grid
        grid.addClassNames("logs-grid");
        grid.setSizeFull();
        grid.setColumns("createdAt","executant","typeCrud","description_log");
        grid.getColumnByKey("description_log").setWidth("30em").setFlexGrow(0);

        // Ajout des détails des items
        grid.setDetailsVisibleOnClick(true);
        grid.setItemDetailsRenderer(createLogsDetailRenderer());

        add(grid);
        updateList();
    }

    private void updateList() {
        grid.setItems(logEnregistrmentService.findAllLogs(filtertext.getValue()));
    }

    public void deleteAncienLogs(){
        logEnregistrmentService.deleteAncienLogs();
    }
    // Création de l'interface des détails des items
    private ComponentRenderer<LogsDetailFormLayout, LogEnregistrement> createLogsDetailRenderer(){
        return new ComponentRenderer<>(LogsDetailFormLayout::new, LogsDetailFormLayout::setLogs);
    }

    public static class LogsDetailFormLayout extends FormLayout {
        private final TextArea description = new TextArea("Description");
        private final TextField executant = new TextField("Executant");
        private final DatePicker date = new DatePicker("Date");
        private final TextField typemodif = new TextField("Type de modification");


        public LogsDetailFormLayout() {
            description.setReadOnly(true);
            executant.setReadOnly(true);
            date.setReadOnly(true);
            typemodif.setReadOnly(true);
            add(executant, date, typemodif, description);
            setResponsiveSteps(new ResponsiveStep("0", 3));
            setColspan(description, 3);
            setColspan(executant, 1);
            setColspan(date, 1);
            setColspan(typemodif, 1);
        }

        public void setLogs(LogEnregistrement log){
            description.setValue(log.getDescription_log());
            executant.setValue(log.getExecutant());
            date.setValue(log.getCreatedAt().toLocalDate());
            typemodif.setValue(log.getTypeCrud().toString());
        }
    }
}
