package nc.unc.application.views.evenement;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Evenement;
import nc.unc.application.data.entity.Formation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EvenementNewOrEdit extends Dialog {

  private Evenement evenement;
  private Evenement cloneEvenement;

  // form qui contiendra les informations générales relatives à l'évenement
  private final FormLayout form = new FormLayout();
  private final TextField libelle = new TextField("Libelle");
  private final TextArea description = new TextArea("Description");
  private final DatePicker dateDebut = new DatePicker("Date de début");
  private final DatePicker dateFin = new DatePicker("Date de fin");
  private final CheckboxGroup<Formation> formations = new CheckboxGroup<>();

  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales sur l'évenement
  Binder<Evenement> evenementBinder = new BeanValidationBinder<>(Evenement.class);

  private final Button save = new Button("Sauvegarder");
  private final Button close = new Button("Fermer");

  public EvenementNewOrEdit(List<Formation> lesFormations){

    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);
    this.setWidth("85vw");
    this.setHeight("90vh");

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité evenement,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    evenementBinder.bindInstanceFields(this);

    // date picker I18n qui permet de taper à la main une date au format français
    // (si l'utilisateur veut se passer de l'utilisation du calendrier intégré)
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");
    dateDebut.setI18n(multiFormatI18n);
    dateDebut.isRequired();
    dateFin.setI18n(multiFormatI18n);
    dateFin.isRequired();

    Set<Formation> setDeFormations = new HashSet<>(lesFormations);
    formations.setItems(setDeFormations);
    formations.setLabel("Formations");
    formations.setItemLabelGenerator(Formation::getLibelleFormation);
    formations.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
    evenementBinder.forField(formations)
            .asRequired("Veuillez sélectionner au moins une formation")
            .bind(Evenement::getFormations, Evenement::setFormations);

    // ajout des champs et des boutons d'action dans le formulaire
    form.add(libelle, description, dateDebut, dateFin, formations, createButtonsLayout());

    // ajout du formulaire dans la modale
    add(form);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new EvenementNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    evenementBinder.addStatusChangeListener(e -> save.setEnabled(evenementBinder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // fonction qui vérifie que le bean a bien un evenement, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      this.evenement.setFormations(formations.getSelectedItems());
      evenementBinder.writeBean(evenement);
      if (this.cloneEvenement == null) {
        fireEvent(new EvenementNewOrEdit.SaveEvent(this, this.evenement));
      } else {
        fireEvent(new EvenementNewOrEdit.SaveEditedEvent(this, this.evenement, this.cloneEvenement));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }
  
  // fonction qui va alimenter le binder d'un évenement
  public void setEvenement(Evenement evenement) {
    this.evenement = evenement;
    // on doit remettre le cloneEvenement à null (sinon garde ancienne valeur de l'edit)
    this.cloneEvenement = null;
    // copie de l'évenement si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (evenement != null && evenement.getId() != null) {
      try {
        // formations.select(this.evenement.getFormations());
        // formations.updateSelection(evenement.getFormations(), null);
        // formations.setValue(this.evenement.getFormations());
        this.cloneEvenement = (Evenement) evenement.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }
    // alimentation du binder
    evenementBinder.readBean(this.evenement);
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir l'étudiant
  // qu'on manipule dans le formulaire
  public static abstract class EvenementFormEvent extends ComponentEvent<EvenementNewOrEdit> {
    private final Evenement evenement;
    private final Evenement evenementOriginal;

    protected EvenementFormEvent(EvenementNewOrEdit source, Evenement evenement, Evenement evenementOriginal) {
      super(source, false);
      this.evenement = evenement;
      this.evenementOriginal = evenementOriginal;
    }

    public Evenement getEvenement() {
      return evenement;
    }

    public Evenement getEvenementOriginal() {
      return evenementOriginal;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'évenement du formulaire (classe fille)
  public static class SaveEvent extends EvenementNewOrEdit.EvenementFormEvent {
    SaveEvent(EvenementNewOrEdit source, Evenement evenement) {
      super(source, evenement, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'évenement du formulaire et sa version originale (avant modification)
  public static class SaveEditedEvent extends EvenementNewOrEdit.EvenementFormEvent {
    SaveEditedEvent(EvenementNewOrEdit source, Evenement evenement, Evenement evenementOriginal) {
      super(source, evenement, evenementOriginal);
    }
  }

  // Event au clic sur le bouton de fermeture du formulaire (classe fille)
  public static class CloseEvent extends EvenementNewOrEdit.EvenementFormEvent {
    CloseEvent(EvenementNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
