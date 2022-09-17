package nc.unc.application.views.contrat;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.*;
import nc.unc.application.data.enums.CodeContrat;
import nc.unc.application.data.enums.Commune;
import nc.unc.application.data.service.ContratService;

import java.util.List;
import java.util.Objects;

public class ContratNewOrEdit extends Dialog {

  private Contrat contrat;
  private Contrat cloneContrat;

  private final ContratView contratView;

  FormLayout form = new FormLayout();

  ComboBox<Etudiant> etudiant = new ComboBox<>("Étudiant");
  ComboBox<Formation> formation = new ComboBox<>("Formation");
  ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");

  HorizontalLayout layoutTuteur = new HorizontalLayout();
  VerticalLayout plus = new VerticalLayout();
  VerticalLayout tuteurs = new VerticalLayout();
  ComboBox<Tuteur> tuteur = new ComboBox<>("Tuteur");
  Icon addTuteurButton = new Icon(VaadinIcon.PLUS);

  ContratService contratService;

  H3 titre = new H3();

  FormLayout derogationAgeRepresentantLegalForm = new FormLayout();
  Div representantLegal = new Div(new H4("Dérogation d'âge et Représentant Légal du salarié"));
  TextField  nomRepresentantLegal = new TextField("NOM représentant légal");
  TextField prenomRepresentantLegal = new TextField("Prénom représentant légal");
  Select<String> relationAvecSalarie = new Select<>("Père", "Mère", "Tuteur");
  TextField adresseRepresentant = new TextField("Adresse représentant légal");
  IntegerField codePostalRepresentant = new IntegerField("Code postal représentant légal");
  ComboBox<String> communeRepresentant = new ComboBox<>("Commune du représentant légal");
  IntegerField telephoneRepresentant = new IntegerField("Téléphone du représentant légal");
  EmailField emailRepresentant = new EmailField("Email du représentant légal");
  Checkbox derogationAge = new Checkbox("Dérogation d'âge");
  DatePicker dateDelivranceDerogationAge = new DatePicker("Date de délivrance de la dérogation d'âge");

  Div infosContrat = new Div(new H4("Informations liées au contrat"));
  Select<String> typeContrat = new Select<>("","1","2","3","4");
  DatePicker debutContrat = new DatePicker("Date de début du contrat");
  DatePicker finContrat = new DatePicker("Date de fin du contrat");
  IntegerField dureePeriodeEssai = new IntegerField("Durée de la période d'essai (nombre de semaines)");

  TextField numeroConventionFormation = new TextField("Numéro de la convention de Formation");
  DatePicker dateConventionFormation = new DatePicker("Date de la convention");
  TextField primeAvantageNature = new TextField("Prime ou Avantage(s) en nature");

  Div decua = new Div(new H4("DECUA"));
  DatePicker dateReceptionDecua = new DatePicker("Date de réception du DECUA");
  DatePicker dateEnvoiRpDecua = new DatePicker("Date d'envoi au référent pédagogique du DECUA pour validation");
  DatePicker dateRetourRpDecua = new DatePicker("Date de retour validation du DECUA par le référent pédagogique");

  Div retourCuaEtConvention = new Div(new H4("Retour CUA et convention signée"));
  DatePicker dateEnvoiEmailCuaConvention = new DatePicker("Date de réception des originaux");
  DatePicker dateDepotAlfrescoCuaConvSigne = new DatePicker("Date de dépôt Alfresco");

  Div convention = new Div(new H4("Convention"));
  DatePicker dateReceptionOriginauxConvention = new DatePicker("Date de réception des originaux");
  H5 remiseExemplaireConv = new H5("Remise d'exemplaires de la convention (originale)");
  Checkbox convOriginaleRemisEtudiant = new Checkbox("Remis à l'étudiant");
  Checkbox convOriginaleRemisTuteur = new Checkbox("Remis au tuteur");
  Checkbox convOriginaleRemisEmployeur = new Checkbox("Remis à l'employeur");

  Div lea = new Div(new H4("Formation LEA"));
  DatePicker formationLea = new DatePicker("Date de formation au LEA");

