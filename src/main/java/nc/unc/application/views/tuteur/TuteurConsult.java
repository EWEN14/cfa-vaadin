package nc.unc.application.views.tuteur;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.entity.TuteurHabilitation;
import nc.unc.application.data.service.TuteurService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Modale (Dialog) qui s'ouvre lorsque l'on clique sur le bouton de détail d'un tuteur
 */
public class TuteurConsult extends Dialog {

  private Tuteur tuteur;
  private TuteurService tuteurService;

  // Layout qui contiendra le contenu en dessous des tabs
  private final VerticalLayout content;

  // form qui contient les informations générales du tuteur
  private final FormLayout form = new FormLayout();
  private final TextField nom = new TextField("NOM");
  private final TextField prenom = new TextField("Prénom");
  private final TextField dateNaissance = new TextField("Date de naissance");
  private final EmailField email = new EmailField("Email");
  private final TextField telephone1 = new TextField("Téléphone 1");
  private final TextField telephone2 = new TextField("Téléphone 2");
  private final TextField civilite = new TextField("Civilité");
  private final TextField diplomeEleveObtenu = new TextField("Diplome le plus élevé obtenu");
  private final TextField niveauDiplome = new TextField("Niveau du Diplôme");
  private final TextField posteOccupe = new TextField("Poste occupé");
  private final TextField anneeExperienceProfessionnelle = new TextField("Années d'expérience professionnelle");
  private final Checkbox casierJudiciaireFourni = new Checkbox("Casier Judiciaire fourni");
  private final Checkbox diplomeFourni = new Checkbox("Diplôme fourni");
  private final Checkbox certificatTravailFourni = new Checkbox("Certificat de Travail fourni");
  private final Checkbox cvFourni = new Checkbox("CV fourni");

  // form qui contiendra les informations relatives à l'entreprise dans laquelle est le tuteur
  private final FormLayout formTuteursEntrepriseInfos = new FormLayout();
  private final TextField entrepriseEnseigne = new TextField("Enseigne");
  private final TextField entrepriseRaisonSociale = new TextField("Raison Sociale");

  // grid qui contiendra les habilitations du tuteur
  private final Grid<TuteurHabilitation> tuteurHabilitationGrid = new Grid<>(TuteurHabilitation.class);

  // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
  private final Tab tuteursInfosTab = new Tab(VaadinIcon.ACADEMY_CAP.create(), new Span("Tuteur"));
  private final Tab tuteursHabilitationsTab = new Tab(VaadinIcon.DIPLOMA.create(), new Span("Habiliations"));
  private final Tab entrepriseTuteurInfosTab = new Tab(VaadinIcon.WORKPLACE.create(), new Span("Entreprise"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer le tuteur");

  public TuteurConsult() {
    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);
    this.setWidth("85vw");

    // grille des habilitations du tuteur
    tuteurHabilitationGrid.addClassName("tuteur-habilitation-grid");
    tuteurHabilitationGrid.setColumns("dateFormation","formation.libelleFormation", "statutFormation");

    // Méthode qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
    setAllFieldsToReadOnly();

    // On instancie la Tabs, et on lui donne les tab que l'on veut insérer
    // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
    Tabs tabsTuteurs = new Tabs(tuteursInfosTab, tuteursHabilitationsTab, entrepriseTuteurInfosTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsTuteurs.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    // on définit les champs qu'il y aura dans le formulaire d'informations générales de l'étudiant
    form.add(nom, prenom, dateNaissance, email, civilite, telephone1, telephone2, diplomeEleveObtenu, niveauDiplome, posteOccupe,
            anneeExperienceProfessionnelle, casierJudiciaireFourni, diplomeFourni, certificatTravailFourni,
            cvFourni, createButtonsLayout());

    // pareil, mais pour le formulaire relatif à son entreprise
    formTuteursEntrepriseInfos.add(entrepriseEnseigne, entrepriseRaisonSociale);

    // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
    content = new VerticalLayout();
    content.setSpacing(false);
    // à l'ouverture, on définit le contenu par rapport à la tab sélectionné (la première par défaut)
    setContent(tabsTuteurs.getSelectedTab());

    // on ajoute la tabs, le contenu et les boutons du bas dans la Modale/Dialog
    add(tabsTuteurs, content, createButtonsLayout());
  }

  // Méthode appelée à l'ouverture de la vue pour alimenter les champs du formulaire.
  // Ici pas besoin de binder étant donné que l'on ne fait que consulter les informations (pas de sauvegarde)
  public void setTuteur(Tuteur tuteur) {
    this.tuteur = tuteur;
    if (tuteur != null) {
      // champs obligatoirement remplis
      nom.setValue(tuteur.getNom());
      prenom.setValue(tuteur.getPrenom());
      // affichage date au format français
      dateNaissance.setValue(tuteur.getDateNaissance() != null ?
              tuteur.getDateNaissance().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) : "");
      email.setValue(tuteur.getEmail());
      telephone1.setValue(tuteur.getTelephone1().toString());

      // champs pouvant être null (ou false donc pas coché dans les cas des cases à cocher)
      telephone2.setValue(tuteur.getTelephone2() != null ? tuteur.getTelephone2().toString() : "");
      civilite.setValue(tuteur.getCivilite() != null ? tuteur.getCivilite().toString() : "");
      diplomeEleveObtenu.setValue(tuteur.getDiplomeEleveObtenu() != null ? tuteur.getDiplomeEleveObtenu() : "");
      niveauDiplome.setValue(tuteur.getNiveauDiplome() != null ? tuteur.getNiveauDiplome().toString() : "");
      posteOccupe.setValue(tuteur.getPosteOccupe() != null ? tuteur.getPosteOccupe() : "");
      anneeExperienceProfessionnelle.setValue(tuteur.getAnneeExperienceProfessionnelle() != null ? tuteur.getAnneeExperienceProfessionnelle() : "");
      casierJudiciaireFourni.setValue(tuteur.getCasierJudiciaireFourni() != null ? tuteur.getCasierJudiciaireFourni() : false);
      diplomeFourni.setValue(tuteur.getDiplomeFourni() != null ? tuteur.getDiplomeFourni() : false);
      certificatTravailFourni.setValue(tuteur.getCertificatTravailFourni() != null ? tuteur.getCertificatTravailFourni() : false);
      cvFourni.setValue(tuteur.getCvFourni() != null ? tuteur.getCvFourni() : false);

      // si le tuteur a une entreprise, on passe les infos relatives à l'entreprise en formulaire
      // et on affiche la tab "Entreprise", sinon on la masque
      if (tuteur.getEntreprise() != null) {
        entrepriseTuteurInfosTab.setVisible(true);
        // champs obligatoirement remplis dans entreprise
        entrepriseEnseigne.setValue(tuteur.getEntreprise().getEnseigne());

        // champs non obligatoirement remplis dans entreprise
        entrepriseRaisonSociale.setValue(tuteur.getEntreprise().getRaisonSociale() != null ? tuteur.getEntreprise().getRaisonSociale() : "");
      } else {
        entrepriseTuteurInfosTab.setVisible(false);
      }

      if (tuteur.getTuteurHabilitations() != null) {
        tuteursHabilitationsTab.setVisible(true);
        tuteurHabilitationGrid.setItems(tuteur.getTuteurHabilitations());
      } else {
        tuteursHabilitationsTab.setVisible(false);
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
    } else if (tab.equals(tuteursHabilitationsTab)) {
      content.add(tuteurHabilitationGrid);
    } else if (tab.equals(entrepriseTuteurInfosTab)) {
      content.add(formTuteursEntrepriseInfos);
    }
  }

  private void setAllFieldsToReadOnly() {
    nom.setReadOnly(true);
    prenom.setReadOnly(true);
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
