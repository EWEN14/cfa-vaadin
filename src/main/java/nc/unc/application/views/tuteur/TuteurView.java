package nc.unc.application.views.tuteur;


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
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.Sexe;
import nc.unc.application.data.service.EtudiantService;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.data.service.TuteurService;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.button.Button;
import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "tuteurs", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Tuteurs | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class TuteurView extends VerticalLayout {

    // Les attributs de notre vue
    Grid<Tuteur> grid = new Grid<>(Tuteur.class);
    TextField filterText = new TextField();
    Button addTuteurButton;
    TuteurNewOrEdit tuteurModal;
    TuteurConsult tuteurModalConsult;

    private TuteurService tuteurService;
    private EtudiantService etudiantService;
    private LogEnregistrmentService logEnregistrmentService;

    public TuteurView(TuteurService tuteurService, FormationService formationService, EtudiantService etudiantService, LogEnregistrmentService logEnregistrmentService){

        this.tuteurService = tuteurService;
        this.etudiantService = etudiantService;
        this.logEnregistrmentService = logEnregistrmentService;

        addClassName("list-view");
        setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
        configureGrid(); // configuration de la grille (colonnes, données...)

        tuteurModalConsult = new TuteurConsult(etudiantService);
        // On définit que les différents events (TuteurForm.fooEvent) dans le Tuteur  vont déclencher une fonction
        // contenant l'objet tuteur (dans le cas du save ou delete).
        tuteurModalConsult.addListener(TuteurConsult.DeleteEvent.class, this::deleteTuteur);
        tuteurModalConsult.addListener(TuteurConsult.CloseEvent.class, e -> closeConsultModal());

        tuteurModal = new TuteurNewOrEdit(tuteurService.findAllEntreprises(), formationService.findAllFormations(""),
                formationService, tuteurService, logEnregistrmentService);
        tuteurModal.addListener(TuteurNewOrEdit.SaveEvent.class, this::saveTuteur);
        tuteurModal.addListener(TuteurNewOrEdit.SaveEditedEvent.class, this::saveEditedTuteur);
        tuteurModal.addListener(TuteurNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());
        tuteurModal.addListener(TuteurNewOrEdit.CloseAndReloadEvent.class, e -> closeNewOrEditModalAndRefreshGrid());

        // ajout d'un FlexLayout qui place la grille
        FlexLayout content = new FlexLayout(grid);
        content.setFlexGrow(2, grid);
        // défini le facteur de rétrécissement. Ici comme il est à 0, le formulaire ne se réduira pas.
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        // ajout de la toolbar (recherche + nouveau tuteur) et du content (grid + formulaire)
        add(getToolbar(), content, tuteurModal);
        // initialisation des données de la grille à l'ouverture de la vue
        updateList();

        /*grid.asSingleSelect().addValueChangeListener(event ->
                editTuteurModal(event.getValue()));*/
    }

    //Configuration de notre grille où on affiche les tuteurs
    private void configureGrid() {
        grid.addClassNames("tuteur-grid");
        grid.setSizeFull();
        grid.setColumns("prenomTuteur", "nomTuteur", "dateNaissanceTuteur");
        // bouton consultation tuteur
        grid.addComponentColumn(tuteur -> new Button(new Icon(VaadinIcon.EYE), click -> {
            consultTuteur(tuteur);
        })).setHeader("Consulter");;
        // bouton édition tuteur
        grid.addComponentColumn(tuteur -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
            editTuteurModal(tuteur);
        })).setHeader("Éditer");;
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Recherche par nom ou prénom...");
        filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
        // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc que après que l'utilisateur
        // a arrêté de taper dans le champ depuis un petit moment.
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        // appel de la fonction qui va mettre à jour la liste des tuteurs en tenant compte de ce qu'on a tapé en recherche
        filterText.addValueChangeListener(e -> updateList());

        addTuteurButton = new Button("Nouveau tuteur");
        addTuteurButton.addClickListener(click -> addTuteur());

        // on met le champ de recherche et le bouton d'ajout dans un HorizontalLayout, pour qu'ils soient côte à côte
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addTuteurButton);
        toolbar.addClassName("toolbar");
        return toolbar;
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

        // mise à jour de la grid, fermeture du formulaire et notification
        updateList();
        closeNewOrEditModal();
        Notification.show(tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() + " créé(e)");
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

        updateList();
        closeNewOrEditModal();
        Notification.show(tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() + " modifié(e)");
    }

    // suppression du tuteur en utilisant TuteurService
    private void deleteTuteur(TuteurConsult.DeleteEvent event) {
        Tuteur tuteur = event.getTuteur();
        if (tuteur != null) {
            tuteurService.deleteTuteur(tuteur);

            // ajout du log de suppression
            logEnregistrmentService.saveLogDeleteString(tuteur.toString());

            updateList();
            closeConsultModal();
            Notification.show(tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() + " retiré(e)");
        }
    }

    public void consultTuteur(Tuteur tuteur) {
        tuteurModalConsult.setTuteur(tuteur);
        tuteurModalConsult.open();
    }

    // si tuteur null, on ferme le formulaire, sinon on l'affiche (new or edit)
    public void editTuteurModal(Tuteur tuteur) {
        if (tuteur == null) {
            closeNewOrEditModal();
        } else {
            tuteurModal.setTuteur(tuteur);
            tuteurModal.open();
            addClassName("editing");
        }
    }

    // ajout d'un tuteur
    void addTuteur() {
        // on retire le focus s'il y avait une ligne sélectionnée
        grid.asSingleSelect().clear();
        // appel de la fonction juste au-dessus
        editTuteurModal(new Tuteur());
    }

    private void closeConsultModal() {
        tuteurModalConsult.setTuteur(null);
        tuteurModalConsult.close();
        grid.asSingleSelect().clear();
    }

    private void closeNewOrEditModal() {
        tuteurModal.setTuteur(null);
        tuteurModal.close();
        grid.asSingleSelect().clear();
    }

    private void closeNewOrEditModalAndRefreshGrid() {
        closeNewOrEditModal();
        updateList();
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

    // fonction qui récupère la liste des tuteurs pour les afficher dans la grille (avec les valeurs de recherche)
    private void updateList() {
        grid.setItems(tuteurService.findAllTuteurs(filterText.getValue()));
    }
}
