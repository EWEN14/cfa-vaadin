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

import static nc.unc.application.utils.Utils.frenchDateFormater;

public class TuteurNewOrEdit extends Dialog {

  // Objets tuteurs
  private Tuteur tuteur;
  private Tuteur cloneTuteur;

  // habilitation, qui sera une nouvelle ou une habilitation en cours d'??dition
  private TuteurHabilitation tuteurHabilitation;
  // variable qui prendra les valeurs d'une habilitation avant sa modification, utilis?? pour les logs
  private TuteurHabilitation tuteurHabilitationOldValues;
  // contiendra la liste des habilitations du tuteur
  private List<TuteurHabilitation> tuteurHabilitationsList;

  // TuteurService pour utiliser la sauvegarde ou la suppression d'habilitations
  private TuteurService tuteurService;
  // FormationService qu'on va requ??ter pour obtenir les formations pour lesquelles le tuteur n'est pas habilit??
  private FormationService formationService;
  // LogEnregistrementService pour les logs sur les habilitations du tuteur
  private LogEnregistrmentService logEnregistrmentService;

  // Contiendra la liste des formations pour lesquelles le tuteur n'est pas habilit??
  private List<Formation> formationsNonHabilites;

  // Layout qui contiendra le contenu en dessous des tabs
  private final VerticalLayout content;

  // Champs de notre formulaire
  FormLayout form = new FormLayout();
  TextField nomTuteur = new TextField("NOM");
  TextField prenomTuteur = new TextField("Pr??nom");
  DatePicker dateNaissanceTuteur = new DatePicker("Date de naissance");
  EmailField emailTuteur = new EmailField("Email");
  IntegerField telephoneTuteur1 = new IntegerField("T??l??phone 1");
  IntegerField telephoneTuteur2 = new IntegerField("T??l??phone 2");
  Select<Civilite> civiliteTuteur = new Select<>();
  TextField diplomeEleveObtenu = new TextField("Diplome le plus ??lev?? obtenu");
  Select<Integer> niveauDiplome = new Select<>();
  TextField posteOccupe = new TextField("Poste occup??");
  TextField anneeExperienceProfessionnelle = new TextField("Ann??es exp??rience professionnelle");
  ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");
  TextArea observationsTuteur = new TextArea("Observations");
  Checkbox casierJudiciaireFourni = new Checkbox("Casier Judiciaire fourni");
  Checkbox diplomeFourni = new Checkbox("Dipl??me fourni");
  Checkbox certificatTravailFourni = new Checkbox("Certificat de Travail fourni");
  Checkbox cvFourni = new Checkbox("CV fourni");

  // ??l??ments associ??s aux habilitations du tuteur
  // grid qui contient la liste des habilitations du tuteur
  Grid<TuteurHabilitation> tuteurHabilitations = new Grid<>(TuteurHabilitation.class, false);
  // formulaire et ??l??ments du formulaire pour l'ajout et l'??dition d'habilitations
  FormLayout formNewHabilitation = new FormLayout();
  Select<String> statutFormation = new Select<>();
  DatePicker dateFormation = new DatePicker("Date de Formation");
  Select<String> modaliteFormation = new Select<>("FACE ?? FACE","PR??SENTIEL");
  DatePicker dateHabilitation = new DatePicker("Date d'Habilitation");
  TextArea observations = new TextArea("Observations");
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

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entit?? tuteur,
    // (les noms sont les m??mes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);
    this.setWidth("85vw");
    // m??thode appel??e lors de la fermeture de la modale au clic sur le bouton ECHAP ou en cliquant en dehors de la modale
    this.addDialogCloseActionListener(dialogCloseActionEvent -> closeDialog());

    Tabs tabsTuteurs = new Tabs(tuteursInfosTab, tuteursHabilitationsTab);
    // Au clic sur une des tab, on appelle notre m??thode setContent pour pouvoir changer le contenu
    tabsTuteurs.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    // d??finition du label de civilite, et alimentation en valeurs de l'enum Civilite
    civiliteTuteur.setLabel("Civilit??");
    civiliteTuteur.setItems(Civilite.values());

    // Remplissage de nos select
    niveauDiplome.setLabel("Niveau de dipl??me");
    niveauDiplome.setItems(1, 2, 3, 4, 5, 6, 7, 8);

    // on passe les entreprises sur la ComboBox pour pouvoir s??lectionner une entreprise existante
    entreprise.setItems(entreprises);
    entreprise.setItemLabelGenerator(Entreprise::getEnseigne);

    // date picker I18n qui permet de taper ?? la main une date au format fran??ais
    // (si l'utilisateur veut se passer de l'utilisation du calendrier int??gr??)
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");
    dateNaissanceTuteur.setI18n(multiFormatI18n);
    dateNaissanceTuteur.isRequired();

