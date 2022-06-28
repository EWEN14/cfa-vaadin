package nc.unc.application.views.evenement;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Evenement;
import nc.unc.application.data.service.EvenementService;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.views.ConfirmDelete;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "evenements", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Evenements | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
@CssImport("./styles/evenement.css")
public class EvenementView extends VerticalLayout {
  Grid<Evenement> grid = new Grid<>(Evenement.class, false);
  TextField filterText = new TextField();
  Button addEvenementButton;
  Evenement evenementToMaybeDelete;
  ConfirmDelete modalConfirmDelete;
  
  EvenementNewOrEdit modalNewOrEdit;
  EvenementConsult modalConsult;
  EvenementService evenementService;
  FormationService formationService;
  LogEnregistrmentService logEnregistrmentService;

  public EvenementView(EvenementService evenementService, LogEnregistrmentService logEnregistrmentService, FormationService formationService){
    this.evenementService = evenementService;
    this.logEnregistrmentService = logEnregistrmentService;
    this.formationService = formationService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    // ajout d'un FlexLayout dans lequel on place la grille
    FlexLayout content = new FlexLayout(grid);
    content.setFlexGrow(2, grid);
    content.addClassNames("content", "gap-m");
    content.setSizeFull();

    // ajout de la modale d'édition ou de création d'un évenement dans la vue
    modalNewOrEdit = new EvenementNewOrEdit(formationService.findAllFormations(null));
    modalNewOrEdit.addListener(EvenementNewOrEdit.SaveEvent.class, this::saveEvenement);
    modalNewOrEdit.addListener(EvenementNewOrEdit.SaveEditedEvent.class, this::saveEditedEvenement);
    modalNewOrEdit.addListener(EvenementNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    // ajout de la modale de consultation
    modalConsult = new EvenementConsult(evenementService, formationService.findAllFormations(null));
    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet evenement (dans le cas du delete dans la modalConsult ou du save dans modalNewOrdEdit ).
    modalConsult.addListener(EvenementConsult.DeleteEvent.class, this::transfertEvenementFromEventToDelete);
    modalConsult.addListener(EvenementConsult.CloseEvent.class, e -> closeConsultModal());

    modalConfirmDelete = new ConfirmDelete("cet évenement");
    modalConfirmDelete.addListener(ConfirmDelete.DeleteEventGrid.class, this::deleteFromConfirmDelete);

    // ajout de la toolbar (recherche + nouveau etudiant) et du content (grid + formulaire)
    // et des modales de consultation et de création/modification
    add(getToolbar(), content);
    // initialisation des données de la grille à l'ouverture de la vue
    updateList();
  }

  private void configureGrid(){
    grid.addClassNames("evenement-grid");
    grid.setSizeFull();
    grid.addColumn(evenement -> evenement.getLibelle()).setHeader("Libelle").setSortable(true);
    grid.addColumn(evenement -> evenement.getDescription()).setHeader("Description").setSortable(true);
    grid.addColumn(Evenement::getDateDebut).setHeader("Date début").setSortable(true);
    grid.addColumn(Evenement::getDateFin).setHeader("Date fin");
    // ajout du bouton de consultation d'un étudiant
    grid.addComponentColumn(evenement -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultEvenement(evenement);
    })).setHeader("Consulter");
    // bouton édition étudiant
    grid.addComponentColumn(evenement -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      editEvenementModal(evenement);
    })).setHeader("Éditer");
    // bouton de suppression
    grid.addComponentColumn(evenement -> new Button(new Icon(VaadinIcon.TRASH), click -> {
      prepareToDelete(evenement);
    })).setHeader("Supprimer");
    // on définit que chaque colonne à une largeur autodéterminée
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setHelperText("Recherche par description...");
    filterText.setPrefixComponent(VaadinIcon.SEARCH.create()); // affiche une petite loupe au début du champ
    filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
    // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc qu'après que l'utilisateur
    // ait arrêté de taper dans le champ depuis un petit moment.
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    // appel de la fonction qui va mettre à jour la liste d'étudiants en tenant compte de ce qu'on a tapé en recherche
    filterText.addValueChangeListener(e -> updateList());

    addEvenementButton = new Button("Nouveau évenement");
    addEvenementButton.addClickListener(click -> addEvenement());

    // on met le champ de recherche et le bouton d'ajout dans un HorizontalLayout, pour qu'ils soient côte à côte
    HorizontalLayout toolbar = new HorizontalLayout(filterText, addEvenementButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  // fonction qui récupère la liste des évenements pour les afficher dans la grille (avec les valeurs de recherche)
  private void updateList() {
    grid.setItems(evenementService.findAllEvenements(filterText.getValue()));
  }

  // ajout d'un évenement
  void addEvenement() {
    // on retire le focus s'il y avait une ligne sélectionnée
    grid.asSingleSelect().clear();
    // appel de la fonction d'edition de l'evenement, en passant un nouveau évenement
    editEvenementModal(new Evenement());
  }

  //Sauvegarde du nouveau évenement
  private void saveEvenement(EvenementNewOrEdit.SaveEvent event){
    // utilisation du getEvenement de la classe mère EvenementFormEvent pour récupérer l'évenement
    Evenement evenement = event.getEvenement();

    //Sauvegarde
    evenementService.saveEvenement(evenement);

    //Ajout du log d'enregistrement
    logEnregistrmentService.saveLogAjoutString(evenement.toString());

    // mise à jour de la grid, fermeture du formulaire et notification
    updateList();
    closeNewOrEditModal();
    Notification.show("Evenement du" + evenement.getDateDebut() + " au "+ evenement.getDateFin() + " créé(e).");

  }

  // sauvegarde de l'évenement modifié
  private void saveEditedEvenement(EvenementNewOrEdit.SaveEditedEvent event) {
    // utilisation du getEvenement de la classe mère EvenementFormEvent pour récupérer l'évenement
    Evenement evenement = event.getEvenement();
    // récupération de l'évenement original avant modification
    Evenement evenementOriginal = event.getEvenementOriginal();

    // sauvegarde de l'évenement
    evenementService.saveEvenement(evenement);

    // ajout du log de modification
    logEnregistrmentService.saveLogEditString(evenementOriginal.toString(), evenement.toString());

    updateList();
    closeNewOrEditModal();
    Notification.show("Evenement du " + evenement.getDateDebut() + " au " + evenement.getDateFin() + " modifié(e)");
  }

  // ouverture de modale de consultation d'un évenement
  public void consultEvenement(Evenement evenement) {
    modalConsult.setEvenement(evenement);
    modalConsult.open();
  }

  private void closeNewOrEditModal() {
    modalNewOrEdit.setEvenement(null);
    modalNewOrEdit.close();
    grid.asSingleSelect().clear();
  }

  private void closeConsultModal() {
    modalConsult.setEvenement(null);
    modalConsult.close();
    grid.asSingleSelect().clear();
  }

  private void deleteFromConfirmDelete(ConfirmDelete.DeleteEventGrid event) {
    Boolean supprimer = event.getSuppression();
    if (supprimer) {
      deleteEvenement(evenementToMaybeDelete);
    }
    evenementToMaybeDelete = null;
    closeConfirmDelete();
  }

  // suppression de l'évenement en utilisant EvenementService
  private void deleteEvenement(Evenement evenementToDelete) {
    if (evenementToDelete != null) {
      evenementService.deleteEvenement(evenementToDelete);

      // ajout du log de suppression
      logEnregistrmentService.saveLogDeleteString(evenementToDelete.toString());

      updateList();
      closeConsultModal();
      Notification.show(evenementToDelete.getLibelle() +"  retiré(e)");
    }
  }

  private void openConfirmDelete() {
    modalConfirmDelete.open();
  }

  private void closeConfirmDelete() {
    modalConfirmDelete.close();
    grid.asSingleSelect().clear();
  }

  private void prepareToDelete(Evenement evenement) {
    evenementToMaybeDelete = evenement;
    openConfirmDelete();
  }

  private void transfertEvenementFromEventToDelete(EvenementConsult.DeleteEvent event) {
    Evenement evenement = event.getEvenement();
    deleteEvenement(evenement);
  }

  // si évenement null, on ferme le formulaire, sinon on l'affiche (new or edit)
  public void editEvenementModal(Evenement evenement) {
    if (evenement == null) {
      closeNewOrEditModal();
    } else {
      modalNewOrEdit.setEvenement(evenement);
      modalNewOrEdit.open();
      addClassName("editing");
    }
  }
}
