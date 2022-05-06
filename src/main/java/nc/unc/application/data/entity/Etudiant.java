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
  private String nomEtudiant;

  @NotEmpty(message = "Le prénom doit être renseigné")
  @NotNull(message = "Le prénom ne peut pas être nul")
  @Column(name = "prenom", nullable = false)
  private String prenomEtudiant;

  @NotNull(message = "La civilité ne peut pas être nulle")
  @Enumerated(EnumType.STRING)
  @Column(name = "civilite", nullable = false)
  private Civilite civiliteEtudiant;

  @Enumerated(EnumType.STRING)
  @Column(name = "sexe")
  private Sexe sexeEtudiant;

  @NotNull(message = "La date de naissance ne peut pas être nulle")
  @Past(message = "La date de naissance ne peut pas être aujourd'hui ou dans le futur")
  @Column(name = "date_naissance", nullable = false)
  private LocalDate dateNaissanceEtudiant;

  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @NotNull(message = "Le numéro de téléphone 1 ne doit pas être nul")
  @Column(name = "telephone_1", nullable = false)
  private Integer telephoneEtudiant1;

  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @NotNull(message = "Le numéro de téléphone 2 ne doit pas être nul")
  @Column(name = "telephone_2", nullable = false)
  private Integer telephoneEtudiant2;

  @Email
  @Column(name = "email", nullable = false)
  private String emailEtudiant;

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

  @Range(message = "Le numéro Cafat est composé de 6 chiffres", min = 100000, max = 999999)
  @Column(name = "numero_cafat")
  private Integer numeroCafatEtudiant;

  @Column(name = "adresse")
  private String adresseEtudiant;

  @Column(name = "boite_postale")
  private String boitePostaleEtudiant;

  @Range(message = "Le code postal doit correspondre à une commune en Nouvelle-Calédonie", min = 98000, max = 98999)
  @Column(name = "code_postal")
  private Integer codePostalEtudiant;

  @Column(name = "commune")
  private String communeEtudiant;

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

  @Range(message = "L'année de promotion est après l'an 2000", min = 2000, max = 9999)
  @Column(name = "annee_promotion")
  private Integer anneePromotion;

  @Column(name = "observations", length = 15000)
  private String observationsEtudiant;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Entreprise.class)
  @JoinColumn(name = "id_entreprise")
  @JsonIgnoreProperties({"etudiants"})
  private Entreprise entreprise;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Tuteur.class)
  @JoinColumn(name = "id_tuteur")
  @JsonIgnoreProperties({"etudiants"})
  private Tuteur tuteur;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Formation.class)
  @JoinColumn(name = "id_formation")
  private Formation formation;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = ReferentPedagogique.class)
  @JoinColumn(name = "id_referent_pedagogique")
  private ReferentPedagogique referentPedagogique;

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

  public String getNomEtudiant() {
    return nomEtudiant;
  }

  public void setNomEtudiant(String nom) {
    this.nomEtudiant = nom;
  }

  public String getPrenomEtudiant() {
    return prenomEtudiant;
  }

  public void setPrenomEtudiant(String prenom) {
    this.prenomEtudiant = prenom;
  }

  public Civilite getCiviliteEtudiant() {
    return civiliteEtudiant;
  }

  public void setCiviliteEtudiant(Civilite civilite) {
    this.civiliteEtudiant = civilite;
  }

  public Sexe getSexeEtudiant() {
    return sexeEtudiant;
  }

  public void setSexeEtudiant(Sexe sexe) {
    this.sexeEtudiant = sexe;
  }

  public LocalDate getDateNaissanceEtudiant() {
    return dateNaissanceEtudiant;
  }

  public void setDateNaissanceEtudiant(LocalDate dateNaissance) {
    this.dateNaissanceEtudiant = dateNaissance;
  }

  public Integer getTelephoneEtudiant1() {
    return telephoneEtudiant1;
  }

  public void setTelephoneEtudiant1(Integer telephone1) {
    this.telephoneEtudiant1 = telephone1;
  }

  public Integer getTelephoneEtudiant2() {
    return telephoneEtudiant2;
  }

  public void setTelephoneEtudiant2(Integer telephone2) {
    this.telephoneEtudiant2 = telephone2;
  }

  public String getEmailEtudiant() {
    return emailEtudiant;
  }

  public void setEmailEtudiant(String email) {
    this.emailEtudiant = email;
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

  public Integer getNumeroCafatEtudiant() {
    return numeroCafatEtudiant;
  }

  public void setNumeroCafatEtudiant(Integer numeroCafat) {
    this.numeroCafatEtudiant = numeroCafat;
  }

  public String getAdresseEtudiant() {
    return adresseEtudiant;
  }

  public void setAdresseEtudiant(String adresse) {
    this.adresseEtudiant = adresse;
  }

  public String getBoitePostaleEtudiant() {
    return boitePostaleEtudiant;
  }

  public void setBoitePostaleEtudiant(String boitePostale) {
    this.boitePostaleEtudiant = boitePostale;
  }

  public Integer getCodePostalEtudiant() {
    return codePostalEtudiant;
  }

  public void setCodePostalEtudiant(Integer codePostal) {
    this.codePostalEtudiant = codePostal;
  }

  public String getCommuneEtudiant() {
    return communeEtudiant;
  }

  public void setCommuneEtudiant(String commune) {
    this.communeEtudiant = commune;
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

  public Integer getAnneePromotion() {
    return anneePromotion;
  }

  public void setAnneePromotion(Integer anneePromotion) {
    this.anneePromotion = anneePromotion;
  }

  public String getObservationsEtudiant() {
    return observationsEtudiant;
  }

  public void setObservationsEtudiant(String observations) {
    this.observationsEtudiant = observations;
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

  public Formation getFormation() {
    return formation;
  }

  public void setFormation(Formation formation) {
    this.formation = formation;
  }

  public ReferentPedagogique getReferentPedagogique() {
    return referentPedagogique;
  }

  public void setReferentPedagogique(ReferentPedagogique referentPedagogique) {
    this.referentPedagogique = referentPedagogique;
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
    return id.equals(etudiant.id) && nomEtudiant.equals(etudiant.nomEtudiant) && prenomEtudiant.equals(etudiant.prenomEtudiant) && civiliteEtudiant == etudiant.civiliteEtudiant && sexeEtudiant == etudiant.sexeEtudiant && dateNaissanceEtudiant.equals(etudiant.dateNaissanceEtudiant) && telephoneEtudiant1.equals(etudiant.telephoneEtudiant1) && telephoneEtudiant2.equals(etudiant.telephoneEtudiant2) && emailEtudiant.equals(etudiant.emailEtudiant) && dernierDiplomeObtenuOuEnCours.equals(etudiant.dernierDiplomeObtenuOuEnCours) && Objects.equals(niveauDernierDiplome, etudiant.niveauDernierDiplome) && Objects.equals(anneeObtentionDernierDiplome, etudiant.anneeObtentionDernierDiplome) && admis.equals(etudiant.admis) && Objects.equals(situationUnc, etudiant.situationUnc) && Objects.equals(situationEntreprise, etudiant.situationEntreprise) && Objects.equals(lieuNaissance, etudiant.lieuNaissance) && Objects.equals(nationalite, etudiant.nationalite) && Objects.equals(numeroCafatEtudiant, etudiant.numeroCafatEtudiant) && Objects.equals(adresseEtudiant, etudiant.adresseEtudiant) && Objects.equals(boitePostaleEtudiant, etudiant.boitePostaleEtudiant) && Objects.equals(codePostalEtudiant, etudiant.codePostalEtudiant) && Objects.equals(communeEtudiant, etudiant.communeEtudiant) && Objects.equals(situationAnneePrecedente, etudiant.situationAnneePrecedente) && Objects.equals(etablissementDeProvenance, etudiant.etablissementDeProvenance) && Objects.equals(travailleurHandicape, etudiant.travailleurHandicape) && Objects.equals(parcours, etudiant.parcours) && Objects.equals(veepap, etudiant.veepap) && Objects.equals(priseEnChargeFraisInscription, etudiant.priseEnChargeFraisInscription) && Objects.equals(obtentionDiplomeMention, etudiant.obtentionDiplomeMention) && Objects.equals(anneePromotion, etudiant.anneePromotion) && Objects.equals(observationsEtudiant, etudiant.observationsEtudiant) && Objects.equals(entreprise, etudiant.entreprise) && Objects.equals(createdAt, etudiant.createdAt) && Objects.equals(updatedAt, etudiant.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nomEtudiant, prenomEtudiant, civiliteEtudiant, sexeEtudiant, dateNaissanceEtudiant, telephoneEtudiant1, telephoneEtudiant2, emailEtudiant, dernierDiplomeObtenuOuEnCours, niveauDernierDiplome, anneeObtentionDernierDiplome, admis, situationUnc, situationEntreprise, lieuNaissance, nationalite, numeroCafatEtudiant, adresseEtudiant, boitePostaleEtudiant, codePostalEtudiant, communeEtudiant, situationAnneePrecedente, etablissementDeProvenance, travailleurHandicape, parcours, veepap, priseEnChargeFraisInscription, obtentionDiplomeMention, anneePromotion, observationsEtudiant, entreprise, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "Étudiant { " +
            "\n id=" + id +
            "\n nomEtudiant='" + nomEtudiant + '\'' +
            "\n prenomEtudiant='" + prenomEtudiant + '\'' +
            "\n civiliteEtudiant=" + civiliteEtudiant +
            "\n sexeEtudiant=" + sexeEtudiant +
            "\n dateNaissanceEtudiant=" + dateNaissanceEtudiant +
            "\n telephoneEtudiant1=" + telephoneEtudiant1 +
            "\n telephoneEtudiant2=" + telephoneEtudiant2 +
            "\n emailEtudiant='" + emailEtudiant + '\'' +
            "\n dernierDiplomeObtenuOuEnCours='" + dernierDiplomeObtenuOuEnCours + '\'' +
            "\n niveauDernierDiplome=" + niveauDernierDiplome +
            "\n anneeObtentionDernierDiplome=" + anneeObtentionDernierDiplome +
            "\n admis='" + admis + '\'' +
            "\n situationUnc='" + situationUnc + '\'' +
            "\n situationEntreprise='" + situationEntreprise + '\'' +
            "\n lieuNaissance='" + lieuNaissance + '\'' +
            "\n nationalite='" + nationalite + '\'' +
            "\n numeroCafatEtudiant=" + numeroCafatEtudiant +
            "\n adresseEtudiant='" + adresseEtudiant + '\'' +
            "\n boitePostaleEtudiant='" + boitePostaleEtudiant + '\'' +
            "\n codePostalEtudiant=" + codePostalEtudiant +
            "\n communeEtudiant='" + communeEtudiant + '\'' +
            "\n situationAnneePrecedente='" + situationAnneePrecedente + '\'' +
            "\n etablissementDeProvenance='" + etablissementDeProvenance + '\'' +
            "\n travailleurHandicape=" + travailleurHandicape +
            "\n parcours='" + parcours + '\'' +
            "\n veepap=" + veepap +
            "\n priseEnChargeFraisInscription='" + priseEnChargeFraisInscription + '\'' +
            "\n obtentionDiplomeMention='" + obtentionDiplomeMention + '\'' +
            "\n anneePromotion='" + anneePromotion + '\'' +
            "\n observationsEtudiant='" + observationsEtudiant + '\'' +
            "\n entreprise=" + (entreprise != null ? entreprise.getEnseigne() : "") +
            "\n tuteur=" + (tuteur != null ? tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() : "") +
            "\n formation=" + (formation != null ? formation.getLibelleFormation() : "") +
            "\n referentPedagogique=" +
            (referentPedagogique != null ? referentPedagogique.getPrenomReferentPedago() + " " + referentPedagogique.getNomReferentPedago() : "") +
            " }";
  }
}
