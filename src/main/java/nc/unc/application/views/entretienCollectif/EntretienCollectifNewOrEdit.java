package nc.unc.application.views.entretienCollectif;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.*;

import java.util.List;

public class EntretienCollectifNewOrEdit extends Dialog {

  private EntretienCollectif entretienCollectif;
  private EntretienCollectif cloneEntretienCollectif;

  // form qui contient les informations générales de l'entretien collectif
  private final FormLayout formEntretien = new FormLayout();
  private final TextArea observations = new TextArea("Observations");
  private final DatePicker date = new DatePicker("Date");
  ComboBox<Formation> formation = new ComboBox<>("Formation concernée");
  ComboBox<ReferentCfa> referentCfa = new ComboBox<>("Référent CFA concerné");
  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales de l'entretien collectif
  Binder<EntretienCollectif> binder = new BeanValidationBinder<>(EntretienCollectif.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public EntretienCollectifNewOrEdit(List<ReferentCfa> referentCfas, List<Formation> formations) {
    this.setWidth("60vw");
    this.setHeight("60vh");

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité entretienCollectif,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    // date picker I18n qui permet de taper à la main une date au format français
    // (si l'utilisateur veut se passer de l'utilisation du calendrier intégré)
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");
    date.setI18n(multiFormatI18n);
    date.isRequired();

    formation.setItems(formations);
    formation.setItemLabelGenerator(formation -> formation.getLibelleFormation());
    formation.setClearButtonVisible(true);

    referentCfa.setItems(referentCfas);
    referentCfa.setItemLabelGenerator(referentCfa -> referentCfa.getNomReferentCfa()+ " " + referentCfa.getPrenomReferentCfa());
    referentCfa.setClearButtonVisible(true);

    // ajout des champs et des boutons d'action dans le formulaire
    formEntretien.add(date, formation, referentCfa, observations, createButtonsLayout());

    // ajout du formulaire dans la modale
    add(formEntretien);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new EntretienCollectifNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // fonction qui va alimenter le binder d'un entretien
  public void setEntretienCollectif(EntretienCollectif entretienCollectif) {
    this.entretienCollectif = entretienCollectif;
    // on doit remettre le cloneEntretien à null (sinon garde ancienne valeur de l'edit)
    this.cloneEntretienCollectif = null;
    // copie de l'entretien si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (this.entretienCollectif != null && this.entretienCollectif.getId() != null) {
      try {
        this.cloneEntretienCollectif = (EntretienCollectif) this.entretienCollectif.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }
    // alimentation du binder
    binder.readBean(entretienCollectif);
  }

  // fonction qui vérifie que le bean a bien un entretien, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(entretienCollectif);
      if (this.cloneEntretienCollectif == null) {
        fireEvent(new EntretienCollectifNewOrEdit.SaveEvent(this, entretienCollectif));
      } else {
        fireEvent(new EntretienCollectifNewOrEdit.SaveEditedEvent(this, entretienCollectif, cloneEntretienCollectif));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir l'entretien
  // qu'on manipule dans le formulaire
  public static abstract class EntretienFormEvent extends ComponentEvent<EntretienCollectifNewOrEdit> {
    private final EntretienCollectif entretienCollectif;
    private final EntretienCollectif entretienCollectifOriginal;

    protected EntretienFormEvent(EntretienCollectifNewOrEdit source, EntretienCollectif entretienCollectif, EntretienCollectif entretienCollectifOriginal) {
      super(source, false);
      this.entretienCollectifOriginal = entretienCollectifOriginal;
      this.entretienCollectif = entretienCollectif;
    }

    public EntretienCollectif getEntretienCollectif() {
      return entretienCollectif;
    }

    public EntretienCollectif getEntretienOriginal() {
      return entretienCollectifOriginal;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'entretien du formulaire (classe fille)
  public static class SaveEvent extends EntretienCollectifNewOrEdit.EntretienFormEvent {
    SaveEvent(EntretienCollectifNewOrEdit source, EntretienCollectif entretienCollectif) {
      super(source, entretienCollectif, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'entretien du formulaire et sa version originale (avant modification)
  public static class SaveEditedEvent extends EntretienCollectifNewOrEdit.EntretienFormEvent {
    SaveEditedEvent(EntretienCollectifNewOrEdit source, EntretienCollectif entretienCollectif, EntretienCollectif entretienCollectifOriginal) {
      super(source, entretienCollectif, entretienCollectifOriginal);
    }
  }

  // Event au clic sur le bouton de fermeture du formulaire (classe fille)
  public static class CloseEvent extends EntretienCollectifNewOrEdit.EntretienFormEvent {
    CloseEvent(EntretienCollectifNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}

