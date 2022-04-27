package nc.unc.application.views.etudiant;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
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

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Modale (Dialog) qui s'ouvre lorsque l'on clique sur le bouton de détail d'un étudiant
 */
public class EtudiantConsult extends Dialog {

  private Etudiant etudiant;

  // Layout qui contiendra le contenu en dessous des tabs
  private final VerticalLayout content;

  // form qui contiendra les informations générales relatives à l'étudiants
  private final FormLayout formEtudiantInfos = new FormLayout();
  private final TextField nom = new TextField("NOM");
  private final TextField prenom = new TextField("Prénom");
  private final TextField civilite = new TextField("Civilité");
  private final TextField dateNaissance = new TextField("Date de Naissance");
  private final TextField age = new TextField("Âge");
  private final TextField telephone1 = new TextField("Téléphone 1");
  private final TextField telephone2 = new TextField("Téléphone 2");
  private final EmailField email = new EmailField("Email");
  private final TextField dernierDiplomeObtenuOuEnCours = new TextField("Dernier diplôme obtenu ou en cours");
  private final TextField niveauDernierDiplome = new TextField("Niveau dernier diplôme obtenu");
  private final TextField anneeObtentionDernierDiplome = new TextField("Année d'obtention du dernier diplôme");
  private final TextField admis = new TextField("Admission");
  private final TextField situationUnc = new TextField("Situation à l'UNC");
  private final TextField situationEntreprise = new TextField("Situation en entreprise");
  private final TextField lieuNaissance = new TextField("Lieu de naissance");
  private final TextField nationalite = new TextField("Nationalité");
  private final TextField numeroCafat = new TextField("Numéro Cafat");
  private final TextField adresse = new TextField("Adresse");
  private final TextField boitePostale = new TextField("Boîte Postale");
  private final TextField codePostal = new TextField("Code Postal");
  private final TextField commune = new TextField("Commune");
  private final TextField situationAnneePrecedente = new TextField("Situation Année Précédente");
  private final TextField etablissementDeProvenance = new TextField("Établissement de provenance");
  private final Checkbox travailleurHandicape = new Checkbox("Travailleur handicapé");
  private final TextField parcours = new TextField("Parcours");
  private final Checkbox veepap = new Checkbox("VEEPAP");
  private final TextField priseEnChargeFraisInscription = new TextField("Prise en charge des frais d'incription");
  private final TextField obtentionDiplomeMention = new TextField("Obtention du diplôme et mention");
  private final TextArea observations = new TextArea("Observations");

  // form qui contiendra les informations relatives à l'entreprise dans laquelle est l'étudiant
  private final FormLayout formEtudiantEntrepriseInfos = new FormLayout();
  private final TextField enseigne = new TextField("Enseigne");
  private final TextField raisonSociale = new TextField("Raison Sociale");
  private final TextField statutActifEntreprise = new TextField("Statut de l'entreprise");
  private final IntegerField telephoneContactCfa = new IntegerField("Téléphone contact CFA");
  // binder qui sera utilisé pour remlir automatiquement les champs de l'entreprise liée à l'étudiant
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
    entrepriseBinder.bindInstanceFields(this);
    tuteurBinder.bindInstanceFields(this);
    formationBinder.bindInstanceFields(this);
    referentPedagogiqueBinder.bindInstanceFields(this);

