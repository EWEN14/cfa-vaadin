package nc.unc.application.views.etudiant;

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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.*;
import nc.unc.application.data.enums.Civilite;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

/**
 * Modale (Dialog) qui s'ouvre lorsque l'on clique sur le bouton de détail d'un étudiant
 */
public class EtudiantConsult extends Dialog {

  private Etudiant etudiant;

  // Layout qui contiendra le contenu en dessous des tabs
  private final VerticalLayout content;

  // form qui contiendra les informations générales relatives à l'étudiants
  private final FormLayout formEtudiantInfos = new FormLayout();
  private final TextField nomEtudiant = new TextField("NOM");
  private final TextField prenomEtudiant = new TextField("Prénom");
  private IntegerField numeroEtudiant = new IntegerField("N° Étudiant");
  // utilisation de select lorsque nombre de choix assez petis
  private final Select<Civilite> civiliteEtudiant = new Select<>();
  private final DatePicker dateNaissanceEtudiant = new DatePicker("Date de Naissance");
  private final TextField ageEtudiant = new TextField("Âge");
  private final IntegerField telephoneEtudiant1 = new IntegerField("Téléphone 1");
  private final IntegerField telephoneEtudiant2 = new IntegerField("Téléphone 2");
  private final EmailField emailEtudiant = new EmailField("Email");
  private final TextField dernierDiplomeObtenuOuEnCours = new TextField("Dernier diplôme obtenu ou en cours");
  private final IntegerField niveauDernierDiplome = new IntegerField("Niveau dernier diplôme obtenu");
  private final IntegerField anneeObtentionDernierDiplome = new IntegerField("Année d'obtention du dernier diplôme");
  private final TextField admis = new TextField("Admis");
  private final TextField situationUnc = new TextField("Situation à l'UNC");
  private final TextField situationEntreprise = new TextField("Situation en entreprise");
  private final TextField lieuNaissance = new TextField("Lieu de Naissance");
  private final TextField nationalite = new TextField("Nationalité");
  private final IntegerField numeroCafatEtudiant = new IntegerField("Numéro Cafat");
  private final TextField adresseEtudiant = new TextField("Adresse");
  private final TextField boitePostaleEtudiant = new TextField("Boîte Postale");
  private final IntegerField codePostalEtudiant = new IntegerField("Code Postal");
  private final TextField communeEtudiant = new TextField("Commune");
  private final TextField situationAnneePrecedente = new TextField("Situation l'année précédente");
  private final TextField etablissementDeProvenance = new TextField("Établissement de provenance");
  private final TextField dernierEmploiOccupe = new TextField("Dernier emploi occupé");
  private final TextField parcours = new TextField("Parcours");
  private final IntegerField anneePromotion = new IntegerField("Année de début de la promotion");
  private final Checkbox travailleurHandicape = new Checkbox("Travailleur Handicapé");
  private final Checkbox veepap = new Checkbox("VEEPAP");
  private final TextField priseEnChargeFraisInscription = new TextField("Prise en charge des frais d'inscription");
  private final TextField obtentionDiplomeMention = new TextField("Obtention du diplôme et mention");
  private final TextArea observationsEtudiant = new TextArea("Observations");
  // binder qui sera utilisé pour remlir automatiquement les champs d'infos générales sur l'étudiant
  Binder<Etudiant> etudiantBinder = new BeanValidationBinder<>(Etudiant.class);

  // form qui contiendra les informations relatives à l'entreprise dans laquelle est l'étudiant
  private final FormLayout formEtudiantEntrepriseInfos = new FormLayout();
  private final TextField enseigne = new TextField("Enseigne");
  private final TextField raisonSociale = new TextField("Raison Sociale");
  private final TextField statutActifEntreprise = new TextField("Statut de l'entreprise");
  private final IntegerField telephoneContactCfa = new IntegerField("Téléphone contact CFA");
  // binder qui sera utilisé pour remplir automatiquement les champs de l'entreprise liée à l'étudiant
  Binder<Entreprise> entrepriseBinder = new BeanValidationBinder<>(Entreprise.class);

  // Champs du formulaire relatifs aux informations du tuteur lié à l'étudiant
  FormLayout formEtudiantTuteur = new FormLayout();
  TextField nomTuteur = new TextField("NOM");
  TextField prenomTuteur = new TextField("Prenom");
  EmailField emailTuteur = new EmailField("Email");
  IntegerField telephoneTuteur1 = new IntegerField("Téléphone 1");
  IntegerField telephoneTuteur2 = new IntegerField("Téléphone 2");
  // Binder qui sera utilisé pour remplir automatiquement les champs du tuteur
  Binder<Tuteur> tuteurBinder = new BeanValidationBinder<>(Tuteur.class);

