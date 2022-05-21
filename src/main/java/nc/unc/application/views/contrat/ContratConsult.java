package nc.unc.application.views.contrat;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.*;
import nc.unc.application.data.enums.CodeContrat;

public class ContratConsult extends Dialog {

  private Contrat contrat;

  // Layout qui contiendra le contenu en dessous des tabs
  private VerticalLayout content = new VerticalLayout();

  // Layout qui contiendra les liens vers la prévisualisation et le téléchargement d'un contrat
  private HorizontalLayout lienContainer = new HorizontalLayout();
  private Anchor lienPreview = new Anchor("unc.nc", "Consulter le contrat");
  private Anchor lienDownloadPdf = new Anchor("unc.nc", "Télécharger le contrat");

  // form qui contiendra les informations générales relatives au contrat
  private final FormLayout form = new FormLayout();

  private final H3 titre = new H3("Consultation d'un contrat");

  private final Div infosContrat = new Div(new H4("Informations liées au contrat"));
  private final DatePicker debutContrat = new DatePicker("Date de début du contrat");
  private final DatePicker finContrat = new DatePicker("Date de fin du contrat");
  private final TextField typeContrat = new TextField("Type du Contrat");
  private final IntegerField dureePeriodeEssai = new IntegerField("Durée de la période d'essai (nombre de semaines)");
  private final TextField numeroConventionFormation = new TextField("Numéro de la convention de Formation");
  private final TextField primeAvantageNature = new TextField("Prime ou Avantage(s) en nature");
  private final Div decua = new Div(new H4("DECUA"));
  private final DatePicker dateReceptionDecua = new DatePicker("Date de réception du DECUA");
  private final DatePicker dateEnvoiRpDecua = new DatePicker("Date d'envoi au référent pédagogique du DECUA pour validation");
  private final DatePicker dateRetourRpDecua = new DatePicker("Date de retour validation du DECUA par le référent pédagogique");
  private final Div retourCuaEtConvention = new Div(new H4("Retour CUA et convention signée"));
  private final DatePicker dateEnvoiEmailCuaConvention = new DatePicker("Date de réception des originaux");
  private final DatePicker dateDepotAlfrescoCuaConvSigne = new DatePicker("Date de dépôt Alfresco");
  private final Div convention = new Div(new H4("Convention"));
  private final DatePicker dateReceptionOriginauxConvention = new DatePicker("Date de réception des originaux");
  private final H5 remiseExemplaireConv = new H5("Remise d'exemplaires de la convention (originale)");
  private final Checkbox convOriginaleRemisEtudiant = new Checkbox("Remis à l'étudiant");
  private final Checkbox convOriginaleRemisTuteur = new Checkbox("Remis au tuteur");
  private final Checkbox convOriginaleRemisEmployeur = new Checkbox("Remis à l'employeur");
  private final Div lea = new Div(new H4("Formation LEA"));
  private final DatePicker formationLea = new DatePicker("Date de formation au LEA");
  // binder qui sera utilisé pour remlir automatiquement les champs d'infos propres au contrat
  Binder<Contrat> contratBinder = new BeanValidationBinder<>(Contrat.class);

  // form qui contiendra les informations relatives à la dérogation d'âge et du représentant légal
  private final FormLayout derogationAgeRepresentantLegalForm = new FormLayout();
  private final Div representantLegal = new Div(new H4("Dérogation d'âge et Représentant Légal du salarié"));
  private final Checkbox derogationAge = new Checkbox("Dérogation d'âge");
  private final DatePicker dateDelivranceDerogationAge = new DatePicker("Date de délivrance de la dérogation d'âge");
  private final TextField nomRepresentantLegal = new TextField("NOM représentant légal");
  private final TextField prenomRepresentantLegal = new TextField("Prénom représentant légal");
  private final TextField relationAvecSalarie = new TextField("Relation du représentant avec le salarié");
  private final TextField adresseRepresentant = new TextField("Adresse représentant légal");
  private final IntegerField codePostalRepresentant = new IntegerField("Code postal représentant légal");
  private final TextField communeRepresentant = new TextField("Commune du représentant légal");
  private final IntegerField telephoneRepresentant = new IntegerField("Téléphone du représentant légal");
  private final EmailField emailRepresentant = new EmailField("Email du représentant légal");