    // On instancie la Tabs, et on lui donne les tab que l'on veut insérer
    // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
    Tabs tabsEtudiant = new Tabs(etudiantInfosTab, entrepriseEtudiantInfosTab, tuteurEtudiantInfosTab,
            formationEtudiantInfosTab, referentPedagoEtudiantInfosTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsEtudiant.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    // on définit les champs qu'il y aura dans le formulaire d'informations générales de l'étudiant
    formEtudiantInfos.add(nom, prenom, civilite, dateNaissance, age, telephone1, telephone2, email, dernierDiplomeObtenuOuEnCours,
            niveauDernierDiplome, anneeObtentionDernierDiplome, admis, situationUnc, lieuNaissance, nationalite,
            numeroCafat, adresse, boitePostale, codePostal, commune, situationAnneePrecedente, etablissementDeProvenance,
            parcours, travailleurHandicape, veepap, priseEnChargeFraisInscription, obtentionDiplomeMention,
            observations);
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

  // méthode appelé à l'ouverture de la vue pour alimenter les champs du formulaire.
  // Ici pas besoin de binder étant donné que l'on ne fait que consulter les informations (pas de sauvegarde)
  public void setEtudiant(Etudiant etudiant) {
    this.etudiant = etudiant;
    if (etudiant != null) {
      // champs obligatoirement remplis
      nom.setValue(etudiant.getNomEtudiant());
      prenom.setValue(etudiant.getPrenomEtudiant());
      civilite.setValue(etudiant.getCiviliteEtudiant().toString());
      // affichage date au format français
      dateNaissance.setValue(etudiant.getDateNaissanceEtudiant().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
      age.setValue(etudiant.getAge().toString());
      telephone1.setValue(etudiant.getTelephoneEtudiant1().toString());
      telephone2.setValue(etudiant.getTelephoneEtudiant2().toString());
      email.setValue(etudiant.getEmailEtudiant());
      dernierDiplomeObtenuOuEnCours.setValue(etudiant.getDernierDiplomeObtenuOuEnCours());
      admis.setValue(etudiant.getAdmis());

      // champs pouvant être null (ou false donc pas coché dans les cas des cases à cocher)
      niveauDernierDiplome.setValue(etudiant.getNiveauDernierDiplome() != null ? etudiant.getNiveauDernierDiplome().toString() : "");
      anneeObtentionDernierDiplome.setValue(etudiant.getAnneeObtentionDernierDiplome() != null ? etudiant.getAnneeObtentionDernierDiplome().toString() : "");
      situationUnc.setValue(etudiant.getSituationUnc() != null ? etudiant.getSituationUnc() : "");
      situationEntreprise.setValue(etudiant.getSituationEntreprise() != null ? etudiant.getSituationEntreprise() : "");
      lieuNaissance.setValue(etudiant.getLieuNaissance() != null ? etudiant.getLieuNaissance() : "");
      nationalite.setValue(etudiant.getNationalite() != null ? etudiant.getNationalite() : "");
      numeroCafat.setValue(etudiant.getNumeroCafatEtudiant() != null ? etudiant.getNumeroCafatEtudiant().toString() : "");
      adresse.setValue(etudiant.getAdresseEtudiant() != null ? etudiant.getAdresseEtudiant() : "");
      boitePostale.setValue(etudiant.getBoitePostaleEtudiant() != null ? etudiant.getBoitePostaleEtudiant() : "");
      codePostal.setValue(etudiant.getCodePostalEtudiant() != null ? etudiant.getCodePostalEtudiant().toString() : "");
      commune.setValue(etudiant.getCommuneEtudiant() != null ? etudiant.getCommuneEtudiant() : "");
      situationAnneePrecedente.setValue(etudiant.getSituationAnneePrecedente() != null ? etudiant.getSituationAnneePrecedente() : "");
      etablissementDeProvenance.setValue(etudiant.getEtablissementDeProvenance() != null ? etudiant.getEtablissementDeProvenance() : "");
      parcours.setValue(etudiant.getParcours() != null ? etudiant.getParcours() : "");
      travailleurHandicape.setValue(etudiant.getTravailleurHandicape() != null ? etudiant.getTravailleurHandicape() : false);
      veepap.setValue(etudiant.getVeepap() != null ? etudiant.getVeepap() : false);
      priseEnChargeFraisInscription.setValue(etudiant.getPriseEnChargeFraisInscription() != null ? etudiant.getPriseEnChargeFraisInscription() : "");
      obtentionDiplomeMention.setValue(etudiant.getObtentionDiplomeMention() != null ? etudiant.getObtentionDiplomeMention() : "");
      observations.setValue(etudiant.getObservationsEtudiant() != null ? etudiant.getObservationsEtudiant() : "");
      
      entrepriseBinder.readBean(etudiant.getEntreprise());

      tuteurBinder.readBean(etudiant.getTuteur());

      formationBinder.readBean(etudiant.getFormation());

      referentPedagogiqueBinder.readBean(etudiant.getReferentPedagogique());
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
    nom.setReadOnly(true);
    prenom.setReadOnly(true);
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
