package nc.unc.application.views.entretienCollectif;


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
import nc.unc.application.data.entity.EntretienCollectif;
import nc.unc.application.data.entity.EntretienIndividuel;
import nc.unc.application.data.service.*;
import nc.unc.application.views.ConfirmDelete;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.entretienIndividuelle.EntretienIndividuelConsult;
import nc.unc.application.views.entretienIndividuelle.EntretienIndividuelNewOrEdit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "entretiensCollectifs", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Entretiens Collectifs | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class EntretienCollectifView extends VerticalLayout {

    // Les attributs de notre vue
    Grid<EntretienCollectif> grid = new Grid<>(EntretienCollectif.class, false);
    TextField filterText = new TextField();
    Button addEntretienButton;

    EntretienCollectif entretienMaybeToDelete;

    EntretienCollectifNewOrEdit entretienModal;
    EntretienCollectifConsult entretienModalConsult;
    ConfirmDelete confirmDelete;

    private EntretienCollectifService entretienCollectifService;
    private LogEnregistrmentService logEnregistrmentService;
    private ReferentCfaService referentCfaService;
    private FormationService formationService;

    public EntretienCollectifView(LogEnregistrmentService logEnregistrmentService, EntretienCollectifService entretienCollectifService, ReferentCfaService referentCfaService, FormationService formationService){

        this.entretienCollectifService = entretienCollectifService;
        this.logEnregistrmentService = logEnregistrmentService;
        this.formationService = formationService;
        this.referentCfaService = referentCfaService;

        addClassName("list-view");
        setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
        configureGrid(); // configuration de la grille (colonnes, données...)

        entretienModalConsult = new EntretienCollectifConsult(formationService, referentCfaService, entretienCollectifService);
        // On définit que les différents events (EntretienIndividuelForm.fooEvent) vont déclencher une fonction
        // contenant l'objet entretien (dans le cas du save ou delete).
        entretienModalConsult.addListener(EntretienCollectifConsult.DeleteEvent.class, this::transfertEntretienFromEventToDelete);
        entretienModalConsult.addListener(EntretienCollectifConsult.CloseEvent.class, e -> closeConsultModal());

        entretienModal = new EntretienCollectifNewOrEdit(referentCfaService.findAllReferentCfa(null), formationService.findAllFormations(null));
        entretienModal.addListener(EntretienCollectifNewOrEdit.SaveEvent.class, this::saveEntretien);
        entretienModal.addListener(EntretienCollectifNewOrEdit.SaveEditedEvent.class, this::saveEditedEntretien);
        entretienModal.addListener(EntretienCollectifNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

        confirmDelete = new ConfirmDelete("cet entretien collectif");
        confirmDelete.addListener(ConfirmDelete.DeleteEventGrid.class, this::deleFromConfirmDelete);

        // ajout d'un FlexLayout qui place la grille
        FlexLayout content = new FlexLayout(grid);
        content.setFlexGrow(2, grid);
        // défini le facteur de rétrécissement. Ici comme il est à 0, le formulaire ne se réduira pas.
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        // ajout de la toolbar (recherche + nouveau entretien) et du content (grid + formulaire)
        add(getToolbar(), content, entretienModal);
        // initialisation des données de la grille à l'ouverture de la vue
        updateList();

        /*grid.asSingleSelect().addValueChangeListener(event ->
                editTuteurModal(event.getValue()));*/
    }

    //Configuration de notre grille où on affiche les entretiens
    private void configureGrid() {
        grid.addClassNames("entretien-grid");
        grid.setSizeFull();
        grid.addColumn(entretienCollectif -> entretienCollectif.getDate()).setHeader("Date de l'entretien").setSortable(true);
        grid.addColumn(entretienCollectif -> entretienCollectif.getFormation().getLibelleFormation()).setHeader("Formation");
        grid.addColumn(entretienCollectif -> entretienCollectif.getReferentCfa().getNomReferentCfa() + ' ' + entretienCollectif.getReferentCfa().getPrenomReferentCfa()).setHeader("Référent CFA");
        // bouton consultation entretien
        grid.addComponentColumn(entretienCollectif -> new Button(new Icon(VaadinIcon.EYE), click -> {
            consultEntretien(entretienCollectif);
        })).setHeader("Consulter");
        // bouton édition entretien
        grid.addComponentColumn(entretienCollectif -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
            editEntretien(entretienCollectif);
        })).setHeader("Éditer");
        // bouton suppression entretien
        grid.addComponentColumn(entretienCollectif -> new Button(new Icon(VaadinIcon.TRASH), click -> {
            prepareToDelete(entretienCollectif);
        })).setHeader("Supprimer");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Recherche par formation...");
        filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
        // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc que après que l'utilisateur
        // a arrêté de taper dans le champ depuis un petit moment.
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        // appel de la fonction qui va mettre à jour la liste des tuteurs en tenant compte de ce qu'on a tapé en recherche
        filterText.addValueChangeListener(e -> updateList());

        addEntretienButton = new Button("Nouvel entretien");
        addEntretienButton.addClickListener(click -> addEntretien());

        // on met le champ de recherche et le bouton d'ajout dans un HorizontalLayout, pour qu'ils soient côte à côte
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addEntretienButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    // sauvegarde de l'entretien(nouveau)
    private void saveEntretien(EntretienCollectifNewOrEdit.SaveEvent event) {
        EntretienCollectif entretienCollectif = event.getEntretienCollectif();
        // sauvegarde de l'entretien
        entretienCollectifService.save(entretienCollectif);

        // ajout du log d'ajout
        logEnregistrmentService.saveLogAjoutString(entretienCollectif.toString());

        // mise à jour de la grid, fermeture du formulaire et notification
        updateList();
        closeNewOrEditModal();
        Notification.show("Entretien créé(e)");
    }

    // sauvegarde du tuteur modifié
    private void saveEditedEntretien(EntretienCollectifNewOrEdit.SaveEditedEvent event) {
        EntretienCollectif entretienCollectif = event.getEntretienCollectif();
        // récupération du tuteur avant modification
        EntretienCollectif entretienCollectifOriginal = event.getEntretienOriginal();

        // sauvegarde de l'entretien
        entretienCollectifService.save(entretienCollectif);

        // ajout du log de modification
        logEnregistrmentService.saveLogEditString(entretienCollectifOriginal.toString(), entretienCollectif.toString());

        updateList();
        closeNewOrEditModal();
        Notification.show("Entretien modifié(e)");
    }

    private void transfertEntretienFromEventToDelete(EntretienCollectifConsult.DeleteEvent event){
        EntretienCollectif entretienCollectif = event.getEntretien();
        deleteEntretien(entretienCollectif);
    }

    // suppression de l'entretien
    private void deleteEntretien(EntretienCollectif entretienCollectif) {
        if (entretienCollectif != null) {
            entretienCollectifService.delete(entretienCollectif);

            // ajout du log de suppression
            logEnregistrmentService.saveLogDeleteString(entretienCollectif.toString());

            updateList();
            closeConsultModal();
            Notification.show("Entretien retiré");
        }
    }

    private void deleFromConfirmDelete(ConfirmDelete.DeleteEventGrid event){
        Boolean supprimer = event.getSuppression();
        if(supprimer){
            deleteEntretien(entretienMaybeToDelete);
        }
        entretienMaybeToDelete = null;
        closeConfirmDelete();
    }

    public void prepareToDelete(EntretienCollectif entretienCollectif){
        entretienMaybeToDelete = entretienCollectif;
        openConfirmDelete();
    }

    public void consultEntretien(EntretienCollectif entretienCollectif) {
        entretienModalConsult.setEntretien(entretienCollectif);
        entretienModalConsult.open();
    }

    // si entretien null, on ferme le formulaire, sinon on l'affiche (new or edit)
    public void editEntretien(EntretienCollectif entretienCollectif) {
        if (entretienCollectif == null) {
            closeNewOrEditModal();
        } else {
            entretienModal.setEntretienCollectif(entretienCollectif);
            entretienModal.open();
            addClassName("editing");
        }
    }

    // ajout d'un entretien
    void addEntretien() {
        // on retire le focus s'il y avait une ligne sélectionnée
        grid.asSingleSelect().clear();
        editEntretien(new EntretienCollectif());
    }

    private void closeConsultModal() {
        entretienModalConsult.setEntretien(null);
        entretienModalConsult.close();
        grid.asSingleSelect().clear();
    }

    private void closeNewOrEditModal() {
        entretienModal.setEntretienCollectif(null);
        entretienModal.close();
        grid.asSingleSelect().clear();
    }

    private void openConfirmDelete() {
        confirmDelete.open();
    }

    private void closeConfirmDelete(){
        confirmDelete.close();
        grid.asSingleSelect().clear();
    }

    private void closeNewOrEditModalAndRefreshGrid() {
        closeNewOrEditModal();
        updateList();
    }

    // fonction qui récupère la liste des entretiens pour les afficher dans la grille (avec les valeurs de recherche)
    private void updateList() {
        grid.setItems(entretienCollectifService.findAll(filterText.getValue()));
    }
}
