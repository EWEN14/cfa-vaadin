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
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Evenement;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.service.EvenementService;

import java.util.List;

public class EvenementConsult extends Dialog {

  private Evenement evenement;

  private final H3 titre = new H3("Consultation d'un événement");

  // form qui contiendra les informations générales relatives à l'évenement
  private final FormLayout form = new FormLayout();
  private final TextField libelle = new TextField("Libelle");
  private final TextArea description = new TextArea("Description");
  private final DatePicker dateDebut = new DatePicker("Date de début");
  private final DatePicker dateFin = new DatePicker("Date de fin");
  CheckboxGroup<Formation> formations = new CheckboxGroup<>();

  // binder qui sera utilisé pour remplir automatiquement les champs d'infos générales sur l'évenement
  Binder<Evenement> binder = new BeanValidationBinder<>(Evenement.class);

  private final Button delete = new Button("Supprimer l'évenement");
  private final Button close = new Button("Fermer");

  private final EvenementService evenementService;

  public EvenementConsult(EvenementService evenementService, List<Formation> lesFormations){
    this.evenementService = evenementService;
    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);
    this.setWidth("85vw");
    this.setHeight("50vh");

    // fonction qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
    setAllFieldsToReadOnly();

    binder.bindInstanceFields(this);

    formations.setItems(lesFormations);
    formations.setLabel("Formations");
    formations.setItemLabelGenerator(Formation::getLibelleFormation);
    formations.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

    // ajout des champs et des boutons d'action dans le formulaire
    form.add(libelle, description, dateDebut, dateFin, formations, createButtonsLayout());

    // ajout du titre et du formulaire dans la modale
    add(titre, form);
  }

  // méthode appelée à l'ouverture de la vue pour alimenter les champs du formulaire.
  public void setEvenement(Evenement evenement) {
    this.evenement = evenement;
    if (evenement != null) {
      // lecture du binder pour compléter les champs dans le formulaire
      binder.readBean(evenement);
      // formations.select(evenement.getFormations());
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // évènements delete et close
    delete.addClickListener(event -> fireEvent(new EvenementConsult.DeleteEvent(this, evenement)));
    close.addClickListener(event -> fireEvent(new EvenementConsult.CloseEvent(this)));

    return new HorizontalLayout(delete, close);
  }

  private void setAllFieldsToReadOnly(){
    libelle.setReadOnly(true);
    description.setReadOnly(true);
    dateDebut.setReadOnly(true);
    dateFin.setReadOnly(true);
    formations.setReadOnly(true);
  }

  public void hideDeleteButton() {
    delete.setVisible(false);
  }

  // Event "global" (class mère), qui étend les deux events ci-dessous, dont le but est de fournir l'évenement
  // que l'on consulte dans le formulaire.
  public static abstract class EvenementConsultFormEvent extends ComponentEvent<EvenementConsult> {
    private final Evenement evenement;

    protected EvenementConsultFormEvent(EvenementConsult source, Evenement evenement) {
      super(source, false);
      this.evenement = evenement;
    }

    public Evenement getEvenement() {
      return evenement;
    }
  }

  // Event au clic sur le bouton de suppression (classe fille)
  public static class DeleteEvent extends EvenementConsult.EvenementConsultFormEvent {
    DeleteEvent(EvenementConsult source, Evenement evenement) {
      super(source, evenement);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends EvenementConsult.EvenementConsultFormEvent {
    CloseEvent(EvenementConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
