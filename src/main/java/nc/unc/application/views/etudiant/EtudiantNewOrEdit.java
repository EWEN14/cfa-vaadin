package nc.unc.application.views.etudiant;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.enums.*;

import java.util.List;

public class EtudiantNewOrEdit extends Dialog {

  private Etudiant etudiant;
  private Etudiant cloneEtudiant;

  FormLayout form = new FormLayout();
  TextField nom = new TextField("NOM");
  TextField prenom = new TextField("Prénom");
  // utilisation de select lorsque nombre de choix assez petis
  Select<Civilite> civilite = new Select<>();
  DatePicker dateNaissance = new DatePicker("Date de Naissance");
  IntegerField telephone1 = new IntegerField("Téléphone 1");
  IntegerField telephone2 = new IntegerField("Téléphone 2");
  EmailField email = new EmailField("Email");
  // utilisation de comboBox lorsque qu'il y a beaucoup de choix, l'utilisateur peut ainsi chercher en tapant dans
  // le champ pour filtrer parmi les options.
  ComboBox<String> dernierDiplomeObtenuOuEnCours = new ComboBox<>("Dernier diplôme obtenu ou en cours");
  Select<Integer> niveauDernierDiplome = new Select<>();
  IntegerField anneeObtentionDernierDiplome = new IntegerField("Année d'obtention du dernier diplôme");
  Select<String> admis = new Select<>();
  Select<String> situationUnc = new Select<>();
  Select<String> situationEntreprise = new Select<>();
  ComboBox<String> lieuNaissance = new ComboBox<>("Lieu de naissance");
  Select<String> nationalite = new Select<>();
  IntegerField numeroCafat = new IntegerField("Numéro Cafat");
  TextField adresse = new TextField("Adresse");
  TextField boitePostale = new TextField("Boîte Postale");
  IntegerField codePostal = new IntegerField("Code Postal");
  ComboBox<String> commune = new ComboBox<>("Commune");
  Select<String> situationAnneePrecedente = new Select<>();
  ComboBox<String> etablissementDeProvenance = new ComboBox<>("Établissement de provenance");
  Select<String> parcours = new Select<>();
  Checkbox travailleurHandicape = new Checkbox("Travailleur Handicapé");
  Checkbox veepap = new Checkbox("VEEPAP");
  Select<String> priseEnChargeFraisInscription = new Select<>();
  Select<String> obtentionDiplomeMention = new Select<>();
  ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");
  TextArea observations = new TextArea("Observations");

