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
  private String nom;

  // Prénom du tuteur
  @NotEmpty(message = "Le prénom doit être renseigné")
  @NotNull(message = "Le prénom ne peut pas être nul")
  @Column(name = "prenom", nullable = false)
  private String prenom;

  // Date de naissance du tuteur
  @Past(message = "La date de naissance ne peut pas être aujourd'hui ou dans le futur")
  @Column(name = "date_naissance")
  private LocalDate dateNaissance;

  // Email du tuteur
  @Email
  @NotNull(message = "L'email ne peut pas être null")
  @Column(name = "email", nullable = false)
  private String email;

  // Premier numéro de téléphone
  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @NotNull(message = "Le numéro de téléphone 1 ne doit pas être nul")
  @Column(name = "telephone_1", nullable = false)
  private Integer telephone1;

  // Deuxième numéro de téléphone
  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @Column(name = "telephone_2")
  private Integer telephone2;

  @Enumerated(EnumType.STRING)
  @Column(name = "civilite")
  private Civilite civilite;

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
  private String observations;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "tuteur", cascade = CascadeType.MERGE, orphanRemoval = true, targetEntity = TuteurHabilitation.class)
  @OrderBy("dateFormation DESC")
  private List<TuteurHabilitation> tuteurHabilitations = new ArrayList<>();

  // Getters et Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public LocalDate getDateNaissance() {
    return dateNaissance;
  }

  public void setDateNaissance(LocalDate dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getTelephone1() {
    return telephone1;
  }

  public void setTelephone1(Integer telephone1) {
    this.telephone1 = telephone1;
  }

  public Integer getTelephone2() {
    return telephone2;
  }

  public void setTelephone2(Integer telephone2) {
    this.telephone2 = telephone2;
  }

  public Civilite getCivilite() {
    return civilite;
  }

  public void setCivilite(Civilite civilite) {
    this.civilite = civilite;
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

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public List<TuteurHabilitation> getTuteurHabilitations() {
    return tuteurHabilitations;
  }

  public void setTuteurHabilitations(List<TuteurHabilitation> tuteurHabilitations) {
    this.tuteurHabilitations = tuteurHabilitations;
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
    return "Tuteur{" +
            "\n id=" + id +
            "\n nom='" + nom + '\'' +
            "\n prenom='" + prenom + '\'' +
            "\n dateNaissance=" + dateNaissance +
            "\n email='" + email + '\'' +
            "\n telephone1=" + telephone1 +
            "\n telephone2=" + telephone2 +
            "\n civilite=" + civilite +
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
            "\n observations='" + observations + '\'' +
            " }";
  }
}