  FormLayout ruptureContainer = new FormLayout();
  Div rupture = new Div(new H4("Rupture"));
  DatePicker dateRupture = new DatePicker("Date de rupture du contrat");
  TextArea motifRupture = new TextArea("Motif de la rupture");

  FormLayout avenantContainer = new FormLayout();
  Div avenant = new Div(new H4("AVENANT"));
  IntegerField numeroAvenant = new IntegerField("Numéro de l'avenant");
  TextArea motifAvn = new TextArea("Motif de l'avenant");
  Div suiviAvenantCua = new Div(new H5("Suivi Avenant CUA"));
  DatePicker dateMailOuRdvSignatureCuaAvn = new DatePicker("Date mail ou RDV pour signature");
  DatePicker dateDepotAlfrescoCuaAvn = new DatePicker("Date de dépôt sur Alfresco");
  Div suiviAvenantConv = new Div(new H5("Suivi Avenant Convention"));
  DatePicker dateMailOuRdvSignatureConvAvn = new DatePicker("Date mail ou RDV pour signature");
  DatePicker dateDepotAlfrescoConvAvn = new DatePicker("Date de dépôt sur Alfresco");
  H5 remiseExemplaireConvAvn = new H5("Remise d'exemplaires de la convention (avenant)");
  Checkbox convAvenantRemisEtudiant = new Checkbox("Remis à l'étudiant");
  Checkbox convAvenantRemisTuteur = new Checkbox("Remis au tuteur");
  Checkbox convAvenantRemisEmployeur = new Checkbox("Remis à l'employeur");

  Binder<Contrat> binder = new BeanValidationBinder<>(Contrat.class);

  Button addRupture = new Button("+ Rupture");
  Button addDerogation = new Button("+ Dérogation d'âge");
  Button addAvenant = new Button("Créer un Avenant à partir de ce Contrat");

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public ContratNewOrEdit(ContratView contratView, List<Entreprise> entrepriseList, List<Formation> formationList, List<Etudiant> etudiantList,
                          List<Tuteur> tuteurList, ContratService contratService) {
    this.setWidth("85vw");
    this.setHeight("90vh");

    this.contratView = contratView;

    this.contratService = contratService;

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    // on set les éléments des différentes entités qui sont liées aux contrats
    etudiant.setRequired(true);
    etudiant.setItems(etudiantList);
    etudiant.setItemLabelGenerator(etudiant -> etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant());
    etudiant.setClearButtonVisible(true);

    formation.setRequired(true);
    formation.setItems(formationList);
    formation.setItemLabelGenerator(Formation::getLibelleFormation);
    formation.setClearButtonVisible(true);

    entreprise.setRequired(true);
    entreprise.setItems(entrepriseList);
    entreprise.setItemLabelGenerator(Entreprise::getEnseigne);
    entreprise.setClearButtonVisible(true);

    tuteur.setRequired(true);
    tuteur.setItems(tuteurList);
    tuteur.setItemLabelGenerator(tuteur1 -> tuteur1.getPrenomTuteur() + " " + tuteur1.getNomTuteur());
    tuteur.setClearButtonVisible(true);

    // définition d'un picker customisé pour toutes les dates
    setCustomDatePicker();

    // ajout de labels sur les select
    typeContrat.setLabel("Type du Contrat");
    relationAvecSalarie.setLabel("Relation du représentant avec le salarié");

    // on passe les communes de NC dans nos combo box en ayant besoin
    communeRepresentant.setItems(Commune.getCommunesStr());
    communeRepresentant.setClearButtonVisible(true);

    // numéro de l'avenant en ReadOnly, car à ne pas modifier, on le présente juste à caractère informatif
    numeroAvenant.setReadOnly(true);

    // ajout des éléments au formulaire principal
    FlexLayout content = new FlexLayout(tuteur);
    content.setFlexGrow(2, tuteur);
    content.setSizeFull();
    layoutTuteur.setSpacing(false);
    layoutTuteur.add(content,addTuteurButton);
    layoutTuteur.setAlignItems(FlexComponent.Alignment.CENTER);

    addTuteurButton.addClassNames("tuteurcontrat-button");


    form.add(etudiant, formation, entreprise, layoutTuteur,
            infosContrat, new Div(), debutContrat, finContrat, typeContrat, dureePeriodeEssai, numeroConventionFormation, dateConventionFormation, primeAvantageNature,
            new Div(), decua, new Div(), dateReceptionDecua, dateEnvoiRpDecua, dateRetourRpDecua, new Div(), retourCuaEtConvention, new Div(),
            dateEnvoiEmailCuaConvention, dateDepotAlfrescoCuaConvSigne, convention, new Div(), dateReceptionOriginauxConvention, new Div(),
            remiseExemplaireConv, new Div(), convOriginaleRemisEtudiant, convOriginaleRemisTuteur, convOriginaleRemisEmployeur,
            new Div(), lea, new Div(), formationLea);

    // ajout des petits formulaires correspondants à des champs qui seront cachés ou non selon les circonstances
    derogationAgeRepresentantLegalForm.add(representantLegal, new Div(), derogationAge, dateDelivranceDerogationAge, nomRepresentantLegal,
            prenomRepresentantLegal, relationAvecSalarie, adresseRepresentant, codePostalRepresentant, communeRepresentant,
            telephoneRepresentant, emailRepresentant);
    ruptureContainer.add(rupture, new Div(), motifRupture, dateRupture);
    avenantContainer.add(avenant, new Div(), numeroAvenant, motifAvn, suiviAvenantCua, new Div(), dateMailOuRdvSignatureCuaAvn, dateDepotAlfrescoCuaAvn,
            suiviAvenantConv, new Div(), dateMailOuRdvSignatureConvAvn, dateDepotAlfrescoConvAvn, remiseExemplaireConvAvn, new Div(),
            convAvenantRemisEtudiant, convAvenantRemisTuteur, convAvenantRemisEmployeur);

    // ajout des éléments à la modale
    this.add(titre, createButtonsForSpecialFormDisplay(), form, ruptureContainer, avenantContainer,
            derogationAgeRepresentantLegalForm, createButtonsLayout());
  }