  Binder<Etudiant> binder = new BeanValidationBinder<>(Etudiant.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");

  public EtudiantNewOrEdit(List<Entreprise> entreprises) {
    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité étudiant,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    // définition du label de civilite, et alimentation en valeurs de l'enum Civilite
    civilite.setLabel("Civilité");
    civilite.setItems(Civilite.values());

    // date picker I18n qui permet de taper à la main une date au format français
    // (si l'utilisateur veut se passer de l'utilisation du calendrier intégré)
    DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
    multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");
    dateNaissance.setI18n(multiFormatI18n);
    dateNaissance.isRequired();

    dernierDiplomeObtenuOuEnCours.setItems(DernierDiplomeObtenu.getDiplomeStr());
    dernierDiplomeObtenuOuEnCours.setClearButtonVisible(true);

    niveauDernierDiplome.setLabel("Niveau dernier diplôme obtenu ou en cours");
    niveauDernierDiplome.setItems(1,2,3,4,5,6,7,8);

    admis.setLabel("Admission");
    admis.setItems(Admis.getAdmisStr());

    situationUnc.setLabel("Situation à l'UNC");
    situationUnc.setItems(SituationUnc.getSituationsStr());

    situationEntreprise.setLabel("Situation en entreprise");
    situationEntreprise.setItems(SituationEntreprise.getSituationsStr());

    lieuNaissance.setItems(Commune.getCommunesStr());
    lieuNaissance.setClearButtonVisible(true);

    nationalite.setLabel("Nationalité");
    nationalite.setItems(Nationalite.getNationalitesStr());

    commune.setItems(Commune.getCommunesStr());
    commune.setClearButtonVisible(true);

    // alimentation en valeurs de l'enum SituationAnneePrecedente,
    // mais de la version "chaîne de caractère" de chaque énumération
    situationAnneePrecedente.setLabel("Situation Année Précédente");
    situationAnneePrecedente.setItems(SituationAnneePrecedente.getSituationAnneePrecedenteStr());

    etablissementDeProvenance.setItems(EtablissementProvenance.getEtablissementProvenanceStr());
    etablissementDeProvenance.setClearButtonVisible(true);

    parcours.setLabel("Parcours");
    parcours.setItems(Parcours.getParcoursStr());

    priseEnChargeFraisInscription.setLabel("Prise en charge des frais d'inscription");
    priseEnChargeFraisInscription.setItems(PriseChargeFraisInscription.getSituationsStr());

    obtentionDiplomeMention.setLabel("Obtention du diplôme et mention");
    obtentionDiplomeMention.setItems(ObtentionDiplome.getObtentionDiplomeStr());

    entreprise.setItems(entreprises);
    entreprise.setItemLabelGenerator(Entreprise::getEnseigne);
    entreprise.setClearButtonVisible(true);

    // ajout des champs et des boutons d'action dans le formulaire
    form.add(nom, prenom, civilite, dateNaissance, telephone1, telephone2, email, dernierDiplomeObtenuOuEnCours,
            niveauDernierDiplome, anneeObtentionDernierDiplome, admis, situationUnc, lieuNaissance, nationalite,
            numeroCafat, adresse, boitePostale, codePostal, commune, situationAnneePrecedente, etablissementDeProvenance,
            parcours, travailleurHandicape, veepap, priseEnChargeFraisInscription, obtentionDiplomeMention, entreprise,
            observations, createButtonsLayout());

    // ajout du formulaire dans la modale
    add(form);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new EtudiantNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // fonction qui va alimenter le binder d'un étudiant
  public void setEtudiant(Etudiant etudiant) {
    this.etudiant = etudiant;
    // on doit remettre le cloneEtudiant à null (sinon garde ancienne valeur de l'edit)
    this.cloneEtudiant = null;
    // copie de l'étudiant si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (etudiant != null && etudiant.getId() != null) {
      try {
        this.cloneEtudiant = (Etudiant) etudiant.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }
    // alimentation du binder
    binder.readBean(etudiant);
  }

  // fonction qui vérifie que le bean a bien un étudiant, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(etudiant);
      if (this.cloneEtudiant == null) {
        fireEvent(new EtudiantNewOrEdit.SaveEvent(this, etudiant));
      } else {
        fireEvent(new EtudiantNewOrEdit.SaveEditedEvent(this, etudiant, cloneEtudiant));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir l'étudiant
  // qu'on manipule dans le formulaire
  public static abstract class EtudiantFormEvent extends ComponentEvent<EtudiantNewOrEdit> {
    private final Etudiant etudiant;
    private final Etudiant etudiantOriginal;

    protected EtudiantFormEvent(EtudiantNewOrEdit source, Etudiant etudiant, Etudiant etudiantOriginal) {
      super(source, false);
      this.etudiant = etudiant;
      this.etudiantOriginal = etudiantOriginal;
    }

    public Etudiant getEtudiant() {
      return etudiant;
    }

    public Etudiant getEtudiantOriginal() {
      return etudiantOriginal;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
  public static class SaveEvent extends EtudiantNewOrEdit.EtudiantFormEvent {
    SaveEvent(EtudiantNewOrEdit source, Etudiant etudiant) {
      super(source, etudiant, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
  public static class SaveEditedEvent extends EtudiantNewOrEdit.EtudiantFormEvent {
    SaveEditedEvent(EtudiantNewOrEdit source, Etudiant etudiant, Etudiant etudiantOriginal) {
      super(source, etudiant, etudiantOriginal);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends EtudiantNewOrEdit.EtudiantFormEvent {
    CloseEvent(EtudiantNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}

