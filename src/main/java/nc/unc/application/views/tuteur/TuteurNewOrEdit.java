package nc.unc.application.views.tuteur;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.checkbox.Checkbox;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.entity.Tuteur;
import com.vaadin.flow.component.textfield.TextField;
import nc.unc.application.data.entity.TuteurHabilitation;
import nc.unc.application.data.enums.Civilite;
import nc.unc.application.data.enums.StatutFormation;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.data.service.TuteurService;

import java.util.ArrayList;
import java.util.List;

public class TuteurNewOrEdit extends Dialog {

  // Objets tuteurs
  private Tuteur tuteur;
  private Tuteur cloneTuteur;

  // habilitation, qui sera une nouvelle ou une habilitation en cours d'édition
  private TuteurHabilitation tuteurHabilitation;
  // variable qui prendra les valeurs d'une habilitation avant sa modification, utilisé pour les logs
  private TuteurHabilitation tuteurHabilitationOldValues;
  // contiendra la liste des habilitations du tuteur
  private List<TuteurHabilitation> tuteurHabilitationsList;

  // TuteurService pour utiliser la sauvegarde ou la suppression d'habilitations
  private TuteurService tuteurService;
  // FormationService qu'on va requêter pour obtenir les formations pour lesquelles le tuteur n'est pas habilité
  private FormationService formationService;
  // LogEnregistrementService pour les logs sur les habilitations du tuteur
  private LogEnregistrmentService logEnregistrmentService;

  // Contiendra la liste des formations pour lesquelles le tuteur n'est pas habilité
  private List<Formation> formationsNonHabilites;

  // Layout qui contiendra le contenu en dessous des tabs
  private final VerticalLayout content;

  // Champs de notre formulaire
  FormLayout form = new FormLayout();
  TextField nom = new TextField("NOM");
  TextField prenom = new TextField("Prenom");
  DatePicker dateNaissance = new DatePicker("Date de naissance");
  EmailField email = new EmailField("Email");
  IntegerField telephone1 = new IntegerField("Téléphone 1");
  IntegerField telephone2 = new IntegerField("Téléphone 2");
  Select<Civilite> civilite = new Select<>();
  TextField diplomeEleveObtenu = new TextField("Diplome le plus élevé obtenu");
  Select<Integer> niveauDiplome = new Select<>();
  TextField posteOccupe = new TextField("Poste occupé");
  TextField anneeExperienceProfessionnelle = new TextField("Années expérience professionnelle");
  ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");
  TextArea observations = new TextArea("Observations");
  Checkbox casierJudiciaireFourni = new Checkbox("Casier Judiciaire fourni");
  Checkbox diplomeFourni = new Checkbox("Diplôme fourni");
  Checkbox certificatTravailFourni = new Checkbox("Certificat de Travail fourni");
  Checkbox cvFourni = new Checkbox("CV fourni");

  // éléments associés aux habilitations du tuteur
  // grid qui contient la liste des habilitations du tuteur
  Grid<TuteurHabilitation> tuteurHabilitations = new Grid<>(TuteurHabilitation.class);
  // formulaire et éléments du formulaire pour l'ajout et l'édition d'habilitations
  FormLayout formNewHabilitation = new FormLayout();
  Select<String> statutFormation = new Select<>();
  DatePicker dateFormation = new DatePicker("Date de Formation");
  ComboBox<Formation> formation = new ComboBox<>("Formation");

  // Onglets
  Tab tuteursInfosTab = new Tab(VaadinIcon.USER.create(), new Span("Tuteur"));
  Tab tuteursHabilitationsTab = new Tab(VaadinIcon.DIPLOMA.create(), new Span("Habilitations"));

  // Notre binder
  Binder<Tuteur> binder = new BeanValidationBinder<>(Tuteur.class);
  // binder pour le formulaire de nouvelle habilitation
  Binder<TuteurHabilitation> tuteurHabilitationBinder = new BeanValidationBinder<>(TuteurHabilitation.class);

  // les boutons (sauvegarder, annuler)
  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");
  // bouton de sauvegarde et effacer pour le formulaire d'habilitation
  Button saveNewHabilitation = new Button("Sauvegarder");
  Button effacerChamps = new Button("Effacer / Annuler");

