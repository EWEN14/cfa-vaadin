package nc.unc.application.views.formation;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.entity.ReferentPedagogique;
import nc.unc.application.data.enums.Commune;

import java.util.List;

public class FormationNewOrEdit extends Dialog {

  private Formation formation;
  private Formation cloneFormation;

  private FormationListView formationListView;

  H3 titre = new H3();

  FormLayout form = new FormLayout();
  TextField libelleFormation = new TextField("Libellé de la formation");
  TextField codeFormation = new TextField("Code de la formation");
  TextField codeRome = new TextField("Code ROME");
  Select<Integer> niveauCertificationProfessionnelle = new Select<>(3,4,5,6,7,8);
  TextField typeEmploiExerce = new TextField("Type d'emploi occupé");
  IntegerField semainesEntreprise = new IntegerField("Nombre de semaines en entreprise");
  IntegerField heuresFormation = new IntegerField("Nombre d'heures en formation");
  IntegerField heuresProjetUniversitaire = new IntegerField("Nombre d'heures dédiées au(x) projet(s) universitaires");
  IntegerField semainesFormation = new IntegerField("Nombre de semaines en formation");
  ComboBox<String> lieuFormation = new ComboBox<>("Lieu de la formation");
  IntegerField dureeHebdomadaireTravail = new IntegerField("Durée hebdomadaire de travail");
  TextArea observations = new TextArea("Observations");
  ComboBox<ReferentPedagogique> referentPedagogique = new ComboBox<>("Responsable de la formation");
  Icon addReferentButton = new Icon(VaadinIcon.PLUS);

  Binder<Formation> binder = new BeanValidationBinder<>(Formation.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public FormationNewOrEdit(List<ReferentPedagogique> referentPedagogiqueList, FormationListView formationListView) {
    this.setWidth("85vw");

    this.formationListView = formationListView;
    binder.bindInstanceFields(this);

    niveauCertificationProfessionnelle.setLabel("Niveau de la certification professionnelle");

    codeRome.setPlaceholder("ex: M1234");
    codeRome.setPattern("[A-Z][0-9]{4}$");

    lieuFormation.setItems(Commune.getCommunesStr());

    FlexLayout content = new FlexLayout(referentPedagogique);
    content.setFlexGrow(2, referentPedagogique);
    content.setSizeFull();

    HorizontalLayout layoutReferent = new HorizontalLayout();
    layoutReferent.setSpacing(false);
    layoutReferent.add(content,addReferentButton);

    addReferentButton.addClassNames("referentbutton-contrat");
    layoutReferent.setAlignItems(FlexComponent.Alignment.CENTER);

    referentPedagogique.setItems(referentPedagogiqueList);
    referentPedagogique.setItemLabelGenerator(rp -> rp.getPrenomReferentPedago() + " " + rp.getNomReferentPedago());
    referentPedagogique.setClearButtonVisible(true);

    form.add(libelleFormation, codeFormation, codeRome, niveauCertificationProfessionnelle,
            typeEmploiExerce, semainesEntreprise, heuresFormation, heuresProjetUniversitaire, semainesFormation, lieuFormation, dureeHebdomadaireTravail,
            layoutReferent, observations, createButtonsLayout());

    // ajout du formulaire dans la modale
    add(titre, form);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new FormationNewOrEdit.CloseEvent(this)));

    //Evènement sur le click sur le bouton ajouter un tuteur
    addReferentButton.addClickListener(click -> formationListView.addReferent());

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  public void setFormation(Formation formation) {
    titre.removeAll();
    this.formation = formation;
    // on doit remettre le cloneFormation à null (sinon garde ancienne valeur de l'edit)
    this.cloneFormation = null;
    // copie de la formation si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (formation != null && formation.getId() != null) {
      titre.add("Modification d'une formation");
      try {
        this.cloneFormation = (Formation) formation.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    } else {
      titre.add("Création d'une formation");
    }
    // alimentation du binder
    binder.readBean(formation);
  }

  // fonction qui vérifie que le bean a bien une formation avec des champs valide, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(formation);
      if (this.cloneFormation == null) {
        fireEvent(new FormationNewOrEdit.SaveEvent(this, formation));
      } else {
        fireEvent(new FormationNewOrEdit.SaveEditedEvent(this, formation, cloneFormation));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  public void modifyReferents(List<ReferentPedagogique> referentPedagogiqueList){
    this.referentPedagogique.setItems(referentPedagogiqueList);
  }

  public static abstract class FormationFormEvent extends ComponentEvent<FormationNewOrEdit> {
    private final Formation formation;
    private final Formation formationOriginale;

    protected FormationFormEvent(FormationNewOrEdit source, Formation formation, Formation formationOriginale) {
      super(source, false);
      this.formation = formation;
      this.formationOriginale = formationOriginale;
    }

    public Formation getFormation() {
      return formation;
    }

    public Formation getFormationOriginale() {
      return formationOriginale;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère la formation du formulaire (classe fille)
  public static class SaveEvent extends FormationNewOrEdit.FormationFormEvent {
    SaveEvent(FormationNewOrEdit source, Formation formation) {
      super(source, formation, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère la formation du formulaire et sa version originale (avant modification)
  public static class SaveEditedEvent extends FormationNewOrEdit.FormationFormEvent {
    SaveEditedEvent(FormationNewOrEdit source, Formation formation, Formation formationOriginal) {
      super(source, formation, formationOriginal);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends FormationNewOrEdit.FormationFormEvent {
    CloseEvent(FormationNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
