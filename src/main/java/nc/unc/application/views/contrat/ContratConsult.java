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
  private final TextField codeContrat = new TextField("Code du Contrat");
  private final TextField typeContrat = new TextField("Type du Contrat");
  private final Div representantLegal = new Div(new H4("Représentant légal du salarié et dérogation d'âge"));
  private final TextField nomRepresentantLegal = new TextField("NOM représentant légal");
  private final TextField prenomRepresentantLegal = new TextField("Prénom représentant légal");
  private final TextField relationAvecSalarie = new TextField("Relation du représentant avec le salarié");
  private final TextField adresseRepresentant = new TextField("Adresse représentant légal");
  private final IntegerField codePostalRepresentant = new IntegerField("Code postal représentant légal");
  private final TextField communeRepresentant = new TextField("Commune du représentant légal");
  private final IntegerField telephoneRepresentant = new IntegerField("Téléphone du représentant légal");
  private final EmailField emailRepresentant = new EmailField("Email du représentant légal");
  private final Checkbox derogationAge = new Checkbox("Dérogation d'âge");
  private final DatePicker dateDelivranceDerogationAge = new DatePicker("Date de délivrance de la dérogation d'âge");
  private final Div cadreAdministration = new Div(new H4("Cadre réservé à l'administration"));
  private final TextField cadreAdminNumEnregistrementContrat = new TextField("Numéro d'enregistrement du contrat");
  private final TextField cadreAdminNumAvenant = new TextField("Numéro d'avenant");
  private final DatePicker cadreAdminRecuLe = new DatePicker("Reçu le");
  private final Div infosContrat = new Div(new H4("Informations liées au contrat"));
  private final DatePicker debutContrat = new DatePicker("Date de début du contrat");
  private final DatePicker finContrat = new DatePicker("Date de fin du contrat");
  private final TextField emploiOccupeSalarieEtudiant = new TextField("Emploi occupé par le salarié");
  private final TextField codeRomeEmploiOccupe = new TextField("Code ROME de l'emploi occupé");
  private final IntegerField dureePeriodeEssai = new IntegerField("Durée de la période d'essai (nombre de semaines)");
  private final TextField numeroConventionFormation = new TextField("Numéro de la convention de Formation");
  private final IntegerField semainesEntreprise = new IntegerField("Nombre de semaines en entreprise");
  private final IntegerField heuresFormation = new IntegerField("Nombre d'heures en formation");
  private final IntegerField semainesFormation = new IntegerField("Nombre de semaines en formation");
  private final TextField lieuFormation = new TextField("Lieu de la formation");
  private final IntegerField dureeHebdomadaireTravail = new IntegerField("Durée hebdomadaire de travail");
  private final Div decua = new Div(new H4("DECUA"));
  private final DatePicker dateReceptionDecua = new DatePicker("Date de réception du DECUA");
  private final DatePicker dateEnvoiRpDecua = new DatePicker("Date d'envoi au référent pédagogique du DECUA pour validation");
  private final DatePicker dateRetourRpDecua = new DatePicker("Date de retour validation du DECUA par le référent pédagogique");
  private final Div retourCuaEtConvention = new Div(new H4("Retour CUA et convention signée"));
  private final DatePicker dateEnvoiEmailCuaConvention = new DatePicker("Date de réception des originaux");
  private final DatePicker dateDepotAlfrescoCuaConvSigne = new DatePicker("Date de dépôt Alfresco");
  private final Div convention = new Div(new H4("Convention"));
  private final DatePicker dateReceptionOriginauxConvention = new DatePicker("Date de réception des originaux");
  private final TextField exemplaireOriginauxRemisAlternantOuEntreprise = new TextField("Exemplaires originaux (x3) remis à l'alternant ou à l'entreprise");
  private final Div lea = new Div(new H4("Formation LEA"));
  private final DatePicker formationLea = new DatePicker("Date de formation au LEA");
  // binder qui sera utilisé pour remlir automatiquement les champs d'infos propres au contrat
  Binder<Contrat> contratBinder = new BeanValidationBinder<>(Contrat.class);

  // form qui contiendra les informations relatives à la rupture du contrat
  private final FormLayout ruptureContainer = new FormLayout();
  private final Div rupture = new Div(new H4("Rupture"));
  private final DatePicker dateRupture = new DatePicker("Date de rupture du contrat");
  private final TextArea motifRupture = new TextArea("Motif de la rupture");

  // form qui contiendra les informations de l'avenant 1
  private final FormLayout avenant1Container = new FormLayout();
  private final Div avenant1 = new Div(new H4("Avenant N°1"));
  private final TextArea motifAvn1 = new TextArea("Motif de l'avenant");
  private final Div suiviAvenantCua1 = new Div(new H5("Suivi Avenant CUA N°1"));
  private final DatePicker dateMailOuRdvSignatureCuaAvn1 = new DatePicker("Date mail ou RDV pour signature");
  private final DatePicker dateDepotAlfrescoCuaAvn1 = new DatePicker("Date de dépôt sur Alfresco");
  private final Div suiviAvenantConv1 = new Div(new H5("Suivi Avenant Convention N°1"));
  private final DatePicker dateMailOuRdvSignatureConvAvn1 = new DatePicker("Date mail ou RDV pour signature");
  private final DatePicker dateDepotAlfrescoConvAvn1 = new DatePicker("Date de dépôt sur Alfresco");
  private final DatePicker dateRemiseOriginauxAvn1 = new DatePicker("Date de remise des exemplaires originaux (x3) à l'alternant");

  // form qui contiendra les informations de l'avenant 2
  private final FormLayout avenant2Container = new FormLayout();
  private final Div avenant2 = new Div(new H4("Avenant N°2"));
  private final TextArea motifAvn2 = new TextArea("Motif de l'avenant");
  private final Div suiviAvenantCua2 = new Div(new H5("Suivi Avenant CUA N°1"));
  private final DatePicker dateMailOuRdvSignatureCuaAvn2 = new DatePicker("Date mail ou RDV pour signature");
  private final DatePicker dateDepotAlfrescoCuaAvn2 = new DatePicker("Date de dépôt sur Alfresco");
  private final DatePicker dateRemiseOriginauxCuaAvn2 = new DatePicker("Date de remise des exemplaires originaux (x3) à l'alternant");
  private final Div suiviAvenantConv2 = new Div(new H5("Suivi Avenant Convention N°1"));
  private final DatePicker dateMailOuRdvSignatureConvAvn2 = new DatePicker("Date mail ou RDV pour signature");
  private final DatePicker dateDepotAlfrescoConvAvn2 = new DatePicker("Date de dépôt sur Alfresco");
  private final DatePicker dateRemiseOriginauxAvn2 = new DatePicker("Date de remise des exemplaires originaux (x3) à l'alternant");

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
  private final Tab avenant1ContratInfosTab = new Tab(VaadinIcon.FILE_ADD.create(), new Span("Avenant N°1"));
  private final Tab avenant2ContratInfosTab = new Tab(VaadinIcon.FILE_ADD.create(), new Span("Avenant N°2"));
  private final Tab ruptureContratInfosTab = new Tab(VaadinIcon.CLOSE_SMALL.create(), new Span("Rupture"));

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
            entrepriseContratInfosTab, formationContratInfosTab, avenant1ContratInfosTab, avenant2ContratInfosTab, ruptureContratInfosTab);
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
    form.add(codeContrat, typeContrat, representantLegal, new Div(), nomRepresentantLegal,
            prenomRepresentantLegal, relationAvecSalarie, adresseRepresentant, codePostalRepresentant, communeRepresentant,
            telephoneRepresentant, emailRepresentant, derogationAge, dateDelivranceDerogationAge, cadreAdministration, new Div(), cadreAdminNumEnregistrementContrat, cadreAdminNumAvenant,
            cadreAdminRecuLe, new Div(), infosContrat, new Div(), debutContrat, finContrat, emploiOccupeSalarieEtudiant, codeRomeEmploiOccupe, dureePeriodeEssai, numeroConventionFormation, semainesEntreprise,
            heuresFormation, semainesFormation, lieuFormation, dureeHebdomadaireTravail, new Div(), decua, new Div(), dateReceptionDecua, dateEnvoiRpDecua,
            dateRetourRpDecua, new Div(), retourCuaEtConvention, new Div(), dateEnvoiEmailCuaConvention, dateDepotAlfrescoCuaConvSigne, convention, new Div(),
            dateReceptionOriginauxConvention, exemplaireOriginauxRemisAlternantOuEntreprise, lea, new Div(), formationLea);

    // ajout des petits formulaires qui seront dans les autres tab
    ruptureContainer.add(rupture, new Div(), motifRupture, dateRupture);
    avenant1Container.add(avenant1, new Div(), motifAvn1, new Div(), suiviAvenantCua1, new Div(), dateMailOuRdvSignatureCuaAvn1, dateDepotAlfrescoCuaAvn1,
            suiviAvenantConv1, new Div(), dateMailOuRdvSignatureConvAvn1, dateDepotAlfrescoConvAvn1, dateRemiseOriginauxAvn1);
    avenant2Container.add(avenant2, new Div(), motifAvn2, new Div(), suiviAvenantCua2, new Div(), dateMailOuRdvSignatureCuaAvn2, dateDepotAlfrescoCuaAvn2,
            dateRemiseOriginauxCuaAvn2, new Div(), suiviAvenantConv2, new Div(), dateMailOuRdvSignatureConvAvn2, dateDepotAlfrescoConvAvn2, dateRemiseOriginauxAvn2);

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
      content.add(form);
    } else if (tab.equals(etudiantContratInfosTab)) {
      content.add(formContratEtudiant);
    } else if (tab.equals(formationContratInfosTab)) {
      content.add(formContratFormation);
    } else if (tab.equals(entrepriseContratInfosTab)) {
      content.add(formContratEntrepriseInfos);
    } else if (tab.equals(tuteurContratInfosTab)) {
      content.add(formContratTuteur);
    } else if (tab.equals(avenant1ContratInfosTab)) {
      content.add(avenant1Container);
    } else if (tab.equals(avenant2ContratInfosTab)) {
      content.add(avenant2Container);
    } else if (tab.equals(ruptureContratInfosTab)) {
      content.add(ruptureContainer);
    }
  }

  // Méthode qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
  private void setAllFieldsToReadOnly() {
    // contrat
    codeContrat.setReadOnly(true);
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
    cadreAdminNumEnregistrementContrat.setReadOnly(true);
    cadreAdminNumAvenant.setReadOnly(true);
    cadreAdminRecuLe.setReadOnly(true);
    debutContrat.setReadOnly(true);
    finContrat.setReadOnly(true);
    emploiOccupeSalarieEtudiant.setReadOnly(true);
    codeRomeEmploiOccupe.setReadOnly(true);
    dureePeriodeEssai.setReadOnly(true);
    numeroConventionFormation.setReadOnly(true);
    semainesEntreprise.setReadOnly(true);
    heuresFormation.setReadOnly(true);
    semainesFormation.setReadOnly(true);
    lieuFormation.setReadOnly(true);
    dureeHebdomadaireTravail.setReadOnly(true);
    dateReceptionDecua.setReadOnly(true);
    dateEnvoiRpDecua.setReadOnly(true);
    dateRetourRpDecua.setReadOnly(true);
    dateEnvoiEmailCuaConvention.setReadOnly(true);
    dateDepotAlfrescoCuaConvSigne.setReadOnly(true);
    dateReceptionOriginauxConvention.setReadOnly(true);
    exemplaireOriginauxRemisAlternantOuEntreprise.setReadOnly(true);
    formationLea.setReadOnly(true);
    dateRupture.setReadOnly(true);
    motifRupture.setReadOnly(true);
    motifAvn1.setReadOnly(true);
    dateMailOuRdvSignatureCuaAvn1.setReadOnly(true);
    dateDepotAlfrescoCuaAvn1.setReadOnly(true);
    dateMailOuRdvSignatureConvAvn1.setReadOnly(true);
    dateDepotAlfrescoConvAvn1.setReadOnly(true);
    dateRemiseOriginauxAvn1.setReadOnly(true);
    motifAvn2.setReadOnly(true);
    dateMailOuRdvSignatureCuaAvn2.setReadOnly(true);
    dateDepotAlfrescoCuaAvn2.setReadOnly(true);
    dateRemiseOriginauxCuaAvn2.setReadOnly(true);
    dateMailOuRdvSignatureConvAvn2.setReadOnly(true);
    dateDepotAlfrescoConvAvn2.setReadOnly(true);
    dateRemiseOriginauxAvn2.setReadOnly(true);

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
