package nc.unc.application.views.referentPedagogique;

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
import nc.unc.application.data.entity.ReferentPedagogique;
import nc.unc.application.data.enums.Civilite;

public class ReferentPedagogiqueNewOrEdit extends Dialog {
  private ReferentPedagogique referentPedagogique;
  private ReferentPedagogique cloneReferentPedagogique;

  H3 titre = new H3();

  FormLayout formReferentPedagogiqueInfos = new FormLayout();
  TextField nomReferentPedago = new TextField("NOM");
  TextField prenomReferentPedago = new TextField("Prénom");
  Select<Civilite> civiliteReferentPedago = new Select<>();
  IntegerField telephoneReferentPedago = new IntegerField("Téléphone");
  EmailField emailReferentPedago = new EmailField("Email");

  Binder<ReferentPedagogique> binder = new BeanValidationBinder<>(ReferentPedagogique.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public ReferentPedagogiqueNewOrEdit() {
    binder.bindInstanceFields(this);
    this.setWidth("85vw");
    civiliteReferentPedago.setLabel("Civilité");
    civiliteReferentPedago.setItems(Civilite.values());

    formReferentPedagogiqueInfos.add(nomReferentPedago, prenomReferentPedago, civiliteReferentPedago, telephoneReferentPedago, emailReferentPedago, createButtonsLayout());

    add(titre, formReferentPedagogiqueInfos);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new ReferentPedagogiqueNewOrEdit.CloseEvent(this)));

    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  public void setReferentPedagogique(ReferentPedagogique referentPedagogique) {
    titre.removeAll();
    this.referentPedagogique = referentPedagogique;
    // on doit remettre le cloneReferentPedagogique à null (sinon garde ancienne valeur de l'edit)
    this.cloneReferentPedagogique = null;
    if (referentPedagogique != null && referentPedagogique.getId() != null) {
      titre.add("Modification d'un référent pédégogique");
      try {
        this.cloneReferentPedagogique = (ReferentPedagogique) referentPedagogique.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    } else {
      titre.add("Création d'un référent pédagogique");
    }

    binder.readBean(referentPedagogique);
  }

  private void validateAndSave() {
    try {
      binder.writeBean(referentPedagogique);
      if (this.cloneReferentPedagogique == null) {
        fireEvent(new ReferentPedagogiqueNewOrEdit.SaveEvent(this, referentPedagogique));
      } else {
        fireEvent(new ReferentPedagogiqueNewOrEdit.SaveEditedEvent(this, referentPedagogique, cloneReferentPedagogique));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  public static abstract class ReferentPedagogiqueFormEvent extends ComponentEvent<ReferentPedagogiqueNewOrEdit> {
    private final ReferentPedagogique referentPedagogique;
    private final ReferentPedagogique referentPedagogiqueOriginal;

    protected ReferentPedagogiqueFormEvent(ReferentPedagogiqueNewOrEdit source, ReferentPedagogique referentPedagogique, ReferentPedagogique referentPedagogiqueOriginal) {
      super(source, false);
      this.referentPedagogique = referentPedagogique;
      this.referentPedagogiqueOriginal = referentPedagogiqueOriginal;
    }

    public ReferentPedagogique getReferentPedagogique() {
      return referentPedagogique;
    }

    public ReferentPedagogique getReferentPedagogiqueOriginal() {
      return referentPedagogiqueOriginal;
    }
  }

  public static class SaveEvent extends ReferentPedagogiqueNewOrEdit.ReferentPedagogiqueFormEvent {
    SaveEvent(ReferentPedagogiqueNewOrEdit source, ReferentPedagogique referentPedagogique) {
      super(source, referentPedagogique, null);
    }
  }

  public static class SaveEditedEvent extends ReferentPedagogiqueNewOrEdit.ReferentPedagogiqueFormEvent {
    SaveEditedEvent(ReferentPedagogiqueNewOrEdit source, ReferentPedagogique referentPedagogique, ReferentPedagogique referentPedagogiqueOriginal) {
      super(source, referentPedagogique, referentPedagogiqueOriginal);
    }
  }

  public static class CloseEvent extends ReferentPedagogiqueNewOrEdit.ReferentPedagogiqueFormEvent {
    CloseEvent(ReferentPedagogiqueNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

}
