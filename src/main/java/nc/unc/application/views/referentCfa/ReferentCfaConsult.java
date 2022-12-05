package nc.unc.application.views.referentCfa;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.ReferentCfa;
import nc.unc.application.data.enums.Civilite;

public class ReferentCfaConsult extends Dialog {
  private ReferentCfa referentCfa;

  private final H3 titre = new H3("Consultation d'un Référent CFA");

  private final FormLayout formReferentCfaInfos = new FormLayout();
  private final TextField nomReferentCfa = new TextField("NOM");
  private final TextField prenomReferentCfa = new TextField("Prénom");
  private final IntegerField telephoneReferentCfa = new IntegerField("Téléphone");
  private final EmailField emailReferentCfa = new EmailField("Email");
  private final Select<Civilite> civiliteReferentCfa = new Select<>();
  Binder<ReferentCfa> referentCfaBinder = new BeanValidationBinder<>(ReferentCfa.class);
  private final DatePicker dateCreation = new DatePicker("Date de création");
  private final DatePicker dateMiseAJour = new DatePicker("Date de mise à jour");

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer le référent CFA");

  public ReferentCfaConsult() {
    this.setModal(true);
    this.setWidth("85vw");
    this.setHeight("55vh");

    referentCfaBinder.bindInstanceFields(this);

    setAllFieldsToReadOnly();

    civiliteReferentCfa.setLabel("Civilité");
    civiliteReferentCfa.setItems(Civilite.values());

    formReferentCfaInfos.add(nomReferentCfa, prenomReferentCfa, telephoneReferentCfa, emailReferentCfa, civiliteReferentCfa,
            dateCreation, dateMiseAJour);

    add(titre, formReferentCfaInfos, createButtonsLayout());
  }

  public void setReferentCfa(ReferentCfa referentCfa) {
    this.referentCfa = referentCfa;
    if (referentCfa != null) {
      // Transforme les dates en LocalDate et remplies les champs de dates
      dateCreation.setValue(referentCfa.getCreated_at().toLocalDate());
      dateMiseAJour.setValue(referentCfa.getUpdated_at().toLocalDate());

      referentCfaBinder.readBean(referentCfa);
    }
  }

  private HorizontalLayout createButtonsLayout() {
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, referentCfa)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    return new HorizontalLayout(delete, close);
  }

  private void setAllFieldsToReadOnly() {
    nomReferentCfa.setReadOnly(true);
    prenomReferentCfa.setReadOnly(true);
    telephoneReferentCfa.setReadOnly(true);
    emailReferentCfa.setReadOnly(true);
    civiliteReferentCfa.setReadOnly(true);
  }

  public void hideDeleteButton() {
    delete.setVisible(false);
  }

  public static abstract class ReferentCfaConsultFormEvent extends ComponentEvent<ReferentCfaConsult> {
    private final ReferentCfa referentCfa;

    protected ReferentCfaConsultFormEvent(ReferentCfaConsult source, ReferentCfa referentCfa) {
      super(source, false);
      this.referentCfa = referentCfa;
    }

    public ReferentCfa getReferentCfa() {
      return referentCfa;
    }

  }

  public static class DeleteEvent extends ReferentCfaConsultFormEvent {
    DeleteEvent(ReferentCfaConsult source, ReferentCfa referentCfa) {
      super(source, referentCfa);
    }
  }

  public static class CloseEvent extends ReferentCfaConsultFormEvent {
    CloseEvent(ReferentCfaConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
