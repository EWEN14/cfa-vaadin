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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

import java.util.List;

public class ContratNewOrEdit extends Dialog {

  private Contrat contrat;
  private Contrat cloneContrat;

  FormLayout form = new FormLayout();

  ComboBox<Etudiant> etudiant = new ComboBox<>("Étudiant");
  ComboBox<Formation> formation = new ComboBox<>("Formation");
  ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");
  ComboBox<Tuteur> tuteur = new ComboBox<>("Tuteur");

  H3 titre = new H3();

  Select<String> codeContrat = new Select<>(CodeContrat.getCodeContratStr());
  Select<String> typeContrat = new Select<>("1","2","3","4");

  Div representantLegal = new Div(new H4("Représentant légal du salarié et dérogation d'âge"));
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

  Div cadreAdministration = new Div(new H4("Cadre réservé à l'administration"));
  TextField cadreAdminNumEnregistrementContrat = new TextField("Numéro d'enregistrement du contrat");
  TextField cadreAdminNumAvenant = new TextField("Numéro d'avenant");
  DatePicker cadreAdminRecuLe = new DatePicker("Reçu le");

  Div infosContrat = new Div(new H4("Informations liées au contrat"));
  DatePicker debutContrat = new DatePicker("Date de début du contrat");
  DatePicker finContrat = new DatePicker("Date de fin du contrat");
  TextField emploiOccupeSalarieEtudiant = new TextField("Emploi occupé par le salarié");
  TextField codeRomeEmploiOccupe = new TextField("Code ROME de l'emploi occupé");
  IntegerField dureePeriodeEssai = new IntegerField("Durée de la période d'essai (nombre de semaines)");

  TextField numeroConventionFormation = new TextField("Numéro de la convention de Formation");
  IntegerField semainesEntreprise = new IntegerField("Nombre de semaines en entreprise");
  IntegerField heuresFormation = new IntegerField("Nombre d'heures en formation");
  IntegerField semainesFormation = new IntegerField("Nombre de semaines en formation");
  ComboBox<String> lieuFormation = new ComboBox<>("Lieu de la formation");
  IntegerField dureeHebdomadaireTravail = new IntegerField("Durée hebdomadaire de travail");

  Div decua = new Div(new H4("DECUA"));
  DatePicker dateReceptionDecua = new DatePicker("Date de réception du DECUA");
  DatePicker dateEnvoiRpDecua = new DatePicker("Date d'envoi au référent pédagogique du DECUA pour validation");
  DatePicker dateRetourRpDecua = new DatePicker("Date de retour validation du DECUA par le référent pédagogique");

  Div retourCuaEtConvention = new Div(new H4("Retour CUA et convention signée"));
  DatePicker dateEnvoiEmailCuaConvention = new DatePicker("Date de réception des originaux");
  DatePicker dateDepotAlfrescoCuaConvSigne = new DatePicker("Date de dépôt Alfresco");

  Div convention = new Div(new H4("Convention"));
  DatePicker dateReceptionOriginauxConvention = new DatePicker("Date de réception des originaux");
  TextField exemplaireOriginauxRemisAlternantOuEntreprise = new TextField("Exemplaires originaux (x3) remis à l'alternant ou à l'entreprise");

  Div lea = new Div(new H4("Formation LEA"));
  DatePicker formationLea = new DatePicker("Date de formation au LEA");

  FormLayout ruptureContainer = new FormLayout();
  Div rupture = new Div(new H4("Rupture"));
  DatePicker dateRupture = new DatePicker("Date de rupture du contrat");
  TextArea motifRupture = new TextArea("Motif de la rupture");

