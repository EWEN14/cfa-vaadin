package nc.unc.application.views.formation;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "formations", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Formations | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class FormationListView extends VerticalLayout {

  // VirtualList<> cardsFormations = new VirtualList<Formation>();

  // private List<Formation> = FormationService.fin
}
