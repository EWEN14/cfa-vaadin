package nc.unc.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

  // Entreprise du tuteur
  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Entreprise.class)
  @JoinColumn(name = "id_entreprise")
  @JsonIgnoreProperties({"tuteurs"})
  private Entreprise entreprise;

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

  @Column(name = "observations", length = 15000)
  private String observationsTuteur;

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

  // Getters et Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNomTuteur() {
    return nomTuteur;
  }

  public void setNomTuteur(String nom) {
    this.nomTuteur = nom;
  }

  public String getPrenomTuteur() {
    return prenomTuteur;
  }

  public void setPrenomTuteur(String prenom) {
    this.prenomTuteur = prenom;
  }

  public LocalDate getDateNaissanceTuteur() {
    return dateNaissanceTuteur;
  }

  public void setDateNaissanceTuteur(LocalDate dateNaissance) {
    this.dateNaissanceTuteur = dateNaissance;
  }

  public String getEmailTuteur() {
    return emailTuteur;
  }

  public void setEmailTuteur(String email) {
    this.emailTuteur = email;
  }

  public Integer getTelephoneTuteur1() {
    return telephoneTuteur1;
  }

  public void setTelephoneTuteur1(Integer telephone1) {
    this.telephoneTuteur1 = telephone1;
  }

  public Integer getTelephoneTuteur2() {
    return telephoneTuteur2;
  }

  public void setTelephoneTuteur2(Integer telephone2) {
    this.telephoneTuteur2 = telephone2;
  }

  public Civilite getCiviliteTuteur() {
    return civiliteTuteur;
  }

  public void setCiviliteTuteur(Civilite civilite) {
    this.civiliteTuteur = civilite;
  }

  public Sexe getSexe() {
    return sexe;
  }

  public void setSexe(Sexe sexe) {
    this.sexe = sexe;
  }

  public String getDiplomeEleveObtenu() {
    return diplomeEleveObtenu;
  }

  public void setDiplomeEleveObtenu(String diplomeEleveObtenu) {
    this.diplomeEleveObtenu = diplomeEleveObtenu;
  }

  public Integer getNiveauDiplome() {
    return niveauDiplome;
  }

  public void setNiveauDiplome(Integer niveauDiplome) {
    this.niveauDiplome = niveauDiplome;
  }

  public String getPosteOccupe() {
    return posteOccupe;
  }

  public void setPosteOccupe(String posteOccupe) {
    this.posteOccupe = posteOccupe;
  }

  public String getAnneeExperienceProfessionnelle() {
    return anneeExperienceProfessionnelle;
  }

  public void setAnneeExperienceProfessionnelle(String anneeExperienceProfessionnelle) {
    this.anneeExperienceProfessionnelle = anneeExperienceProfessionnelle;
  }

  public Entreprise getEntreprise() {
    return entreprise;
  }

  public void setEntreprise(Entreprise entreprise) {
    this.entreprise = entreprise;
  }

  public Boolean getCasierJudiciaireFourni() {
    return casierJudiciaireFourni;
  }

  public void setCasierJudiciaireFourni(Boolean casierJudiciaireFourni) {
    this.casierJudiciaireFourni = casierJudiciaireFourni;
  }

  public Boolean getDiplomeFourni() {
    return diplomeFourni;
  }

  public void setDiplomeFourni(Boolean diplomeFourni) {
    this.diplomeFourni = diplomeFourni;
  }

  public Boolean getCertificatTravailFourni() {
    return certificatTravailFourni;
  }

  public void setCertificatTravailFourni(Boolean certificatTravailFourni) {
    this.certificatTravailFourni = certificatTravailFourni;
  }

  public Boolean getCvFourni() {
    return cvFourni;
  }

  public void setCvFourni(Boolean cvFourni) {
    this.cvFourni = cvFourni;
  }

  public String getObservationsTuteur() {
    return observationsTuteur;
  }

  public void setObservationsTuteur(String observations) {
    this.observationsTuteur = observations;
  }

  public List<TuteurHabilitation> getTuteurHabilitations() {
    return tuteurHabilitations;
  }

  public void setTuteurHabilitations(List<TuteurHabilitation> tuteurHabilitations) {
    this.tuteurHabilitations = tuteurHabilitations;
  }

  public List<Contrat> getContrats() {
    return contrats;
  }

  public void setContrats(List<Contrat> contrats) {
    this.contrats = contrats;
  }

  public List<Etudiant> getEtudiants() {
    return etudiants;
  }

  public void setEtudiants(List<Etudiant> etudiants) {
    this.etudiants = etudiants;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
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
            "\n observationsTuteur='" + observationsTuteur + '\'' +
            " }";
  }
}
