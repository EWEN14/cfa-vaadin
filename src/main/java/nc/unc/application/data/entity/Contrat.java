package nc.unc.application.data.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contrat")
public class Contrat implements Cloneable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_contrat", nullable = false)
  private Long id;

  @Column(name = "code_contrat")
  private String codeContrat;

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

  @Column(name = "cadre_admin_num_enregistrement_contrat")
  private String cadreAdminNumEnregistrementContrat;

  @Column(name = "cadre_admin_num_avenant")
  private String cadreAdminNumAvenant;

  @Column(name = "cadre_admin_recu_le")
  private LocalDate cadreAdminRecuLe;

  @Column(name = "debut_contrat")
  private LocalDate debutContrat;

  @Column(name = "fin_contrat")
  private LocalDate finContrat;

  @Column(name = "emploi_occupe_salarie_etudiant")
  private String emploiOccupeSalarieEtudiant;

  @Column(name = "code_rome_emploi_occupe", length = 15)
  private String codeRomeEmploiOccupe;

  @Column(name = "duree_periode_essai")
  private Integer dureePeriodeEssai;

  @Column(name = "niveau_certification_pro")
  private Integer niveauCertificationPro;

  @Column(name = "numero_convention_formation")
  private String numeroConventionFormation;

  @Column(name = "semaines_entreprise")
  private Integer semainesEntreprise;

  @Column(name = "heures_formation")
  private Integer heuresFormation;

  @Column(name = "semaines_formation")
  private Integer semainesFormation;

  @Column(name = "lieu_formation")
  private String lieuFormation;

  @Column(name = "duree_hebdomadaire_travail")
  private Integer dureeHebdomadaireTravail;

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

  @Column(name = "exemplaire_originaux_remis_alternant_ou_entreprise")
  private String exemplaireOriginauxRemisAlternantOuEntreprise;

  @Column(name = "motif_avn_1")
  private String motifAvn1;

  @Column(name = "date_mail_ou_rdv_signature_cua_avn_1")
  private LocalDate dateMailOuRdvSignatureCuaAvn1;

  @Column(name = "date_depot_alfresco_cua_avn_1")
  private LocalDate dateDepotAlfrescoCuaAvn1;

  @Column(name = "date_mail_ou_rdv_signature_conv_avn_1")
  private LocalDate dateMailOuRdvSignatureConvAvn1;

  @Column(name = "date_depot_alfresco_conv_avn_1")
  private LocalDate dateDepotAlfrescoConvAvn1;

  @Column(name = "date_remise_originaux_avn_1")
  private LocalDate dateRemiseOriginauxAvn1;

  @Column(name = "motif_avn_2")
  private String motifAvn2;

  @Column(name = "date_mail_ou_rdv_signature_cua_avn_2")
  private LocalDate dateMailOuRdvSignatureCuaAvn2;

  @Column(name = "date_depot_alfresco_cua_avn_2")
  private LocalDate dateDepotAlfrescoCuaAvn2;

  @Column(name = "date_remise_originaux_cua_avn_2")
  private LocalDate dateRemiseOriginauxCuaAvn2;

  @Column(name = "date_mail_ou_rdv_signature_conv_avn_2")
  private LocalDate dateMailOuRdvSignatureConvAvn2;

  @Column(name = "date_depot_alfresco_conv_avn_2")
  private LocalDate dateDepotAlfrescoConvAvn2;

  @Column(name = "date_remise_originaux_avn_2")
  private LocalDate dateRemiseOriginauxAvn2;

  @Column(name = "formation_lea")
  private LocalDate formationLea;

  @Column(name = "observations", length = 15000)
  private String observations;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_entreprise")
  private Entreprise entreprise;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_etudiant")
  private Etudiant etudiant;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_formation")
  private Formation formation;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_tuteur")
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

  public String getCodeContrat() {
    return codeContrat;
  }

  public void setCodeContrat(String codeContrat) {
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

  public String getCadreAdminNumEnregistrementContrat() {
    return cadreAdminNumEnregistrementContrat;
  }

  public void setCadreAdminNumEnregistrementContrat(String cadreAdminNumEnregistrementContrat) {
    this.cadreAdminNumEnregistrementContrat = cadreAdminNumEnregistrementContrat;
  }

  public String getCadreAdminNumAvenant() {
    return cadreAdminNumAvenant;
  }

  public void setCadreAdminNumAvenant(String cadreAdminNumAvenant) {
    this.cadreAdminNumAvenant = cadreAdminNumAvenant;
  }

  public LocalDate getCadreAdminRecuLe() {
    return cadreAdminRecuLe;
  }

  public void setCadreAdminRecuLe(LocalDate cadreAdminRecuLe) {
    this.cadreAdminRecuLe = cadreAdminRecuLe;
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

  public String getEmploiOccupeSalarieEtudiant() {
    return emploiOccupeSalarieEtudiant;
  }

  public void setEmploiOccupeSalarieEtudiant(String emploiOccupeSalarieEtudiant) {
    this.emploiOccupeSalarieEtudiant = emploiOccupeSalarieEtudiant;
  }

  public String getCodeRomeEmploiOccupe() {
    return codeRomeEmploiOccupe;
  }

  public void setCodeRomeEmploiOccupe(String codeRomeEmploiOccupe) {
    this.codeRomeEmploiOccupe = codeRomeEmploiOccupe;
  }

  public Integer getDureePeriodeEssai() {
    return dureePeriodeEssai;
  }

  public void setDureePeriodeEssai(Integer dureePeriodeEssai) {
    this.dureePeriodeEssai = dureePeriodeEssai;
  }

  public Integer getNiveauCertificationPro() {
    return niveauCertificationPro;
  }

  public void setNiveauCertificationPro(Integer niveauCertificationPro) {
    this.niveauCertificationPro = niveauCertificationPro;
  }

  public String getNumeroConventionFormation() {
    return numeroConventionFormation;
  }

  public void setNumeroConventionFormation(String numeroConventionTripartie) {
    this.numeroConventionFormation = numeroConventionTripartie;
  }

  public Integer getSemainesEntreprise() {
    return semainesEntreprise;
  }

  public void setSemainesEntreprise(Integer semainesEntreprise) {
    this.semainesEntreprise = semainesEntreprise;
  }

  public Integer getHeuresFormation() {
    return heuresFormation;
  }

  public void setHeuresFormation(Integer heuresFormation) {
    this.heuresFormation = heuresFormation;
  }

  public Integer getSemainesFormation() {
    return semainesFormation;
  }

  public void setSemainesFormation(Integer semainesFormation) {
    this.semainesFormation = semainesFormation;
  }

  public String getLieuFormation() {
    return lieuFormation;
  }

  public void setLieuFormation(String lieuFormation) {
    this.lieuFormation = lieuFormation;
  }

  public Integer getDureeHebdomadaireTravail() {
    return dureeHebdomadaireTravail;
  }

  public void setDureeHebdomadaireTravail(Integer dureeHebdomadaireTravail) {
    this.dureeHebdomadaireTravail = dureeHebdomadaireTravail;
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

  public String getExemplaireOriginauxRemisAlternantOuEntreprise() {
    return exemplaireOriginauxRemisAlternantOuEntreprise;
  }

  public void setExemplaireOriginauxRemisAlternantOuEntreprise(String exemplaire_originaux_remis_alternant_ou_entreprise) {
    this.exemplaireOriginauxRemisAlternantOuEntreprise = exemplaire_originaux_remis_alternant_ou_entreprise;
  }

  public String getMotifAvn1() {
    return motifAvn1;
  }

  public void setMotifAvn1(String motifAvn1) {
    this.motifAvn1 = motifAvn1;
  }

  public LocalDate getDateMailOuRdvSignatureCuaAvn1() {
    return dateMailOuRdvSignatureCuaAvn1;
  }

  public void setDateMailOuRdvSignatureCuaAvn1(LocalDate dateMailOuRdvSignatureCuaAvn1) {
    this.dateMailOuRdvSignatureCuaAvn1 = dateMailOuRdvSignatureCuaAvn1;
  }

  public LocalDate getDateDepotAlfrescoCuaAvn1() {
    return dateDepotAlfrescoCuaAvn1;
  }

  public void setDateDepotAlfrescoCuaAvn1(LocalDate dateDepotAlfrescoCuaAvn1) {
    this.dateDepotAlfrescoCuaAvn1 = dateDepotAlfrescoCuaAvn1;
  }

  public LocalDate getDateMailOuRdvSignatureConvAvn1() {
    return dateMailOuRdvSignatureConvAvn1;
  }

  public void setDateMailOuRdvSignatureConvAvn1(LocalDate dateMailOuRdvSignatureConvAvn1) {
    this.dateMailOuRdvSignatureConvAvn1 = dateMailOuRdvSignatureConvAvn1;
  }

  public LocalDate getDateDepotAlfrescoConvAvn1() {
    return dateDepotAlfrescoConvAvn1;
  }

  public void setDateDepotAlfrescoConvAvn1(LocalDate dateDepotAlfrescoConvAvn1) {
    this.dateDepotAlfrescoConvAvn1 = dateDepotAlfrescoConvAvn1;
  }

  public LocalDate getDateRemiseOriginauxAvn1() {
    return dateRemiseOriginauxAvn1;
  }

  public void setDateRemiseOriginauxAvn1(LocalDate dateRemiseOriginauxAvn1) {
    this.dateRemiseOriginauxAvn1 = dateRemiseOriginauxAvn1;
  }

  public String getMotifAvn2() {
    return motifAvn2;
  }

  public void setMotifAvn2(String motifAvn2) {
    this.motifAvn2 = motifAvn2;
  }

  public LocalDate getDateMailOuRdvSignatureCuaAvn2() {
    return dateMailOuRdvSignatureCuaAvn2;
  }

  public void setDateMailOuRdvSignatureCuaAvn2(LocalDate dateMailOuRdvSignatureCuaAvn2) {
    this.dateMailOuRdvSignatureCuaAvn2 = dateMailOuRdvSignatureCuaAvn2;
  }

  public LocalDate getDateDepotAlfrescoCuaAvn2() {
    return dateDepotAlfrescoCuaAvn2;
  }

  public void setDateDepotAlfrescoCuaAvn2(LocalDate dateDepotAlfrescoCuaAvn2) {
    this.dateDepotAlfrescoCuaAvn2 = dateDepotAlfrescoCuaAvn2;
  }

  public LocalDate getDateRemiseOriginauxCuaAvn2() {
    return dateRemiseOriginauxCuaAvn2;
  }

  public void setDateRemiseOriginauxCuaAvn2(LocalDate dateRemiseOriginauxCuaAvn2) {
    this.dateRemiseOriginauxCuaAvn2 = dateRemiseOriginauxCuaAvn2;
  }

  public LocalDate getDateMailOuRdvSignatureConvAvn2() {
    return dateMailOuRdvSignatureConvAvn2;
  }

  public void setDateMailOuRdvSignatureConvAvn2(LocalDate dateMailOuRdvSignatureConvAvn2) {
    this.dateMailOuRdvSignatureConvAvn2 = dateMailOuRdvSignatureConvAvn2;
  }

  public LocalDate getDateDepotAlfrescoConvAvn2() {
    return dateDepotAlfrescoConvAvn2;
  }

  public void setDateDepotAlfrescoConvAvn2(LocalDate dateDepotAlfrescoConvAvn2) {
    this.dateDepotAlfrescoConvAvn2 = dateDepotAlfrescoConvAvn2;
  }

  public LocalDate getDateRemiseOriginauxAvn2() {
    return dateRemiseOriginauxAvn2;
  }

  public void setDateRemiseOriginauxAvn2(LocalDate dateRemiseOriginauxAvn2) {
    this.dateRemiseOriginauxAvn2 = dateRemiseOriginauxAvn2;
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
            "\n cadreAdminNumEnregistrementContrat='" + cadreAdminNumEnregistrementContrat + '\'' +
            "\n cadreAdminNumAvenant='" + cadreAdminNumAvenant + '\'' +
            "\n cadreAdminRecuLe=" + cadreAdminRecuLe +
            "\n debutContrat=" + debutContrat +
            "\n finContrat=" + finContrat +
            "\n emploiOccupeSalarieEtudiant='" + emploiOccupeSalarieEtudiant + '\'' +
            "\n codeRomeEmploiOccupe='" + codeRomeEmploiOccupe + '\'' +
            "\n dureePeriodeEssai=" + dureePeriodeEssai +
            "\n niveauCertificationPro=" + niveauCertificationPro +
            "\n numeroConventionFormation='" + numeroConventionFormation + '\'' +
            "\n semainesEntreprise=" + semainesEntreprise +
            "\n heuresFormation=" + heuresFormation +
            "\n semainesFormation=" + semainesFormation +
            "\n lieuFormation='" + lieuFormation + '\'' +
            "\n dureeHebdomadaireTravail=" + dureeHebdomadaireTravail +
            "\n dateRupture=" + dateRupture +
            "\n motifRupture='" + motifRupture + '\'' +
            "\n dateReceptionDecua=" + dateReceptionDecua +
            "\n dateEnvoiRpDecua=" + dateEnvoiRpDecua +
            "\n dateRetourRpDecua=" + dateRetourRpDecua +
            "\n dateEnvoiEmailCuaConvention=" + dateEnvoiEmailCuaConvention +
            "\n dateDepotAlfrescoCuaConvSigne=" + dateDepotAlfrescoCuaConvSigne +
            "\n dateReceptionOriginauxConvention=" + dateReceptionOriginauxConvention +
            "\n exemplaireOriginauxRemisAlternantOuEntreprise='" + exemplaireOriginauxRemisAlternantOuEntreprise + '\'' +
            "\n motifAvn1='" + motifAvn1 + '\'' +
            "\n dateMailOuRdvSignatureCuaAvn1=" + dateMailOuRdvSignatureCuaAvn1 +
            "\n dateDepotAlfrescoCuaAvn1=" + dateDepotAlfrescoCuaAvn1 +
            "\n dateMailOuRdvSignatureConvAvn1=" + dateMailOuRdvSignatureConvAvn1 +
            "\n dateDepotAlfrescoConvAvn1=" + dateDepotAlfrescoConvAvn1 +
            "\n dateRemiseOriginauxAvn1=" + dateRemiseOriginauxAvn1 +
            "\n motifAvn2='" + motifAvn2 + '\'' +
            "\n dateMailOuRdvSignatureCuaAvn2=" + dateMailOuRdvSignatureCuaAvn2 +
            "\n dateDepotAlfrescoCuaAvn2=" + dateDepotAlfrescoCuaAvn2 +
            "\n dateRemiseOriginauxCuaAvn2=" + dateRemiseOriginauxCuaAvn2 +
            "\n dateMailOuRdvSignatureConvAvn2=" + dateMailOuRdvSignatureConvAvn2 +
            "\n dateDepotAlfrescoConvAvn2=" + dateDepotAlfrescoConvAvn2 +
            "\n dateRemiseOriginauxAvn2=" + dateRemiseOriginauxAvn2 +
            "\n formationLea=" + formationLea +
            "\n observations='" + observations + '\'' +
            "\n entreprise=" + (entreprise != null ? entreprise.getEnseigne() : "") +
            "\n etudiant=" + (etudiant != null ? etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant() : "") +
            "\n formation=" + (formation != null ? formation.getCodeFormation() : "") +
            "\n tuteur=" + (tuteur != null ? tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() : "") +
            '}';
  }
}
