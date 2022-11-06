package nc.unc.application.views.formation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
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
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.entity.ReferentPedagogique;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.data.service.ReferentPedagogiqueService;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.referentPedagogique.ReferentPedagogiqueNewOrEdit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import java.util.Objects;

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
  ReferentPedagogiqueNewOrEdit referentNewOrEdit;

  VirtualList<Formation> cardsFormations = new VirtualList<>();
  Button addFormationButton = new Button("Nouvelle Formation");

  Formation formationEnEdition = new Formation();

  // Composant qui définit l'aspect de chaque "carte" qui présente une formation
  private final ComponentRenderer<com.vaadin.flow.component.Component, Formation> formationCardRenderer = new ComponentRenderer<>(formation -> {
    HorizontalLayout cardLayout = new HorizontalLayout();
    cardLayout.setMargin(true);

    H3 libelleFormation = new H3(formation.getLibelleFormation());
    Span nbHeure = new Span();
    Span nbSemaineFormation = new Span();
    Span nbSemaineEntreprise = new Span();
    if (formation.getHeuresFormation() != null) {
      nbHeure.add("Nombre d'heures dans l'année : " + formation.getHeuresFormation().toString() + " heures");
    }
    if(formation.getSemainesFormation() != null){
      nbSemaineFormation.add("Nombre de semaine de formation : " + formation.getSemainesFormation() + " semaines");
    }
    if(formation.getSemainesEntreprise() != null){
      nbSemaineEntreprise.add("Nombre de semaine d'entreprise : " + formation.getSemainesEntreprise() + " semaines");
    }

    String prenomNomDirecteurFormation = formation.getReferentPedagogique() != null ? formation.getReferentPedagogique().getPrenomReferentPedago()
            + " " + formation.getReferentPedagogique().getNomReferentPedago() : "";
    Span directeurFormation = new Span("Directeur de formation : " + prenomNomDirecteurFormation);

    // bouton de consultation de la formation
    Button consultFormationButton = new Button(VaadinIcon.EYE.create());
    consultFormationButton.addClickListener(click -> consultFormation(formation));

    // bouton d'édition de la formation
    Button editFormationButton = new Button(VaadinIcon.PENCIL.create());
    editFormationButton.addClickListener(click -> editFormationModal(formation));

    // on place les boutons côte à côte dans un HorizontalLayout
    HorizontalLayout buttonsContainer = new HorizontalLayout(consultFormationButton, editFormationButton);

    Div lienPageFormation = new Div(new RouterLink("Voir les étudiants", FormationEtudiantView.class, new RouteParameters("idFormation", String.valueOf(formation.getId()))));
    Div lienPageEvenements = new Div(new RouterLink("Voir les évènements", FormationEvenementView.class, new RouteParameters("idFormation", String.valueOf(formation.getId()))));

    lienPageEvenements.addClassNames("link-formation");
    lienPageFormation.addClassNames("link-formation");

    VerticalLayout insideCardLayout = new VerticalLayout();
    insideCardLayout.add(libelleFormation, directeurFormation, nbSemaineFormation, nbSemaineEntreprise, nbHeure, buttonsContainer, lienPageFormation, lienPageEvenements);

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

    modalNewOrEdit = new FormationNewOrEdit(referentPedagogiqueService.findAllReferentPedagogique(""), this);
    modalNewOrEdit.addListener(FormationNewOrEdit.SaveEvent.class, this::saveFormation);
    modalNewOrEdit.addListener(FormationNewOrEdit.SaveEditedEvent.class, this::saveEditedFormation);
    modalNewOrEdit.addListener(FormationNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    referentNewOrEdit = new ReferentPedagogiqueNewOrEdit();
    referentNewOrEdit.addListener(ReferentPedagogiqueNewOrEdit.CloseEvent.class, e -> closeNewOrEditModalReferent());
    referentNewOrEdit.addListener(ReferentPedagogiqueNewOrEdit.SaveEvent.class, this::saveReferentPedagogique);

    modalConsult = new FormationConsult();
    modalConsult.addListener(FormationConsult.CloseEvent.class, e -> closeConsultModal());

    // injection des éléments visuels dans la vue
    add(addFormationButton, cardsFormations, modalConsult, modalNewOrEdit);
    // on passe les formations à notre VirtualList
    updateVirtualList();
  }

  /**
   * Sauvegarde d'une nouvelle formation
   *
   * @param event
   */
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
    Notification.show("Formation " + formationtoSave.getCodeFormation() + " créée.");
  }

  /**
   * Modification d'une formation
   *
   * @param event
   */
  private void saveEditedFormation(FormationNewOrEdit.SaveEditedEvent event) {
    Formation formationToEdit = event.getFormation();
    Formation formationBeforeEdit = event.getFormationOriginale();

    formationService.saveFormation(formationToEdit);

    logEnregistrmentService.saveLogEditString(formationBeforeEdit.toString(), formationToEdit.toString());

    updateVirtualList();
    closeNewOrEditModal();
    Notification.show("Formation " + formationToEdit.getCodeFormation() + " modifiée.");
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

  private void editReferentModal(ReferentPedagogique referentPedagogique) {
    if (referentPedagogique == null) {
      closeNewOrEditModalReferent();
    } else {
      referentNewOrEdit.setReferentPedagogique(referentPedagogique);
      referentNewOrEdit.open();
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

  // fermeture modale de consultation
  private void closeConsultModal() {
    modalConsult.setFormation(null);
    modalConsult.close();
  }

  // fermeture modale création/édition
  private void closeNewOrEditModal() {
    modalNewOrEdit.setFormation(null);
    modalNewOrEdit.close();
  }

  // mise à jour des cartes présentant les formations
  public void updateVirtualList() {
    cardsFormations.setItems(formationService.findAllFormations(""));
  }

  public void addReferent(Formation formation){
    this.formationEnEdition = formation;
    closeNewOrEditModal();
    editReferentModal(new ReferentPedagogique());
  }

  // Sauvegarde d'un référent pédagogique créé "à la volée"
  private void saveReferentPedagogique(ReferentPedagogiqueNewOrEdit.SaveEvent event) {
    ReferentPedagogique referentPedagogique = event.getReferentPedagogique();

    referentPedagogique.setNomReferentPedago(referentPedagogique.getNomReferentPedago().toUpperCase());

    referentPedagogiqueService.saveReferentPedagogique(referentPedagogique);

    logEnregistrmentService.saveLogAjoutString(referentPedagogique.toString());

    // On met à jour la liste des référents pédagogiques dans la combobox dédiée pour la création/édition d'une formation
    modalNewOrEdit.modifyReferents(referentPedagogiqueService.findAllReferentPedagogique(null));

    closeNewOrEditModalReferent();

    Notification.show(referentPedagogique.getPrenomReferentPedago() + " " + referentPedagogique.getNomReferentPedago() + " créé(e).");
  }


  private void closeNewOrEditModalReferent() {
    referentNewOrEdit.setReferentPedagogique(null);
    referentNewOrEdit.close();

    // Si la formation à partir de laquelle on a créé le référent pédagoqique est déjà existante (édition),
    // on la passe en paramètre, sinon on repart d'une nouvelle formation.
    editFormationModal(Objects.requireNonNullElseGet(this.formationEnEdition, Formation::new));
  }

}

