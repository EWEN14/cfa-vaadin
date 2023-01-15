package nc.unc.application.views.entretienIndividuelle;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
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
import nc.unc.application.data.entity.EntretienIndividuel;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.ReferentCfa;
import nc.unc.application.data.enums.Civilite;
import nc.unc.application.data.service.EntretienIndividuelService;
import nc.unc.application.data.service.EtudiantService;
import nc.unc.application.data.service.ReferentCfaService;

/**
 * Modale (Dialog) qui s'ouvre lorsque l'on clique sur le bouton de détail d'un entretien individuel
 */
public class EntretienIndividuelConsult extends Dialog {

  private EntretienIndividuelService entretienIndividuelService;
  private EtudiantService etudiantService;
  private ReferentCfaService referentCfaService;
  private EntretienIndividuel entretienIndividuel;

  // Layout qui contiendra le contenu en dessous des tabs
  private final VerticalLayout content = new VerticalLayout();

  H3 titre = new H3("Consultation d'un entretien individuel");

  // form qui contient les informations générales de l'entretien individuel
  private final FormLayout formEntretien = new FormLayout();
  private final TextArea observations_entretien_individuel = new TextArea("Observations");
  private final DatePicker date = new DatePicker();
  ComboBox<Etudiant> etudiant = new ComboBox<>("Etudiant concerné par l'entretien");
  ComboBox<ReferentCfa> referentCfa = new ComboBox<>("Référent CFA concerné par l'entretien");
  private final Checkbox suivreEtudiant = new Checkbox("Suivre étudiant");
  private final DatePicker dateCreation = new DatePicker("Date de création");
  private final DatePicker dateModification = new DatePicker("Date de mise à jour");
  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales de l'entretien individuel
  Binder<EntretienIndividuel> entretienIndividuelBinder = new BeanValidationBinder<>(EntretienIndividuel.class);

  // form qui contiendra les informations relatives à l'étudiant concerné par l'entretien
  private final FormLayout formEtudiantInfos = new FormLayout();
  private final TextField nomEtudiant = new TextField("NOM");
  private final TextField prenomEtudiant = new TextField("Prénom");
  private final IntegerField numeroEtudiant = new IntegerField("N° Étudiant");
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
  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales sur l'étudiant
  Binder<Etudiant> etudiantBinder = new BeanValidationBinder<>(Etudiant.class);

  // form qui contiendra les informations relatives au référent cfa concerné par l'entretien
  private final FormLayout formReferentCFAInfos = new FormLayout();
  private final TextField nomReferentCfa = new TextField("NOM");
  private final TextField prenomReferentCfa = new TextField("Prénom");
  private final IntegerField telephoneReferentCfa = new IntegerField("Téléphone");
  private final EmailField emailReferentCfa = new EmailField("Email");
  private final Select<Civilite> civiliteReferentCfa = new Select<>();
  Binder<ReferentCfa> referentCFABinder = new BeanValidationBinder<>(ReferentCfa.class);

  // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
  private final Tab entretienInfosTab = new Tab(VaadinIcon.USER.create(), new Span("Entretien individuel"));
  private final Tab referentTab = new Tab(VaadinIcon.DIPLOMA.create(), new Span("Referent CFA"));
  private final Tab etudiantTab = new Tab(VaadinIcon.ACADEMY_CAP.create(), new Span("Étudiant"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer l'entretien");

  public EntretienIndividuelConsult(EtudiantService etudiantService, ReferentCfaService referentCfaService, EntretienIndividuelService entretienIndividuelService) {
    this.etudiantService = etudiantService;
    this.entretienIndividuelService = entretienIndividuelService;
    this.referentCfaService = referentCfaService;

    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);
    this.setWidth("85vw");
    this.setHeight("90vh");

    // instanciation des différents binder qui serviront au remplissage automatique des formulaires d'informations rattachés à l'entretien
    etudiantBinder.bindInstanceFields(this);
    referentCFABinder.bindInstanceFields(this);
    entretienIndividuelBinder.bindInstanceFields(this);

    //Labels des dates de création et mise à jour de l'entretien individuel
    etudiant.setItems(etudiantService.findAllEtudiants(null));
    etudiant.setItemLabelGenerator(etudiant -> etudiant.getNomEtudiant()+ " " + etudiant.getPrenomEtudiant());
    etudiant.setClearButtonVisible(true);

    referentCfa.setItems(referentCfaService.findAllReferentCfa(null));
    referentCfa.setItemLabelGenerator(referentCfa -> referentCfa.getNomReferentCfa()+ " " + referentCfa.getPrenomReferentCfa());
    referentCfa.setClearButtonVisible(true);

    // nécessité de set les items de civilité (étant donné que ce n'est pas une des enums qui retourne des String)
    civiliteEtudiant.setLabel("Civilité");
    civiliteEtudiant.setItems(Civilite.values());
    civiliteReferentCfa.setLabel("Civilité");
    civiliteReferentCfa.setItems(Civilite.values());

    // Méthode qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
    setAllFieldsToReadOnly();

    // On instancie la Tabs, et on lui donne les tab que l'on veut insérer
    // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
    Tabs tabsEntretien = new Tabs(entretienInfosTab, referentTab, etudiantTab);

    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsEntretien.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );


    //Le formulaire concernant l'entretien
    formEntretien.add(date, etudiant, referentCfa, observations_entretien_individuel, suivreEtudiant, dateCreation, dateModification);

    //Le formulaire du référent CFA
    formReferentCFAInfos.add(nomReferentCfa, prenomReferentCfa, emailReferentCfa, civiliteReferentCfa, telephoneReferentCfa);

    // on définit les champs qu'il y aura dans le formulaire d'informations générales de l'étudiant
    formEtudiantInfos.add(nomEtudiant, prenomEtudiant, numeroEtudiant, civiliteEtudiant, dateNaissanceEtudiant, ageEtudiant, telephoneEtudiant1, telephoneEtudiant2,
            emailEtudiant, dernierDiplomeObtenuOuEnCours, niveauDernierDiplome, anneeObtentionDernierDiplome, admis, situationUnc, situationEntreprise,
            lieuNaissance, nationalite, numeroCafatEtudiant, adresseEtudiant, boitePostaleEtudiant, codePostalEtudiant, communeEtudiant, situationAnneePrecedente,
            etablissementDeProvenance, dernierEmploiOccupe, parcours, anneePromotion, travailleurHandicape, veepap, priseEnChargeFraisInscription,
            obtentionDiplomeMention, observationsEtudiant, createButtonsLayout());

    // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
    content.setSpacing(false);
    // à l'ouverture, on définit le contenu par rapport à la tab sélectionné (la première par défaut)
    setContent(tabsEntretien.getSelectedTab());

    // on ajoute la tabs, le contenu et les boutons du bas dans la Modale/Dialog
    add(tabsEntretien, titre, content, createButtonsLayout());
  }