  public TuteurNewOrEdit(List<Entreprise> entreprises, List<Formation> allFormations, FormationService formationService,
                         TuteurService tuteurService, LogEnregistrmentService logEnregistrmentService) {
    this.tuteurService = tuteurService;
    this.formationService = formationService;
    this.logEnregistrmentService = logEnregistrmentService;

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité tuteur,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);
    this.setWidth("85vw");
    // méthode appelée lors de la fermeture de la modale au clic sur le bouton ECHAP ou en cliquant en dehors de la modale
    this.addDialogCloseActionListener(dialogCloseActionEvent -> closeDialog());

    Tabs tabsTuteurs = new Tabs(tuteursInfosTab, tuteursHabilitationsTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsTuteurs.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    // définition du label de civilite, et alimentation en valeurs de l'enum Civilite
    civilite.setLabel("Civilité");
    civilite.setItems(Civilite.values());

    // Remplissage de nos select
    niveauDiplome.setLabel("Niveau de diplôme");
    niveauDiplome.setItems(1, 2, 3, 4, 5, 6, 7, 8);

    // on passe les entreprises sur la ComboBox pour pouvoir sélectionner une entreprise existante
    entreprise.setItems(entreprises);
    entreprise.setItemLabelGenerator(Entreprise::getEnseigne);

    // date picker I18n qui permet de taper à la main une date au format français
    // (si l'utilisateur veut se passer de l'utilisation du calendrier intégré)
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");
    dateNaissance.setI18n(multiFormatI18n);
    dateNaissance.isRequired();

    // grille des habilitations du tuteur
    tuteurHabilitations.addClassName("tuteur-habilitation-grid");
    tuteurHabilitations.setColumns("dateFormation","formation.libelleFormation", "statutFormation");
    // ajout d'un bouton d'édition pour pouvoir modifier une habilitation
    tuteurHabilitations.addComponentColumn(tuteur -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      editHabilitationTuteur(tuteur);
    }));
    // ajout d'un bouton de suppression pour pouvoir supprimer une habilitation
    tuteurHabilitations.addComponentColumn(tuteur -> new Button(new Icon(VaadinIcon.TRASH), click -> {
      deleteHabilitationTuteur(tuteur);
    }));

    // ajout des champs et des boutons d'action dans le formulaire
    form.add(nom, prenom, dateNaissance, civilite, email, telephone1, telephone2, diplomeEleveObtenu, niveauDiplome,
            posteOccupe, anneeExperienceProfessionnelle, entreprise, observations, casierJudiciaireFourni, diplomeFourni,
            certificatTravailFourni, cvFourni, createButtonsLayout());

    // --- configuration champs et formulaire d'ajout d'habilités ---
    // ajout du binder pour le formulaire d'ajout
    tuteurHabilitationBinder.bindInstanceFields(this);

    // ajout des valeurs possibles sur le select du statut de formation
    statutFormation.setLabel("Statut de la Formation");
    statutFormation.setItems(StatutFormation.getStatutFormationStr());

    dateFormation.setI18n(multiFormatI18n);

    // On propose au départ toutes les formations pour le champ de sélection d'habilitation
    formation.setItems(allFormations);
    formation.setItemLabelGenerator(Formation::getLibelleFormation);

    // ajout des champs dans le formulaire d'ajout d'habilités au tuteur
    formNewHabilitation.add(statutFormation, dateFormation, formation, createSaveHabilitationButtonLayout());

    // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
    content = new VerticalLayout();
    content.setSpacing(false);
    // à l'ouverture, on ouvre les informations générales du tuteur
    setContent(tuteursInfosTab);

    add(tabsTuteurs, content);
  }

  // Configuration des boutons de sauvegarde et de fermeture de la modale du tuteur
  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, delete, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new TuteurNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide (si champs obligatoires remplis)
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // ajout des événements relatifs aux boutons sauvegarder et effacer dans le formulaire des habilitations
  private HorizontalLayout createSaveHabilitationButtonLayout() {
    saveNewHabilitation.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    effacerChamps.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // lancement d'événement au clic sur les boutons
    saveNewHabilitation.addClickListener(event -> validateHabilitationAndSave());
    effacerChamps.addClickListener(event -> effacerChampsFormHabilitation());

    // bouton de sauvegarde de l'habilitation actif si champs obligatoire sont remplis
    tuteurHabilitationBinder.addStatusChangeListener(e -> saveNewHabilitation.setEnabled(tuteurHabilitationBinder.isValid()));

    return new HorizontalLayout(saveNewHabilitation, effacerChamps);
  }

  // fonction qui va alimenter le binder d'un tuteur
  public void setTuteur(Tuteur tuteur) {
    this.tuteur = tuteur;
    this.cloneTuteur = null;
    // copie du tuteur si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (tuteur != null && tuteur.getId() != null) {
      try {
        this.cloneTuteur = (Tuteur) tuteur.clone();
        // on récupère la liste des habilitations du tuteur
        tuteurHabilitationsList = tuteur.getTuteurHabilitations();
        // on utilise cette liste pour remplire la grille
        tuteurHabilitations.setItems(tuteurHabilitationsList);
        // on rend l'onglet des habilitation visibles (car édition et pas création)
        tuteursHabilitationsTab.setVisible(true);
        // on met le champ de sélection de formation éditable, si jamais il était verouillé par une précédente édition
        formation.setReadOnly(false);
        // méthode affichera dans le champ de sélection de formation que les formations pour lesquelles le tuteur n'est pas habilité
        selectFormationNonHabilite(tuteurHabilitationsList);
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    } else {
      // si nouveau tuteur, on cache l'onglet des habilitations et on passe sur l'onglet des infos tuteur
      tuteursHabilitationsTab.setVisible(false);
      tuteursInfosTab.setSelected(true);
      setContent(tuteursInfosTab);
    }
    // alimentation du binder
    binder.readBean(tuteur);
    // par défaut, on définit une nouvelle habilitation dans le formulaire
    tuteurHabilitation = new TuteurHabilitation();
    // on associe le binder à cette potentielle nouvelle habilitation
    tuteurHabilitationBinder.readBean(tuteurHabilitation);
  }

  // fonction qui vérifie que le bean a bien un tuteur, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(tuteur);
      if (this.cloneTuteur == null) {
        // sauvegarde nouveau tuteur
        fireEvent(new TuteurNewOrEdit.SaveEvent(this, tuteur));
      } else {
        // sauvegarde des modifications sur un tuteur existant
        fireEvent(new TuteurNewOrEdit.SaveEditedEvent(this, tuteur, cloneTuteur));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // sauvegarde de l'habilitation (nouveau et édition)
  private void validateHabilitationAndSave() {
    try {
      tuteurHabilitationBinder.writeBean(tuteurHabilitation);
      if (tuteurHabilitation != null) {
        tuteurHabilitation.setTuteur(tuteur);
        // cas où l'on créé un nouveau tuteur
        if (tuteurHabilitation.getId() == null) {
          tuteurService.saveTuteurHabilitation(tuteurHabilitation);
          logEnregistrmentService.saveLogAjoutString(tuteurHabilitation.toString());
          fireEvent(new TuteurNewOrEdit.CloseAndReloadEvent(this));
          Notification.show("Habilitation ajoutée");
        } else {
          // cas où l'on modifie une habilitation
          if (!tuteurHabilitationOldValues.equals(tuteurHabilitation)) {
            // on ne créé un log de modification que s'il y a eu des valeurs de changées
            logEnregistrmentService.saveLogEditString(tuteurHabilitationOldValues.toString(), tuteurHabilitation.toString());
            tuteurService.saveTuteurHabilitation(tuteurHabilitation);
            tuteurHabilitationOldValues = null;
            fireEvent(new TuteurNewOrEdit.CloseAndReloadEvent(this));
            Notification.show("Habilitation modifiée");
          } else {
            // sinon on signale que les valeurs n'ont pas changés
            Notification.show("Les valeurs sont les mêmes qu'avant !");
          }
        }
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // suppression d'une habilitation
  private void deleteHabilitationTuteur(TuteurHabilitation tuteurHabilitation) {
    if (tuteurHabilitation != null) {
      logEnregistrmentService.saveLogDeleteString(tuteurHabilitation.toString());
      tuteurService.deleteTuteurHabilitation(tuteurHabilitation);
      fireEvent(new TuteurNewOrEdit.CloseAndReloadEvent(this));
      Notification.show("Habilitation supprimée");
    }
  }

  // On définit que les formations qui doivent apparaître dans le select pour ajouter
  // une habilitation doivent être celles pour lesquelles le tuteur n'est pas encore habilité
  private void selectFormationNonHabilite(List<TuteurHabilitation> tuteurHabilitationListe) {
    List<Long> idFormationsHabilites = new ArrayList<Long>();
    if (!tuteurHabilitationListe.isEmpty()) {
      for (TuteurHabilitation tuteurHabilitation : tuteurHabilitationListe) {
        idFormationsHabilites.add(tuteurHabilitation.getFormation().getId());
      }
      formationsNonHabilites = formationService.getAllFormationNonHabilite(idFormationsHabilites);
    } else {
      // si aucune habilitation, on récupère toutes les formations
      formationsNonHabilites = formationService.findAllFormations("");
    }
    formation.setItems(formationsNonHabilites);
    formation.setItemLabelGenerator(Formation::getLibelleFormation);
  }

  // fonction qui va afficher les informations de l'habilitation sur laquelle on a cliqué dans la grid dans
  // le formulaire d'édition pour pouvoir modifier l'habilitation
  private void editHabilitationTuteur(TuteurHabilitation tuteurHabilitation) {
    try {
      this.tuteurHabilitationOldValues = (TuteurHabilitation) tuteurHabilitation.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    this.tuteurHabilitation = tuteurHabilitation;
    tuteurHabilitationBinder.readBean(tuteurHabilitation);
    formation.setReadOnly(true);
  }

  // appelée au clic sur "effacer", met à vide les champs du formulaire.
  private void effacerChampsFormHabilitation() {
    this.tuteurHabilitationOldValues = null;
    this.tuteurHabilitation = new TuteurHabilitation();
    tuteurHabilitationBinder.readBean(tuteurHabilitation);
    formation.setReadOnly(false);
  }

  private void setContent(Tab tab) {
    // au début on enlève tout le contenu avant remplacement
    content.removeAll();
    // on insère le contenu adéquat en fonction de la tab sélectionnée
    if (tab.equals(tuteursInfosTab)) {
      content.add(form);
    } else if (tab.equals(tuteursHabilitationsTab)) {
      content.add(formNewHabilitation, tuteurHabilitations);
    }
  }

  private void closeDialog() {
    // on enlève l'éventuel focus sur l'onglet des infos du tuteur.
    if (tuteursInfosTab.isEnabled()) {
      tuteursInfosTab.setSelected(false);
    }
    // on appelle l'événement pour fermer la modale.
    fireEvent(new TuteurNewOrEdit.CloseEvent(this));
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir l'étudiant
  // qu'on manipule dans le formulaire
  public static abstract class TuteurFormEvent extends ComponentEvent<TuteurNewOrEdit> {
    private final Tuteur tuteur;
    private final Tuteur tuteurOriginal;

    protected TuteurFormEvent(TuteurNewOrEdit source, Tuteur tuteur, Tuteur tuteurOriginal) {
      super(source, false);
      this.tuteur = tuteur;
      this.tuteurOriginal = tuteurOriginal;
    }

    public Tuteur getTuteur() {
      return tuteur;
    }

    public Tuteur getTuteurOriginal() {
      return tuteurOriginal;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
  public static class SaveEvent extends TuteurNewOrEdit.TuteurFormEvent {
    SaveEvent(TuteurNewOrEdit source, Tuteur tuteur) {
      super(source, tuteur, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
  public static class SaveEditedEvent extends TuteurNewOrEdit.TuteurFormEvent {
    SaveEditedEvent(TuteurNewOrEdit source, Tuteur tuteur, Tuteur tuteurOriginal) {
      super(source, tuteur, tuteurOriginal);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends TuteurNewOrEdit.TuteurFormEvent {
    CloseEvent(TuteurNewOrEdit source) {
      super(source, null, null);
    }
  }

  public static class CloseAndReloadEvent extends TuteurNewOrEdit.TuteurFormEvent {
    CloseAndReloadEvent(TuteurNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
