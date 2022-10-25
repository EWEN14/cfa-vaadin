package nc.unc.application.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import nc.unc.application.data.enums.CodeContrat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contrat")
public class Contrat implements Cloneable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_contrat", nullable = false)
  private Long id;

  // colonne désignant un contrat parent
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_contrat_parent")
  private Contrat contratParent;

  // liste des contrats enfants (donc les avenants d'un contrat)
  // CascadeType.REMOVE pour supprimer les avenants quand le contrat initial est supprimé
  @OneToMany(mappedBy = "contratParent", cascade = CascadeType.REMOVE)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private List<Contrat> avenants;

  @Enumerated(EnumType.STRING)
  @Column(name = "code_contrat", nullable = false)
  private CodeContrat codeContrat;

  @Column(name = "type_contrat")
  private String typeContrat;

  @Column(name = "derogation_age")
  private Boolean derogationAge;

  @Column(name = "date_delivrance_derogation_age")
  private LocalDate dateDelivranceDerogationAge;

  @Column(name = "nom_representant_legal")
  private String nomRepresentantLegal;

  @Column(name = "prenom_representant_legal")
  private String prenomRepresentantLegal;

  @Column(name = "relation_avec_salarie")
  private String relationAvecSalarie;

  @Column(name = "adresse_representant")
  private String adresseRepresentant;

  @Range(message = "Le code postal doit correspondre à une commune en Nouvelle-Calédonie", min = 98000, max = 98999)
  @Column(name = "code_postal_representant")
  private Integer codePostalRepresentant;

  @Column(name = "commune_representant")
  private String communeRepresentant;

  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @Column(name = "telephone_representant")
  private Integer telephoneRepresentant;

  @Email
  @Column(name = "email_representant")
  private String emailRepresentant;

  @FutureOrPresent(message = "La date du début du contrat ne peut être dans le passé")
  @NotNull(message = "La date de début du contrat ne peut pas être nulle")
  @Column(name = "debut_contrat")
  private LocalDate debutContrat;

  @FutureOrPresent(message = "La date de fin du contrat ne peut être dans le passé")
  @NotNull(message = "La date de fin du contrat ne peut pas être nulle")
  @Column(name = "fin_contrat")
  private LocalDate finContrat;

  @Column(name = "duree_periode_essai")
  private Integer dureePeriodeEssai;

  @Column(name = "numero_convention_formation")
  private String numeroConventionFormation;

  @Column(name = "date_convention_formation")
  private LocalDate dateConventionFormation;

  @Column(name = "prime_avantage_nature")
  private String primeAvantageNature;

  @Column(name = "date_rupture")
  private LocalDate dateRupture;

  @Column(name = "motif_rupture")
  private String motifRupture;

  @Column(name = "date_reception_decua")
  private LocalDate dateReceptionDecua;

  @Column(name = "date_envoi_rp_decua")
  private LocalDate dateEnvoiRpDecua;

  @Column(name = "date_retour_rp_decua")
  private LocalDate dateRetourRpDecua;

  @Column(name = "date_envoi_email_cua_convention")
  private LocalDate dateEnvoiEmailCuaConvention;

  @Column(name = "date_depot_alfresco_cua_conv_signe")
  private LocalDate dateDepotAlfrescoCuaConvSigne;

  @Column(name = "date_reception_originaux_convention")
  private LocalDate dateReceptionOriginauxConvention;

  @Column(name = "conv_originale_remis_etudiant")
  private Boolean convOriginaleRemisEtudiant;

  @Column(name = "conv_originale_remis_tuteur")
  private Boolean convOriginaleRemisTuteur;

  @Column(name = "conv_originale_remis_employeur")
  private Boolean convOriginaleRemisEmployeur;

  @Column(name = "numero_avenant")
  private Integer numeroAvenant;

  @Column(name = "motif_avn")
  private String motifAvn;

  @Column(name = "date_mail_ou_rdv_signature_cua_avn")
  private LocalDate dateMailOuRdvSignatureCuaAvn;

  @Column(name = "date_depot_alfresco_cua_avn")
  private LocalDate dateDepotAlfrescoCuaAvn;

  @Column(name = "date_mail_ou_rdv_signature_conv_avn")
  private LocalDate dateMailOuRdvSignatureConvAvn;

  @Column(name = "date_depot_alfresco_conv_avn")
  private LocalDate dateDepotAlfrescoConvAvn;

  @Column(name = "conv_avenant_remis_etudiant")
  private Boolean convAvenantRemisEtudiant;

  @Column(name = "conv_avenant_remis_tuteur")
  private Boolean convAvenantRemisTuteur;

  @Column(name = "conv_avenant_remis_employeur")
  private Boolean convAvenantRemisEmployeur;

  @Column(name = "formation_lea")
  private LocalDate formationLea;

  @Column(name = "observations", length = 15000)
  private String observations;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Entreprise.class)
  @JoinColumn(name = "id_entreprise", nullable = true)
  private Entreprise entreprise;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Etudiant.class)
  @JoinColumn(name = "id_etudiant", nullable = true)
  private Etudiant etudiant;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Formation.class)
  @JoinColumn(name = "id_formation", nullable = true)
  private Formation formation;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Tuteur.class)
  @JoinColumn(name = "id_tuteur", nullable = true)
  private Tuteur tuteur;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // getters et setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Contrat getContratParent() {
    return contratParent;
  }

  public void setContratParent(Contrat contrat) {
    this.contratParent = contrat;
  }

  public List<Contrat> getAvenants() {
    return avenants;
  }

  public void setAvenants(List<Contrat> contrats) {
    this.avenants = contrats;
  }

  public CodeContrat getCodeContrat() {
    return codeContrat;
  }

  public void setCodeContrat(CodeContrat codeContrat) {
    this.codeContrat = codeContrat;
  }

  public String getTypeContrat() {
    return typeContrat;
  }

  public void setTypeContrat(String typeContrat) {
    this.typeContrat = typeContrat;
  }

  public Boolean getDerogationAge() {
    return derogationAge;
  }

  public void setDerogationAge(Boolean derogationAge) {
    this.derogationAge = derogationAge;
  }

  public LocalDate getDateDelivranceDerogationAge() {
    return dateDelivranceDerogationAge;
  }

  public void setDateDelivranceDerogationAge(LocalDate dateDelivranceDerogationAge) {
    this.dateDelivranceDerogationAge = dateDelivranceDerogationAge;
  }

  public String getNomRepresentantLegal() {
    return nomRepresentantLegal;
  }

  public void setNomRepresentantLegal(String nomRepresentantLegal) {
    this.nomRepresentantLegal = nomRepresentantLegal;
  }

  public String getPrenomRepresentantLegal() {
    return prenomRepresentantLegal;
  }

  public void setPrenomRepresentantLegal(String prenomRepresentantLegal) {
    this.prenomRepresentantLegal = prenomRepresentantLegal;
  }

  public String getRelationAvecSalarie() {
    return relationAvecSalarie;
  }

  public void setRelationAvecSalarie(String relationAvecSalarie) {
    this.relationAvecSalarie = relationAvecSalarie;
  }

  public String getAdresseRepresentant() {
    return adresseRepresentant;
  }

  public void setAdresseRepresentant(String adresseRepresentant) {
    this.adresseRepresentant = adresseRepresentant;
  }

  public Integer getCodePostalRepresentant() {
    return codePostalRepresentant;
  }

  public void setCodePostalRepresentant(Integer codePostalRepresentant) {
    this.codePostalRepresentant = codePostalRepresentant;
  }

  public String getCommuneRepresentant() {
    return communeRepresentant;
  }

  public void setCommuneRepresentant(String communeRepresentant) {
    this.communeRepresentant = communeRepresentant;
  }

  public Integer getTelephoneRepresentant() {
    return telephoneRepresentant;
  }

  public void setTelephoneRepresentant(Integer telephoneRepresentant) {
    this.telephoneRepresentant = telephoneRepresentant;
  }

  public String getEmailRepresentant() {
    return emailRepresentant;
  }

  public void setEmailRepresentant(String emailRepresentant) {
    this.emailRepresentant = emailRepresentant;
  }

  public LocalDate getDebutContrat() {
    return debutContrat;
  }

  public void setDebutContrat(LocalDate debutContrat) {
    this.debutContrat = debutContrat;
  }

  public LocalDate getFinContrat() {
    return finContrat;
  }

  public void setFinContrat(LocalDate finContrat) {
    this.finContrat = finContrat;
  }

  public Integer getDureePeriodeEssai() {
    return dureePeriodeEssai;
  }

  public void setDureePeriodeEssai(Integer dureePeriodeEssai) {
    this.dureePeriodeEssai = dureePeriodeEssai;
  }

  public String getNumeroConventionFormation() {
    return numeroConventionFormation;
  }

  public void setNumeroConventionFormation(String numeroConventionTripartie) {
    this.numeroConventionFormation = numeroConventionTripartie;
  }

  public LocalDate getDateConventionFormation() {
    return dateConventionFormation;
  }

  public void setDateConventionFormation(LocalDate dateConventionFormation) {
    this.dateConventionFormation = dateConventionFormation;
  }

  public String getPrimeAvantageNature() {
    return primeAvantageNature;
  }

  public void setPrimeAvantageNature(String primeAvantageNature) {
    this.primeAvantageNature = primeAvantageNature;
  }

  public LocalDate getDateRupture() {
    return dateRupture;
  }

  public void setDateRupture(LocalDate dateRupture) {
    this.dateRupture = dateRupture;
  }

  public String getMotifRupture() {
    return motifRupture;
  }

  public void setMotifRupture(String motifRupture) {
    this.motifRupture = motifRupture;
  }

  public LocalDate getDateReceptionDecua() {
    return dateReceptionDecua;
  }

  public void setDateReceptionDecua(LocalDate dateReceptionDecua) {
    this.dateReceptionDecua = dateReceptionDecua;
  }

  public LocalDate getDateEnvoiRpDecua() {
    return dateEnvoiRpDecua;
  }

  public void setDateEnvoiRpDecua(LocalDate dateEnvoiRpDecua) {
    this.dateEnvoiRpDecua = dateEnvoiRpDecua;
  }

  public LocalDate getDateRetourRpDecua() {
    return dateRetourRpDecua;
  }

  public void setDateRetourRpDecua(LocalDate dateRetourRpDecua) {
    this.dateRetourRpDecua = dateRetourRpDecua;
  }

  public LocalDate getDateEnvoiEmailCuaConvention() {
    return dateEnvoiEmailCuaConvention;
  }

  public void setDateEnvoiEmailCuaConvention(LocalDate dateEnvoiEmailCuaConvention) {
    this.dateEnvoiEmailCuaConvention = dateEnvoiEmailCuaConvention;
  }

  public LocalDate getDateDepotAlfrescoCuaConvSigne() {
    return dateDepotAlfrescoCuaConvSigne;
  }

  public void setDateDepotAlfrescoCuaConvSigne(LocalDate dateDepotAlfrescoCuaConvSigne) {
    this.dateDepotAlfrescoCuaConvSigne = dateDepotAlfrescoCuaConvSigne;
  }

  public LocalDate getDateReceptionOriginauxConvention() {
    return dateReceptionOriginauxConvention;
  }

  public void setDateReceptionOriginauxConvention(LocalDate dateReceptionOriginauxConvention) {
    this.dateReceptionOriginauxConvention = dateReceptionOriginauxConvention;
  }

  public Boolean getConvOriginaleRemisEtudiant() {
    return convOriginaleRemisEtudiant;
  }

  public void setConvOriginaleRemisEtudiant(Boolean convOriginaleRemisEtudiant) {
    this.convOriginaleRemisEtudiant = convOriginaleRemisEtudiant;
  }

  public Boolean getConvOriginaleRemisTuteur() {
    return convOriginaleRemisTuteur;
  }

  public void setConvOriginaleRemisTuteur(Boolean convOriginaleRemisTuteur) {
    this.convOriginaleRemisTuteur = convOriginaleRemisTuteur;
  }

  public Boolean getConvOriginaleRemisEmployeur() {
    return convOriginaleRemisEmployeur;
  }

  public void setConvOriginaleRemisEmployeur(Boolean convOriginaleRemisEmployeur) {
    this.convOriginaleRemisEmployeur = convOriginaleRemisEmployeur;
  }

  public Integer getNumeroAvenant() {
    return numeroAvenant;
  }

  public void setNumeroAvenant(Integer numeroAvenant) {
    this.numeroAvenant = numeroAvenant;
  }

  public String getMotifAvn() {
    return motifAvn;
  }

  public void setMotifAvn(String motifAvn1) {
    this.motifAvn = motifAvn1;
  }

  public LocalDate getDateMailOuRdvSignatureCuaAvn() {
    return dateMailOuRdvSignatureCuaAvn;
  }

  public void setDateMailOuRdvSignatureCuaAvn(LocalDate dateMailOuRdvSignatureCuaAvn1) {
    this.dateMailOuRdvSignatureCuaAvn = dateMailOuRdvSignatureCuaAvn1;
  }

  public LocalDate getDateDepotAlfrescoCuaAvn() {
    return dateDepotAlfrescoCuaAvn;
  }

  public void setDateDepotAlfrescoCuaAvn(LocalDate dateDepotAlfrescoCuaAvn1) {
    this.dateDepotAlfrescoCuaAvn = dateDepotAlfrescoCuaAvn1;
  }

  public LocalDate getDateMailOuRdvSignatureConvAvn() {
    return dateMailOuRdvSignatureConvAvn;
  }

  public void setDateMailOuRdvSignatureConvAvn(LocalDate dateMailOuRdvSignatureConvAvn1) {
    this.dateMailOuRdvSignatureConvAvn = dateMailOuRdvSignatureConvAvn1;
  }

  public LocalDate getDateDepotAlfrescoConvAvn() {
    return dateDepotAlfrescoConvAvn;
  }

  public void setDateDepotAlfrescoConvAvn(LocalDate dateDepotAlfrescoConvAvn1) {
    this.dateDepotAlfrescoConvAvn = dateDepotAlfrescoConvAvn1;
  }

  public Boolean getConvAvenantRemisEtudiant() {
    return convAvenantRemisEtudiant;
  }

  public void setConvAvenantRemisEtudiant(Boolean convAvenantRemisEtudiant) {
    this.convAvenantRemisEtudiant = convAvenantRemisEtudiant;
  }

  public Boolean getConvAvenantRemisEmployeur() {
    return convAvenantRemisEmployeur;
  }

  public void setConvAvenantRemisEmployeur(Boolean convAvenantRemisEmployeur) {
    this.convAvenantRemisEmployeur = convAvenantRemisEmployeur;
  }

  public Boolean getConvAvenantRemisTuteur() {
    return convAvenantRemisTuteur;
  }

  public void setConvAvenantRemisTuteur(Boolean convAvenantRemisTuteur) {
    this.convAvenantRemisTuteur = convAvenantRemisTuteur;
  }

  public LocalDate getFormationLea() {
    return formationLea;
  }

  public void setFormationLea(LocalDate formationLea) {
    this.formationLea = formationLea;
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

  public Etudiant getEtudiant() {
    return etudiant;
  }

  public void setEtudiant(Etudiant etudiant) {
    this.etudiant = etudiant;
  }

  public Formation getFormation() {
    return formation;
  }

  public void setFormation(Formation formation) {
    this.formation = formation;
  }

  public Tuteur getTuteur() {
    return tuteur;
  }

  public void setTuteur(Tuteur tuteur) {
    this.tuteur = tuteur;
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
    return "Contrat{" +
            "\n id=" + id +
            "\n codeContrat='" + codeContrat + '\'' +
            "\n typeContrat='" + typeContrat + '\'' +
            "\n derogationAge=" + derogationAge +
            "\n dateDelivranceDerogationAge=" + dateDelivranceDerogationAge +
            "\n nomRepresentantLegal='" + nomRepresentantLegal + '\'' +
            "\n prenomRepresentantLegal='" + prenomRepresentantLegal + '\'' +
            "\n relationAvecSalarie='" + relationAvecSalarie + '\'' +
            "\n adresseRepresentant='" + adresseRepresentant + '\'' +
            "\n codePostalRepresentant=" + codePostalRepresentant +
            "\n communeRepresentant='" + communeRepresentant + '\'' +
            "\n telephoneRepresentant=" + telephoneRepresentant +
            "\n emailRepresentant='" + emailRepresentant + '\'' +
            "\n debutContrat=" + debutContrat +
            "\n finContrat=" + finContrat +
            "\n dureePeriodeEssai=" + dureePeriodeEssai +
            "\n numeroConventionFormation='" + numeroConventionFormation + '\'' +
            "\n primeAvantageNature='" + primeAvantageNature + '\'' +
            "\n dateRupture=" + dateRupture +
            "\n motifRupture='" + motifRupture + '\'' +
            "\n dateReceptionDecua=" + dateReceptionDecua +
            "\n dateEnvoiRpDecua=" + dateEnvoiRpDecua +
            "\n dateRetourRpDecua=" + dateRetourRpDecua +
            "\n dateEnvoiEmailCuaConvention=" + dateEnvoiEmailCuaConvention +
            "\n dateDepotAlfrescoCuaConvSigne=" + dateDepotAlfrescoCuaConvSigne +
            "\n dateReceptionOriginauxConvention=" + dateReceptionOriginauxConvention +
            "\n convOriginaleRemisEtudiant=" + convOriginaleRemisEtudiant +
            "\n convOriginaleRemisTuteur='" + convOriginaleRemisTuteur + '\'' +
            "\n convOriginaleRemisEmployeur='" + convOriginaleRemisEmployeur + '\'' +
            "\n numeroAvenant=" + numeroAvenant +
            "\n motifAvn='" + motifAvn + '\'' +
            "\n dateMailOuRdvSignatureCuaAvn=" + dateMailOuRdvSignatureCuaAvn +
            "\n dateDepotAlfrescoCuaAvn=" + dateDepotAlfrescoCuaAvn +
            "\n dateMailOuRdvSignatureConvAvn=" + dateMailOuRdvSignatureConvAvn +
            "\n dateDepotAlfrescoConvAvn=" + dateDepotAlfrescoConvAvn +
            "\n convAvenantRemisEtudiant=" + convAvenantRemisEtudiant +
            "\n convAvenantRemisTuteur=" + convAvenantRemisTuteur +
            "\n convAvenantRemisEmployeur=" + convAvenantRemisEmployeur +
            "\n formationLea=" + formationLea +
            "\n observations='" + observations + '\'' +
            "\n entreprise=" + (entreprise != null ? entreprise.getEnseigne() : "") +
            "\n etudiant=" + (etudiant != null ? etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant() : "") +
            "\n formation=" + (formation != null ? formation.getCodeFormation() : "") +
            "\n tuteur=" + (tuteur != null ? tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() : "") +
            '}';
  }
}
