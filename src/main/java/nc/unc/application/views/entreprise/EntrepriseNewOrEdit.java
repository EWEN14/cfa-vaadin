package nc.unc.application.views.entreprise;

import com.vaadin.flow.component.BlurNotifier;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.enums.Commune;
import nc.unc.application.data.enums.StatutActifEntreprise;
import nc.unc.application.data.service.EntrepriseService;

public class EntrepriseNewOrEdit extends Dialog {

  private Entreprise entreprise;
  private Entreprise cloneEntreprise;

  FormLayout form = new FormLayout();

  H3 titre = new H3();

  Select<String> statutActifEntreprise = new Select<>(StatutActifEntreprise.getStatutActifEntrepriseStr());
  TextField enseigne = new TextField("Enseigne de l'entreprise");
  TextField raisonSociale = new TextField("Raison sociale de l'entreprise");
  TextField numeroRidet = new TextField("Numéro de ridet de l'entreprise");
  TextField formeJuridique = new TextField("Forme juridique de l'entreprise");
  IntegerField numeroCafatEntreprise = new IntegerField("Numéro Cafat");
  IntegerField nombreSalarie = new IntegerField("Nombre de salariés");
  TextField codeNaf = new TextField("Code NAF");
  TextField activiteEntreprise = new TextField("Domaine d'activité de l'entreprise");
  TextField conventionCollective = new TextField("Convention collective");
  TextField prenomRepresentantEmployeur = new TextField("Prénom du représentant de l'employeur");
  TextField nomRepresentantEmployeur = new TextField("NOM du représentant de l'employeur");
  TextField fonctionRepresentantEmployeur = new TextField("Fonction du représentant de l'employeur");
  IntegerField telephoneEntreprise = new IntegerField("Numéro de téléphone de l'entreprise");
  EmailField emailEntreprise = new EmailField("Email de l'entreprise");
  TextField prenomContactCfa = new TextField("Prénom du contact au CFA");
  TextField nomContactCfa = new TextField("NOM du contact au CFA");
  TextField fonctionContactCfa = new TextField("Fonction du contact au CFA");
  IntegerField telephoneContactCfa = new IntegerField("Téléphone du contact au CFA");
  EmailField emailContactCfa = new EmailField("Email du contact au CFA");
  ComboBox<String> adressePhysiqueCommune = new ComboBox<>("Commune de l'entreprise (adresse physique)");
  IntegerField adressePhysiqueCodePostal = new IntegerField("Code Postal (adresse physique)");
  TextField adressePhysiqueRue = new TextField("Adresse/Rue (adresse physique)");
  ComboBox<String> adressePostaleCommune = new ComboBox<>("Commune de l'entreprise (adresse postale)");
  IntegerField adressePostaleCodePostal = new IntegerField("Code Postal (adresse postale)");
  TextField adressePostaleRueOuBp = new TextField("Adresse/Rue ou Boîte Postale (adresse postale)");
  TextArea observations = new TextArea("Observations");

  Binder<Entreprise> binder = new BeanValidationBinder<>(Entreprise.class);

  Button save = new com.vaadin.flow.component.button.Button("Sauvegarder");
  Button close = new Button("Fermer");

  EntrepriseService entrepriseService;