  // form qui contiendra les informations relatives à la rupture du contrat
  private final FormLayout ruptureContainer = new FormLayout();
  private final Div rupture = new Div(new H4("Rupture"));
  private final DatePicker dateRupture = new DatePicker("Date de rupture du contrat");
  private final TextArea motifRupture = new TextArea("Motif de la rupture");

  // form qui contiendra les informations de l'avenant
  private final FormLayout avenantContainer = new FormLayout();
  private final Div avenant = new Div(new H4("Informations Avenant"));
  private final IntegerField numeroAvenant = new IntegerField("Numéro de l'avenant");
  private final TextArea motifAvn = new TextArea("Motif de l'avenant");
  private final Div suiviAvenantCua = new Div(new H5("Suivi Avenant CUA"));
  private final DatePicker dateMailOuRdvSignatureCuaAvn = new DatePicker("Date mail ou RDV pour signature");
  private final DatePicker dateDepotAlfrescoCuaAvn = new DatePicker("Date de dépôt sur Alfresco");
  private final Div suiviAvenantConv = new Div(new H5("Suivi Avenant Convention"));
  private final DatePicker dateMailOuRdvSignatureConvAvn = new DatePicker("Date mail ou RDV pour signature");
  private final DatePicker dateDepotAlfrescoConvAvn = new DatePicker("Date de dépôt sur Alfresco");
  private final H5 remiseExemplaireConvAvn = new H5("Remise d'exemplaires de la convention (avenant)");
  private final Checkbox convAvenantRemisEtudiant = new Checkbox("Remis à l'étudiant");
  private final Checkbox convAvenantRemisTuteur = new Checkbox("Remis au tuteur");
  private final Checkbox convAvenantRemisEmployeur = new Checkbox("Remis à l'employeur");

  // form qui contiendra les informations relatives à l'étudiant lié au contrat
  private final FormLayout formContratEtudiant = new FormLayout();
  private final TextField nomEtudiant = new TextField("NOM");
  private final TextField prenomEtudiant = new TextField("Prénom");
  private final IntegerField numeroEtudiant = new IntegerField("N° Étudiant");
  private final TextField situationEntreprise = new TextField("Situation en entreprise");
  private final IntegerField telephoneEtudiant1 = new IntegerField("Téléphone 1");
  private final EmailField emailEtudiant = new EmailField("Email");
  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales sur l'étudiant
  Binder<Etudiant> etudiantBinder = new BeanValidationBinder<>(Etudiant.class);

  // Champs du formulaire relatif aux informations de la formation lié au contrat
  private final FormLayout formContratFormation = new FormLayout();
  private final TextField libelleFormation = new TextField("Libellé de la formation");
  private final TextField codeFormation = new TextField("Code de la formation");
  private final TextField codeRome = new TextField("Code ROME de la formation");
  private final IntegerField niveauCertificationProfessionnelle = new IntegerField("Niveau de la certification professionnelle");
  // Binder qui sera utilisé pour remplir automatiquement les champs de formation
  Binder<Formation> formationBinder = new BeanValidationBinder<>(Formation.class);

  // form qui contiendra les informations relatives à l'entreprise stipulée dans le contrat
  private final FormLayout formContratEntrepriseInfos = new FormLayout();
  private final TextField enseigne = new TextField("Enseigne");
  private final TextField raisonSociale = new TextField("Raison Sociale");
  private final TextField statutActifEntreprise = new TextField("Statut de l'entreprise");
  private final IntegerField telephoneContactCfa = new IntegerField("Téléphone contact CFA");
  // binder qui sera utilisé pour remplir automatiquement les champs de l'entreprise liée au contrat
  Binder<Entreprise> entrepriseBinder = new BeanValidationBinder<>(Entreprise.class);