  // Méthode appelée à l'ouverture de la vue pour alimenter les champs du formulaire.
  public void setEntretien(EntretienIndividuel entretienIndividuel) {
    this.entretienIndividuel = entretienIndividuel;
    if (entretienIndividuel != null) {
      //Transforme les dates en LocalDate et remplies les champs de dates
      dateCreation.setValue(entretienIndividuel.getCreated_at().toLocalDate());
      dateModification.setValue(entretienIndividuel.getUpdated_at().toLocalDate());
      // on passe les éléments de l'entrtien en paramètre pour les appliquer sur les champs du formulaire
      entretienIndividuelBinder.readBean(entretienIndividuel);
      etudiantBinder.readBean(entretienIndividuel.getEtudiant());
      referentCFABinder.readBean(entretienIndividuel.getReferentCfa());
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // évènements delete et close
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, entretienIndividuel)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    return new HorizontalLayout(delete, close);
  }

  // méthode qui définit quel contenu mettre en dessous des tabs
  private void setContent(Tab tab) {
    // au début on enlève tout le contenu avant remplacement
    content.removeAll();
    // on insère le contenu (formulaire) adéquat en fonction de la tab sélectionnée
    if (tab.equals(entretienInfosTab)) {
      content.add(formEntretien);
    } else if (tab.equals(etudiantTab)) {
      content.add(formEtudiantInfos);
    } else if (tab.equals(referentTab)) {
      content.add(formReferentCFAInfos);
    }
  }

  private void setAllFieldsToReadOnly() {

    //entretien
    date.setReadOnly(true);
    observations_entretien_individuel.setReadOnly(true);
    etudiant.setReadOnly(true);
    referentCfa.setReadOnly(true);
    suivreEtudiant.setReadOnly(true);
    dateCreation.setReadOnly(true);
    dateModification.setReadOnly(true);

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

    //Référent CFA
    nomReferentCfa.setReadOnly(true);
    prenomReferentCfa.setReadOnly(true);
    telephoneReferentCfa.setReadOnly(true);
    emailReferentCfa.setReadOnly(true);
    civiliteReferentCfa.setReadOnly(true);
  }

  // Event "global" (class mère), qui étend les deux events ci-dessous, dont le but est de fournir l'entretien
  // que l'on consulte dans le formulaire.
  public static abstract class EntretienConsultFormEvent extends ComponentEvent<EntretienIndividuelConsult> {
    private final EntretienIndividuel entretienIndividuel;

    protected EntretienConsultFormEvent(EntretienIndividuelConsult source, EntretienIndividuel entretienIndividuel) {
      super(source, false);
      this.entretienIndividuel = entretienIndividuel;
    }

    public EntretienIndividuel getEntretien() {
      return entretienIndividuel;
    }
  }

  // Event au clic sur le bouton de suppression (classe fille)
  public static class DeleteEvent extends EntretienConsultFormEvent {
    DeleteEvent(EntretienIndividuelConsult source, EntretienIndividuel entretienIndividuel) {
      super(source, entretienIndividuel);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends EntretienConsultFormEvent {
    CloseEvent(EntretienIndividuelConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