    modaliteFormation.setLabel("Modalit?? de formation");

    // grille des habilitations du tuteur
    tuteurHabilitations.addClassName("tuteur-habilitation-grid");
    tuteurHabilitations.addColumn(th -> th.getDateFormation() != null ? frenchDateFormater(th.getDateFormation()) : "").setHeader("Date de formation").setSortable(true);
    tuteurHabilitations.addColumn(th -> th.getDateHabilitation() != null ? frenchDateFormater(th.getDateHabilitation()) : "").setHeader("Date d'habilitation").setSortable(true);
    tuteurHabilitations.addColumn(th -> th.getFormation() != null ? th.getFormation().getCodeFormation() : "").setHeader("Formation");
    tuteurHabilitations.addColumn(TuteurHabilitation::getStatutFormation).setHeader("Statut de formation").setSortable(true);
    tuteurHabilitations.addColumn(TuteurHabilitation::getModaliteFormation).setHeader("Modalit?? de formation").setSortable(true);
    // ajout d'un bouton d'??dition pour pouvoir modifier une habilitation
    tuteurHabilitations.addComponentColumn(tuteur -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      editHabilitationTuteur(tuteur);
    })).setHeader("??diter");
    // ajout d'un bouton de suppression pour pouvoir supprimer une habilitation
    tuteurHabilitations.addComponentColumn(tuteur -> new Button(new Icon(VaadinIcon.TRASH), click -> {
      deleteHabilitationTuteur(tuteur);
    })).setHeader("Supprimer");
    tuteurHabilitations.getColumns().forEach(col -> col.setAutoWidth(true));

    // ajout des champs et des boutons d'action dans le formulaire
    form.add(nomTuteur, prenomTuteur, dateNaissanceTuteur, civiliteTuteur, emailTuteur, telephoneTuteur1, telephoneTuteur2, diplomeEleveObtenu, niveauDiplome,
            posteOccupe, anneeExperienceProfessionnelle, entreprise, observationsTuteur, casierJudiciaireFourni, diplomeFourni,
            certificatTravailFourni, cvFourni, createButtonsLayout());

    // --- configuration champs et formulaire d'ajout d'habilit??s ---
    // ajout du binder pour le formulaire d'ajout
    tuteurHabilitationBinder.bindInstanceFields(this);

    // ajout des valeurs possibles sur le select du statut de formation
    statutFormation.setLabel("Statut de la Formation");
    statutFormation.setItems(StatutFormation.getStatutFormationStr());

    dateFormation.setI18n(multiFormatI18n);
    dateHabilitation.setI18n(multiFormatI18n);

    // On propose au d??part toutes les formations pour le champ de s??lection d'habilitation
    formation.setItems(allFormations);
    formation.setItemLabelGenerator(Formation::getLibelleFormation);

    // ajout des champs dans le formulaire d'ajout d'habilit??s au tuteur
    formNewHabilitation.add(statutFormation, dateFormation, formation, modaliteFormation, dateHabilitation,
            observations, createSaveHabilitationButtonLayout());

    // contenu qui sera affich?? en dessous des tabs, qui change en fonction de la tab s??lectionn??
    content = new VerticalLayout();
    content.setSpacing(false);
    // ?? l'ouverture, on ouvre les informations g??n??rales du tuteur
    setContent(tuteursInfosTab);

    add(tabsTuteurs, content);
  }

  // Configuration des boutons de sauvegarde et de fermeture de la modale du tuteur
  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // ??v??nements save, delete, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new TuteurNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide (si champs obligatoires remplis)
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // ajout des ??v??nements relatifs aux boutons sauvegarder et effacer dans le formulaire des habilitations
  private HorizontalLayout createSaveHabilitationButtonLayout() {
    saveNewHabilitation.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    effacerChamps.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // lancement d'??v??nement au clic sur les boutons
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
        // on r??cup??re la liste des habilitations du tuteur
        tuteurHabilitationsList = tuteur.getTuteurHabilitations();
        // on utilise cette liste pour remplire la grille
        tuteurHabilitations.setItems(tuteurHabilitationsList);
        // on rend l'onglet des habilitation visibles (car ??dition et pas cr??ation)
        tuteursHabilitationsTab.setVisible(true);
        // on met le champ de s??lection de formation ??ditable, si jamais il ??tait verouill?? par une pr??c??dente ??dition
        formation.setReadOnly(false);
        // m??thode affichera dans le champ de s??lection de formation que les formations pour lesquelles le tuteur n'est pas habilit??
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
    // par d??faut, on d??finit une nouvelle habilitation dans le formulaire
    tuteurHabilitation = new TuteurHabilitation();
    // on associe le binder ?? cette potentielle nouvelle habilitation
    tuteurHabilitationBinder.readBean(tuteurHabilitation);
  }

  // fonction qui v??rifie que le bean a bien un tuteur, avant de lancer l'event de sauvegarde
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

  // sauvegarde de l'habilitation (nouveau et ??dition)
  private void validateHabilitationAndSave() {
    try {
      tuteurHabilitationBinder.writeBean(tuteurHabilitation);
      if (tuteurHabilitation != null) {
        tuteurHabilitation.setTuteur(tuteur);
        // cas o?? l'on cr???? une nouvelle hablitation
        if (tuteurHabilitation.getId() == null) {
          tuteurService.saveTuteurHabilitation(tuteurHabilitation);
          logEnregistrmentService.saveLogAjoutString(tuteurHabilitation.toString());
          fireEvent(new TuteurNewOrEdit.CloseAndReloadEvent(this));
          Notification.show("Habilitation ajout??e");
        } else {
          // cas o?? l'on modifie une habilitation
          if (!tuteurHabilitationOldValues.equals(tuteurHabilitation)) {
            // on ne cr???? un log de modification que s'il y a eu des valeurs de chang??es
            logEnregistrmentService.saveLogEditString(tuteurHabilitationOldValues.toString(), tuteurHabilitation.toString());
            tuteurService.saveTuteurHabilitation(tuteurHabilitation);
            tuteurHabilitationOldValues = null;
            fireEvent(new TuteurNewOrEdit.CloseAndReloadEvent(this));
            Notification.show("Habilitation modifi??e");
          } else {
            // sinon on signale que les valeurs n'ont pas chang??s
            Notification.show("Les valeurs sont les m??mes qu'avant !");
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
      Notification.show("Habilitation supprim??e");
    }
  }

  // On d??finit que les formations qui doivent appara??tre dans le select pour ajouter
  // une habilitation doivent ??tre celles pour lesquelles le tuteur n'est pas encore habilit??
  private void selectFormationNonHabilite(List<TuteurHabilitation> tuteurHabilitationListe) {
    List<Long> idFormationsHabilites = new ArrayList<Long>();
    if (!tuteurHabilitationListe.isEmpty()) {
      for (TuteurHabilitation tuteurHabilitation : tuteurHabilitationListe) {
        idFormationsHabilites.add(tuteurHabilitation.getFormation().getId());
      }
      formationsNonHabilites = formationService.getAllFormationNonHabilite(idFormationsHabilites);
    } else {
      // si aucune habilitation, on r??cup??re toutes les formations
      formationsNonHabilites = formationService.findAllFormations("");
    }
    formation.setItems(formationsNonHabilites);
    formation.setItemLabelGenerator(Formation::getLibelleFormation);
  }

  // fonction qui va afficher les informations de l'habilitation sur laquelle on a cliqu?? dans la grid dans
  // le formulaire d'??dition pour pouvoir modifier l'habilitation
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

  // appel??e au clic sur "effacer", met ?? vide les champs du formulaire.
  private void effacerChampsFormHabilitation() {
    this.tuteurHabilitationOldValues = null;
    this.tuteurHabilitation = new TuteurHabilitation();
    tuteurHabilitationBinder.readBean(tuteurHabilitation);
    formation.setReadOnly(false);
  }

  private void setContent(Tab tab) {
    // au d??but on enl??ve tout le contenu avant remplacement
    content.removeAll();
    // on ins??re le contenu ad??quat en fonction de la tab s??lectionn??e
    if (tab.equals(tuteursInfosTab)) {
      content.add(form);
    } else if (tab.equals(tuteursHabilitationsTab)) {
      content.add(formNewHabilitation, tuteurHabilitations);
    }
  }

  private void closeDialog() {
    // on enl??ve l'??ventuel focus sur l'onglet des infos du tuteur.
    if (tuteursInfosTab.isEnabled()) {
      tuteursInfosTab.setSelected(false);
    }
    // on appelle l'??v??nement pour fermer la modale.
    fireEvent(new TuteurNewOrEdit.CloseEvent(this));
  }

  // Event "global" (class m??re), qui ??tend les trois event ci-dessous, dont le but est de fournir le tuteur
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

  // Event au clic sur le bouton de sauvegarde qui r??cup??re le tuteur du formulaire (classe fille)
  public static class SaveEvent extends TuteurNewOrEdit.TuteurFormEvent {
    SaveEvent(TuteurNewOrEdit source, Tuteur tuteur) {
      super(source, tuteur, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui r??cup??re le tuteur du formulaire (classe fille)
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