  // Champs du formulaire relatifs aux informations du tuteur lié au contrat
  private final FormLayout formContratTuteur = new FormLayout();
  private final TextField nomTuteur = new TextField("NOM");
  private final TextField prenomTuteur = new TextField("Prenom");
  private final EmailField emailTuteur = new EmailField("Email");
  private final IntegerField telephoneTuteur1 = new IntegerField("Téléphone 1");
  private final IntegerField telephoneTuteur2 = new IntegerField("Téléphone 2");
  // Binder qui sera utilisé pour remplir automatiquement les champs du tuteur
  Binder<Tuteur> tuteurBinder = new BeanValidationBinder<>(Tuteur.class);

  // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
  private final Tab contratInfosTab = new Tab(VaadinIcon.NEWSPAPER.create(), new Span("Contrat"));
  private final Tab etudiantContratInfosTab = new Tab(VaadinIcon.ACADEMY_CAP.create(),new Span("Étudiant"));
  private final Tab entrepriseContratInfosTab = new Tab(VaadinIcon.WORKPLACE.create(),new Span("Entreprise"));
  private final Tab tuteurContratInfosTab = new Tab(VaadinIcon.USER.create(), new Span("Tuteur"));
  private final Tab formationContratInfosTab = new Tab(VaadinIcon.DIPLOMA.create(), new Span("Formation"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer le contrat");


  public ContratConsult() {
    this.setWidth("85vw");
    this.setHeight("90vh");

    // fonction qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
    setAllFieldsToReadOnly();

    // instanciation des différents binder qui serviront au remplissage automatique des formulaires d'informations rattachés à l'étudiant
    contratBinder.bindInstanceFields(this);
    etudiantBinder.bindInstanceFields(this);
    entrepriseBinder.bindInstanceFields(this);
    tuteurBinder.bindInstanceFields(this);
    formationBinder.bindInstanceFields(this);

    Tabs tabsContrat = new Tabs(contratInfosTab, etudiantContratInfosTab, formationContratInfosTab, tuteurContratInfosTab,
            entrepriseContratInfosTab, formationContratInfosTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsContrat.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    // on ouvre la page du contrat à générer dans un pdf dans un nouvel onglet
    lienPreview.setTarget("_blank");
    lienDownloadPdf.setTarget("_blank");

    // on met les liens dans des Div, qu'on met ensuite dans notre HorizontalLayout
    lienContainer.add(new Div(lienPreview), new Div(lienDownloadPdf));

    // ajout des éléments au formulaire principal
    form.add(infosContrat, new Div(), debutContrat, finContrat, typeContrat, dureePeriodeEssai, numeroConventionFormation, primeAvantageNature,
            decua, new Div(), dateReceptionDecua, dateEnvoiRpDecua,
            dateRetourRpDecua, new Div(), retourCuaEtConvention, new Div(), dateEnvoiEmailCuaConvention, dateDepotAlfrescoCuaConvSigne, convention, new Div(),
            dateReceptionOriginauxConvention, new Div(), remiseExemplaireConv, new Div(),
            convOriginaleRemisEtudiant, convOriginaleRemisTuteur, convOriginaleRemisEmployeur, new Div(),
            lea, new Div(), formationLea);

    // ajout des petits formulaires qui seront présents ou non dans la tab principale selon qu'ils sont renseignés ou non
    derogationAgeRepresentantLegalForm.add(representantLegal, new Div(), derogationAge, dateDelivranceDerogationAge, nomRepresentantLegal,
            prenomRepresentantLegal, relationAvecSalarie, adresseRepresentant, codePostalRepresentant, communeRepresentant,
            telephoneRepresentant, emailRepresentant);
    ruptureContainer.add(rupture, new Div(), motifRupture, dateRupture);
    avenantContainer.add(avenant, new Div(), numeroAvenant, motifAvn, suiviAvenantCua, new Div(), dateMailOuRdvSignatureCuaAvn, dateDepotAlfrescoCuaAvn,
            suiviAvenantConv, new Div(), dateMailOuRdvSignatureConvAvn, dateDepotAlfrescoConvAvn, remiseExemplaireConvAvn, new Div(),
            convAvenantRemisEtudiant, convAvenantRemisTuteur, convAvenantRemisEmployeur);

    // ajout des petits formulaires qui seront dans les autres tab
    formContratEtudiant.add(prenomEtudiant, nomEtudiant, numeroEtudiant, situationEntreprise, telephoneEtudiant1, emailEtudiant);
    formContratFormation.add(libelleFormation, codeFormation, codeRome, niveauCertificationProfessionnelle);
    formContratEntrepriseInfos.add(enseigne, raisonSociale, statutActifEntreprise, telephoneContactCfa);
    formContratTuteur.add(prenomTuteur, nomTuteur, emailTuteur, telephoneTuteur1, telephoneTuteur2);

    // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
    content.setSpacing(false);
    // à l'ouverture, on ouvre la tab d'infos générales sur le contrat
    setContent(contratInfosTab);

    add(tabsContrat, titre, lienContainer, content, createButtonsLayout());
  }

  public void setContrat(Contrat contrat) {
    this.contrat = contrat;
    if (contrat != null) {
      // on passe l'id du contrat pour la page de generation pdf du contrat
      lienPreview.setHref("/contrat-generation/"+contrat.getId());
      lienDownloadPdf.setHref("/contrat-generation/download/"+contrat.getId());

      // on affiche le layout avec les liens pour les contrats que si celui-ci contient étudiant, formation, entreprise et tuteur
      lienContainer.setVisible(contrat.getEtudiant() != null && contrat.getFormation() != null && contrat.getEntreprise() != null && contrat.getTuteur() != null);

      showOrNotRuptureForm();
      showOrNotDerogationAgeForm();
      showOrNotAvenantForm();

      // lecture des binder pour compléter les champs dans les différents formulaires
      contratBinder.readBean(contrat);
      etudiantBinder.readBean(contrat.getEtudiant());
      entrepriseBinder.readBean(contrat.getEntreprise());
      tuteurBinder.readBean(contrat.getTuteur());
      formationBinder.readBean(contrat.getFormation());
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // évènements delete et close
    delete.addClickListener(event -> fireEvent(new ContratConsult.DeleteEventConsult(this, contrat)));
    close.addClickListener(event -> fireEvent(new ContratConsult.CloseEventConsult(this)));

    return new HorizontalLayout(delete, close);
  }

  // méthode qui définit quel contenu mettre en dessous des tabs
  private void setContent(Tab tab) {
    // au début on enlève tout le contenu avant remplacement
    content.removeAll();
    // on insère le contenu (formulaire) adéquat en fonction de la tab sélectionnée
    if (tab.equals(contratInfosTab)) {
      content.add(form, ruptureContainer, avenantContainer, derogationAgeRepresentantLegalForm);
    } else if (tab.equals(etudiantContratInfosTab)) {
      content.add(formContratEtudiant);
    } else if (tab.equals(formationContratInfosTab)) {
      content.add(formContratFormation);
    } else if (tab.equals(entrepriseContratInfosTab)) {
      content.add(formContratEntrepriseInfos);
    } else if (tab.equals(tuteurContratInfosTab)) {
      content.add(formContratTuteur);
    }
  }

  // fonction qui vérifie si le contrat a déjà des informations concernant la rupture du contrat
  private void showOrNotRuptureForm() {
    // si rupture (déterminé par la présence de la date de rupture),
    // on affiche les champs du formulaire de rupture et on cache le bouton d'ajout de rupture sinon on fait l'inverse
    ruptureContainer.setVisible(this.contrat.getDateRupture() != null);
  }

  // fonction qui vérifie si le contrat a déjà des informations concernant la dérogation d'âge sur le contrat
  private void showOrNotDerogationAgeForm() {
    derogationAgeRepresentantLegalForm.setVisible(this.contrat.getDerogationAge() || this.contrat.getRelationAvecSalarie() != null);
  }

  // fonction qui vérifie si le contrat est un contrat original ou un parent et affiche les informations d'avenant en conséquence
  private void showOrNotAvenantForm() {
    avenantContainer.setVisible(this.contrat.getCodeContrat() == CodeContrat.AVENANT);
  }

  // Méthode qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
  private void setAllFieldsToReadOnly() {
    // contrat
    typeContrat.setReadOnly(true);
    nomRepresentantLegal.setReadOnly(true);
    prenomRepresentantLegal.setReadOnly(true);
    relationAvecSalarie.setReadOnly(true);
    adresseRepresentant.setReadOnly(true);
    codePostalRepresentant.setReadOnly(true);
    communeRepresentant.setReadOnly(true);
    telephoneRepresentant.setReadOnly(true);
    emailRepresentant.setReadOnly(true);
    derogationAge.setReadOnly(true);
    dateDelivranceDerogationAge.setReadOnly(true);
    debutContrat.setReadOnly(true);
    finContrat.setReadOnly(true);
    dureePeriodeEssai.setReadOnly(true);
    numeroConventionFormation.setReadOnly(true);
    primeAvantageNature.setReadOnly(true);
    dateReceptionDecua.setReadOnly(true);
    dateEnvoiRpDecua.setReadOnly(true);
    dateRetourRpDecua.setReadOnly(true);
    dateEnvoiEmailCuaConvention.setReadOnly(true);
    dateDepotAlfrescoCuaConvSigne.setReadOnly(true);
    dateReceptionOriginauxConvention.setReadOnly(true);
    convOriginaleRemisEtudiant.setReadOnly(true);
    convOriginaleRemisTuteur.setReadOnly(true);
    convOriginaleRemisEmployeur.setReadOnly(true);
    formationLea.setReadOnly(true);
    dateRupture.setReadOnly(true);
    motifRupture.setReadOnly(true);
    numeroAvenant.setReadOnly(true);
    motifAvn.setReadOnly(true);
    dateMailOuRdvSignatureCuaAvn.setReadOnly(true);
    dateDepotAlfrescoCuaAvn.setReadOnly(true);
    dateMailOuRdvSignatureConvAvn.setReadOnly(true);
    dateDepotAlfrescoConvAvn.setReadOnly(true);
    convAvenantRemisEtudiant.setReadOnly(true);
    convAvenantRemisTuteur.setReadOnly(true);
    convAvenantRemisEmployeur.setReadOnly(true);

    // etudiant
    prenomEtudiant.setReadOnly(true);
    nomEtudiant.setReadOnly(true);
    numeroEtudiant.setReadOnly(true);
    situationEntreprise.setReadOnly(true);
    telephoneEtudiant1.setReadOnly(true);
    emailEtudiant.setReadOnly(true);

    // formation
    libelleFormation.setReadOnly(true);
    codeFormation.setReadOnly(true);
    codeRome.setReadOnly(true);
    niveauCertificationProfessionnelle.setReadOnly(true);

    // tuteur
    nomTuteur.setReadOnly(true);
    prenomTuteur.setReadOnly(true);
    telephoneTuteur1.setReadOnly(true);
    telephoneTuteur2.setReadOnly(true);
    emailTuteur.setReadOnly(true);

    // entreprise
    enseigne.setReadOnly(true);
    raisonSociale.setReadOnly(true);
    statutActifEntreprise.setReadOnly(true);
    telephoneContactCfa.setReadOnly(true);
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir le contrat
  // qu'on manipule dans le formulaire
  public static abstract class ContratConsultFormEvent extends ComponentEvent<ContratConsult> {
    private final Contrat contrat;

    protected ContratConsultFormEvent(ContratConsult source, Contrat contrat) {
      super(source, false);
      this.contrat = contrat;
    }

    public Contrat getContrat() {
      return contrat;
    }
  }

  // Event au clic sur le bouton de suppression (classe fille)
  public static class DeleteEventConsult extends ContratConsultFormEvent {
    DeleteEventConsult(ContratConsult source, Contrat contrat) {
      super(source, contrat);
    }
  }

  // Event au clic sur le bouton de fermeture du formulaire (classe fille)
  public static class CloseEventConsult extends ContratConsultFormEvent {
    CloseEventConsult(ContratConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
