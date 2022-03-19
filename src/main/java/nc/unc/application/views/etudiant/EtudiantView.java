package nc.unc.application.views.etudiant;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.enums.TypeCrud;
import nc.unc.application.data.service.EtudiantService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "etudiants", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Étudiants | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class EtudiantView extends VerticalLayout {

  Grid<Etudiant> grid = new Grid<>(Etudiant.class);
  com.vaadin.flow.component.textfield.TextField filterText = new TextField();
  Button addEtudiantButton;
  EtudiantForm form;
  EtudiantService service;
  LogEnregistrmentService logEnregistrmentService;

  public EtudiantView(EtudiantService service, LogEnregistrmentService logEnregistrmentService) {
    this.service = service;
    this.logEnregistrmentService = logEnregistrmentService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    form = new EtudiantForm();
    form.setWidth("30em");
    // On définit que les différents events (EtudiantForm.fooEvent) dans le Etudiant vont déclencher une fonction
    // contenant l'objet etudiant (dans le cas du save ou delete).
    form.addListener(EtudiantForm.SaveEvent.class, this::saveEtudiant);
    form.addListener(EtudiantForm.SaveEditedEvent.class, this::saveEditedEtudiant);
    form.addListener(EtudiantForm.DeleteEvent.class, this::deleteEtudiant);
    form.addListener(EtudiantForm.CloseEvent.class, e -> closeEditor());

    // ajout d'un FlexLayout qui place la grille et le formulaire côte à côte (quand formulaire ouvert)
    FlexLayout content = new FlexLayout(grid, form);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, form);
    // défini le facteur de rétrécissement. Ici comme il est à 0, le formulaire ne se réduira pas.
    content.setFlexShrink(0, form);
    content.addClassNames("content", "gap-m");
    content.setSizeFull();

    // ajout de la toolbar (recherche + nouveau etudiant) et du content (grid + formulaire)
    add(getToolbar(), content);
    // initialisation des données de la grille à l'ouverture de la vue
    updateList();
    // formulaire d'ajout/d'édition d'étudiant fermé à l'ouverture de la vue
    closeEditor();

    grid.asSingleSelect().addValueChangeListener(event ->
            editEtudiant(event.getValue()));
  }

  private void configureGrid() {
    grid.addClassNames("etudiant-grid");
    grid.setSizeFull();
    grid.setColumns("prenom", "nom", "civilite", "dateNaissance");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setPlaceholder("Recherche par nom ou prénom...");
    filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
    // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc que après que l'utilisateur
    // a arrêté de taper dans le champ depuis un petit moment.
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

  // sauvegarde de l'étudiant en utilisant EtudiantService (nouveau)
  private void saveEtudiant(EtudiantForm.SaveEvent event) {
    // utilisation du getEtudiant de la classe mère EtudiantFormEvent pour récupérer l'étudiant
    Etudiant etudiant = event.getEtudiant();
    // mise en majuscule du nom avant sauvegarde
    etudiant.setNom(etudiant.getNom().toUpperCase());
    // sauvegarde de l'étudiant
    service.saveEtudiant(etudiant);

    // ajout du log d'ajout
    logEnregistrmentService.saveLogString(etudiant.toString(), TypeCrud.AJOUT);

    // mise à jour de la grid, fermeture du formulaire et notification
    updateList();
    closeEditor();
    Notification.show(etudiant.getPrenom() + " " + etudiant.getNom() + " créé(e)");
  }

  // sauvegarde de l'étudiant modifié en utilisant EtudiantService
  private void saveEditedEtudiant(EtudiantForm.SaveEditedEvent event) {
    // utilisation du getEtudiant de la classe mère EtudiantFormEvent pour récupérer l'étudiant
    Etudiant etudiant = event.getEtudiant();
    // récupération de l'étudiant avant modification
    Etudiant etudiantOriginal = event.getEtudiantOriginal();
    // mise en majuscule du nom avant sauvegarde
    etudiant.setNom(etudiant.getNom().toUpperCase());

    // sauvegarde de l'étudiant
    service.saveEtudiant(etudiant);

    // ajout du log de modification
    logEnregistrmentService.saveLogString("Anciennes valeurs : "
            + etudiantOriginal.toString() + " remplacées par : "
            + etudiant.toString(), TypeCrud.MODIFICATION);

    updateList();
    closeEditor();
    Notification.show(etudiant.getPrenom() + " " + etudiant.getNom() + " modifié(e)");
  }

  // suppression de l'étudiant en utilisant EtudiantService
  private void deleteEtudiant(EtudiantForm.DeleteEvent event) {
    Etudiant etudiant = event.getEtudiant();
    service.deleteEtudiant(etudiant);

    // ajout du log de suppression
    logEnregistrmentService.saveLogString(etudiant.toString(), TypeCrud.SUPPRESSION);

    updateList();
    closeEditor();
    Notification.show(etudiant.getPrenom() + " " + etudiant.getNom() + " retiré(e)");
  }

  // si étudiant null, on ferme le formulaire, sinon on l'affiche (new or edit)
  public void editEtudiant(Etudiant etudiant) {
    if (etudiant == null) {
      closeEditor();
    } else {
      form.setEtudiant(etudiant);
      form.setVisible(true);
      addClassName("editing");
    }
  }

  // ajout d'un étudiant
  void addEtudiant() {
    // on retire le focus s'il y avait une ligne sélectionnée
    grid.asSingleSelect().clear();
    // appel de la fonction juste au-dessus
    editEtudiant(new Etudiant());
  }

  // fermeture du formulaire
  private void closeEditor() {
    form.setEtudiant(null);
    form.setVisible(false);
    removeClassName("editing");
    // retrait du focus sur la ligne
    grid.asSingleSelect().clear();
  }

  // fonction qui récupère la liste des étudiants pour les afficher dans la grille (avec les valeurs de recherche)
  private void updateList() {
    grid.setItems(service.findAllEtudiants(filterText.getValue()));
  }
}
