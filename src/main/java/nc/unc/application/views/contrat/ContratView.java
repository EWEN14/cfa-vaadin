package nc.unc.application.views.contrat;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.Sexe;
import nc.unc.application.data.service.*;
import nc.unc.application.views.ConfirmDelete;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.tuteur.TuteurNewOrEdit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

import static nc.unc.application.utils.Utils.frenchDateFormater;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "contrats", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Contrats | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class ContratView extends VerticalLayout {

  Grid<Contrat> grid = new Grid<>(Contrat.class, false);

  TextField filterText = new TextField();
  Button addContratButton;

  TuteurNewOrEdit tuteurModal;

  Contrat contratMaybeToDelete;

  ContratConsult modalConsult;
  ContratNewOrEdit modalNewOrEdit;
  ConfirmDelete confirmDelete;

  ContratService contratService;
  EtudiantService etudiantService;
  FormationService formationService;
  EntrepriseService entrepriseService;
  TuteurService tuteurService;
  LogEnregistrmentService logEnregistrmentService;

  public ContratView(ContratService contratService, EtudiantService etudiantService, FormationService formationService,
                     EntrepriseService entrepriseService, TuteurService tuteurService, LogEnregistrmentService logEnregistrmentService) {

    this.contratService = contratService;
    this.etudiantService = etudiantService;
    this.formationService = formationService;
    this.entrepriseService = entrepriseService;
    this.tuteurService = tuteurService;
    this.logEnregistrmentService = logEnregistrmentService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    // ajout de la modale de consultation du contrat dans la vue
    modalConsult = new ContratConsult(contratService, this);
    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet etudiant (dans le cas du delete dans la modalConsult ou du save dans modalNewOrdEdit).
    modalConsult.addListener(ContratConsult.DeleteEventConsult.class, this::transfertContractFromEventToDelete);
    modalConsult.addListener(ContratConsult.CloseEventConsult.class, e -> closeConsultModal());

    // ajout de la modale d'édition ou de création d'un contrat dans la vue
    modalNewOrEdit = new ContratNewOrEdit(this, entrepriseService.findAllEntreprises(""), formationService.findAllFormations(""),
            etudiantService.findAllEtudiants(""), tuteurService.findAllTuteurs(""), contratService);
    modalNewOrEdit.addListener(ContratNewOrEdit.SaveEvent.class, this::saveContrat);
    modalNewOrEdit.addListener(ContratNewOrEdit.SaveEditedEvent.class, this::saveEditedContrat);
    modalNewOrEdit.addListener(ContratNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    confirmDelete = new ConfirmDelete("ce contrat");
    confirmDelete.addListener(ConfirmDelete.DeleteEventGrid.class, this::deleteFromConfirmDelete);

    tuteurModal = new TuteurNewOrEdit(entrepriseService.findAllEntreprises(""), formationService.findAllFormations(""),
            formationService, tuteurService, logEnregistrmentService);
    tuteurModal.addListener(TuteurNewOrEdit.SaveEvent.class, this::saveTuteur);
    tuteurModal.addListener(TuteurNewOrEdit.SaveEditedEvent.class, this::saveEditedTuteur);
    tuteurModal.addListener(TuteurNewOrEdit.CloseEvent.class, e -> closeNewOrEditModalTuteur());

    // ajout de la toolbar (recherche + nouveau contrat) et la grid
    // et des modales de consultation et de création/modification TODO
    add(getToolbar(), grid, modalConsult, modalNewOrEdit);
    // initialisation des données de la grille à l'ouverture de la vue
    updateList();
  }

  private void configureGrid() {
    grid.addClassNames("contrat-grid");
    grid.setSizeFull();

    // ajout des colonnes
    grid.addColumn(contrat -> contrat.getEtudiant() != null ? contrat.getEtudiant().getPrenomEtudiant() + " " + contrat.getEtudiant().getNomEtudiant() : "").setHeader("Étudiant").setSortable(true);
    grid.addColumn(contrat -> contrat.getTuteur() != null ? contrat.getTuteur().getPrenomTuteur() + " " + contrat.getTuteur().getNomTuteur() : "").setHeader("Tuteur").setSortable(true);
    grid.addColumn(contrat -> contrat.getEntreprise() != null ? contrat.getEntreprise().getEnseigne() : "").setHeader("Entreprise").setSortable(true);
    grid.addColumn(contrat -> contrat.getFormation() != null ? contrat.getFormation().getCodeFormation() : "").setHeader("Formation").setSortable(true);
    grid.addColumn(Contrat::getCodeContrat).setHeader("Contrat/Avenant").setSortable(true);
    grid.addColumn(Contrat::getNumeroAvenant).setHeader("Numéro Avenant").setSortable(true);
    grid.addColumn(contrat -> contrat.getCreatedAt() != null ? frenchDateFormater(contrat.getCreatedAt().toLocalDate()) : "").setHeader("Date Création").setSortable(true);

    // ajout du bouton de consultation d'un contrat
    grid.addComponentColumn(contrat -> new Button(new Icon(VaadinIcon.EYE), click ->
            consultContrat(contrat))).setHeader("Consulter");
    // ajout du bouton d'édition d'un contrat
    grid.addComponentColumn(contrat -> new Button(new Icon(VaadinIcon.PENCIL), click ->
            editContratModal(contrat))).setHeader("Éditer");
    // ajout du bouton de suppresion d'un contract
    grid.addComponentColumn(contrat -> new Button(new Icon(VaadinIcon.TRASH), click -> {
      prepareToDelete(contrat);
    })).setHeader("Supprimer");

    // on définit que chaque colonne à une largeur autodéterminée
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setWidth("450px");
    filterText.setHelperText("Recherche par prénom/nom étudiant ou tuteur, par enseigne entreprise, formation ou code contrat");
    filterText.setPrefixComponent(VaadinIcon.SEARCH.create()); // affiche une petite loupe au début du champ
    filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
    // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc qu'après que l'utilisateur
    // ait arrêté de taper dans le champ depuis un petit moment.
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    // appel de la fonction qui va mettre à jour la liste des contrats en tenant compte de ce qu'on a tapé en recherche
    filterText.addValueChangeListener(e -> updateList());

    addContratButton = new Button("Nouveau contrat");
    addContratButton.addClickListener(click -> addContrat());

    // on met le champ de recherche et le bouton d'ajout dans un HorizontalLayout, pour qu'ils soient côte à côte
    HorizontalLayout toolbar = new HorizontalLayout(filterText, addContratButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  // sauvegarde du nouveau contrat en utilisant ContratService
  private void saveContrat(ContratNewOrEdit.SaveEvent event) {
    // utilisation du getContrat de la classe mère ContratFormEvent pour récupérer le contrat
    Contrat contrat = event.getContrat();
    // sauvegarde du contrat
    contratService.saveContrat(contrat);

    // ajout du log d'ajout
    logEnregistrmentService.saveLogAjoutString(contrat.toString());

    // mise à jour de la grid, fermeture du formulaire et notification
    updateList();
    closeNewOrEditModal();
    Notification.show("Contrat créé.");
  }

  // sauvegarde du contrat à modifier en utilisant ContratService
  private void saveEditedContrat(ContratNewOrEdit.SaveEditedEvent event) {
    // utilisation du getContrat de la classe mère ContratFormEvent pour récupérer le contrat
    Contrat contrat = event.getContrat();
    // récupération du contrat original avant modification
    Contrat contratOriginal = event.getContratOriginal();
    // sauvegarde du contrat
    contratService.saveContrat(contrat);

    // ajout du log d'ajout
    logEnregistrmentService.saveLogEditString(contratOriginal.toString(), contrat.toString());

    // mise à jour de la grid, fermeture du formulaire et notification
    updateList();
    closeNewOrEditModal();
    Notification.show("Contrat modifié.");
  }

  private void transfertContractFromEventToDelete(ContratConsult.DeleteEventConsult event) {
    Contrat contrat = event.getContrat();
    deleteContrat(contrat);
  }

  // suppression du contrat
  private void deleteContrat(Contrat contrat) {
    if (contrat != null) {
      contratService.deleteContrat(contrat);

      // ajout du log de suppression
      logEnregistrmentService.saveLogDeleteString(contrat.toString());

      updateList();
      closeConsultModal();
      Notification.show("Contrat supprimé");
    }
  }

  private void deleteFromConfirmDelete(ConfirmDelete.DeleteEventGrid event) {
    Boolean supprimer = event.getSuppression();
    if (supprimer) {
      deleteContrat(contratMaybeToDelete);
    }
    contratMaybeToDelete = null;
    closeConfirmDelete();
  }

  public void prepareToDelete(Contrat contrat) {
    contratMaybeToDelete = contrat;
    openConfirmDelete();
  }

  // si contrat null, on ferme le formulaire, sinon on l'affiche (new or edit)
  public void editContratModal(Contrat contrat) {
    if (contrat == null) {
      closeNewOrEditModal();
    } else {
      modalNewOrEdit.setContrat(contrat);
      modalNewOrEdit.open();
      addClassName("editing");
    }
  }

  // ajout d'un contrat
  void addContrat() {
    // on retire le focus s'il y avait une ligne sélectionnée
    grid.asSingleSelect().clear();
    // appel de la fonction d'edition du contrat, en passant un nouveau contrat
    editContratModal(new Contrat());
  }

  // ouverture de modale de consultation d'un contrat
  public void consultContrat(Contrat contrat) {
    modalConsult.setContrat(contrat);
    modalConsult.open();
  }

  private void closeConsultModal() {
    modalConsult.setContrat(null);
    modalConsult.close();
    grid.asSingleSelect().clear();
  }

  private void closeNewOrEditModal() {
    modalNewOrEdit.setContrat(null);
    modalNewOrEdit.close();
    grid.asSingleSelect().clear();
  }

  private void openConfirmDelete() {
    confirmDelete.open();
  }

  private void closeConfirmDelete() {
    confirmDelete.close();
    grid.asSingleSelect().clear();
  }

  // fonction qui récupère la liste des contrats pour les afficher dans la grille (avec les valeurs de recherche)
  private void updateList() {
    grid.setItems(contratService.findAllContrats(filterText.getValue()));
  }

  // sauvegarde du tuteur en utilisant TuteurService (nouveau)
  private void saveTuteur(TuteurNewOrEdit.SaveEvent event) {
    // utilisation du getTuteur de la classe mère TuteurFormEvent pour récupérer le tuteur
    Tuteur tuteur = event.getTuteur();
    // mise en majuscule du nom et définition du sexe avant sauvegarde
    setSexeTuteur(tuteur);
    // sauvegarde de l'étudiant
    tuteurService.saveTuteur(tuteur);

    // ajout du log d'ajout
    logEnregistrmentService.saveLogAjoutString(tuteur.toString());

    //fermeture du formulaire et notification
    closeNewOrEditModalTuteur();

    //Recharger la liste des tuteurs dans la modale d'edit d'un contrat pour récupérer le tuteur crée dans la vue contrat
    modalNewOrEdit.modifyTuteurs(tuteurService.findAllTuteurs(""));

    Notification.show(tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() + " créé(e)");
  }

  // fonction qui met le nom du tuteur en majuscule et défini son sexe en fonction de sa civilté
  private void setSexeTuteur(Tuteur tuteur) {
    tuteur.setNomTuteur(tuteur.getNomTuteur().toUpperCase());
    // définition du sexe que si le champ civilité est rempli
    if (tuteur.getCiviliteTuteur() != null) {
      switch (tuteur.getCiviliteTuteur()) {
        case MONSIEUR:
          tuteur.setSexe(Sexe.M);
          break;
        case MADAME:
          tuteur.setSexe(Sexe.F);
          break;
        case NON_BINAIRE:
          tuteur.setSexe(Sexe.NB);
      }
    }
  }

  // sauvegarde du tuteur modifié en utilisant TuteurService
  private void saveEditedTuteur(TuteurNewOrEdit.SaveEditedEvent event) {
    // utilisation du getTuteur de la classe mère TuteurFormEvent pour récupérer le tuteur
    Tuteur tuteur = event.getTuteur();
    // récupération du tuteur avant modification
    Tuteur tuteurOriginal = event.getTuteurOriginal();
    // mise en majuscule du nom et définition du sexe avant sauvegarde
    setSexeTuteur(tuteur);

    // sauvegarde du tuteur
    tuteurService.saveTuteur(tuteur);

    // ajout du log de modification
    logEnregistrmentService.saveLogEditString(tuteurOriginal.toString(), tuteur.toString());

    closeNewOrEditModalTuteur();

    modalNewOrEdit.modifyTuteurs(tuteurService.findAllTuteurs(""));
    Notification.show(tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() + " modifié(e)");
  }

  private void closeNewOrEditModalTuteur() {
    tuteurModal.setTuteur(null);
    tuteurModal.close();
  }

  public void addTuteur() {
    closeNewOrEditModal();
    editTuteurModal(new Tuteur());
  }

  // si tuteur null, on ferme le formulaire, sinon on l'affiche (new or edit)
  public void editTuteurModal(Tuteur tuteur) {
    if (tuteur == null) {
      closeNewOrEditModalTuteur();
    } else {
      tuteurModal.setTuteur(tuteur);
      tuteurModal.open();
      addClassName("editing");
    }
  }
}
