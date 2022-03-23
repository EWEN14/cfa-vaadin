package nc.unc.application.views.tuteur;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.Civilite;
import nc.unc.application.data.enums.Decua;
import nc.unc.application.data.enums.NiveauDiplome;

import java.awt.*;

public class TuteurForm extends FormLayout {

    private Tuteur tuteur;
    private Tuteur cloneTuteur;

    TextField prenom = new TextField("Prenom");
    TextField nom = new TextField("Nom");
    DatePicker dateNaissance = new DatePicker("Date de Naissance");
    EmailField email  = new EmailField("Email");
    TextField diplomeEleveeObtenu = new TextField("Diplome elevé obtenu");
    Select<String> niveauDiplomeSelect = new Select<>();
    Select<Decua> pieceJointe1 = new Select<>();
    TextField posteOccupe = new TextField("Poste occupé");
    Select<Decua> pieceJointe2 = new Select<>();
    TextField experienceProfessionnelle = new TextField("Expérience professionnelle");
    Checkbox casierJudiciaireFourni = new Checkbox("Casier judiciaire fournit");
    NumberField telephone1 = new NumberField("Téléphone 1");
    NumberField telephone2 = new NumberField("Téléphone 2");

    Binder<Tuteur> binder = new BeanValidationBinder<>(Tuteur.class);

    Button save = new Button("Sauvegarder");
    Button delete = new Button("Supprimer");
    Button close = new Button("Fermer");

    public TuteurForm(){
        //classe css
        addClassName("contact-form");
        // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité étudiant,
        // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
        binder.bindInstanceFields(this);
        //Remplissage de no
        niveauDiplomeSelect.setLabel("Niveau du diplôme");
        niveauDiplomeSelect.setItems(NiveauDiplome.getNiveauDiplomeStr());
    }


}
