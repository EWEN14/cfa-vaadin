package nc.unc.application.views.etudiant;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Etudiant;

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
  private final TextField prenom = new TextField("Prénom");
  private final TextField nom = new TextField("NOM");
  private final TextField civilite = new TextField("Civilité");
  private final TextField dateNaissance = new TextField("Date de Naissance");
  private final TextField situationAnneePrecedente = new TextField("Situation Année Précédente");
  private final TextField parcours = new TextField("Parcours");

  // form qui contiendra les informations relatives à l'entreprise dans laquelle est l'étudiant
  private final FormLayout formEtudiantEntrepriseInfos = new FormLayout();
  private final TextField entrepriseEnseigne = new TextField("Enseigne");
  private final TextField entrepriseRaisonSociale = new TextField("Raison Sociale");

  // tab (onglet) qui seront insérés dans une tabs (ensemble d'onglets) les regroupant
  private final Tab etudiantInfosTab = new Tab(VaadinIcon.ACADEMY_CAP.create(),new Span("Étudiant"));
  private final Tab entrepriseEtudiantInfosTab = new Tab(VaadinIcon.WORKPLACE.create(),new Span("Entreprise"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer");

  public EtudiantConsult() {
    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);

    // On instancie la Tabs, et on lui donne les tab que l'on veut insérer
    // tabs qui contiendra les tab permettant de passer d'un groupe d'informations à un autre
    Tabs tabsEtudiant = new Tabs(etudiantInfosTab, entrepriseEtudiantInfosTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsEtudiant.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    // on définit les champs qu'il y aura dans le formulaire d'informations générales de l'étudiant
    formEtudiantInfos.add(prenom, nom, civilite, dateNaissance, situationAnneePrecedente, parcours);
    // pareil, mais pour le formulaire relative à son entreprise
    formEtudiantEntrepriseInfos.add(entrepriseEnseigne, entrepriseRaisonSociale);

    // contenu qui sera affiché en dessous des tabs, qui change en fonction de la tab sélectionné
    content = new VerticalLayout();
    content.setSpacing(false);
    // à l'ouverture, on définit le contenu par rapport à la tab sélectionné (la première par défaut)
    setContent(tabsEtudiant.getSelectedTab());

    // on ajoute la tabs, le contenu et les boutons du bas dans la Modale/Dialog
    add(tabsEtudiant, content, createButtonsLayout());
  }

  // méthode appelé à l'ouverture de la vue pour alimenter les champs du formulaire.
  // Ici pas besoin de binder étant donné que l'on ne fait que consulter les informations (pas de sauvegarde)
  public void setEtudiant(Etudiant etudiant) {
    this.etudiant = etudiant;
    if (etudiant != null) {
      prenom.setValue(etudiant.getPrenom());
      nom.setValue(etudiant.getNom());
      civilite.setValue(etudiant.getCivilite().toString());
      // affichage date au format français
      dateNaissance.setValue(etudiant.getDateNaissance().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
      situationAnneePrecedente.setValue(etudiant.getSituationAnneePrecedente());
      if (etudiant.getParcours() != null) {
        parcours.setValue(etudiant.getParcours());
      } else {
        parcours.setValue("");
      }

      // si l'étudiant a une entreprise, on passe les infos relatives à l'entreprise en formulaire
      // et on affiche la tab "Entreprise", sinon on la masque
      if (etudiant.getEntreprise() != null) {
        entrepriseEtudiantInfosTab.setVisible(true);
        entrepriseEnseigne.setValue(etudiant.getEntreprise().getEnseigne());
        entrepriseRaisonSociale.setValue(etudiant.getEntreprise().getRaisonSociale());
      } else {
       entrepriseEtudiantInfosTab.setVisible(false);
      }
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
    }
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
