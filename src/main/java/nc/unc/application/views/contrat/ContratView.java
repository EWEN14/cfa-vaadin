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
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.service.*;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.etudiant.EtudiantConsult;
import nc.unc.application.views.etudiant.EtudiantNewOrEdit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "contrats", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Contrats | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class ContratView extends VerticalLayout {

  Grid<Contrat> grid = new Grid<>(Contrat.class, false);

  TextField filterText = new TextField();
  Button addContratButton;

  ContratConsult modalConsult;
  ContratNewOrEdit modalNewOrEdit;

  ContratService contratService;
  EtudiantService etudiantService;
  FormationService formationService;
  EntrepriseService entrepriseService;
  TuteurService tuteurService;
  LogEnregistrmentService logEnregistrmentService;

  public ContratView(ContratService contratService, EtudiantService etudiantService, FormationService formationService,
                     EntrepriseService entrepriseService, TuteurService tuteurService, LogEnregistrmentService logEnregistrmentService){

    this.contratService = contratService;
    this.etudiantService = etudiantService;
    this.formationService = formationService;
    this.entrepriseService = entrepriseService;
    this.tuteurService = tuteurService;
    this.logEnregistrmentService = logEnregistrmentService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    // ajout de la modale de consultation de l'étudiant dans la vue
    modalConsult = new ContratConsult();
    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet etudiant (dans le cas du delete dans la modalConsult ou du save dans modalNewOrdEdit).
    modalConsult.addListener(ContratConsult.DeleteEventConsult.class, this::deleteContrat);
    modalConsult.addListener(ContratConsult.CloseEventConsult.class, e -> closeConsultModal());


    // ajout de la modale d'édition ou de création d'un étudiant dans la vue, en lui passant la liste des entreprises et des tuteurs
    modalNewOrEdit = new ContratNewOrEdit( entrepriseService.findAllEntreprises(), formationService.findAllFormations(""),
            etudiantService.findAllEtudiants(""), tuteurService.findAllTuteurs(""));
    modalNewOrEdit.addListener(ContratNewOrEdit.SaveEvent.class, this::saveContrat);
    modalNewOrEdit.addListener(ContratNewOrEdit.SaveEditedEvent.class, this::saveEditedContrat);
    modalNewOrEdit.addListener(ContratNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());


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
    grid.addColumn(contrat -> contrat.getEtudiant().getPrenomEtudiant() + " " + contrat.getEtudiant().getNomEtudiant()).setHeader("Étudiant");
    grid.addColumn(contrat -> contrat.getTuteur().getPrenomTuteur() + " " + contrat.getTuteur().getNomTuteur()).setHeader("Tuteur");
    grid.addColumn(contrat -> contrat.getEntreprise().getEnseigne()).setHeader("Entreprise");
    grid.addColumn(contrat -> contrat.getFormation().getCodeFormation()).setHeader("Formation");
    grid.addColumn(Contrat::getCodeContrat).setHeader("Code Contrat");

    // ajout du bouton de consultation d'un contrat
    grid.addComponentColumn(contrat -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultContrat(contrat);
    })).setHeader("Consulter");
    // ajout du bouton d'édition d'un contrat
    grid.addComponentColumn(contrat -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      editContratModal(contrat);
    })).setHeader("Éditer");

    // on définit que chaque colonne à une largeur autodéterminée
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar(){
    filterText.setHelperText("Recherche par numéro...");
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

  // suppression du contrat
  private void deleteContrat(ContratConsult.DeleteEventConsult event) {
    Contrat contrat = event.getContrat();
    if (contrat != null) {
      contratService.deleteContrat(contrat);

      // ajout du log de suppression
      logEnregistrmentService.saveLogDeleteString(contrat.toString());

      updateList();
      closeConsultModal();
      Notification.show("Contrat supprimé");
    }
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

  // ouverture de modale de consultation d'un étudiant
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

  // fonction qui récupère la liste des contrat pour les afficher dans la grille (avec les valeurs de recherche)
  private void updateList() {
    grid.setItems(contratService.findAllContrats(filterText.getValue()));
  }
}
