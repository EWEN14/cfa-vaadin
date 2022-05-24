package nc.unc.application.views.formation;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Formation;

public class FormationConsult extends Dialog {

  // form qui contiendra les informations relatives à la formation
  private final FormLayout form = new FormLayout();
  private final TextField libelleFormation = new TextField("Libellé de la formation");
  private final TextField codeFormation = new TextField("Code de la formation");
  private final TextField codeRome = new TextField("Code ROME");
  private final IntegerField niveauCertificationProfessionnelle = new IntegerField("Niveau de la certification professionnelle");
  private final TextField typeEmploiExerce = new TextField("Type d'emploi occupé");
  private final IntegerField semainesEntreprise = new IntegerField("Nombre de semaines en entreprise");
  private final IntegerField heuresFormation = new IntegerField("Nombre d'heures en formation");
  private final IntegerField semainesFormation = new IntegerField("Nombre de semaines en formation");
  private final TextField lieuFormation = new TextField("Lieu de la formation");
  private final IntegerField dureeHebdomadaireTravail = new IntegerField("Durée hebdomadaire de travail");
  private final TextField responsableDeFormation = new TextField("Responsable de formation");
  private final TextArea observations = new TextArea("Observations");
  private final DatePicker dateCreation = new DatePicker();
  private final DatePicker dateMiseAJour = new DatePicker();
  // binder qui permettra le remplissage automatique des champs
  Binder<Formation> formationBinder = new BeanValidationBinder<>(Formation.class);

  private final Button close = new Button("Fermer");

  public FormationConsult() {
    this.setWidth("85vw");
    this.setHeight("90vh");

    // fonction qui met tous les champs en ReadOnly
    setAllFieldsToReadOnly();

    // instanciation du binder
    formationBinder.bindInstanceFields(this);

    //Labels des dates de création et mise à jour de la formation
    dateCreation.setLabel("Date de création");
    dateMiseAJour.setLabel("Date de mise à jour");

    // ajout des champs dans le formulaire
    form.add(libelleFormation, codeFormation, codeRome, niveauCertificationProfessionnelle, typeEmploiExerce,
            semainesEntreprise, heuresFormation, semainesFormation, lieuFormation, dureeHebdomadaireTravail,
            responsableDeFormation, observations);

    // ajout du formulaire dans la vue
    add(form, createButtonsLayout());
  }

  public void setFormation(Formation formation) {
    if (formation != null) {
      //Transforme les dates en LocalDate et remplies les champs de dates
      dateCreation.setValue(formation.getCreatedAt().toLocalDate());
      dateMiseAJour.setValue(formation.getUpdatedAt().toLocalDate());
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

    // évènements de fermeture de la modale
    close.addClickListener(event -> fireEvent(new FormationConsult.CloseEvent(this)));

    return new HorizontalLayout(close);
  }

  private void setAllFieldsToReadOnly() {
    libelleFormation.setReadOnly(true);
    codeFormation.setReadOnly(true);
    codeRome.setReadOnly(true);
    niveauCertificationProfessionnelle.setReadOnly(true);
    typeEmploiExerce.setReadOnly(true);
    semainesEntreprise.setReadOnly(true);
    heuresFormation.setReadOnly(true);
    semainesFormation.setReadOnly(true);
    lieuFormation.setReadOnly(true);
    dureeHebdomadaireTravail.setReadOnly(true);
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