  FormLayout avenant1Container = new FormLayout();
  Div avenant1 = new Div(new H4("Avenant N°1"));
  TextArea motifAvn1 = new TextArea("Motif de l'avenant");
  Div suiviAvenantCua1 = new Div(new H5("Suivi Avenant CUA N°1"));
  DatePicker dateMailOuRdvSignatureCuaAvn1 = new DatePicker("Date mail ou RDV pour signature");
  DatePicker dateDepotAlfrescoCuaAvn1 = new DatePicker("Date de dépôt sur Alfresco");
  Div suiviAvenantConv1 = new Div(new H5("Suivi Avenant Convention N°1"));
  DatePicker dateMailOuRdvSignatureConvAvn1 = new DatePicker("Date mail ou RDV pour signature");
  DatePicker dateDepotAlfrescoConvAvn1 = new DatePicker("Date de dépôt sur Alfresco");
  DatePicker dateRemiseOriginauxAvn1 = new DatePicker("Date de remise des exemplaires originaux (x3) à l'alternant");

  FormLayout avenant2Container = new FormLayout();
  Div avenant2 = new Div(new H4("Avenant N°2"));
  TextArea motifAvn2 = new TextArea("Motif de l'avenant");
  Div suiviAvenantCua2 = new Div(new H5("Suivi Avenant CUA N°1"));
  DatePicker dateMailOuRdvSignatureCuaAvn2 = new DatePicker("Date mail ou RDV pour signature");
  DatePicker dateDepotAlfrescoCuaAvn2 = new DatePicker("Date de dépôt sur Alfresco");
  DatePicker dateRemiseOriginauxCuaAvn2 = new DatePicker("Date de remise des exemplaires originaux (x3) à l'alternant");
  Div suiviAvenantConv2 = new Div(new H5("Suivi Avenant Convention N°1"));
  DatePicker dateMailOuRdvSignatureConvAvn2 = new DatePicker("Date mail ou RDV pour signature");
  DatePicker dateDepotAlfrescoConvAvn2 = new DatePicker("Date de dépôt sur Alfresco");
  DatePicker dateRemiseOriginauxAvn2 = new DatePicker("Date de remise des exemplaires originaux (x3) à l'alternant");