  public EntrepriseNewOrEdit(EntrepriseService entrepriseService) {
    this.setWidth("85vw");
    this.setHeight("90vh");

    this.entrepriseService = entrepriseService;

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    statutActifEntreprise.setLabel("Statut de l'entreprise");

    adressePhysiqueCommune.setItems(Commune.getCommunesStr());
    adressePhysiqueCommune.setClearButtonVisible(true);

    adressePostaleCommune.setItems(Commune.getCommunesStr());
    adressePostaleCommune.setClearButtonVisible(true);

    // regex pour le ridet:
    numeroRidet.setHelperText("ex: 0 000 000.000");
    numeroRidet.setPattern("\\d\\s\\d{3}\\s\\d{3}\\.\\d{3}$");
    numeroRidet.setValueChangeMode(ValueChangeMode.ON_BLUR);
    numeroRidet.addBlurListener(change ->
      checkExistingRidet());

    codeNaf.setHelperText("ex: 12.3 ou 12.34 ou 12.34Z");

    form.add(numeroRidet, statutActifEntreprise, enseigne, raisonSociale, formeJuridique, numeroCafatEntreprise, nombreSalarie, codeNaf, activiteEntreprise,
            conventionCollective, prenomRepresentantEmployeur, nomRepresentantEmployeur, fonctionRepresentantEmployeur,
            telephoneEntreprise, emailEntreprise, prenomContactCfa, nomContactCfa, fonctionContactCfa, telephoneContactCfa,
            emailContactCfa, adressePhysiqueCommune, adressePhysiqueCodePostal, adressePhysiqueRue, adressePostaleCommune,
            adressePostaleCodePostal, adressePostaleRueOuBp, observations);

    this.add(titre, form, createButtonsLayout());
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new EntrepriseNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // fonction qui va alimenter le binder d'une entreprise
  public void setEntreprise(Entreprise entreprise) {
    titre.removeAll();
    this.entreprise = entreprise;
    // on doit remettre le cloneEntreprise à null (sinon garde ancienne valeur de l'edit)
    this.cloneEntreprise = null;
    // copie de l'entreprise si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (entreprise != null && entreprise.getId() != null) {
      try {
        this.cloneEntreprise = (Entreprise) entreprise.clone();
        titre.add("Modification d'une entreprise");
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    } else {
      titre.add("Création d'une nouvelle entreprise");
    }
    // alimentation du binder
    binder.readBean(entreprise);
  }

  // fonction qui vérifie que le bean a bien une entreprise valide, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(entreprise);
      if (this.cloneEntreprise == null) {
        fireEvent(new EntrepriseNewOrEdit.SaveEvent(this, entreprise));
      } else {
        fireEvent(new EntrepriseNewOrEdit.SaveEditedEvent(this, entreprise, cloneEntreprise));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // fonction qui vérifie que le ridet entrée n'est pas déjà existant dans le cas d'une création d'entreprise
  private void checkExistingRidet() {
    if (this.entreprise.getId() == null) {
      if (this.entrepriseService.findEntrepriseWithRidet(numeroRidet.getValue())) {
        Notification notification = Notification
                .show("Une entreprise avec ce numéro de ridet existe déjà!", 10000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      };
    }
  }

  public static abstract class EntrepriseFormEvent extends ComponentEvent<EntrepriseNewOrEdit> {
    private final Entreprise entreprise;
    private final Entreprise entrepriseOriginal;

    protected EntrepriseFormEvent(EntrepriseNewOrEdit source, Entreprise entreprise, Entreprise entrepriseOriginal) {
      super(source, false);
      this.entreprise = entreprise;
      this.entrepriseOriginal = entrepriseOriginal;
    }

    public Entreprise getEntreprise() {
      return entreprise;
    }

    public Entreprise getEntrepriseOriginal() {
      return entrepriseOriginal;
    }
  }

  public static class SaveEvent extends EntrepriseNewOrEdit.EntrepriseFormEvent {
    SaveEvent(EntrepriseNewOrEdit source, Entreprise entreprise) {
      super(source, entreprise, null);
    }
  }

  public static class SaveEditedEvent extends EntrepriseNewOrEdit.EntrepriseFormEvent {
    SaveEditedEvent(EntrepriseNewOrEdit source, Entreprise entreprise, Entreprise entrepriseOriginal) {
      super(source, entreprise, entrepriseOriginal);
    }
  }

  public static class CloseEvent extends EntrepriseNewOrEdit.EntrepriseFormEvent {
    CloseEvent(EntrepriseNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
