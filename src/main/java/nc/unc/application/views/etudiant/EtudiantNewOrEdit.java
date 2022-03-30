package nc.unc.application.views.etudiant;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.enums.Civilite;
import nc.unc.application.data.enums.Parcours;
import nc.unc.application.data.enums.SituationAnneePrecedente;

import java.util.List;

public class EtudiantNewOrEdit extends Dialog {

  private Etudiant etudiant;
  private Etudiant cloneEtudiant;

  FormLayout form = new FormLayout();
  TextField prenom = new TextField("Prénom");
  TextField nom = new TextField("NOM");
  Select<Civilite> civilite = new Select<>();
  Select<String> situationAnneePrecedente = new Select<>();
  Select<String> parcours = new Select<>();
  ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");

  DatePicker dateNaissance = new DatePicker("Date de Naissance");

  Binder<Etudiant> binder = new BeanValidationBinder<>(Etudiant.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public EtudiantNewOrEdit(List<Entreprise> entreprises) {
    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité étudiant,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    // définition du label de civilite, et alimentation en valeurs de l'enum Civilite
    civilite.setLabel("Civilité");
    civilite.setItems(Civilite.values());

    // définition du label de civilite, et alimentation en valeurs de l'enum SituationAnneePrecedente,
    // mais de la version "chaîne de caractère" de chaque énumération
    situationAnneePrecedente.setLabel("Situation Année Précédente");
    situationAnneePrecedente.setItems(SituationAnneePrecedente.getSituationAnneePrecedenteStr());

    // date picker I18n qui permet de taper à la main une date au format français
    // (si l'utilisateur veut se passer de l'utilisation du calendrier intégré)
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");
    dateNaissance.setI18n(multiFormatI18n);
    dateNaissance.isRequired();

    parcours.setLabel("Parcours");
    parcours.setItems(Parcours.getParcoursStr());

    entreprise.setItems(entreprises);
    entreprise.setItemLabelGenerator(Entreprise::getEnseigne);
    entreprise.setClearButtonVisible(true);

    // ajout des champs et des boutons d'action dans le formulaire
    form.add(prenom, nom, civilite, dateNaissance, situationAnneePrecedente, parcours, entreprise, createButtonsLayout());

    add(form);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new EtudiantNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // fonction qui va alimenter le binder d'un étudiant
  public void setEtudiant(Etudiant etudiant) {
    this.etudiant = etudiant;
    // on doit remettre le cloneEtudiant à null (sinon garde ancienne valeur de l'edit)
    this.cloneEtudiant = null;
    // copie de l'étudiant si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (etudiant != null && etudiant.getId() != null) {
      try {
        this.cloneEtudiant = (Etudiant) etudiant.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }
    // alimentation du binder
    binder.readBean(etudiant);
  }

  // fonction qui vérifie que le bean a bien un étudiant, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(etudiant);
      if (this.cloneEtudiant == null) {
        fireEvent(new EtudiantNewOrEdit.SaveEvent(this, etudiant));
      } else {
        fireEvent(new EtudiantNewOrEdit.SaveEditedEvent(this, etudiant, cloneEtudiant));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir l'étudiant
  // qu'on manipule dans le formulaire
  public static abstract class EtudiantFormEvent extends ComponentEvent<EtudiantNewOrEdit> {
    private final Etudiant etudiant;
    private final Etudiant etudiantOriginal;

    protected EtudiantFormEvent(EtudiantNewOrEdit source, Etudiant etudiant, Etudiant etudiantOriginal) {
      super(source, false);
      this.etudiant = etudiant;
      this.etudiantOriginal = etudiantOriginal;
    }

    public Etudiant getEtudiant() {
      return etudiant;
    }

    public Etudiant getEtudiantOriginal() {
      return etudiantOriginal;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
  public static class SaveEvent extends EtudiantNewOrEdit.EtudiantFormEvent {
    SaveEvent(EtudiantNewOrEdit source, Etudiant etudiant) {
      super(source, etudiant, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
  public static class SaveEditedEvent extends EtudiantNewOrEdit.EtudiantFormEvent {
    SaveEditedEvent(EtudiantNewOrEdit source, Etudiant etudiant, Etudiant etudiantOriginal) {
      super(source, etudiant, etudiantOriginal);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends EtudiantNewOrEdit.EtudiantFormEvent {
    CloseEvent(EtudiantNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}

