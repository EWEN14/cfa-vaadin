package nc.unc.application.views.contrat;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import nc.unc.application.data.entity.*;
import nc.unc.application.data.enums.Civilite;

public class ContratNewOrEdit {

  private Contrat contrat;
  private Contrat cloneContrat;

  FormLayout form = new FormLayout();

  ComboBox<Entreprise> entreprise = new ComboBox<>("Entreprise");
  ComboBox<Etudiant> etudiant = new ComboBox<>("Etudiant");
  ComboBox<Formation> formation = new ComboBox<>("Formation");
  ComboBox<Tuteur> tuteur = new ComboBox<>("Tuteur");

  TextField codeContrat = new TextField("Code contrat");
  TextField typeContrat = new TextField("Type contrat");
  TextField  nomRepresentantLegal = new TextField("Nom représentant légal");
  TextField prenomRepresentantLegal = new TextField("Prénom Représentant légal");
  TextField relationAvecSalarie = new TextField("Relation avec salarié");
  TextField adresseRepresentant = new TextField("Adresse Représentant");
  TextField codePostalRepresentant = new TextField("Code postal Représentant");
  Select<String> communeRepresentant = new Select<>();
  IntegerField telephoneRepresentant = new IntegerField("Téléphone Représentant");
  EmailField emailRepresentant = new EmailField("Email Représentant");
  DatePicker debutContrat = new DatePicker("Date de Naissance");

  TextField prenom = new TextField("Type contrat");
  TextField prenom = new TextField("Type contrat");
  TextField prenom = new TextField("Type contrat");
  IntegerField telephone1 = new IntegerField("Téléphone 1");

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
  TextArea observations = new TextArea("Observations");

}
