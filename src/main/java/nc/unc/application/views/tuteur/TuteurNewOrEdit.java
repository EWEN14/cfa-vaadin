package nc.unc.application.views.tuteur;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import  com.vaadin.flow.component.checkbox.Checkbox;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.Decua;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class TuteurNewOrEdit extends Dialog {

        //Objets tuteurs
        private Tuteur tuteur;
        private Tuteur cloneTuteur;

        //Eléments de notre formulaire
        TextField prenom = new TextField("Prenom");
        TextField nom = new TextField("Nom");
        DatePicker dateNaissance = new DatePicker("Date de Naissance");
        EmailField email = new EmailField("Email");
        TextField diplomeEleveObtenu = new TextField("Diplome elevé obtenu");
        Select<Long> niveauDiplome = new Select<>();
        Select<Decua> pieceJointe1 = new Select<>();
        TextField posteOccupe = new TextField("Poste occupé");
        Select<Decua> pieceJointe2 = new Select<>();
        TextField anneeExperienceProfessionnelle = new TextField("Année expérience professionnelle");
        Checkbox casierJudiciaireFourni = new Checkbox("Casier judiciaire fournit");
        IntegerField telephone1 = new IntegerField("Téléphone 1");
        IntegerField telephone2 = new IntegerField("Téléphone 2");
        ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");
        FormLayout form = new FormLayout();


        //Notre binder
        Binder<Tuteur> binder = new BeanValidationBinder<>(Tuteur.class);

        //Nos boutons(sauvegarder, supprimer, annuler)
        Button save = new Button("Sauvegarder");
        Button delete = new Button("Supprimer");
        Button close = new Button("Fermer");

        public TuteurNewOrEdit(List<Entreprise> entreprises) {
            // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité étudiant,
            // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
            binder.bindInstanceFields(this);

            //Remplissage de nos select
            niveauDiplome.setLabel("Niveau de diplôme");
            niveauDiplome.setItems(1L,2L,3L,4L,5L,6L,7L,8L);
            niveauDiplome.setValue(1L);
            pieceJointe1.setLabel("Pièce jointe 1");
            pieceJointe1.setItems(Decua.values());
            pieceJointe2.setLabel("Pièce jointe 2");
            pieceJointe2.setItems(Decua.values());

            entreprise.setItems(entreprises);
            entreprise.setItemLabelGenerator(Entreprise::getEnseigne);

            // date picker I18n qui permet de taper à la main une date au format français
            // (si l'utilisateur veut se passer de l'utilisation du calendrier intégré)
            DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
            multiFormatI18n.setDateFormats("dd/MM/yyyy", "yyyy-MM-dd");
            dateNaissance.setI18n(multiFormatI18n);
            dateNaissance.isRequired();

            // ajout des champs et des boutons d'action dans le formulaire
            form.add(prenom, nom, dateNaissance, email, diplomeEleveObtenu, niveauDiplome,pieceJointe1, posteOccupe, pieceJointe2, anneeExperienceProfessionnelle, casierJudiciaireFourni, telephone1, telephone2, entreprise, createButtonsLayout());

            add(form);
        }

        //Evènements lancés lors du click sur nos boutons
        private HorizontalLayout createButtonsLayout() {

            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            // on peut appuyer sur échap pour fermer le formulaire
            close.addClickShortcut(Key.ESCAPE);

            // évènements save, delete, close
            save.addClickListener(event -> validateAndSave());
            close.addClickListener(event -> fireEvent(new TuteurNewOrEdit.CloseEvent(this)));

            // met le bouton de sauvegarde actif que si le binder est valide
            binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

            return new HorizontalLayout(save, close);
        }

        // fonction qui va alimenter le binder d'un tuteur
        public void setTuteur(Tuteur tuteur) {
            this.tuteur = tuteur;
            this.cloneTuteur = null;
            // copie du tuteur si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
            if (tuteur != null && tuteur.getId() != null) {
                try {
                    this.cloneTuteur = (Tuteur) tuteur.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            // alimentation du binder
            binder.readBean(tuteur);
        }

        // fonction qui vérifie que le bean a bien un tuteur, avant de lancer l'event de sauvegarde
        private void validateAndSave() {
            try {
                binder.writeBean(tuteur);
                if (this.cloneTuteur == null) {
                    fireEvent(new TuteurNewOrEdit.SaveEvent(this, tuteur));
                } else {
                    fireEvent(new TuteurNewOrEdit.SaveEditedEvent(this, tuteur, cloneTuteur));
                }
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }



    // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir l'étudiant
    // qu'on manipule dans le formulaire
    public static abstract class TuteurFormEvent extends ComponentEvent<TuteurNewOrEdit> {
        private final Tuteur tuteur;
        private final Tuteur tuteurOriginal;

        protected TuteurFormEvent(TuteurNewOrEdit source, Tuteur tuteur, Tuteur tuteurOriginal) {
            super(source, false);
            this.tuteur = tuteur;
            this.tuteurOriginal = tuteurOriginal;
        }

        public Tuteur getTuteur() {
            return tuteur;
        }

        public Tuteur getTuteurOriginal() {
            return tuteurOriginal;
        }
    }

    // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
    public static class SaveEvent extends TuteurNewOrEdit.TuteurFormEvent {
        SaveEvent(TuteurNewOrEdit source, Tuteur tuteur) {
            super(source, tuteur, null);
        }
    }

    // Event au clic sur le bouton de sauvegarde qui récupère le contact du formulaire (classe fille)
    public static class SaveEditedEvent extends TuteurNewOrEdit.TuteurFormEvent {
        SaveEditedEvent(TuteurNewOrEdit source, Tuteur tuteur, Tuteur tuteurOriginal) {
            super(source, tuteur, tuteurOriginal);
        }
    }

    // Event au clic sur le bouton de fermerture du formulaire (classe fille)
    public static class CloseEvent extends TuteurNewOrEdit.TuteurFormEvent {
        CloseEvent(TuteurNewOrEdit source) {
            super(source, null, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


    }