  public void modifyTuteurs(List<Tuteur> tuteurs){
    tuteur.setItems(tuteurs);
  }

  private HorizontalLayout createButtonsForSpecialFormDisplay() {
    addRupture.addClickListener(event -> showOrNotRuptureForm(true));
    addDerogation.addClickListener(event -> showOrNotDerogationAgeForm(true));
    // on passe le cloneContrat en paramètre comme ça on est sûr qu'on passe un clone de l'élément "parent" qui n'a pas
    // été modifié depuis le clic sur le bouton d'ajout de l'avenant.
    addAvenant.addClickListener(event -> setNewAvenant(this.cloneContrat));

    return new HorizontalLayout(addRupture, addDerogation, addAvenant);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new ContratNewOrEdit.CloseEvent(this)));

    //Evènement sur le click sur le bouton ajouter un tuteur
    addTuteurButton.addClickListener(click -> contratView.addTuteur());

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()
            && !etudiant.isEmpty() && !formation.isEmpty() && !entreprise.isEmpty() && !tuteur.isEmpty()));

    return new HorizontalLayout(save, close);
  }

  // fonction qui va alimenter le binder d'un contrat
  public void setContrat(Contrat contrat) {
    titre.removeAll();
    this.contrat = contrat;
    // on doit remettre le cloneContrat à null (sinon garde ancienne valeur de l'edit)
    this.cloneContrat = null;
    // copie du contrat si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (contrat != null && contrat.getId() != null) {
      try {
        this.cloneContrat = (Contrat) contrat.clone();
        if (contrat.getCodeContrat() == CodeContrat.CONTRAT) {
          titre.add("Modification d'un contrat");
        } else {
          titre.add("Modification de l'avenant N°"+contrat.getNumeroAvenant());
          addAvenant.setText("Créer un nouvel Avenant à partir de cet Avenant");
        }
        showOrNotCreationAvenant();
        showOrNotRuptureForm(false);
        showOrNotDerogationAgeForm(false);
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    } else {
      // cas on l'on créé un nouveau contrat
      titre.add("Création d'un nouveau contrat");
      if (this.contrat != null && this.contrat.getId() == null) {
        // si nouveau contrat, on définit qu'il s'agit d'un contrat et pas d'un avenant
        this.contrat.setCodeContrat(CodeContrat.CONTRAT);
        // détermine si on affiche on non le formulaire de dérogation d'âge
        this.contrat.setDerogationAge(false);
        showOrNotDerogationAgeForm(false);
        // on n'affiche pas le bouton d'ajout de rupture de contrat et son formulaire et celui d'avenant si on créé le contrat
        ruptureContainer.setVisible(false);
        addRupture.setVisible(false);
        avenantContainer.setVisible(false);
        showOrNotCreationAvenant();
      }
    }

    // alimentation du binder
    binder.readBean(contrat);
  }

  /**
   * Création d'un avenant qui va reprendre les informations d'un contrat ou avenant existant que l'utilisateur pourra
   * modifier avant de sauvegarder ce nouvel avenant.
   * @param contratOrAvenant le contrat parent ou l'avenant depuis lequel on créé un nouvel avenant
   */
  public void setNewAvenant(Contrat contratOrAvenant) {
    titre.removeAll();
    try {
      // on clone le contrat ou l'avenant en paramètre
      this.contrat = (Contrat) contratOrAvenant.clone();
      // si le contrat en paramètre est bien un contrat, on le définit en tant que parent
      if (contratOrAvenant.getCodeContrat() == CodeContrat.CONTRAT) {
        this.contrat.setContratParent(contratOrAvenant);
        // cela implique que l'on créé le premier avenant, donc on définit le numéro d'avenant à 1
        this.contrat.setNumeroAvenant(1);
      } else { // mais si c'est un avenant en paramètre, on récupère son contrat parent et on définit que le nouvel avenant aura aussi ce parent
        this.contrat.setContratParent(contratOrAvenant.getContratParent());
        // on défini le numéro d'avenant à celui de l'avenant depuis lequel il est créé +1
        this.contrat.setNumeroAvenant(contratOrAvenant.getNumeroAvenant()+1);
      }
      // on set l'id à null, pour définir que cet avenant n'existe pas encore et qu'il sera nouveau
      this.contrat.setId(null);
      this.contrat.setCodeContrat(CodeContrat.AVENANT);
      this.contrat.setCreatedAt(null);
      this.contrat.setUpdatedAt(null);
      avenantContainer.setVisible(true);
      addAvenant.setVisible(false);
      titre.add("Création de l'avenant N°"+ this.contrat.getNumeroAvenant());
      Notification.show("Avenant prêt à être créé. Voir et compléter plus bas les champs d'avenant avant sauvegarde ↓");
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    // alimentation du binder
    binder.readBean(this.contrat);
  }

  // fonction qui vérifie que le bean a bien un contrat valide, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(contrat);
      if (this.cloneContrat == null) {
        fireEvent(new ContratNewOrEdit.SaveEvent(this, contrat));
      } else {
        fireEvent(new ContratNewOrEdit.SaveEditedEvent(this, contrat, cloneContrat));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // fonction qui vérifie si le contrat a déjà des informations concernant la rupture du contrat
  private void showOrNotRuptureForm(Boolean addRuptureButtonClicked) {
    // si rupture (déterminé par la présence de la date de rupture), ou au clic sur le bouton d'ajout de rupture,
    // on affiche les champs du formulaire de rupture et on cache le bouton d'ajout de rupture
    if (this.contrat.getDateRupture() != null || addRuptureButtonClicked) {
      ruptureContainer.setVisible(true);
      addRupture.setVisible(false);
      if (addRuptureButtonClicked) {
        Notification.show("Ajout des champs de Rupture du contrat. Voir plus bas ↓");
      }
    } else { // sinon on fait l'inverse
      ruptureContainer.setVisible(false);
      addRupture.setVisible(true);
    }
  }

  // fonction qui vérifie si le contrat a déjà des informations concernant la dérogation d'âge sur le contrat
  private void showOrNotDerogationAgeForm(Boolean addDerogationButtonClicked) {
    if (this.contrat.getDerogationAge() != null || this.contrat.getRelationAvecSalarie() != null || addDerogationButtonClicked) {
      if (this.contrat.getDerogationAge() || this.contrat.getRelationAvecSalarie() != null && this.contrat.getId() != null) {
        derogationAgeRepresentantLegalForm.setVisible(true);
        addDerogation.setVisible(false);
        if (addDerogationButtonClicked) {
          Notification.show("Ajout des champs de Dérogation d'âge et du Représentant Légal. Voir plus bas ↓");
        }
      } else {
        if (addDerogationButtonClicked) {
          derogationAgeRepresentantLegalForm.setVisible(true);
          addDerogation.setVisible(false);
          Notification.show("Ajout des champs de Dérogation d'âge et du Représentant Légal. Voir plus bas ↓");
        } else {
          derogationAgeRepresentantLegalForm.setVisible(false);
          addDerogation.setVisible(true);
        }
      }
    } else {
      derogationAgeRepresentantLegalForm.setVisible(false);
      addDerogation.setVisible(true);
    }
  }

  private void showOrNotCreationAvenant() {
    // on cache les informations d'avenant si on est sur un contrat original
    avenantContainer.setVisible(this.contrat.getCodeContrat() != CodeContrat.CONTRAT);

    // on affiche le bouton d'ajout d'un avenant si la liste des avenants est vide dans un contrat d'origine
    if (this.contrat.getCodeContrat() == CodeContrat.CONTRAT && this.contrat.getId() != null) {
      addAvenant.setVisible(this.contrat.getAvenants().isEmpty());
    } else {
      // mais si on est dans un avenant, on affiche le bouton d'ajout d'un avenant que si c'est le dernier avenant
      addAvenant.setVisible(Objects.equals(this.contrat.getNumeroAvenant(), contratService.countAllAvenantsFromParents(this.contrat)));
    }
  }

  public void setCustomDatePicker() {
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");

    dateDelivranceDerogationAge.setI18n(multiFormatI18n);

    debutContrat.setI18n(multiFormatI18n);
    finContrat.setI18n(multiFormatI18n);
    dateConventionFormation.setI18n(multiFormatI18n);
    dateRupture.setI18n(multiFormatI18n);
    dateReceptionDecua.setI18n(multiFormatI18n);

    dateEnvoiRpDecua.setI18n(multiFormatI18n);
    dateRetourRpDecua.setI18n(multiFormatI18n);

    dateEnvoiEmailCuaConvention.setI18n(multiFormatI18n);
    dateDepotAlfrescoCuaConvSigne.setI18n(multiFormatI18n);

    dateReceptionOriginauxConvention.setI18n(multiFormatI18n);

    dateMailOuRdvSignatureCuaAvn.setI18n(multiFormatI18n);
    dateDepotAlfrescoCuaAvn.setI18n(multiFormatI18n);
    dateMailOuRdvSignatureConvAvn.setI18n(multiFormatI18n);
    dateDepotAlfrescoConvAvn.setI18n(multiFormatI18n);

    formationLea.setI18n(multiFormatI18n);
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir le contrat
  // qu'on manipule dans le formulaire
  public static abstract class ContratFormEvent extends ComponentEvent<ContratNewOrEdit> {
    private final Contrat contrat;
    private final Contrat contratOriginal;

    protected ContratFormEvent(ContratNewOrEdit source, Contrat contrat, Contrat contratOriginal) {
      super(source, false);
      this.contrat = contrat;
      this.contratOriginal = contratOriginal;
    }

    public Contrat getContrat() {
      return contrat;
    }

    public Contrat getContratOriginal() {
      return contratOriginal;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'étudiant du formulaire (classe fille)
  public static class SaveEvent extends ContratNewOrEdit.ContratFormEvent {
    SaveEvent(ContratNewOrEdit source, Contrat contrat) {
      super(source, contrat, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'étudiant du formulaire et sa version originale (avant modification)
  public static class SaveEditedEvent extends ContratNewOrEdit.ContratFormEvent {
    SaveEditedEvent(ContratNewOrEdit source, Contrat contrat, Contrat contratOriginal) {
      super(source, contrat, contratOriginal);
    }
  }

  // Event au clic sur le bouton de fermeture du formulaire (classe fille)
  public static class CloseEvent extends ContratNewOrEdit.ContratFormEvent {
    CloseEvent(ContratNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
