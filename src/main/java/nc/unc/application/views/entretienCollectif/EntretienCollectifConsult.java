package nc.unc.application.views.entretienCollectif;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import nc.unc.application.data.entity.EntretienCollectif;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.entity.ReferentCfa;
import nc.unc.application.data.enums.Civilite;
import nc.unc.application.data.service.EntretienCollectifService;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.data.service.ReferentCfaService;

/**
 * Modale (Dialog) qui s'ouvre lorsque l'on clique sur le bouton de détail d'un entretien collectif
 */
public class EntretienCollectifConsult extends Dialog {

  private EntretienCollectifService entretienCollectifService;
  private FormationService formationService;
  private ReferentCfaService referentCfaService;
  private EntretienCollectif entretienCollectif;

  H3 titre = new H3("Consultation d'un entretien collectif");

  // Layout qui contiendra le contenu en dessous des tabs
  private final VerticalLayout content = new VerticalLayout();

  // form qui contient les informations générales de l'entretien collectif
  private final FormLayout formEntretien = new FormLayout();
  private final TextArea observations_entretien_collectif = new TextArea("Observations");
  private final DatePicker date = new DatePicker();
  ComboBox<Formation> formation = new ComboBox<>("Formation concerné par l'entretien");
  ComboBox<ReferentCfa> referentCfa = new ComboBox<>("Référent CFA concerné par l'entretien");
  private final DatePicker dateCreation = new DatePicker();
  private final DatePicker dateModification = new DatePicker();
  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales de l'entretien collectif
  Binder<EntretienCollectif> entretienCollectifBinder = new BeanValidationBinder<>(EntretienCollectif.class);

  // form qui contiendra les informations relatives à la formation
  private final FormLayout form = new FormLayout();
  private final TextField libelleFormation = new TextField("Libellé de la formation");
  private final TextField codeFormation = new TextField("Code de la formation");
  private final TextField codeRome = new TextField("Code ROME");
  private final IntegerField niveauCertificationProfessionnelle = new IntegerField("Niveau de la certification professionnelle");
  private final TextField typeEmploiExerce = new TextField("Type d'emploi occupé");
  private final IntegerField semainesEntreprise = new IntegerField("Nombre de semaines en entreprise");
  private final IntegerField heuresFormation = new IntegerField("Nombre d'heures en formation");
  private final IntegerField semainesFormation = new IntegerField("Nombre de semaines en formation");
  private final TextField lieuFormation = new TextField("Lieu de la formation");
  private final IntegerField dureeHebdomadaireTravail = new IntegerField("Durée hebdomadaire de travail");
  private final TextField responsableDeFormation = new TextField("Responsable de formation");
  private final TextArea observations = new TextArea("Observations");
  // binder qui permettra le remplissage automatique des champs
  Binder<Formation> formationBinder = new BeanValidationBinder<>(Formation.class);

  // form qui contiendra les informations relatives au référent cfa concerné par l'entretien
  private final FormLayout formReferentCFAInfos = new FormLayout();
  private final TextField nomReferentCfa = new TextField("NOM");
  private final TextField prenomReferentCfa = new TextField("Prénom");
  private final IntegerField telephoneReferentCfa = new IntegerField("Téléphone");
  private final EmailField emailReferentCfa = new EmailField("Email");
  private final Select<Civilite> civiliteReferentCfa = new Select<>();
  Binder<ReferentCfa> referentCFABinder = new BeanValidationBinder<>(ReferentCfa.class);

  // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
  private final Tab entretienInfosTab = new Tab(VaadinIcon.USER.create(), new Span("Entretien collectif"));
  private final Tab referentTab = new Tab(VaadinIcon.DIPLOMA.create(), new Span("Referent CFA"));
  private final Tab formationTab = new Tab(VaadinIcon.ACADEMY_CAP.create(), new Span("Formation"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer l'entretien");

  public EntretienCollectifConsult(FormationService formationService, ReferentCfaService referentCfaService, EntretienCollectifService entretienCollectifService) {
    this.formationService = formationService;
    this.entretienCollectifService = entretienCollectifService;
    this.referentCfaService = referentCfaService;

    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);
    this.setWidth("85vw");
    this.setHeight("90vh");

    // instanciation des différents binder qui serviront au remplissage automatique des formulaires d'informations rattachés à l'entretien
    formationBinder.bindInstanceFields(this);
    referentCFABinder.bindInstanceFields(this);
    entretienCollectifBinder.bindInstanceFields(this);

    //Labels des dates de création et mise à jour de l'entretien collectif
    formation.setItems(formationService.findAllFormations(null));
    formation.setItemLabelGenerator(formation -> formation.getLibelleFormation());
    formation.setClearButtonVisible(true);

    referentCfa.setItems(referentCfaService.findAllReferentCfa(null));
    referentCfa.setItemLabelGenerator(referentCfa -> referentCfa.getNomReferentCfa()+ " " + referentCfa.getPrenomReferentCfa());
    referentCfa.setClearButtonVisible(true);

    dateCreation.setLabel("Date de création");
    dateModification.setLabel("Date de mise à jour");

    // nécessité de set les items de civilité (étant donné que ce n'est pas une des enums qui retourne des String)
    civiliteReferentCfa.setLabel("Civilité");
    civiliteReferentCfa.setItems(Civilite.values());

    // Méthode qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
    setAllFieldsToReadOnly();

    // On instancie la Tabs, et on lui donne les tab que l'on veut insérer
    // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
    Tabs tabsEntretien = new Tabs(entretienInfosTab, referentTab, formationTab);

    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsEntretien.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );


    //Le formulaire concernant l'entretien
    formEntretien.add(date, formation, referentCfa, observations_entretien_collectif);

    //Le formulaire du référent CFA
    formReferentCFAInfos.add(nomReferentCfa, prenomReferentCfa, emailReferentCfa, civiliteReferentCfa, telephoneReferentCfa);

    // on définit les champs qu'il y aura dans le formulaire d'informations générales de la formation
    // ajout des champs dans le formulaire
    form.add(libelleFormation, codeFormation, codeRome, niveauCertificationProfessionnelle, typeEmploiExerce,
            semainesEntreprise, heuresFormation, semainesFormation, lieuFormation, dureeHebdomadaireTravail,
            responsableDeFormation, observations);

    // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
    content.setSpacing(false);
    // à l'ouverture, on définit le contenu par rapport à la tab sélectionné (la première par défaut)
    setContent(tabsEntretien.getSelectedTab());

    // on ajoute la tabs, le contenu et les boutons du bas dans la Modale/Dialog
    add(tabsEntretien, titre, content, createButtonsLayout());
  }

  // Méthode appelée à l'ouverture de la vue pour alimenter les champs du formulaire.
  public void setEntretien(EntretienCollectif entretienCollectif) {
    this.entretienCollectif = entretienCollectif;
    if (entretienCollectif != null) {
      //Transforme les dates en LocalDate et remplies les champs de dates
      dateCreation.setValue(entretienCollectif.getCreated_at().toLocalDate());
      dateModification.setValue(entretienCollectif.getUpdated_at().toLocalDate());
      // on passe les éléments de l'entrtien en paramètre pour les appliquer sur les champs du formulaire
      entretienCollectifBinder.readBean(entretienCollectif);
      formationBinder.readBean(entretienCollectif.getFormation());
      referentCFABinder.readBean(entretienCollectif.getReferentCfa());
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // évènements delete et close
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, entretienCollectif)));
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
    } else if (tab.equals(formationTab)) {
      content.add(form);
    } else if (tab.equals(referentTab)) {
      content.add(formReferentCFAInfos);
    }
  }

  private void setAllFieldsToReadOnly() {

    //entretien
    date.setReadOnly(true);
    observations_entretien_collectif.setReadOnly(true);
    formation.setReadOnly(true);
    referentCfa.setReadOnly(true);
    dateCreation.setReadOnly(true);
    dateModification.setReadOnly(true);

    // formation
    libelleFormation.setReadOnly(true);
    codeFormation.setReadOnly(true);
    codeRome.setReadOnly(true);
    niveauCertificationProfessionnelle.setReadOnly(true);
    typeEmploiExerce.setReadOnly(true);
    semainesEntreprise.setReadOnly(true);
    heuresFormation.setReadOnly(true);
    semainesFormation.setReadOnly(true);
    lieuFormation.setReadOnly(true);
    dureeHebdomadaireTravail.setReadOnly(true);
    responsableDeFormation.setReadOnly(true);
    observations.setReadOnly(true);

    //Référent CFA
    nomReferentCfa.setReadOnly(true);
    prenomReferentCfa.setReadOnly(true);
    telephoneReferentCfa.setReadOnly(true);
    emailReferentCfa.setReadOnly(true);
    civiliteReferentCfa.setReadOnly(true);
  }

  // Event "global" (class mère), qui étend les deux events ci-dessous, dont le but est de fournir l'entretien
  // que l'on consulte dans le formulaire.
  public static abstract class EntretienConsultFormEvent extends ComponentEvent<EntretienCollectifConsult> {
    private final EntretienCollectif entretienCollectif;

    protected EntretienConsultFormEvent(EntretienCollectifConsult source, EntretienCollectif entretienCollectif) {
      super(source, false);
      this.entretienCollectif = entretienCollectif;
    }

    public EntretienCollectif getEntretien() {
      return entretienCollectif;
    }
  }

  // Event au clic sur le bouton de suppression (classe fille)
  public static class DeleteEvent extends EntretienConsultFormEvent {
    DeleteEvent(EntretienCollectifConsult source, EntretienCollectif entretienCollectif) {
      super(source, entretienCollectif);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends EntretienConsultFormEvent {
    CloseEvent(EntretienCollectifConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
