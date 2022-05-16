package nc.unc.application.views.entreprise;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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
import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.service.ContratService;
import nc.unc.application.data.service.EtudiantService;
import nc.unc.application.data.service.TuteurService;

public class EntrepriseConsult extends Dialog {

  private Entreprise entreprise;
  private final EtudiantService etudiantService;
  private final TuteurService tuteurService;
  private final ContratService contratService;

  // Layout qui contiendra le contenu en dessous des tabs
  private VerticalLayout content = new VerticalLayout();

  // form qui contiendra les informations générales relatives à l'entreprise
  private final FormLayout form = new FormLayout();

  private final TextField statutActifEntreprise = new TextField("Satut de l'entreprise");
  private final TextField enseigne = new TextField("Enseigne de l'entreprise");
  private final TextField raisonSociale = new TextField("Raison sociale de l'entreprise");
  private final TextField numeroRidet = new TextField("Numéro de ridet de l'entreprise");
  private final TextField formeJuridique = new TextField("Forme juridique de l'entreprise");
  private final IntegerField numeroCafatEntreprise = new IntegerField("Numéro Cafat");
  private final IntegerField nombreSalarie = new IntegerField("Nombre de salariés");
  private final TextField codeNaf = new TextField("Code NAF");
  private final TextField activiteEntreprise = new TextField("Domaine d'activité de l'entreprise");
  private final TextField conventionCollective = new TextField("Convention collective");
  private final TextField prenomRepresentantEmployeur = new TextField("Prénom du représentant de l'employeur");
  private final TextField nomRepresentantEmployeur = new TextField("NOM du représentant de l'employeur");
  private final TextField fonctionRepresentantEmployeur = new TextField("Fonction du représentant de l'employeur");
  private final IntegerField telephoneEntreprise = new IntegerField("Numéro de téléphone de l'entreprise");
  private final EmailField emailEntreprise = new EmailField("Email de l'entreprise");
  private final TextField prenomContactCfa = new TextField("Prénom du contact au CFA");
  private final TextField nomContactCfa = new TextField("NOM du contact au CFA");
  private final TextField fonctionContactCfa = new TextField("Fonction du contact au CFA");
  private final IntegerField telephoneContactCfa = new IntegerField("Téléphone du contact au CFA");
  private final EmailField emailContactCfa = new EmailField("Email du contact au CFA");
  private final TextField adressePhysiqueCommune = new TextField("Commune de l'entreprise (adresse physique)");
  private final IntegerField adressePhysiqueCodePostal = new IntegerField("Code Postal (adresse physique)");
  private final TextField adressePhysiqueRue = new TextField("Adresse/Rue (adresse physique)");
  private final TextField adressePostaleCommune = new TextField("Commune de l'entreprise (adresse postale)");
  private final IntegerField adressePostaleCodePostal = new IntegerField("Code Postal (adresse postale)");
  private final TextField adressePostaleRueOuBp = new TextField("Adresse/Rue ou Boîte Postale (adresse postale)");
  private final TextArea observations = new TextArea("Observations");
  // binder qui sera utilisé pour remlir automatiquement les champs d'infos propres à l'entreprise
  Binder<Entreprise> entrepriseBinder = new BeanValidationBinder<>(Entreprise.class);

  // grid qui contiendra les etudiants en contrat (ou l'ayant été) avec l'entreprise
  private final Grid<Etudiant> entrepriseEtudiantGrid = new Grid<>(Etudiant.class);
  // grid qui contiendra les tuteurs travaillant pour l'entreprise
  private final Grid<Tuteur> entrepriseTuteurGrid = new Grid<>(Tuteur.class);
  // grid qui contiendra les tuteurs travaillant pour l'entreprise
  private final Grid<Contrat> entrepriseContratGrid = new Grid<>(Contrat.class);

  // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
  private final Tab entrepriseInfosTab = new Tab(VaadinIcon.WORKPLACE.create(), new Span("Entreprise"));
  private final Tab etudiantsEntrepriseTab = new Tab(VaadinIcon.ACADEMY_CAP.create(), new Span("Étudiants"));
  private final Tab tuteursEntrepriseTab = new Tab(VaadinIcon.USER.create(), new Span("Tuteur"));
  private final Tab contratsEntrepriseTab = new Tab(VaadinIcon.NEWSPAPER.create(), new Span("Contrat"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer l'entreprise");

  public EntrepriseConsult(EtudiantService etudiantService, TuteurService tuteurService, ContratService contratService) {

    this.etudiantService = etudiantService;
    this.tuteurService = tuteurService;
    this.contratService = contratService;

    this.setWidth("85vw");
    this.setHeight("90vh");

    setAllFieldsToReadOnly();

    // instanciation du binder qui servira au remplissage automatique des champs
    entrepriseBinder.bindInstanceFields(this);

    // grille des étudiants
    entrepriseEtudiantGrid.addClassName("entreprise-etudiants-grid");
    entrepriseEtudiantGrid.setColumns("prenomEtudiant", "nomEtudiant", "telephoneEtudiant1", "emailEtudiant");
    entrepriseEtudiantGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    // grille des tuteurs
    entrepriseTuteurGrid.addClassName("entreprise-tuteurs-grid");
    entrepriseTuteurGrid.setColumns("prenomTuteur", "nomTuteur", "telephoneTuteur1", "emailTuteur");
    entrepriseTuteurGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    // grille des contrats
    entrepriseContratGrid.addClassName("entreprise-contrats-grid");
    entrepriseContratGrid.setColumns("codeContrat", "debutContrat", "finContrat", "numeroConventionFormation");
    entrepriseContratGrid.addColumn(contrat -> contrat.getTuteur().getPrenomTuteur() + " " + contrat.getTuteur().getNomTuteur()).setHeader("Tuteur").setSortable(true);
    entrepriseContratGrid.addColumn(contrat -> contrat.getEtudiant().getPrenomEtudiant() + " " + contrat.getEtudiant().getNomEtudiant()).setHeader("Étudiant").setSortable(true);
    entrepriseContratGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
    Tabs tabsEntreprise = new Tabs(entrepriseInfosTab, etudiantsEntrepriseTab, tuteursEntrepriseTab, contratsEntrepriseTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsEntreprise.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab()));

    form.add(statutActifEntreprise, enseigne, raisonSociale, numeroRidet, formeJuridique, numeroCafatEntreprise, nombreSalarie, codeNaf, activiteEntreprise,
            conventionCollective, prenomRepresentantEmployeur, nomRepresentantEmployeur, fonctionRepresentantEmployeur,
            telephoneEntreprise, emailEntreprise, prenomContactCfa, nomContactCfa, fonctionContactCfa, telephoneContactCfa,
            emailContactCfa, adressePhysiqueCommune, adressePhysiqueCodePostal, adressePhysiqueRue, adressePostaleCommune,
            adressePostaleCodePostal, adressePostaleRueOuBp, observations);

    content.setSpacing(false);
    // à l'ouverture, on ouvre la tab d'infos générales sur l'entreprise
    setContent(entrepriseInfosTab);

    add(tabsEntreprise, content, createButtonsLayout());
  }

  // Méthode appelée à l'ouverture de la vue pour alimenter les champs du formulaire.
  public void setEntreprise(Entreprise entreprise) {
    this.entreprise = entreprise;
    if (entreprise != null) {
      entrepriseBinder.readBean(entreprise);

      // on récupère les étudiants qui ont l'entreprise avec l'id correspondant
      // et on les passe à la grille d'étudiants
      entrepriseEtudiantGrid.setItems(etudiantService.findAllEtudiantsByEntrepriseId(entreprise.getId()));

      // on récupère les tuteurs qui ont l'entreprise  avec l'id correspondant et on les passe à la grid
      entrepriseTuteurGrid.setItems(tuteurService.findAllTuteursByEntrepriseId(entreprise.getId()));

      // on récupère les contrats qui ont l'entreprise avec l'id correspondant et on les passe à la grid
      entrepriseContratGrid.setItems(contratService.findAllContratByEntrepriseId(entreprise.getId()));
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // évènements delete et close
    delete.addClickListener(event -> fireEvent(new EntrepriseConsult.DeleteEvent(this, entreprise)));
    close.addClickListener(event -> fireEvent(new EntrepriseConsult.CloseEvent(this)));

    return new HorizontalLayout(delete, close);
  }

  // méthode qui définit quel contenu mettre en dessous des tabs
  private void setContent(Tab tab) {
    // au début on enlève tout le contenu avant remplacement
    content.removeAll();
    // on insère le contenu (formulaire) adéquat en fonction de la tab sélectionnée
    if (tab.equals(entrepriseInfosTab)) {
      content.add(form);
    } else if (tab.equals(etudiantsEntrepriseTab)) {
      content.add(entrepriseEtudiantGrid);
    } else if (tab.equals(tuteursEntrepriseTab)) {
      content.add(entrepriseTuteurGrid);
    } else if (tab.equals(contratsEntrepriseTab)) {
      content.add(entrepriseContratGrid);
    }
  }

  private void setAllFieldsToReadOnly() {
    statutActifEntreprise.setReadOnly(true);
    enseigne.setReadOnly(true);
    raisonSociale.setReadOnly(true);
    numeroRidet.setReadOnly(true);
    formeJuridique.setReadOnly(true);
    numeroCafatEntreprise.setReadOnly(true);
    nombreSalarie.setReadOnly(true);
    codeNaf.setReadOnly(true);
    activiteEntreprise.setReadOnly(true);
    conventionCollective.setReadOnly(true);
    prenomRepresentantEmployeur.setReadOnly(true);
    nomRepresentantEmployeur.setReadOnly(true);
    fonctionRepresentantEmployeur.setReadOnly(true);
    telephoneEntreprise.setReadOnly(true);
    emailEntreprise.setReadOnly(true);
    prenomContactCfa.setReadOnly(true);
    nomContactCfa.setReadOnly(true);
    fonctionContactCfa.setReadOnly(true);
    telephoneContactCfa.setReadOnly(true);
    emailContactCfa.setReadOnly(true);
    adressePhysiqueCommune.setReadOnly(true);
    adressePhysiqueCodePostal.setReadOnly(true);
    adressePhysiqueRue.setReadOnly(true);
    adressePostaleCommune.setReadOnly(true);
    adressePostaleCodePostal.setReadOnly(true);
    adressePostaleRueOuBp.setReadOnly(true);
    observations.setReadOnly(true);
  }

  public static abstract class EntrepriseConsultFormEvent extends ComponentEvent<EntrepriseConsult> {
    private final Entreprise entreprise;

    protected EntrepriseConsultFormEvent(EntrepriseConsult source, Entreprise entreprise) {
      super(source, false);
      this.entreprise = entreprise;
    }

    public Entreprise getEntreprise() {
      return entreprise;
    }
  }

  public static class DeleteEvent extends EntrepriseConsultFormEvent {
    DeleteEvent(EntrepriseConsult source, Entreprise entreprise) {
      super(source, entreprise);
    }
  }

  public static class CloseEvent extends EntrepriseConsultFormEvent {
    CloseEvent(EntrepriseConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
