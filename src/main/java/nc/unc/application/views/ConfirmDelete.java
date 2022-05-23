package nc.unc.application.views;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class ConfirmDelete extends Dialog {

  private VerticalLayout content = new VerticalLayout();

  private Div messageConfirmation = new Div();

  private HorizontalLayout buttonContainer = new HorizontalLayout();

  private Button confirmer = new Button("Confirmer");
  private Button annuler = new Button("Annuler");

  public ConfirmDelete(String str) {

    messageConfirmation.removeAll();
    messageConfirmation.add(new Span("Voulez-vous vraiment supprimer " + str + " ?"));

    confirmer.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    annuler.addThemeVariants(ButtonVariant.LUMO_ERROR);

    confirmer.addClickListener(event -> fireEvent(new ConfirmDelete.DeleteEventGrid(this, true)));
    annuler.addClickListener(event -> fireEvent(new ConfirmDelete.DeleteEventGrid(this, false)));

    buttonContainer.add(confirmer, annuler);

    content.add(messageConfirmation, buttonContainer);

    this.add(content);
  }

  public static abstract class ConfirmDeleteEvent extends ComponentEvent<ConfirmDelete> {
    private final Boolean suppression;

    protected ConfirmDeleteEvent(ConfirmDelete source, Boolean suppression) {
      super(source, false);
      this.suppression = suppression;
    }

    public Boolean getSuppression() {
      return suppression;
    }
  }

  public static class DeleteEventGrid extends ConfirmDeleteEvent {
    DeleteEventGrid(ConfirmDelete source, Boolean suppression) {
      super(source, suppression);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