  Binder<Contrat> binder = new BeanValidationBinder<>(Contrat.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public ContratNewOrEdit(List<Entreprise> entrepriseList, List<Formation> formationList, List<Etudiant> etudiantList, List<Tuteur> tuteurList) {
    this.setWidth("85vw");
    this.setHeight("90vh");

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    // on set les éléments des différentes entités qui sont liées aux contrats
    etudiant.setItems(etudiantList);
    etudiant.setItemLabelGenerator(etudiant -> etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant());
    etudiant.setClearButtonVisible(true);

    formation.setItems(formationList);
    formation.setItemLabelGenerator(Formation::getLibelleFormation);
    formation.setClearButtonVisible(true);

    entreprise.setItems(entrepriseList);
    entreprise.setItemLabelGenerator(Entreprise::getEnseigne);
    entreprise.setClearButtonVisible(true);

    tuteur.setItems(tuteurList);
    tuteur.setItemLabelGenerator(tuteur1 -> tuteur1.getPrenomTuteur() + " " + tuteur1.getNomTuteur());
    tuteur.setClearButtonVisible(true);

    // définition d'un picker customisé pour toutes les dates
    setCustomDatePicker();

    // regex pour le code rome
    codeRomeEmploiOccupe.setPlaceholder("ex: M1234");
    codeRomeEmploiOccupe.setPattern("[A-Z][0-9]{4}$");

    // ajout de labels sur les select
    codeContrat.setLabel("Code du Contrat");
    typeContrat.setLabel("Type du Contrat");
    relationAvecSalarie.setLabel("Relation du représentant avec le salarié");

    // on passe les communes de NC dans nos combo box en ayant besoin
    communeRepresentant.setItems(Commune.getCommunesStr());
    communeRepresentant.setClearButtonVisible(true);
    lieuFormation.setItems(Commune.getCommunesStr());
    lieuFormation.setClearButtonVisible(true);

    // ajout des éléments au formulaire principal
    form.add(etudiant, formation, entreprise, tuteur, codeContrat, typeContrat, representantLegal, new Div(), nomRepresentantLegal,
            prenomRepresentantLegal, relationAvecSalarie, adresseRepresentant, codePostalRepresentant, communeRepresentant,
            telephoneRepresentant, emailRepresentant, derogationAge, dateDelivranceDerogationAge, cadreAdministration, new Div(), cadreAdminNumEnregistrementContrat, cadreAdminNumAvenant,
            cadreAdminRecuLe, new Div(), infosContrat, new Div(), debutContrat, finContrat, emploiOccupeSalarieEtudiant, codeRomeEmploiOccupe, dureePeriodeEssai, numeroConventionFormation, semainesEntreprise,
            heuresFormation, semainesFormation, lieuFormation, dureeHebdomadaireTravail, decua, new Div(), dateReceptionDecua, dateEnvoiRpDecua,
            dateRetourRpDecua, new Div(), retourCuaEtConvention, new Div(), dateEnvoiEmailCuaConvention, dateDepotAlfrescoCuaConvSigne, convention, new Div(),
            dateReceptionOriginauxConvention, exemplaireOriginauxRemisAlternantOuEntreprise, lea, new Div(), formationLea);

    // ajout des petits formulaires correspondants à des champs qui seront cachés ou non selon les circonstances
    ruptureContainer.add(rupture, new Div(), motifRupture, dateRupture);
    avenant1Container.add(avenant1, new Div(), motifAvn1, new Div(), suiviAvenantCua1, new Div(), dateMailOuRdvSignatureCuaAvn1, dateDepotAlfrescoCuaAvn1,
            suiviAvenantConv1, new Div(), dateMailOuRdvSignatureConvAvn1, dateDepotAlfrescoConvAvn1, dateRemiseOriginauxAvn1);
    avenant2Container.add(avenant2, new Div(), motifAvn2, new Div(), suiviAvenantCua2, new Div(), dateMailOuRdvSignatureCuaAvn2, dateDepotAlfrescoCuaAvn2,
            dateRemiseOriginauxCuaAvn2, new Div(), suiviAvenantConv2, new Div(), dateMailOuRdvSignatureConvAvn2, dateDepotAlfrescoConvAvn2, dateRemiseOriginauxAvn2);

    // ajout des éléments à la modale
    this.add(titre, form, ruptureContainer, avenant1Container, avenant2Container, createButtonsLayout());
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new ContratNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

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
        titre.add("Modification d'un contrat");
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    } else {
      titre.add("Création d'un nouveau contrat");
    }
    // alimentation du binder
    binder.readBean(contrat);
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

  public void setCustomDatePicker() {
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");

    dateDelivranceDerogationAge.setI18n(multiFormatI18n);

    cadreAdminRecuLe.setI18n(multiFormatI18n);
    debutContrat.setI18n(multiFormatI18n);
    finContrat.setI18n(multiFormatI18n);
    dateRupture.setI18n(multiFormatI18n);
    dateReceptionDecua.setI18n(multiFormatI18n);

    dateEnvoiRpDecua.setI18n(multiFormatI18n);
    dateRetourRpDecua.setI18n(multiFormatI18n);

    dateEnvoiEmailCuaConvention.setI18n(multiFormatI18n);
    dateDepotAlfrescoCuaConvSigne.setI18n(multiFormatI18n);

    dateReceptionOriginauxConvention.setI18n(multiFormatI18n);

    dateMailOuRdvSignatureCuaAvn1.setI18n(multiFormatI18n);
    dateDepotAlfrescoCuaAvn1.setI18n(multiFormatI18n);
    dateMailOuRdvSignatureConvAvn1.setI18n(multiFormatI18n);
    dateDepotAlfrescoConvAvn1.setI18n(multiFormatI18n);
    dateRemiseOriginauxAvn1.setI18n(multiFormatI18n);

    dateMailOuRdvSignatureCuaAvn2.setI18n(multiFormatI18n);
    dateDepotAlfrescoCuaAvn2.setI18n(multiFormatI18n);
    dateRemiseOriginauxCuaAvn2.setI18n(multiFormatI18n);
    dateMailOuRdvSignatureConvAvn2.setI18n(multiFormatI18n);
    dateDepotAlfrescoConvAvn2.setI18n(multiFormatI18n);
    dateRemiseOriginauxAvn2.setI18n(multiFormatI18n);

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
