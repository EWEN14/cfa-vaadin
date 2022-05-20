package nc.unc.application.views.entreprise;

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
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.service.*;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "entreprises", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Entreprises | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class EntrepriseView extends VerticalLayout {

  Grid<Entreprise> grid = new Grid<>(Entreprise.class);

  TextField filterText = new TextField();
  Button addEntrepriseButton;

  EntrepriseConsult modalConsult;
  EntrepriseNewOrEdit modalNewOrEdit;

  EntrepriseService entrepriseService;
  ContratService contratService;
  EtudiantService etudiantService;
  TuteurService tuteurService;
  LogEnregistrmentService logEnregistrmentService;

  public EntrepriseView(EntrepriseService entrepriseService, ContratService contratService, EtudiantService etudiantService,
                        TuteurService tuteurService, LogEnregistrmentService logEnregistrmentService) {

    this.entrepriseService = entrepriseService;
    this.contratService = contratService;
    this.etudiantService = etudiantService;
    this.tuteurService = tuteurService;
    this.logEnregistrmentService = logEnregistrmentService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    // ajout de la modale de consultation de l'entreprise dans la vue
    modalConsult = new EntrepriseConsult(etudiantService, tuteurService, contratService);
    modalConsult.addListener(EntrepriseConsult.DeleteEvent.class, this::deleteEntreprise);
    modalConsult.addListener(EntrepriseConsult.CloseEvent.class, e -> closeConsultModal());

    // ajout de la modale d'édition ou de création d'une entreprise dans la vue
    modalNewOrEdit = new EntrepriseNewOrEdit();
    modalNewOrEdit.addListener(EntrepriseNewOrEdit.SaveEvent.class, this::saveEntreprise);
    modalNewOrEdit.addListener(EntrepriseNewOrEdit.SaveEditedEvent.class, this::saveEditedEntreprise);
    modalNewOrEdit.addListener(EntrepriseNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    // ajout de la toolbar (recherche + nouveau contrat) et la grid
    // et des modales de consultation et de création/modification TODO
    add(getToolbar(), grid);
    // initialisation des données de la grille à l'ouverture de la vue
    updateList();
  }

  private void configureGrid() {
    grid.addClassNames("contrat-grid");
    grid.setSizeFull();

    // ajout des colonnes
    grid.setColumns("enseigne", "raisonSociale","telephoneEntreprise", "statutActifEntreprise");
    // ajout du bouton de consultation d'une entreprise
    grid.addComponentColumn(entreprise -> new Button(new Icon(VaadinIcon.EYE), click ->
            consultEntreprise(entreprise))).setHeader("Consulter");
    // bouton édition entreprise
    grid.addComponentColumn(entreprise -> new Button(new Icon(VaadinIcon.PENCIL), click ->
            editEntrepriseModal(entreprise))).setHeader("Éditer");
    // on définit que chaque colonne à une largeur autodéterminée
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar(){
    filterText.setWidth("450px");
    filterText.setHelperText("Recherche par nom d'enseigne  de l'entreprise");
    filterText.setPrefixComponent(VaadinIcon.SEARCH.create()); // affiche une petite loupe au début du champ
    filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
    // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc qu'après que l'utilisateur
    // ait arrêté de taper dans le champ depuis un petit moment.
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    // appel de la fonction qui va mettre à jour la liste des contrats en tenant compte de ce qu'on a tapé en recherche
    filterText.addValueChangeListener(e -> updateList());

    addEntrepriseButton = new Button("Nouvelle Entreprise");
    addEntrepriseButton.addClickListener(click -> addEntreprise());

    // on met le champ de recherche et le bouton d'ajout dans un HorizontalLayout, pour qu'ils soient côte à côte
    HorizontalLayout toolbar = new HorizontalLayout(filterText, addEntrepriseButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  // sauvegarde de la nouvelle entreprise en utilisant EntrepriseService
  private void saveEntreprise(EntrepriseNewOrEdit.SaveEvent event) {
    // récupération de l'entreprise
    Entreprise entreprise = event.getEntreprise();
    // on met les noms de famille en majuscule
    setNameToUppercase(entreprise);

    // sauvegarde du contrat
    entrepriseService.saveEntreprise(entreprise);

    // ajout du log d'ajout
    logEnregistrmentService.saveLogAjoutString(entreprise.toString());

    // mise à jour de la grid, fermeture du formulaire et notification
    updateList();
    closeNewOrEditModal();
    Notification.show("Entreprise créée.");
  }

  // sauvegarde de l'entreprise modifiée en utilisant EntrepriseService
  private void saveEditedEntreprise(EntrepriseNewOrEdit.SaveEditedEvent event) {
    // récupération de l'entreprise
    Entreprise entreprise = event.getEntreprise();
    // récupération de l'entreprise avant modification
    Entreprise entrepriseOriginal = event.getEntrepriseOriginal();
    // on met les noms de famille en majuscule
    setNameToUppercase(entreprise);

    // sauvegarde du contrat
    entrepriseService.saveEntreprise(entreprise);

    // ajout du log d'ajout
    logEnregistrmentService.saveLogEditString(entrepriseOriginal.toString(), entreprise.toString());

    // mise à jour de la grid, fermeture du formulaire et notification
    updateList();
    closeNewOrEditModal();
    Notification.show("Entreprise modifiée.");
  }

  // suppression d'une entreprise
  private void deleteEntreprise(EntrepriseConsult.DeleteEvent event) {
    Entreprise entreprise = event.getEntreprise();
    if (entreprise != null) {
      // suppression de l'entreprise
      entrepriseService.deleteEntreprise(entreprise);

      // ajout log de suppression
      logEnregistrmentService.saveLogDeleteString(entreprise.toString());

      updateList();
      closeConsultModal();
      Notification.show("Entreprise supprimée");
    }
  }

  // si entreprise null, on ferme le formulaire, sinon on l'affiche (new or edit)
  public void editEntrepriseModal(Entreprise entreprise) {
    if (entreprise == null) {
      closeNewOrEditModal();
    } else {
      modalNewOrEdit.setEntreprise(entreprise);
      modalNewOrEdit.open();
      addClassName("editing");
    }
  }

  // ajout d'une entreprise
  void addEntreprise() {
    grid.asSingleSelect().clear();
    editEntrepriseModal(new Entreprise());
  }

  // ouverture de modale de consultation d'une entreprise
  public void consultEntreprise(Entreprise entreprise) {
    modalConsult.setEntreprise(entreprise);
    modalConsult.open();
  }

  private void closeConsultModal() {
    modalConsult.setEntreprise(null);
    modalConsult.close();
    grid.asSingleSelect().clear();
  }

  private void closeNewOrEditModal() {
    modalNewOrEdit.setEntreprise(null);
    modalNewOrEdit.close();
    grid.asSingleSelect().clear();
  }

  private void setNameToUppercase(Entreprise entreprise) {
    entreprise.setNomRepresentantEmployeur(entreprise.getNomRepresentantEmployeur().toUpperCase());
    entreprise.setNomContactCfa(entreprise.getNomContactCfa().toUpperCase());
  }

  // fonction qui récupère la liste des entreprises pour les afficher dans la grille (avec les valeurs de recherche)
  private void updateList() {
    grid.setItems(entrepriseService.findAllEntreprises(filterText.getValue()));
  }
}
