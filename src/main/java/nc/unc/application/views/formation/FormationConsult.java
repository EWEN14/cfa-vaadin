package nc.unc.application.views.formation;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.views.etudiant.EtudiantConsult;

public class FormationConsult extends Dialog {

  private Formation formation;

  // form qui contiendra les informations relatives à la formation
  private final FormLayout form = new FormLayout();
  private final TextField libelleFormation = new TextField("Libellé de la formation");
  private final TextField codeFormation = new TextField("Code de la formation");
  private final TextField codeRome = new TextField("Code ROME");
  private final TextField responsableDeFormation = new TextField("Responsable de formation");
  private final TextArea observations = new TextArea("Observations");
  // binder qui permettra le remplissage automatique des champs
  Binder<Formation> formationBinder = new BeanValidationBinder<>(Formation.class);

  private final Button close = new Button("Fermer");

  public FormationConsult() {
    this.setWidth("85vw");

    // fonction qui met tous les champs en ReadOnly
    setAllFieldsToReadOnly();

    // instanciation du binder
    formationBinder.bindInstanceFields(this);

    // ajout des champs dans le formulaire
    form.add(libelleFormation, codeFormation, codeRome, responsableDeFormation, observations);

    // ajout du formulaire dans la vue
    add(form);
  }

  public void setFormation(Formation formation) {
    this.formation = formation;
    if (formation != null) {
      // alimentation du binder
      formationBinder.readBean(formation);
      // normalement il y aura toujours un referent pédagogique, mais par précaution ajout d'un if/else
      if (formation.getReferentPedagogique() != null) {
        responsableDeFormation.setValue(formation.getReferentPedagogique().getPrenomReferentPedago() + " " + formation.getReferentPedagogique().getNomReferentPedago());
      } else {
        responsableDeFormation.setValue("");
      }
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements delete et close
    close.addClickListener(event -> fireEvent(new FormationConsult.CloseEvent(this)));

    return new HorizontalLayout(close);
  }

  private void setAllFieldsToReadOnly() {
    libelleFormation.setReadOnly(true);
    codeFormation.setReadOnly(true);
    codeRome.setReadOnly(true);
    responsableDeFormation.setReadOnly(true);
    observations.setReadOnly(true);
  }

  public static abstract class FormationFormEvent extends ComponentEvent<FormationConsult> {

    protected FormationFormEvent(FormationConsult source) {
      super(source, false);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends FormationConsult.FormationFormEvent {
    CloseEvent(FormationConsult source) {
      super(source);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
