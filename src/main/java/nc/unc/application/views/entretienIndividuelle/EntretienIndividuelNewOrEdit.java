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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.EntretienIndividuel;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.ReferentCfa;

import java.util.List;

public class EntretienIndividuelNewOrEdit extends Dialog {

  private EntretienIndividuel entretienIndividuel;
  private EntretienIndividuel cloneEntretienIndividuel;

  H3 titre = new H3();

  // form qui contient les informations générales de l'entretien individuel
  private final FormLayout formEntretien = new FormLayout();
  private final TextArea observations_entretien_individuel = new TextArea("Observations");
  private final DatePicker date = new DatePicker("Date");
  private final Checkbox suivreEtudiant = new Checkbox("Suivre l'étudiant ?");
  ComboBox<Etudiant> etudiant = new ComboBox<>("Etudiant concerné par l'entretien");
  ComboBox<ReferentCfa> referentCfa = new ComboBox<>("Référent CFA concerné par l'entretien");
  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales de l'entretien individuel
  Binder<EntretienIndividuel> binder = new BeanValidationBinder<>(EntretienIndividuel.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public EntretienIndividuelNewOrEdit(List<ReferentCfa> referentCfas, List<Etudiant> etudiants) {
    this.setWidth("60vw");
    this.setHeight("55vh");

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité entretienIndividuel,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    // date picker I18n qui permet de taper à la main une date au format français
    // (si l'utilisateur veut se passer de l'utilisation du calendrier intégré)
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");
    date.setI18n(multiFormatI18n);
    date.isRequired();

    etudiant.setItems(etudiants);
    etudiant.setItemLabelGenerator(etudiant -> etudiant.getNomEtudiant()+ " " + etudiant.getPrenomEtudiant());
    etudiant.setClearButtonVisible(true);

    referentCfa.setItems(referentCfas);
    referentCfa.setItemLabelGenerator(referentCfa -> referentCfa.getNomReferentCfa()+ " " + referentCfa.getPrenomReferentCfa());
    referentCfa.setClearButtonVisible(true);

    // ajout des champs et des boutons d'action dans le formulaire
    formEntretien.add(date, etudiant, referentCfa, observations_entretien_individuel, suivreEtudiant, createButtonsLayout());

    // ajout du formulaire dans la modale
    add(titre, formEntretien);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new EntretienIndividuelNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // fonction qui va alimenter le binder d'un entretien
  public void setEntretienIndividuel(EntretienIndividuel entretienIndividuel) {
    this.titre.removeAll();
    this.entretienIndividuel = entretienIndividuel;
    // on doit remettre le cloneEntretien à null (sinon garde ancienne valeur de l'edit)
    this.cloneEntretienIndividuel = null;
    // copie de l'entretien si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (this.entretienIndividuel != null && this.entretienIndividuel.getId() != null) {
      titre.add("Modification d'un entretien individuel");
      try {
        this.cloneEntretienIndividuel = (EntretienIndividuel) this.entretienIndividuel.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    } else {
      titre.add("Création d'un entretien individuel");
    }
    // alimentation du binder
    binder.readBean(entretienIndividuel);
  }

  // fonction qui vérifie que le bean a bien un entretien, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(entretienIndividuel);
      if (this.cloneEntretienIndividuel == null) {
        fireEvent(new EntretienIndividuelNewOrEdit.SaveEvent(this, entretienIndividuel));
      } else {
        fireEvent(new EntretienIndividuelNewOrEdit.SaveEditedEvent(this, entretienIndividuel, cloneEntretienIndividuel));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir l'entretien
  // qu'on manipule dans le formulaire
  public static abstract class EntretienFormEvent extends ComponentEvent<EntretienIndividuelNewOrEdit> {
    private final EntretienIndividuel entretienIndividuel;
    private final EntretienIndividuel entretienIndividuelOriginal;

    protected EntretienFormEvent(EntretienIndividuelNewOrEdit source, EntretienIndividuel entretienIndividuel, EntretienIndividuel entretienIndividuelOriginal) {
      super(source, false);
      this.entretienIndividuelOriginal = entretienIndividuelOriginal;
      this.entretienIndividuel = entretienIndividuel;
    }

    public EntretienIndividuel getEntretienIndividuel() {
      return entretienIndividuel;
    }

    public EntretienIndividuel getEntretienOriginal() {
      return entretienIndividuelOriginal;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'entretien du formulaire (classe fille)
  public static class SaveEvent extends EntretienIndividuelNewOrEdit.EntretienFormEvent {
    SaveEvent(EntretienIndividuelNewOrEdit source, EntretienIndividuel entretienIndividuel) {
      super(source, entretienIndividuel, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'entretien du formulaire et sa version originale (avant modification)
  public static class SaveEditedEvent extends EntretienIndividuelNewOrEdit.EntretienFormEvent {
    SaveEditedEvent(EntretienIndividuelNewOrEdit source, EntretienIndividuel entretienIndividuel, EntretienIndividuel entretienIndividuelOriginal) {
      super(source, entretienIndividuel, entretienIndividuelOriginal);
    }
  }

  // Event au clic sur le bouton de fermeture du formulaire (classe fille)
  public static class CloseEvent extends EntretienIndividuelNewOrEdit.EntretienFormEvent {
    CloseEvent(EntretienIndividuelNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}

