package nc.unc.application.views.referentCfa;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.ReferentCfa;
import nc.unc.application.data.entity.ReferentPedagogique;
import nc.unc.application.data.enums.Civilite;

public class ReferentCfaNewOrEdit extends Dialog {
  private ReferentCfa referentCfa;
  private ReferentCfa cloneReferentCfa;

  H3 titre = new H3();

  FormLayout formReferentCfaInfos = new FormLayout();
  TextField nomReferentCfa = new TextField("NOM");
  TextField prenomReferentCfa = new TextField("Prénom");
  Select<Civilite> civiliteReferentCfa = new Select<>();
  IntegerField telephoneReferentCfa = new IntegerField("Téléphone");
  EmailField emailReferentCfa = new EmailField("Email");
  Binder<ReferentCfa> binder = new BeanValidationBinder<>(ReferentCfa.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public ReferentCfaNewOrEdit() {
    binder.bindInstanceFields(this);
    this.setWidth("85vw");
    civiliteReferentCfa.setLabel("Civilité");
    civiliteReferentCfa.setItems(Civilite.values());

    formReferentCfaInfos.add(nomReferentCfa, prenomReferentCfa, civiliteReferentCfa, telephoneReferentCfa, emailReferentCfa, createButtonsLayout());

    add(titre, formReferentCfaInfos);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new ReferentCfaNewOrEdit.CloseEvent(this)));

    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  public void setReferentCfa(ReferentCfa referentCfa) {
    titre.removeAll();
    this.referentCfa = referentCfa;
    this.cloneReferentCfa = null;

    if (referentCfa != null && referentCfa.getId() != null) {
      titre.add("Modification d'un référent CFA");
      try {
        this.cloneReferentCfa = (ReferentCfa) referentCfa.clone();

      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    } else {
      titre.add("Création d'un référent CFA");
    }

    binder.readBean(referentCfa);
  }

  private void validateAndSave() {
    try {
      binder.writeBean(referentCfa);
      if (this.cloneReferentCfa == null) {
        fireEvent(new ReferentCfaNewOrEdit.SaveEvent(this, referentCfa));
      } else {
        fireEvent(new ReferentCfaNewOrEdit.SaveEditedEvent(this, referentCfa, cloneReferentCfa));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  public static abstract class ReferentCfaFormEvent extends ComponentEvent<ReferentCfaNewOrEdit> {
    private final ReferentCfa referentCfa;
    private final ReferentCfa referentCfaOriginal;

    protected ReferentCfaFormEvent(ReferentCfaNewOrEdit source, ReferentCfa referentCfa, ReferentCfa referentCfaOriginal) {
      super(source, false);
      this.referentCfa = referentCfa;
      this.referentCfaOriginal = referentCfaOriginal;
    }

    public ReferentCfa getReferentCfa() {
      return referentCfa;
    }

    public ReferentCfa getReferentCfaOriginal() {
      return referentCfaOriginal;
    }
  }

  public static class SaveEvent extends ReferentCfaNewOrEdit.ReferentCfaFormEvent {
    SaveEvent(ReferentCfaNewOrEdit source, ReferentCfa referentCfa) {
      super(source, referentCfa, null);
    }
  }

  public static class SaveEditedEvent extends ReferentCfaNewOrEdit.ReferentCfaFormEvent {
    SaveEditedEvent(ReferentCfaNewOrEdit source, ReferentCfa referentCfa, ReferentCfa referentCfaOriginal) {
      super(source, referentCfa, referentCfaOriginal);
    }
  }

  public static class CloseEvent extends ReferentCfaNewOrEdit.ReferentCfaFormEvent {
    CloseEvent(ReferentCfaNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

}
