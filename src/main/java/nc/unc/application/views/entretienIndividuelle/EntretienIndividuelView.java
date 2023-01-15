package nc.unc.application.views.entretienIndividuelle;


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
import nc.unc.application.data.entity.EntretienIndividuel;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.Sexe;
import nc.unc.application.data.service.*;
import nc.unc.application.views.ConfirmDelete;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.tuteur.TuteurConsult;
import nc.unc.application.views.tuteur.TuteurNewOrEdit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

import static nc.unc.application.utils.Utils.frenchDateFormater;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "entretiensIndividuels", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Entretiens Individuels | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class EntretienIndividuelView extends VerticalLayout {

    // Les attributs de notre vue
    Grid<EntretienIndividuel> grid = new Grid<>(EntretienIndividuel.class, false);
    TextField filterText = new TextField();
    Button addEntretienButton;

    EntretienIndividuel entretienMaybeToDelete;

    EntretienIndividuelNewOrEdit entretienModal;
    EntretienIndividuelConsult entretienModalConsult;
    ConfirmDelete confirmDelete;

    private EntretienIndividuelService entretienIndividuelService;
    private LogEnregistrmentService logEnregistrmentService;
    private ReferentCfaService referentCfaService;
    private EtudiantService etudiantService;

    public EntretienIndividuelView(LogEnregistrmentService logEnregistrmentService, EntretienIndividuelService entretienIndividuelService, ReferentCfaService referentCfaService, EtudiantService etudiantService){

        this.entretienIndividuelService = entretienIndividuelService;
        this.logEnregistrmentService = logEnregistrmentService;
        this.etudiantService = etudiantService;
        this.referentCfaService = referentCfaService;

        addClassName("list-view");
        setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
        configureGrid(); // configuration de la grille (colonnes, données...)

        entretienModalConsult = new EntretienIndividuelConsult(etudiantService, referentCfaService, entretienIndividuelService);
        // On définit que les différents events (EntretienIndividuelForm.fooEvent) vont déclencher une fonction
        // contenant l'objet tuteur (dans le cas du save ou delete).
        entretienModalConsult.addListener(EntretienIndividuelConsult.DeleteEvent.class, this::transfertEntretienFromEventToDelete);
        entretienModalConsult.addListener(EntretienIndividuelConsult.CloseEvent.class, e -> closeConsultModal());

        entretienModal = new EntretienIndividuelNewOrEdit(referentCfaService.findAllReferentCfa(null), etudiantService.findAllEtudiants(null));
        entretienModal.addListener(EntretienIndividuelNewOrEdit.SaveEvent.class, this::saveEntretien);
        entretienModal.addListener(EntretienIndividuelNewOrEdit.SaveEditedEvent.class, this::saveEditedEntretien);
        entretienModal.addListener(EntretienIndividuelNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

        confirmDelete = new ConfirmDelete("cet entretien individuel");
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
        grid.addColumn(entretienIndividuel -> entretienIndividuel.getDate() != null ? frenchDateFormater(entretienIndividuel.getDate()) : "").setHeader("Date de l'Entretien").setSortable(true);
        grid.addColumn(entretienIndividuel -> entretienIndividuel.getEtudiant().getNomEtudiant() + ' ' + entretienIndividuel.getEtudiant().getPrenomEtudiant()).setHeader("Etudiant");
        grid.addColumn(entretienIndividuel -> entretienIndividuel.getReferentCfa().getNomReferentCfa() + ' ' + entretienIndividuel.getReferentCfa().getPrenomReferentCfa()).setHeader("Référent CFA");
        grid.addColumn(entretienIndividuel -> (entretienIndividuel.getSuivreEtudiant() != null && entretienIndividuel.getSuivreEtudiant()) ? "Oui" : "").setHeader("À suivre");
        // bouton consultation entretien
        grid.addComponentColumn(entretienIndividuel -> new Button(new Icon(VaadinIcon.EYE), click -> {
            consultEntretien(entretienIndividuel);
        })).setHeader("Consulter");
        // bouton édition entretien
        grid.addComponentColumn(entretienIndividuel -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
            editEntretien(entretienIndividuel);
        })).setHeader("Éditer");
        // bouton suppression entretien
        grid.addComponentColumn(entretienIndividuel -> new Button(new Icon(VaadinIcon.TRASH), click -> {
            prepareToDelete(entretienIndividuel);
        })).setHeader("Supprimer");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Recherche par étudiant...");
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
    private void saveEntretien(EntretienIndividuelNewOrEdit.SaveEvent event) {
        EntretienIndividuel entretienIndividuel = event.getEntretienIndividuel();
        // sauvegarde de l'entretien
        entretienIndividuelService.save(entretienIndividuel);

        // ajout du log d'ajout
        logEnregistrmentService.saveLogAjoutString(entretienIndividuel.toString());

        // mise à jour de la grid, fermeture du formulaire et notification
        updateList();
        closeNewOrEditModal();
        Notification.show("Entretien créé(e)");
    }

    // sauvegarde du tuteur modifié
    private void saveEditedEntretien(EntretienIndividuelNewOrEdit.SaveEditedEvent event) {
        EntretienIndividuel entretienIndividuel = event.getEntretienIndividuel();
        // récupération du tuteur avant modification
        EntretienIndividuel entretienIndividuelOriginal = event.getEntretienOriginal();

        // sauvegarde de l'entretien
        entretienIndividuelService.save(entretienIndividuel);

        // ajout du log de modification
        logEnregistrmentService.saveLogEditString(entretienIndividuelOriginal.toString(), entretienIndividuel.toString());

        updateList();
        closeNewOrEditModal();
        Notification.show("Entretien modifié(e)");
    }

    private void transfertEntretienFromEventToDelete(EntretienIndividuelConsult.DeleteEvent event){
        EntretienIndividuel entretienIndividuel = event.getEntretien();
        deleteEntretien(entretienIndividuel);
    }

    // suppression de l'entretien
    private void deleteEntretien(EntretienIndividuel entretienIndividuel) {
        if (entretienIndividuel != null) {
            entretienIndividuelService.delete(entretienIndividuel);

            // ajout du log de suppression
            logEnregistrmentService.saveLogDeleteString(entretienIndividuel.toString());

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

    public void prepareToDelete(EntretienIndividuel entretienIndividuel){
        entretienMaybeToDelete = entretienIndividuel;
        openConfirmDelete();
    }

    public void consultEntretien(EntretienIndividuel entretienIndividuel) {
        entretienModalConsult.setEntretien(entretienIndividuel);
        entretienModalConsult.open();
    }

    // si entretien null, on ferme le formulaire, sinon on l'affiche (new or edit)
    public void editEntretien(EntretienIndividuel entretienIndividuel) {
        if (entretienIndividuel == null) {
            closeNewOrEditModal();
        } else {
            entretienModal.setEntretienIndividuel(entretienIndividuel);
            entretienModal.open();
            addClassName("editing");
        }
    }

    // ajout d'un entretien
    void addEntretien() {
        // on retire le focus s'il y avait une ligne sélectionnée
        grid.asSingleSelect().clear();
        editEntretien(new EntretienIndividuel());
    }

    private void closeConsultModal() {
        entretienModalConsult.setEntretien(null);
        entretienModalConsult.close();
        grid.asSingleSelect().clear();
    }

    private void closeNewOrEditModal() {
        entretienModal.setEntretienIndividuel(null);
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
        grid.setItems(entretienIndividuelService.findAll(filterText.getValue()));
    }
}
