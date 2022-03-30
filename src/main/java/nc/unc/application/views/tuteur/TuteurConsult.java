package nc.unc.application.views.tuteur;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.Decua;
import nc.unc.application.views.etudiant.EtudiantConsult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Modale (Dialog) qui s'ouvre lorsque l'on clique sur le bouton de détail d'un tuteur
 */
public class TuteurConsult extends Dialog {

    private Tuteur tuteur;

    // Layout qui contiendra le contenu en dessous des tabs
    private VerticalLayout content;
    //Eléments de notre formulaire
    TextField prenom = new TextField("Prenom");
    TextField nom = new TextField("Nom");
    DatePicker dateNaissance = new DatePicker("Date de Naissance");
    EmailField email = new EmailField("Email");
    TextField diplomeEleveObtenu = new TextField("Diplome elevé obtenu");
    Select<Long> niveauDiplome = new Select<>();
    Select<Decua> pieceJointe1 = new Select<>();
    TextField posteOccupe = new TextField("Poste occupé");
    Select<Decua> pieceJointe2 = new Select<>();
    TextField anneeExperienceProfessionnelle = new TextField("Année expérience professionnelle");
    Checkbox casierJudiciaireFourni = new Checkbox("Casier judiciaire fournit");
    IntegerField telephone1 = new IntegerField("Téléphone 1");
    IntegerField telephone2 = new IntegerField("Téléphone 2");
    ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");
    FormLayout form = new FormLayout();

    // form qui contiendra les informations relatives à l'entreprise dans laquelle est le tuteur
    private final FormLayout formTuteursEntrepriseInfos = new FormLayout();
    private final TextField entrepriseEnseigne = new TextField("Enseigne");
    private final TextField entrepriseRaisonSociale = new TextField("Raison Sociale");

    // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
    private final Tab tuteursInfosTab = new Tab(VaadinIcon.ACADEMY_CAP.create(),new Span("Tuteurs"));
    private final Tab entrepriseTuteurInfosTab = new Tab(VaadinIcon.WORKPLACE.create(),new Span("Entreprise"));

    private final Button close = new Button("Fermer");
    private final Button delete = new Button("Supprimer");

    public TuteurConsult() {
        // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
        // tant que la modale n'est pas fermée
        this.setModal(true);

        // On instancie la Tabs, et on lui donne les tab que l'on veut insérer
        // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
        Tabs tabsTuteurs = new Tabs(tuteursInfosTab, entrepriseTuteurInfosTab);
        // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
        tabsTuteurs.addSelectedChangeListener(selectedChangeEvent ->
                setContent(selectedChangeEvent.getSelectedTab())
        );

        // on définit les champs qu'il y aura dans le formulaire d'informations générales de l'étudiant
        form.add(prenom, nom, dateNaissance, email, diplomeEleveObtenu, niveauDiplome,pieceJointe1, posteOccupe, pieceJointe2, anneeExperienceProfessionnelle, casierJudiciaireFourni, telephone1, telephone2, entreprise, createButtonsLayout());
        // pareil, mais pour le formulaire relative à son entreprise
        formTuteursEntrepriseInfos.add(entrepriseEnseigne, entrepriseRaisonSociale);

        // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
        content = new VerticalLayout();
        content.setSpacing(false);
        // à l'ouverture, on définit le contenu par rapport à la tab sélectionné (la première par défaut)
        setContent(tabsTuteurs.getSelectedTab());

        // on ajoute la tabs, le contenu et les boutons du bas dans la Modale/Dialog
        add(tabsTuteurs, content, createButtonsLayout());
    }

    // méthode appelé à l'ouverture de la vue pour alimenter les champs du formulaire.
    // Ici pas besoin de binder étant donné que l'on ne fait que consulter les informations (pas de sauvegarde)
    public void setTuteur(Tuteur tuteur) {
        this.tuteur = tuteur;
        if (tuteur != null) {
            prenom.setValue(tuteur.getPrenom());
            nom.setValue(tuteur.getNom());
            email.setValue(tuteur.getEmail());
            diplomeEleveObtenu.setValue(tuteur.getDiplomeEleveObtenu());
            pieceJointe1.setValue(tuteur.getPieceJointe1());
            posteOccupe.setValue(tuteur.getPosteOccupe());
            pieceJointe2.setValue(tuteur.getPieceJointe2());
            anneeExperienceProfessionnelle.setValue(tuteur.getExperienceProfessionnelle());
            telephone1.setValue(tuteur.getTelephone1());
            casierJudiciaireFourni.setValue(tuteur.getCasierJudiciaireFourni());
            // affichage date au format français
            dateNaissance.setValue(LocalDate.parse(tuteur.getDateNaissance().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
            if (tuteur.getTelephone2() != null) {
                telephone2.setValue(tuteur.getTelephone2());
            } else {
                telephone2.setValue(00000);
            }

            // si le tuteur a une entreprise, on passe les infos relatives à l'entreprise en formulaire
            // et on affiche la tab "Entreprise", sinon on la masque
            if (tuteur.getEntreprise() != null) {
                entrepriseTuteurInfosTab.setVisible(true);
                entrepriseEnseigne.setValue(tuteur.getEntreprise().getEnseigne());
                entrepriseRaisonSociale.setValue(tuteur.getEntreprise().getRaisonSociale());
            } else {
                entrepriseTuteurInfosTab.setVisible(false);
            }
        }
    }

    private HorizontalLayout createButtonsLayout() {
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // évènements delete et close
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, tuteur)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(delete, close);
    }

    // méthode qui définit quel contenu mettre en dessous des tabs
    private void setContent(Tab tab) {
        // au début on enlève tout le contenu avant remplacement
        content.removeAll();
        // on insère le contenu (formulaire) adéquat en fonction de la tab sélectionnée
        if (tab.equals(tuteursInfosTab)) {
            content.add(form);
        } else if (tab.equals(entrepriseTuteurInfosTab)) {
            content.add(formTuteursEntrepriseInfos);
        }
    }

    // Event "global" (class mère), qui étend les deux events ci-dessous, dont le but est de fournir l'étudiant
    // que l'on consulte dans le formulaire.
    public static abstract class TuteurConsultFormEvent extends ComponentEvent<TuteurConsult> {
        private final Tuteur tuteur;

        protected TuteurConsultFormEvent(TuteurConsult source, Tuteur tuteur) {
            super(source, false);
            this.tuteur = tuteur;
        }

        public Tuteur getTuteur() {
            return tuteur;
        }
    }

    // Event au clic sur le bouton de suppression (classe fille)
    public static class DeleteEvent extends TuteurConsultFormEvent {
        DeleteEvent(TuteurConsult source, Tuteur tuteur) {
            super(source, tuteur);
        }
    }

    // Event au clic sur le bouton de fermerture du formulaire (classe fille)
    public static class CloseEvent extends TuteurConsultFormEvent {
        CloseEvent(TuteurConsult source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
