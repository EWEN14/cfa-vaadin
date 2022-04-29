package nc.unc.application.views.formation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.PermitAll;
import java.util.List;
import org.springframework.stereotype.Component;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "formations", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Formations | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class FormationListView extends VerticalLayout {

  FormationService formationService;

  VirtualList<Formation> cardsFormations = new VirtualList<>();
  Button addFormationButton = new Button("Nouvelle Formation");

  // Composant qui définit l'aspect de chaque "carte" qui présente une formation
  private final ComponentRenderer<com.vaadin.flow.component.Component, Formation> formationCardRenderer = new ComponentRenderer<>(formation -> {
    HorizontalLayout cardLayout = new HorizontalLayout();
    cardLayout.setMargin(true);

    H3 libelleFormation = new H3(formation.getLibelleFormation());
    Span directeurFormation = new Span("Directeur de formation : "+formation.getReferentPedagogique().getPrenomReferentPedago()
            +" "+formation.getReferentPedagogique().getNomReferentPedago());

    VerticalLayout insideCardLayout = new VerticalLayout();
    insideCardLayout.add(libelleFormation, directeurFormation);

    cardLayout.add(insideCardLayout);
    return cardLayout;
  });

  public FormationListView(FormationService formationService) {
    this.formationService = formationService;

    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)

    getAllFormations(); // récupérations de toutes les formations

    addFormationButton.addClickListener(click -> addFormation());

    // on passe les formation à notre VirtualList
    cardsFormations.setItems(getAllFormations());
    // et on lui définit son rendu graphique avec notre petit composant formationCardRenderer
    cardsFormations.setRenderer(formationCardRenderer);

    add(addFormationButton, cardsFormations);
  }

  void addFormation() {
    Notification.show("SALUT !");
  }

  public List<Formation> getAllFormations() {
    return formationService.findAllFormations("");
  }
}
