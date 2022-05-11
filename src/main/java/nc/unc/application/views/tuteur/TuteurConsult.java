package nc.unc.application.views.tuteur;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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
import nc.unc.application.data.service.ContratService;
import nc.unc.application.data.service.EtudiantService;

/**
 * Modale (Dialog) qui s'ouvre lorsque l'on clique sur le bouton de détail d'un tuteur
 */
public class TuteurConsult extends Dialog {

  private Tuteur tuteur;
  private EtudiantService etudiantService;
  private ContratService contratService;

  // Layout qui contiendra le contenu en dessous des tabs
  private final VerticalLayout content = new VerticalLayout();;

  // form qui contient les informations générales du tuteur
  private final FormLayout form = new FormLayout();
  private final TextField nomTuteur = new TextField("NOM");
  private final TextField prenomTuteur = new TextField("Prénom");
  private final DatePicker dateNaissanceTuteur = new DatePicker("Date de naissance");
  private final EmailField emailTuteur = new EmailField("Email");
  private final IntegerField telephoneTuteur1 = new IntegerField("Téléphone 1");
  private final IntegerField telephoneTuteur2 = new IntegerField("Téléphone 2");
  private final Select<Civilite> civiliteTuteur = new Select<>();
  private final TextField diplomeEleveObtenu = new TextField("Diplome le plus élevé obtenu");
  private final IntegerField niveauDiplome = new IntegerField("Niveau de diplôme");
  private final TextField posteOccupe = new TextField("Poste occupé");
  private final TextField anneeExperienceProfessionnelle = new TextField("Années d'expérience professionnelle");
  private final TextArea observationsTuteur = new TextArea("Observations");
  private final Checkbox casierJudiciaireFourni = new Checkbox("Casier Judiciaire fourni");
  private final Checkbox diplomeFourni = new Checkbox("Diplôme fourni");
  private final Checkbox certificatTravailFourni = new Checkbox("Certificat de Travail fourni");
  private final Checkbox cvFourni = new Checkbox("CV fourni");
  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales du tuteur
  Binder<Tuteur> tuteurBinder = new BeanValidationBinder<>(Tuteur.class);

  // form qui contiendra les informations relatives à l'entreprise dans laquelle est le tuteur
  private final FormLayout formTuteursEntrepriseInfos = new FormLayout();
  private final TextField enseigne = new TextField("Enseigne");
  private final TextField raisonSociale = new TextField("Raison Sociale");
  private final TextField statutActifEntreprise = new TextField("Statut de l'entreprise");
  private final IntegerField telephoneContactCfa = new IntegerField("Téléphone contact CFA");
  // binder qui sera utilisé pour remplir automatiquement les champs de l'entreprise liée au tuteur
  Binder<Entreprise> entrepriseBinder = new BeanValidationBinder<>(Entreprise.class);

  // grid qui contiendra les habilitations du tuteur
  private final Grid<TuteurHabilitation> tuteurHabilitationGrid = new Grid<>(TuteurHabilitation.class);
  // grid qui contiendra les etudiants cadrés par le tuteur
  private final Grid<Etudiant> tuteurEtudiantsGrid = new Grid<>(Etudiant.class);
  // grid qui contiendra les contrats liés au tuteur
  private final Grid<Contrat> contratGrid = new Grid<>(Contrat.class);

  // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
  private final Tab tuteursInfosTab = new Tab(VaadinIcon.USER.create(), new Span("Tuteur"));
  private final Tab tuteursHabilitationsTab = new Tab(VaadinIcon.DIPLOMA.create(), new Span("Habiliations"));
  private final Tab entrepriseTuteurInfosTab = new Tab(VaadinIcon.WORKPLACE.create(), new Span("Entreprise"));
  private final Tab etudiantsTuteurTab = new Tab(VaadinIcon.ACADEMY_CAP.create(), new Span("Étudiants"));
  private final Tab contratsTuteurTab = new Tab(VaadinIcon.NEWSPAPER.create(), new Span("Contrats"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer le tuteur");

  public TuteurConsult(EtudiantService etudiantService, ContratService contratService) {
    this.etudiantService = etudiantService;
    this.contratService = contratService;

    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);
    this.setWidth("85vw");
    this.setHeight("90vh");

    // instanciation des différents binder qui serviront au remplissage automatique des formulaires d'informations rattachés au tuteur
    tuteurBinder.bindInstanceFields(this);
    entrepriseBinder.bindInstanceFields(this);

    // nécessité de set les items de civilité (étant donné que ce n'est pas une des enums qui retourne des String)
    civiliteTuteur.setLabel("Civilité");
    civiliteTuteur.setItems(Civilite.values());

    // grille des habilitations du tuteur
    tuteurHabilitationGrid.addClassName("tuteur-habilitation-grid");
    tuteurHabilitationGrid.setColumns("dateFormation","formation.libelleFormation", "statutFormation", "modaliteFormation", "dateHabilitation");

    // grille des étudiants cadrés par le tuteur
    tuteurEtudiantsGrid.addClassName("tuteur-etudiants-grid");
    tuteurEtudiantsGrid.setColumns("prenomEtudiant", "nomEtudiant", "telephoneEtudiant1", "emailEtudiant");

    // grilles des contrats liées au tuteur
    contratGrid.addClassName("tuteur-contrats-grid");
    contratGrid.setColumns("codeContrat", "debutContrat", "finContrat", "numeroConventionFormation");
    contratGrid.addColumn(contrat -> contrat.getEtudiant().getPrenomEtudiant() + " " + contrat.getEtudiant().getNomEtudiant()).setHeader("Étudiant Salarié").setSortable(true);
    contratGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    // Méthode qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
    setAllFieldsToReadOnly();

    // On instancie la Tabs, et on lui donne les tab que l'on veut insérer
    // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
    Tabs tabsTuteurs = new Tabs(tuteursInfosTab, tuteursHabilitationsTab, entrepriseTuteurInfosTab, etudiantsTuteurTab, contratsTuteurTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsTuteurs.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    // on définit les champs qu'il y aura dans le formulaire d'informations générales de l'étudiant
    form.add(nomTuteur, prenomTuteur, dateNaissanceTuteur, emailTuteur, civiliteTuteur, telephoneTuteur1, telephoneTuteur2, diplomeEleveObtenu, niveauDiplome, posteOccupe,
            anneeExperienceProfessionnelle, observationsTuteur, casierJudiciaireFourni, diplomeFourni, certificatTravailFourni,
            cvFourni, createButtonsLayout());

    // pareil, mais pour le formulaire relatif à son entreprise
    formTuteursEntrepriseInfos.add(enseigne, raisonSociale, statutActifEntreprise, telephoneContactCfa);

    // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
    content.setSpacing(false);
    // à l'ouverture, on définit le contenu par rapport à la tab sélectionné (la première par défaut)
    setContent(tabsTuteurs.getSelectedTab());

    // on ajoute la tabs, le contenu et les boutons du bas dans la Modale/Dialog
    add(tabsTuteurs, content, createButtonsLayout());
  }

  // Méthode appelée à l'ouverture de la vue pour alimenter les champs du formulaire.
  public void setTuteur(Tuteur tuteur) {
    this.tuteur = tuteur;
    if (tuteur != null) {
      // on passe les éléments du tuteur en paramètre pour les appliquer sur les champs du formulaire
      tuteurBinder.readBean(tuteur);
      entrepriseBinder.readBean(tuteur.getEntreprise());

      // On passe les habilitations du tuteur à la grille affichant les habilitations
      tuteurHabilitationGrid.setItems(tuteur.getTuteurHabilitations());

      // on récupère les étudiants qui ont le tuteur avec l'id correspondant
      // et on les passe à la grille d'étudiants encadré par le tuteur
      tuteurEtudiantsGrid.setItems(etudiantService.findAllEtudiantsTuteur(tuteur.getId()));

      // on récupère les contrats qui ont le tuteur avec l'id correspondant et on les passe à la grid
      contratGrid.setItems(contratService.findAllContratByTuteurId(tuteur.getId()));
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // évènements delete et close
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, tuteur)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    return new HorizontalLayout(delete, close);
  }

  // méthode qui définit quel contenu mettre en dessous des tabs
  private void setContent(Tab tab) {
    // au début on enlève tout le contenu avant remplacement
    content.removeAll();
    // on insère le contenu (formulaire) adéquat en fonction de la tab sélectionnée
    if (tab.equals(tuteursInfosTab)) {
      content.add(form);
    } else if (tab.equals(tuteursHabilitationsTab)) {
      content.add(tuteurHabilitationGrid);
    } else if (tab.equals(entrepriseTuteurInfosTab)) {
      content.add(formTuteursEntrepriseInfos);
    } else if (tab.equals(etudiantsTuteurTab)) {
      content.add(tuteurEtudiantsGrid);
    } else if (tab.equals(contratsTuteurTab)) {
      content.add(contratGrid);
    }
  }

  private void setAllFieldsToReadOnly() {
    // champs d'infos générales du tuteur
    nomTuteur.setReadOnly(true);
    prenomTuteur.setReadOnly(true);
    dateNaissanceTuteur.setReadOnly(true);
    emailTuteur.setReadOnly(true);
    telephoneTuteur1.setReadOnly(true);
    telephoneTuteur2.setReadOnly(true);
    civiliteTuteur.setReadOnly(true);
    diplomeEleveObtenu.setReadOnly(true);
    niveauDiplome.setReadOnly(true);
    posteOccupe.setReadOnly(true);
    anneeExperienceProfessionnelle.setReadOnly(true);
    observationsTuteur.setReadOnly(true);
    // entreprise
    enseigne.setReadOnly(true);
    raisonSociale.setReadOnly(true);
    statutActifEntreprise.setReadOnly(true);
    telephoneContactCfa.setReadOnly(true);
  }

  // Event "global" (class mère), qui étend les deux events ci-dessous, dont le but est de fournir le tuteur
  // que l'on consulte dans le formulaire.
  public static abstract class TuteurConsultFormEvent extends ComponentEvent<TuteurConsult> {
    private final Tuteur tuteur;

    protected TuteurConsultFormEvent(TuteurConsult source, Tuteur tuteur) {
      super(source, false);
      this.tuteur = tuteur;
    }

    public Tuteur getTuteur() {
      return tuteur;
    }
  }

  // Event au clic sur le bouton de suppression (classe fille)
  public static class DeleteEvent extends TuteurConsultFormEvent {
    DeleteEvent(TuteurConsult source, Tuteur tuteur) {
      super(source, tuteur);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends TuteurConsultFormEvent {
    CloseEvent(TuteurConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
