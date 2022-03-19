package nc.unc.application.views.etudiant;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.enums.Civilite;

import java.time.LocalDate;
import java.time.ZoneId;

public class EtudiantForm extends FormLayout {

  private Etudiant etudiant;
  private Etudiant cloneEtudiant;

  TextField prenom = new TextField("prenom");
  TextField nom = new TextField("nom");
  Select<Civilite> civilite = new Select<>();

  DatePicker dateNaissance = new DatePicker("Date de Naissance");

  Binder<Etudiant> binder = new BeanValidationBinder<>(Etudiant.class);

  Button save = new Button("Sauvegarder");
  Button delete = new Button("Supprimer");
  Button close = new Button("Fermer");

  public EtudiantForm() {
    addClassName("contact-form");
    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité étudiant,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    civilite.setLabel("Civilité");
    civilite.setItems(Civilite.values());

    // date picker I18n qui permet de taper à la main une date au format français
    // (si l'utilisateur veut se passer de l'utilisation du calendrier intégré)
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy","yyyy-MM-dd" );

    dateNaissance.setI18n(multiFormatI18n);
    dateNaissance.isRequired();

    // ajout des champs et des boutons d'action dans le formulaire
    add(prenom, nom, civilite, dateNaissance, createButtonsLayout());
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    close.addClickShortcut(Key.ESCAPE); // on peut appuyer sur échap pour fermer le formulaire

    // évènements save, delete, close
    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, etudiant)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, delete, close);
  }

  // fonction qui va alimenter le binder d'un étudiant
  public void setEtudiant(Etudiant etudiant)  {
    this.etudiant = etudiant;
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
        fireEvent(new SaveEvent(this, etudiant));
      } else {
        fireEvent(new SaveEditedEvent(this, etudiant, cloneEtudiant));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir le contact
  // qu'on manipule dans le formulaire
  public static abstract class EtudiantFormEvent extends ComponentEvent<EtudiantForm> {
    private final Etudiant etudiant;
    private final Etudiant etudiantOriginal;

    protected EtudiantFormEvent(EtudiantForm source, Etudiant etudiant, Etudiant etudiantOriginal) {
      super(source, false);
      this.etudiant = etudiant;
      this.etudiantOriginal = etudiantOriginal;
    }

    public Etudiant getEtudiant() {
      return etudiant;
    }

    public Etudiant getEtudiantOriginal() { return etudiantOriginal; }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
  public static class SaveEvent extends EtudiantFormEvent {
    SaveEvent(EtudiantForm source, Etudiant etudiant) {
      super(source, etudiant, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
  public static class SaveEditedEvent extends EtudiantFormEvent {
    SaveEditedEvent(EtudiantForm source, Etudiant etudiant, Etudiant etudiantOriginal) {
      super(source, etudiant, etudiantOriginal);
    }
  }

  // Event au clic sur le bouton de suppression (classe fille)
  public static class DeleteEvent extends EtudiantFormEvent {
    DeleteEvent(EtudiantForm source, Etudiant etudiant) {
      super(source, etudiant, null);
    }

  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends EtudiantFormEvent {
    CloseEvent(EtudiantForm source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