  // Champs du formulaire relatif aux informations de la formation lié à l'étudiant
  FormLayout formEtudiantFormation = new FormLayout();
  TextField libelleFormation = new TextField("Libellé de la formation");
  TextField codeFormation = new TextField("Code de la formation");
  // Binder qui sera utilisé pour remplir automatiquement les champs de formation
  Binder<Formation> formationBinder = new BeanValidationBinder<>(Formation.class);

  // Champs du formulaire relatif aux informations du référent pédagogique lié à l'étudiant
  FormLayout formEtudiantReferentPedago = new FormLayout();
  TextField nomReferentPedago = new TextField("NOM");
  TextField prenomReferentPedago = new TextField("Prénom");
  IntegerField telephoneReferentPedago = new IntegerField("Téléphone");
  EmailField emailReferentPedago = new EmailField("Email");
  // Binder qui sera utilisé pour remplir automatiquement les champs du référent pédagogique
  Binder<ReferentPedagogique> referentPedagogiqueBinder = new BeanValidationBinder<>(ReferentPedagogique.class);

  // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
  private final Tab etudiantInfosTab = new Tab(VaadinIcon.ACADEMY_CAP.create(),new Span("Étudiant"));
  private final Tab entrepriseEtudiantInfosTab = new Tab(VaadinIcon.WORKPLACE.create(),new Span("Entreprise"));
  private final Tab tuteurEtudiantInfosTab = new Tab(VaadinIcon.USER.create(), new Span("Tuteur"));
  private final Tab formationEtudiantInfosTab = new Tab(VaadinIcon.DIPLOMA.create(), new Span("Formation"));
  private final Tab referentPedagoEtudiantInfosTab = new Tab(VaadinIcon.HANDSHAKE.create(), new Span("Référent pédagogique"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer l'étudiant");

  public EtudiantConsult() {
    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);
    this.setWidth("85vw");

    // fonction qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables TODO
    setAllFieldsToReadOnly();

    // instanciation des différents binder qui serviront au remplissage automatique des formulaires d'informations rattachés à l'étudiant
    etudiantBinder.bindInstanceFields(this);
    entrepriseBinder.bindInstanceFields(this);
    tuteurBinder.bindInstanceFields(this);
    formationBinder.bindInstanceFields(this);
    referentPedagogiqueBinder.bindInstanceFields(this);

    civiliteEtudiant.setLabel("Civilité");
    civiliteEtudiant.setItems(Civilite.values());

    // On instancie la Tabs, et on lui donne les tab que l'on veut insérer
    // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
    Tabs tabsEtudiant = new Tabs(etudiantInfosTab, entrepriseEtudiantInfosTab, tuteurEtudiantInfosTab,
            formationEtudiantInfosTab, referentPedagoEtudiantInfosTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsEtudiant.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    // on définit les champs qu'il y aura dans le formulaire d'informations générales de l'étudiant
    formEtudiantInfos.add(nomEtudiant, prenomEtudiant, numeroEtudiant, civiliteEtudiant, dateNaissanceEtudiant, ageEtudiant, telephoneEtudiant1, telephoneEtudiant2,
            emailEtudiant, dernierDiplomeObtenuOuEnCours, niveauDernierDiplome, anneeObtentionDernierDiplome, admis, situationUnc,
            lieuNaissance, nationalite, numeroCafatEtudiant, adresseEtudiant, boitePostaleEtudiant, codePostalEtudiant, communeEtudiant, situationAnneePrecedente,
            etablissementDeProvenance, dernierEmploiOccupe, parcours, anneePromotion, travailleurHandicape, veepap, priseEnChargeFraisInscription,
            obtentionDiplomeMention, observationsEtudiant);
    // pareil, mais pour le formulaire relatif à son entreprise
    formEtudiantEntrepriseInfos.add(enseigne, raisonSociale, statutActifEntreprise, telephoneContactCfa);

    // ajout des champs dans le formulaire de tuteur
    formEtudiantTuteur.add(prenomTuteur, nomTuteur, emailTuteur, telephoneTuteur1, telephoneTuteur2);

    // ajout des champs dans le formulaire de la formation suivie par l'étudiant
    formEtudiantFormation.add(libelleFormation, codeFormation);

    // ajout des champs dans le formulaire du referent pédagogique qui encadre l'étudiant
    formEtudiantReferentPedago.add(prenomReferentPedago, nomReferentPedago, telephoneReferentPedago, emailReferentPedago);

    // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
    content = new VerticalLayout();
    content.setSpacing(false);
    // à l'ouverture, on ouvre la tab d'infos générales sur l'étudiant
    setContent(etudiantInfosTab);

    // on ajoute la tabs, le contenu et les boutons du bas dans la Modale/Dialog
    add(tabsEtudiant, content, createButtonsLayout());
  }

  // méthode appelée à l'ouverture de la vue pour alimenter les champs du formulaire.
  public void setEtudiant(Etudiant etudiant) {
    this.etudiant = etudiant;
    if (etudiant != null) {
      // lecture des binder pour compléter les champs dans les différents formulaire
      etudiantBinder.readBean(etudiant);
      entrepriseBinder.readBean(etudiant.getEntreprise());
      tuteurBinder.readBean(etudiant.getTuteur());
      formationBinder.readBean(etudiant.getFormation());
      referentPedagogiqueBinder.readBean(etudiant.getReferentPedagogique());

      // définition de l'âge de l'étudiant en front, en se basant sur la base de données
      ageEtudiant.setValue(ChronoUnit.YEARS.between(etudiant.getDateNaissanceEtudiant(), LocalDate.now()) + " ans");
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // évènements delete et close
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, etudiant)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    return new HorizontalLayout(delete, close);
  }

  // méthode qui définit quel contenu mettre en dessous des tabs
  private void setContent(Tab tab) {
    // au début on enlève tout le contenu avant remplacement
    content.removeAll();
    // on insère le contenu (formulaire) adéquat en fonction de la tab sélectionnée
    if (tab.equals(etudiantInfosTab)) {
      content.add(formEtudiantInfos);
    } else if (tab.equals(entrepriseEtudiantInfosTab)) {
      content.add(formEtudiantEntrepriseInfos);
    } else if (tab.equals(tuteurEtudiantInfosTab)) {
      content.add(formEtudiantTuteur);
    } else if (tab.equals(formationEtudiantInfosTab)) {
      content.add(formEtudiantFormation);
    } else if (tab.equals(referentPedagoEtudiantInfosTab)) {
      content.add(formEtudiantReferentPedago);
    }
  }

  // Méthode qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
  private void setAllFieldsToReadOnly() {
    // étudiant
    nomEtudiant.setReadOnly(true);
    prenomEtudiant.setReadOnly(true);
    numeroEtudiant.setReadOnly(true);
    civiliteEtudiant.setReadOnly(true);
    dateNaissanceEtudiant.setReadOnly(true);
    ageEtudiant.setReadOnly(true);
    telephoneEtudiant1.setReadOnly(true);
    telephoneEtudiant2.setReadOnly(true);
    emailEtudiant.setReadOnly(true);
    dernierDiplomeObtenuOuEnCours.setReadOnly(true);
    niveauDernierDiplome.setReadOnly(true);
    anneeObtentionDernierDiplome.setReadOnly(true);
    admis.setReadOnly(true);
    situationUnc.setReadOnly(true);
    situationEntreprise.setReadOnly(true);
    lieuNaissance.setReadOnly(true);
    nationalite.setReadOnly(true);
    numeroCafatEtudiant.setReadOnly(true);
    adresseEtudiant.setReadOnly(true);
    boitePostaleEtudiant.setReadOnly(true);
    codePostalEtudiant.setReadOnly(true);
    communeEtudiant.setReadOnly(true);
    situationAnneePrecedente.setReadOnly(true);
    etablissementDeProvenance.setReadOnly(true);
    dernierEmploiOccupe.setReadOnly(true);
    parcours.setReadOnly(true);
    travailleurHandicape.setReadOnly(true);
    veepap.setReadOnly(true);
    priseEnChargeFraisInscription.setReadOnly(true);
    obtentionDiplomeMention.setReadOnly(true);
    anneePromotion.setReadOnly(true);
    observationsEtudiant.setReadOnly(true);
    // entreprise
    enseigne.setReadOnly(true);
    raisonSociale.setReadOnly(true);
    statutActifEntreprise.setReadOnly(true);
    telephoneContactCfa.setReadOnly(true);
    // formation
    libelleFormation.setReadOnly(true);
    codeFormation.setReadOnly(true);
    // tuteur
    prenomTuteur.setReadOnly(true);
    nomTuteur.setReadOnly(true);
    emailTuteur.setReadOnly(true);
    telephoneTuteur1.setReadOnly(true);
    telephoneTuteur2.setReadOnly(true);
    // referent pédagogique
    nomReferentPedago.setReadOnly(true);
    prenomReferentPedago.setReadOnly(true);
    telephoneReferentPedago.setReadOnly(true);
    emailReferentPedago.setReadOnly(true);
  }

  public void hideDeleteButton() {
    delete.setVisible(false);
  }

  // Event "global" (class mère), qui étend les deux events ci-dessous, dont le but est de fournir l'étudiant
  // que l'on consulte dans le formulaire.
  public static abstract class EtudiantConsultFormEvent extends ComponentEvent<EtudiantConsult> {
    private final Etudiant etudiant;

    protected EtudiantConsultFormEvent(EtudiantConsult source, Etudiant etudiant) {
      super(source, false);
      this.etudiant = etudiant;
    }

    public Etudiant getEtudiant() {
      return etudiant;
    }
  }

  // Event au clic sur le bouton de suppression (classe fille)
  public static class DeleteEvent extends EtudiantConsultFormEvent {
    DeleteEvent(EtudiantConsult source, Etudiant etudiant) {
      super(source, etudiant);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends EtudiantConsultFormEvent {
    CloseEvent(EtudiantConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
