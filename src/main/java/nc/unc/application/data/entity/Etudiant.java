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
import java.util.Objects;

@Entity
@Table(name = "etudiant")
public class Etudiant implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_etudiant", nullable = false)
  private Long id;

  // le NotNull permet l'affichage du message du NotEmpty avec le binder
  // dans le formulaire (un peu bizarre, mais ok)
  @NotEmpty(message = "Le nom doit être renseigné")
  @NotNull(message = "Le nom ne peut pas être nul")
  @Column(name = "nom", nullable = false)
  private String nom;

  @NotEmpty(message = "Le prénom doit être renseigné")
  @NotNull(message = "Le prénom ne peut pas être nul")
  @Column(name = "prenom", nullable = false)
  private String prenom;

  @NotNull(message = "La civilité ne peut pas être nulle")
  @Enumerated(EnumType.STRING)
  @Column(name = "civilite", nullable = false)
  private Civilite civilite;

  @Enumerated(EnumType.STRING)
  @Column(name = "sexe")
  private Sexe sexe;

  @NotNull(message = "La date de naissance ne peut pas être nulle")
  @Past(message = "La date de naissance ne peut pas être aujourd'hui ou dans le futur")
  @Column(name = "date_naissance", nullable = false)
  private LocalDate dateNaissance;

  @Column(name = "age")
  private Long age;

  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @NotNull(message = "Le numéro de téléphone 1 ne doit pas être nul")
  @Column(name = "telephone_1", nullable = false)
  private Integer telephone1;

  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @NotNull(message = "Le numéro de téléphone 2 ne doit pas être nul")
  @Column(name = "telephone_2", nullable = false)
  private Integer telephone2;

  @Email
  @Column(name = "email", nullable = false)
  private String email;

  @NotNull(message = "Le dernier diplôme obtenu ne peut pas être nul")
  @Column(name = "dernier_diplome_obtenu_ou_en_cours", nullable = false)
  private String dernierDiplomeObtenuOuEnCours;

  @Column(name = "niveau_dernier_diplome")
  private Integer niveauDernierDiplome;

  @Column(name = "annee_obtention_dernier_diplome")
  private Integer anneeObtentionDernierDiplome;

  @NotNull(message = "le statut d'admission ne doit pas être nul")
  @Column(name = "admis", nullable = false)
  private String admis;

  @Column(name = "situation_unc")
  private String situationUnc;

  @Column(name = "situation_entreprise")
  private String situationEntreprise;

  @Column(name = "lieu_naissance")
  private String lieuNaissance;

  @Column(name = "nationalite")
  private String nationalite;

  @Max(999999)
  @Min(100000)
  @Column(name = "numero_cafat")
  private Integer numeroCafat;

  @Column(name = "adresse")
  private String adresse;

  @Column(name = "boite_postale")
  private String boitePostale;

  @Range(message = "Le code postal doit correspondre à une commune en Nouvelle-Calédonie", min = 98000, max = 98999)
  @Column(name = "code_postal")
  private Integer codePostal;

  @Column(name = "commune")
  private String commune;

  @Column(name = "situation_anne_precedente")
  private String situationAnneePrecedente;

  @Column(name = "etablissement_de_provenance")
  private String etablissementDeProvenance;

  @Column(name = "travailleur_handicape")
  private Boolean travailleurHandicape;

  @Column(name = "parcours")
  private String parcours;

  @Column(name = "veepap")
  private Boolean veepap;

  @Column(name = "prise_en_charge_frais_inscription")
  private String priseEnChargeFraisInscription;

  @Column(name = "obtention_diplome_mention")
  private String obtentionDiplomeMention;

  @Column(name = "observations", length = 15000)
  private String observations;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Entreprise.class)
  @JoinColumn(name = "id_entreprise")
  @JsonIgnoreProperties({"etudiants"})
  private Entreprise entreprise;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Tuteur.class)
  @JoinColumn(name = "id_tuteur")
  @JsonIgnoreProperties({"etudiants"})
  private Tuteur tuteur;

  @OneToMany(mappedBy = "etudiant", cascade = CascadeType.MERGE)
  private List<Contrat> contrats = new ArrayList<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;


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

  public LocalDate getDateNaissance() {
    return dateNaissance;
  }

  public void setDateNaissance(LocalDate dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  public Long getAge() {
    return age;
  }

  public void setAge(Long age) {
    this.age = age;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDernierDiplomeObtenuOuEnCours() {
    return dernierDiplomeObtenuOuEnCours;
  }

  public void setDernierDiplomeObtenuOuEnCours(String dernierDiplomeObtenuOuEnCours) {
    this.dernierDiplomeObtenuOuEnCours = dernierDiplomeObtenuOuEnCours;
  }

  public Integer getNiveauDernierDiplome() {
    return niveauDernierDiplome;
  }

  public void setNiveauDernierDiplome(Integer niveauDernierDiplome) {
    this.niveauDernierDiplome = niveauDernierDiplome;
  }

  public Integer getAnneeObtentionDernierDiplome() {
    return anneeObtentionDernierDiplome;
  }

  public void setAnneeObtentionDernierDiplome(Integer anneeObtentionDernierDiplome) {
    this.anneeObtentionDernierDiplome = anneeObtentionDernierDiplome;
  }

  public String getAdmis() {
    return admis;
  }

  public void setAdmis(String admis) {
    this.admis = admis;
  }

  public String getSituationUnc() {
    return situationUnc;
  }

  public void setSituationUnc(String situationUnc) {
    this.situationUnc = situationUnc;
  }

  public String getSituationEntreprise() {
    return situationEntreprise;
  }

  public void setSituationEntreprise(String situationEntreprise) {
    this.situationEntreprise = situationEntreprise;
  }

  public String getLieuNaissance() {
    return lieuNaissance;
  }

  public void setLieuNaissance(String lieuNaissance) {
    this.lieuNaissance = lieuNaissance;
  }

  public String getNationalite() {
    return nationalite;
  }

  public void setNationalite(String nationalite) {
    this.nationalite = nationalite;
  }

  public Integer getNumeroCafat() {
    return numeroCafat;
  }

  public void setNumeroCafat(Integer numeroCafat) {
    this.numeroCafat = numeroCafat;
  }

  public String getAdresse() {
    return adresse;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  public String getBoitePostale() {
    return boitePostale;
  }

  public void setBoitePostale(String boitePostale) {
    this.boitePostale = boitePostale;
  }

  public Integer getCodePostal() {
    return codePostal;
  }

  public void setCodePostal(Integer codePostal) {
    this.codePostal = codePostal;
  }

  public String getCommune() {
    return commune;
  }

  public void setCommune(String commune) {
    this.commune = commune;
  }

  public String getSituationAnneePrecedente() {
    return situationAnneePrecedente;
  }

  public void setSituationAnneePrecedente(String situationAnneePrecedente) {
    this.situationAnneePrecedente = situationAnneePrecedente;
  }

  public String getEtablissementDeProvenance() {
    return etablissementDeProvenance;
  }

  public void setEtablissementDeProvenance(String etablissementDeProvenance) {
    this.etablissementDeProvenance = etablissementDeProvenance;
  }

  public Boolean getTravailleurHandicape() {
    return travailleurHandicape;
  }

  public void setTravailleurHandicape(Boolean travailleurHandicape) {
    this.travailleurHandicape = travailleurHandicape;
  }

  public String getParcours() {
    return parcours;
  }

  public void setParcours(String parcours) {
    this.parcours = parcours;
  }

  public Boolean getVeepap() {
    return veepap;
  }

  public void setVeepap(Boolean veepap) {
    this.veepap = veepap;
  }

  public String getPriseEnChargeFraisInscription() {
    return priseEnChargeFraisInscription;
  }

  public void setPriseEnChargeFraisInscription(String priseEnChargeFraisInscription) {
    this.priseEnChargeFraisInscription = priseEnChargeFraisInscription;
  }

  public String getObtentionDiplomeMention() {
    return obtentionDiplomeMention;
  }

  public void setObtentionDiplomeMention(String obtentionDiplomeMention) {
    this.obtentionDiplomeMention = obtentionDiplomeMention;
  }

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public Entreprise getEntreprise() {
    return entreprise;
  }

  public void setEntreprise(Entreprise entreprise) {
    this.entreprise = entreprise;
  }

  public Tuteur getTuteur() {
    return tuteur;
  }

  public void setTuteur(Tuteur tuteur) {
    this.tuteur = tuteur;
  }

  public List<Contrat> getContrats() {
    return contrats;
  }

  public void setContrats(List<Contrat> contrats) {
    this.contrats = contrats;
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

  // Autres méthodes
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Etudiant etudiant = (Etudiant) o;
    return id.equals(etudiant.id) && nom.equals(etudiant.nom) && prenom.equals(etudiant.prenom) && civilite == etudiant.civilite && sexe == etudiant.sexe && dateNaissance.equals(etudiant.dateNaissance) && Objects.equals(age, etudiant.age) && telephone1.equals(etudiant.telephone1) && telephone2.equals(etudiant.telephone2) && email.equals(etudiant.email) && dernierDiplomeObtenuOuEnCours.equals(etudiant.dernierDiplomeObtenuOuEnCours) && Objects.equals(niveauDernierDiplome, etudiant.niveauDernierDiplome) && Objects.equals(anneeObtentionDernierDiplome, etudiant.anneeObtentionDernierDiplome) && admis.equals(etudiant.admis) && Objects.equals(situationUnc, etudiant.situationUnc) && Objects.equals(situationEntreprise, etudiant.situationEntreprise) && Objects.equals(lieuNaissance, etudiant.lieuNaissance) && Objects.equals(nationalite, etudiant.nationalite) && Objects.equals(numeroCafat, etudiant.numeroCafat) && Objects.equals(adresse, etudiant.adresse) && Objects.equals(boitePostale, etudiant.boitePostale) && Objects.equals(codePostal, etudiant.codePostal) && Objects.equals(commune, etudiant.commune) && Objects.equals(situationAnneePrecedente, etudiant.situationAnneePrecedente) && Objects.equals(etablissementDeProvenance, etudiant.etablissementDeProvenance) && Objects.equals(travailleurHandicape, etudiant.travailleurHandicape) && Objects.equals(parcours, etudiant.parcours) && Objects.equals(veepap, etudiant.veepap) && Objects.equals(priseEnChargeFraisInscription, etudiant.priseEnChargeFraisInscription) && Objects.equals(obtentionDiplomeMention, etudiant.obtentionDiplomeMention) && Objects.equals(observations, etudiant.observations) && Objects.equals(entreprise, etudiant.entreprise) && Objects.equals(createdAt, etudiant.createdAt) && Objects.equals(updatedAt, etudiant.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nom, prenom, civilite, sexe, dateNaissance, age, telephone1, telephone2, email, dernierDiplomeObtenuOuEnCours, niveauDernierDiplome, anneeObtentionDernierDiplome, admis, situationUnc, situationEntreprise, lieuNaissance, nationalite, numeroCafat, adresse, boitePostale, codePostal, commune, situationAnneePrecedente, etablissementDeProvenance, travailleurHandicape, parcours, veepap, priseEnChargeFraisInscription, obtentionDiplomeMention, observations, entreprise, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "Étudiant { " +
            "\n id=" + id +
            "\n nom='" + nom + '\'' +
            "\n prenom='" + prenom + '\'' +
            "\n civilite=" + civilite +
            "\n sexe=" + sexe +
            "\n dateNaissance=" + dateNaissance +
            "\n age=" + age +
            "\n telephone1=" + telephone1 +
            "\n telephone2=" + telephone2 +
            "\n email='" + email + '\'' +
            "\n dernierDiplomeObtenuOuEnCours='" + dernierDiplomeObtenuOuEnCours + '\'' +
            "\n niveauDernierDiplome=" + niveauDernierDiplome +
            "\n anneeObtentionDernierDiplome=" + anneeObtentionDernierDiplome +
            "\n admis='" + admis + '\'' +
            "\n situationUnc='" + situationUnc + '\'' +
            "\n situationEntreprise='" + situationEntreprise + '\'' +
            "\n lieuNaissance='" + lieuNaissance + '\'' +
            "\n nationalite='" + nationalite + '\'' +
            "\n numeroCafat=" + numeroCafat +
            "\n adresse='" + adresse + '\'' +
            "\n boitePostale='" + boitePostale + '\'' +
            "\n codePostal=" + codePostal +
            "\n commune='" + commune + '\'' +
            "\n situationAnneePrecedente='" + situationAnneePrecedente + '\'' +
            "\n etablissementDeProvenance='" + etablissementDeProvenance + '\'' +
            "\n travailleurHandicape=" + travailleurHandicape +
            "\n parcours='" + parcours + '\'' +
            "\n veepap=" + veepap +
            "\n priseEnChargeFraisInscription='" + priseEnChargeFraisInscription + '\'' +
            "\n obtentionDiplomeMention='" + obtentionDiplomeMention + '\'' +
            "\n observations='" + observations + '\'' +
            "\n entreprise=" + (entreprise != null ? entreprise.getEnseigne() : "") +
            "\n entreprise=" + (tuteur != null ? tuteur.getPrenom() + " " + tuteur.getNom() : "") +
            " }";
  }
}
