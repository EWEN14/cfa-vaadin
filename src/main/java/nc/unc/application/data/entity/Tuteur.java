package nc.unc.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import nc.unc.application.data.enums.Civilite;
import nc.unc.application.data.enums.Sexe;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tuteur")
public class Tuteur implements Cloneable {

  // Id du tuteur
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_tuteur", nullable = false)
  private Long id;

  // Nom du tuteur
  @NotEmpty(message = "Le nom doit être renseigné")
  @NotNull(message = "Le nom ne peut pas être nul")
  @Column(name = "nom", nullable = false)
  private String nomTuteur;

  // Prénom du tuteur
  @NotEmpty(message = "Le prénom doit être renseigné")
  @NotNull(message = "Le prénom ne peut pas être nul")
  @Column(name = "prenom", nullable = false)
  private String prenomTuteur;

  // Date de naissance du tuteur
  @Past(message = "La date de naissance ne peut pas être aujourd'hui ou dans le futur")
  @Column(name = "date_naissance")
  private LocalDate dateNaissanceTuteur;

  // Email du tuteur
  @Email
  @NotNull(message = "L'email ne peut pas être null")
  @Column(name = "email", nullable = false)
  private String emailTuteur;

  // Premier numéro de téléphone
  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @NotNull(message = "Le numéro de téléphone 1 ne doit pas être nul")
  @Column(name = "telephone_1", nullable = false)
  private Integer telephoneTuteur1;

  // Deuxième numéro de téléphone
  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @Column(name = "telephone_2")
  private Integer telephoneTuteur2;

  @Enumerated(EnumType.STRING)
  @Column(name = "civilite")
  private Civilite civiliteTuteur;

  @Enumerated(EnumType.STRING)
  @Column(name = "sexe")
  private Sexe sexe;

  // Diplôme le plus élévé du tuteur
  @Column(name = "diplome_eleve_obtenu")
  private String diplomeEleveObtenu;

  // Niveau du diplôme du tuteur
  @Column(name = "niveau_diplome")
  private Integer niveauDiplome;

  // Le poste occupé du tuteur
  @Column(name = "poste_occupe")
  private String posteOccupe;

  // L'expérience professionnelle du tuteur en années
  @Column(name = "annee_experience_professionnelle")
  private String anneeExperienceProfessionnelle;

  // Casier judiciaire fourni du tuteur
  @Column(name = "casier_judiciaire_fourni")
  private Boolean casierJudiciaireFourni;

  // diplôme fourni
  @Column(name = "diplome_fourni")
  private Boolean diplomeFourni;

  // certificat de travail fourni
  @Column(name = "certificat_travail_fourni")
  private Boolean certificatTravailFourni;

  @Column(name = "cv_fourni")
  private Boolean cvFourni;

  @Column(name = "statut_actif", length = 40)
  private String statutActif;

  @Column(name = "observations", length = 15000)
  private String observationsTuteur;

  // Entreprise du tuteur
  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Entreprise.class)
  @JoinColumn(name = "id_entreprise")
  @JsonIgnoreProperties({"tuteurs"})
  private Entreprise entreprise;

  // Besoin de mettre en EAGER plutôt qu'en LAZY (par défaut), car sinon liste des Habilitations pas initialisées
  // quand le Tuteur est initialisé
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "tuteur", cascade = CascadeType.MERGE, orphanRemoval = true)
  private List<TuteurHabilitation> tuteurHabilitations = new ArrayList<>();

  @OneToMany(mappedBy = "tuteur", cascade = CascadeType.MERGE)
  private List<Contrat> contrats = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "tuteur", cascade = CascadeType.MERGE)
  private List<Etudiant> etudiants = new ArrayList<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  /**
   * TODO : supprimer les Habilitations plutôt que juste mettre tuteur à null
   * Retrait des références d'un tuteur dans les habilitations tuteur,
   * dans les étudiants et dans les contrats
   */
  @PreRemove
  private void preRemove() {
    for (TuteurHabilitation th : tuteurHabilitations) {
      th.setTuteur(null);
    }
    for (Etudiant e : etudiants) {
      e.setTuteur(null);
    }
    for (Contrat c : contrats) {
      c.setTuteur(null);
    }
  }

  // autres fonctions
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return "Tuteur {" +
            "\n id=" + id +
            "\n nomTuteur='" + nomTuteur + '\'' +
            "\n prenomTuteur='" + prenomTuteur + '\'' +
            "\n dateNaissanceTuteur=" + dateNaissanceTuteur +
            "\n emailTuteur='" + emailTuteur + '\'' +
            "\n telephoneTuteur1=" + telephoneTuteur1 +
            "\n telephoneTuteur2=" + telephoneTuteur2 +
            "\n civiliteTuteur=" + civiliteTuteur +
            "\n sexe=" + sexe +
            "\n diplomeEleveObtenu='" + diplomeEleveObtenu + '\'' +
            "\n niveauDiplome=" + niveauDiplome +
            "\n posteOccupe='" + posteOccupe + '\'' +
            "\n anneeExperienceProfessionnelle='" + anneeExperienceProfessionnelle + '\'' +
            "\n entreprise=" + (entreprise != null ? entreprise.getEnseigne() : "") +
            "\n casierJudiciaireFourni=" + casierJudiciaireFourni +
            "\n diplomeFourni=" + diplomeFourni +
            "\n certificatTravailFourni=" + certificatTravailFourni +
            "\n cvFourni=" + cvFourni +
            "\n statutActif='" + statutActif + '\'' +
            "\n observationsTuteur='" + observationsTuteur + '\'' +
            " }";
  }
}
