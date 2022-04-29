package nc.unc.application.views.formation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.data.service.ReferentPedagogiqueService;
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

  // services
  FormationService formationService;
  ReferentPedagogiqueService referentPedagogiqueService;
  LogEnregistrmentService logEnregistrmentService;

  // modales
  FormationConsult modalConsult;
  FormationNewOrEdit modalNewOrEdit;

  VirtualList<Formation> cardsFormations = new VirtualList<>();
  Button addFormationButton = new Button("Nouvelle Formation");

  // Composant qui définit l'aspect de chaque "carte" qui présente une formation
  private final ComponentRenderer<com.vaadin.flow.component.Component, Formation> formationCardRenderer = new ComponentRenderer<>(formation -> {
    HorizontalLayout cardLayout = new HorizontalLayout();
    cardLayout.setMargin(true);

    H3 libelleFormation = new H3(formation.getLibelleFormation());
    Span directeurFormation = new Span("Directeur de formation : "+formation.getReferentPedagogique().getPrenomReferentPedago()
            +" "+formation.getReferentPedagogique().getNomReferentPedago());

    // bouton de consultation de la formation
    Button consultFormationButton = new Button(VaadinIcon.EYE.create());
    consultFormationButton.addClickListener(click -> consultFormation(formation));

    // bouton d'édition de la formation
    Button editFormationButton = new Button(VaadinIcon.PENCIL.create());
    editFormationButton.addClickListener(click -> editFormationModal(formation));

    // on place les boutons côte à côte dans un HorizontalLayout
    HorizontalLayout buttonsContainer = new HorizontalLayout(consultFormationButton, editFormationButton);

    VerticalLayout insideCardLayout = new VerticalLayout();
    insideCardLayout.add(libelleFormation, directeurFormation, buttonsContainer);

    // ajout des éléments dans l'horizontal layout
    cardLayout.add(insideCardLayout);
    return cardLayout;
  });

  public FormationListView(FormationService formationService, ReferentPedagogiqueService referentPedagogiqueService,
                           LogEnregistrmentService logEnregistrmentService) {
    this.formationService = formationService;
    this.referentPedagogiqueService = referentPedagogiqueService;
    this.logEnregistrmentService = logEnregistrmentService;

    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)

    // au clic sur le bouton d'ajout d'une formation, on appelle la méthode qui permettra d'en créer une nouvelle
    addFormationButton.addClickListener(click -> addFormation());

    // et on lui définit son rendu graphique avec notre petit composant formationCardRenderer
    cardsFormations.setRenderer(formationCardRenderer);

    modalNewOrEdit = new FormationNewOrEdit(referentPedagogiqueService.findAllReferentPedagogique(""));
    modalNewOrEdit.addListener(FormationNewOrEdit.SaveEvent.class, this::saveFormation);
    modalNewOrEdit.addListener(FormationNewOrEdit.SaveEditedEvent.class, this::saveEditedFormation);
    modalNewOrEdit.addListener(FormationNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    modalConsult = new FormationConsult();
    modalConsult.addListener(FormationConsult.CloseEvent.class, e -> closeConsultModal());

    // injection des éléments visuels dans la vue
    add(addFormationButton, cardsFormations, modalConsult, modalNewOrEdit);
    // on passe les formations à notre VirtualList
    updateVirtualList();
  }

  private void saveFormation(FormationNewOrEdit.SaveEvent event) {
    // récupération de la formation de la modale
    Formation formationtoSave = event.getFormation();
    // sauvegarde de la formation
    formationService.saveFormation(formationtoSave);

    // log d'ajout
    logEnregistrmentService.saveLogAjoutString(formationtoSave.toString());

    // mise à jour de la VitualList et fermeture de la modale
    updateVirtualList();
    closeNewOrEditModal();
    Notification.show("Formation "+formationtoSave.getCodeFormation()+" créée.");
  }

  private void saveEditedFormation(FormationNewOrEdit.SaveEditedEvent event) {
    Formation formationToEdit = event.getFormation();
    Formation formationBeforeEdit = event.getFormationOriginale();

    formationService.saveFormation(formationToEdit);

    logEnregistrmentService.saveLogEditString(formationBeforeEdit.toString(), formationToEdit.toString());

    updateVirtualList();
    closeNewOrEditModal();
    Notification.show("Formation "+ formationToEdit.getCodeFormation() + " modifiée.");
  }

  private void editFormationModal(Formation formation) {
    if (formation == null) {
      closeNewOrEditModal();
    } else {
      modalNewOrEdit.setFormation(formation);
      modalNewOrEdit.open();
      addClassName("editing");
    }
  }

  void addFormation() {
    // appel de la fonction d'edition de la formation, en passant une nouvelle formation
    editFormationModal(new Formation());
  }

  // ouverture de modale de consultation d'une formation
  public void consultFormation(Formation formation) {
    modalConsult.setFormation(formation);
    modalConsult.open();
  }

  private void closeConsultModal() {
    modalConsult.setFormation(null);
    modalConsult.close();
  }

  private void closeNewOrEditModal() {
    modalNewOrEdit.setFormation(null);
    modalNewOrEdit.close();
  }

  public void updateVirtualList() {
    cardsFormations.setItems(formationService.findAllFormations(""));
  }
}
