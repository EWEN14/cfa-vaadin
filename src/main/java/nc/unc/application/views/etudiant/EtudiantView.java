package nc.unc.application.views.etudiant;

import com.vaadin.flow.component.button.Button;
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
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.enums.Sexe;
import nc.unc.application.data.service.*;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "etudiants", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Étudiants | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class EtudiantView extends VerticalLayout {

  Grid<Etudiant> grid = new Grid<>(Etudiant.class);
  TextField filterText = new TextField();
  Button addEtudiantButton;
  EtudiantConsult modalConsult;
  EtudiantNewOrEdit modalNewOrEdit;
  EtudiantService etudiantService;
  TuteurService tuteurService;
  FormationService formationService;
  ReferentPedagogiqueService referentPedagogiqueService;
  LogEnregistrmentService logEnregistrmentService;

  public EtudiantView(EtudiantService etudiantService, TuteurService tuteurService, FormationService formationService,
                      ReferentPedagogiqueService referentPedagogiqueService, LogEnregistrmentService logEnregistrmentService) {
    this.etudiantService = etudiantService;
    this.tuteurService = tuteurService;
    this.formationService = formationService;
    this.referentPedagogiqueService = referentPedagogiqueService;
    this.logEnregistrmentService = logEnregistrmentService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    // ajout de la modale de consultation de l'étudiant dans la vue
    modalConsult = new EtudiantConsult();
    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet etudiant (dans le cas du delete dans la modalConsult ou du save dans modalNewOrdEdit).
    modalConsult.addListener(EtudiantConsult.DeleteEvent.class, this::deleteEtudiant);
    modalConsult.addListener(EtudiantConsult.CloseEvent.class, e -> closeConsultModal());

    // ajout de la modale d'édition ou de création d'un étudiant dans la vue, en lui passant la liste des entreprises et des tuteurs
    modalNewOrEdit = new EtudiantNewOrEdit(etudiantService.findAllEntreprises(), tuteurService.findAllTuteurs(""),
            formationService.findAllFormations(""), referentPedagogiqueService.findAllReferentPedagogique(""));
    modalNewOrEdit.addListener(EtudiantNewOrEdit.SaveEvent.class, this::saveEtudiant);
    modalNewOrEdit.addListener(EtudiantNewOrEdit.SaveEditedEvent.class, this::saveEditedEtudiant);
    modalNewOrEdit.addListener(EtudiantNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    // ajout d'un FlexLayout qui place la grille et le formulaire côte à côte (quand formulaire ouvert)
    FlexLayout content = new FlexLayout(grid);
    content.setFlexGrow(2, grid);
    content.addClassNames("content", "gap-m");
    content.setSizeFull();

    // ajout de la toolbar (recherche + nouveau etudiant) et du content (grid + formulaire)
    // et des modales de consultation et de création/modification
    add(getToolbar(), content, modalConsult, modalNewOrEdit);
    // initialisation des données de la grille à l'ouverture de la vue
    updateList();
    // modales de consultation et d'édition/création de l'étudiant fermée à l'ouverture de la vue
    closeConsultModal();

    // je le commente pour l'instant, car on doit cliquer deux fois sur fermer pour que ça fonctionne si on ouvre
    // la modale de consultation depuis le clic sur une ligne... mais clic en dehors de la modale ou ECHAP fonctionne.
    /*grid.asSingleSelect().addValueChangeListener(event ->
            consultEtudiant(event.getValue()));*/
  }

  private void configureGrid() {
    grid.addClassNames("etudiant-grid");
    grid.setSizeFull();
    // ajout des colonnes
    grid.setColumns("prenomEtudiant", "nomEtudiant", "civiliteEtudiant", "dateNaissanceEtudiant");
    // ajout du bouton de consultation d'un étudiant
    grid.addComponentColumn(etudiant -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultEtudiant(etudiant);
    }));
    grid.addComponentColumn(etudiant -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      editEtudiantModal(etudiant);
    }));
    // on définit que chaque colonne à une largeur autodéterminée
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setHelperText("Recherche par nom ou prénom...");
    filterText.setPrefixComponent(VaadinIcon.SEARCH.create()); // affiche une petite loupe au début du champ
    filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
    // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc qu'après que l'utilisateur
    // ait arrêté de taper dans le champ depuis un petit moment.
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    // appel de la fonction qui va mettre à jour la liste d'étudiants en tenant compte de ce qu'on a tapé en recherche
    filterText.addValueChangeListener(e -> updateList());

    addEtudiantButton = new Button("Nouvel Étudiant");
    addEtudiantButton.addClickListener(click -> addEtudiant());

    // on met le champ de recherche et le bouton d'ajout dans un HorizontalLayout, pour qu'ils soient côte à côte
    HorizontalLayout toolbar = new HorizontalLayout(filterText, addEtudiantButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  // sauvegarde du nouveau étudiant en utilisant EtudiantService
  private void saveEtudiant(EtudiantNewOrEdit.SaveEvent event) {
    // utilisation du getEtudiant de la classe mère EtudiantFormEvent pour récupérer l'étudiant
    Etudiant etudiant = event.getEtudiant();
    // mise en majuscule du nom, définition sexe et âge avant sauvegarde
    setNameSexeAgeEtudiant(etudiant);
    // sauvegarde de l'étudiant
    etudiantService.saveEtudiant(etudiant);

    // ajout du log d'ajout
    logEnregistrmentService.saveLogAjoutString(etudiant.toString());

    // mise à jour de la grid, fermeture du formulaire et notification
    updateList();
    closeNewOrEditModal();
    Notification.show(etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant() + " créé(e)");
  }

  // sauvegarde de l'étudiant modifié en utilisant EtudiantService
  private void saveEditedEtudiant(EtudiantNewOrEdit.SaveEditedEvent event) {
    // utilisation du getEtudiant de la classe mère EtudiantFormEvent pour récupérer l'étudiant
    Etudiant etudiant = event.getEtudiant();
    // récupération de l'étudiant avant modification
    Etudiant etudiantOriginal = event.getEtudiantOriginal();
    // mise en majuscule du nom, définition sexe et âge avant sauvegarde
    setNameSexeAgeEtudiant(etudiant);

    // sauvegarde de l'étudiant
    etudiantService.saveEtudiant(etudiant);

    // ajout du log de modification
    logEnregistrmentService.saveLogEditString(etudiantOriginal.toString(), etudiant.toString());

    updateList();
    closeNewOrEditModal();
    Notification.show(etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant() + " modifié(e)");
  }

  // suppression de l'étudiant en utilisant EtudiantService
  private void deleteEtudiant(EtudiantConsult.DeleteEvent event) {
    Etudiant etudiant = event.getEtudiant();
    etudiantService.deleteEtudiant(etudiant);

    // ajout du log de suppression
    logEnregistrmentService.saveLogDeleteString(etudiant.toString());

    updateList();
    closeConsultModal();
    Notification.show(etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant() + " retiré(e)");
  }

  // si étudiant null, on ferme le formulaire, sinon on l'affiche (new or edit)
  public void editEtudiantModal(Etudiant etudiant) {
    if (etudiant == null) {
      closeNewOrEditModal();
    } else {
      modalNewOrEdit.setEtudiant(etudiant);
      modalNewOrEdit.open();
      addClassName("editing");
    }
  }

  public void consultEtudiant(Etudiant etudiant) {
    modalConsult.setEtudiant(etudiant);
    modalConsult.open();
  }

  // ajout d'un étudiant
  void addEtudiant() {
    // on retire le focus s'il y avait une ligne sélectionnée
    grid.asSingleSelect().clear();
    // appel de la fonction juste au-dessus
    editEtudiantModal(new Etudiant());
  }

  private void closeConsultModal() {
    modalConsult.setEtudiant(null);
    modalConsult.close();
    grid.asSingleSelect().clear();
  }

  private void closeNewOrEditModal() {
    modalNewOrEdit.setEtudiant(null);
    modalNewOrEdit.close();
    grid.asSingleSelect().clear();
  }

  // fonction qui récupère la liste des étudiants pour les afficher dans la grille (avec les valeurs de recherche)
  private void updateList() {
    grid.setItems(etudiantService.findAllEtudiants(filterText.getValue()));
  }

  // fonction qui met le nom de l'étudiant en majuscule, défini son sexe en fonction de sa civilté
  // et son âge selon sa date de naissance
  private void setNameSexeAgeEtudiant(Etudiant etudiant) {
    etudiant.setNomEtudiant(etudiant.getNomEtudiant().toUpperCase());
    switch (etudiant.getCiviliteEtudiant()) {
      case MONSIEUR:
        etudiant.setSexeEtudiant(Sexe.M);
        break;
      case MADAME:
        etudiant.setSexeEtudiant(Sexe.F);
        break;
      case NON_BINAIRE:
        etudiant.setSexeEtudiant(Sexe.NB);
    }
    etudiant.setAge(ChronoUnit.YEARS.between(etudiant.getDateNaissanceEtudiant(), LocalDate.now()));
  }
}
