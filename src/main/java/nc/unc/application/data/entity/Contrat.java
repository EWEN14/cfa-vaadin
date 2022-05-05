package nc.unc.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;

@Entity
@Table(name = "contrat")
public class Contrat implements Cloneable{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_contrat", nullable = false)
  private Long id;

  @Column(name = "type_contrat")
  private String typeContrat;

  @Column(name = "nom_representant_legal")
  private String nomRepresentantLegal;

  @Column(name = "prenom_representant_legal")
  private String prenomRepresentantLegal;

  @Column(name = "relation_avec_salarie")
  private String relationAvecSalarie;

  @Column(name = "adresse_representant")
  private String adresseRepresentant;

  @Column(name = "code_postal_representant")
  private String codePostalRepresentant;

  @Column(name = "commune_representant")
  private String communeRepresentant;

  @Column(name = "telephone_representant")
  private int telephoneRepresentant;

  @Email
  @Column(name = "email_representant")
  private String emailRepresentant;

  @Temporal(TemporalType.DATE)
  @Column(name = "debut_contrat")
  private Date debutContrat;

  @Temporal(TemporalType.DATE)
  @Column(name = "fin_contrat")
  private Date finContrat;

  @Column(name = "duree_periode_essai")
  private Integer dureePeriodeEssai;

  @Column(name = "intitule_certification_pro")
  private String intituleCertificationPro;

  @Column(name = "niveau_certification_pro")
  private Integer niveauCertificationPro;

  @Column(name = "formation_assuree")
  private String formationAssuree;

  @Column(name = "numero_convention_tripartie")
  private String numeroConventionTripartie;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_convention_tripartie")
  private Date dateConventionTripartie;

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

  @Temporal(TemporalType.DATE)
  @Column(name = "date_rupture")
  private Date dateRupture;

  @Column(name = "motif_rupture")
  private String motifRupture;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_reception_decua")
  private Date dateReceptionDecua;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_envoi_rp_decua")
  private Date dateEnvoiRpDecua;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_retour_rp_decua")
  private Date dateRetourRpDecua;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_envoi_email_cua_convention")
  private Date dateEnvoiEmailCuaConvention;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_depot_alfresco_cua_avn_1")
  private Date dateDepotAlfrescoCuaAvn1;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_remise_originaux")
  private Date dateRemiseOriginaux;

  @Column(name = "motif_avn_2")
  private String motifAvn2;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_mail_ou_rdv_signature_cua_avn_2")
  private Date dateMailOuRdvSignatureCuaAvn2;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_depot_alfresco_cua_avn_2")
  private Date dateDepotAlfrescoCuaAvn2;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_mail_ou_rdv_signature_conv_avn_2")
  private Date dateMailOuRdvSignatureConvAvn2;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_depot_alfresco_conv_avn_2")
  private Date dateDepotAlfrescoConvAvn2;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_remise_originaux_avn_2")
  private Date dateRemiseOriginauxAvn2;

  @Temporal(TemporalType.DATE)
  @Column(name = "formation_lea")
  private Date formationLea;

  @Column(name = "observations", length = 2000)
  private String observations;

  @Temporal(TemporalType.DATE)
  @Column(name = "created_at")
  private Date createdAt;

  @Temporal(TemporalType.DATE)
  @Column(name = "updated_at")
  private Date updatedAt;

  @Column(name = "code_contrat")
  private String codeContrat;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_depot_alfresco_cua_conv_signe")
  private Date dateDepotAlfrescoCuaConvSigne;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_reception_originaux_convention")
  private Date dateReceptionOriginauxConvention;

  @Column(name = "motif_avn_1")
  private String motifAvn1;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_mail_ou_rdv_siganture_conv_avn_1")
  private Date dateMailOuRdvSigantureConvAvn1;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_mail_ou_rdv_signature_conv_avn_1")
  private Date dateMailOuRdvSignatureConvAvn1;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_depot_alfresco_conv_avn_1")
  private Date dateDepotAlfrescoConvAvn1;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_remise_originaux_avn_1")
  private Date dateRemiseOriginauxAvn1;

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


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTypeContrat() {
    return typeContrat;
  }

  public void setTypeContrat(String typeContrat) {
    this.typeContrat = typeContrat;
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

  public String getCodePostalRepresentant() {
    return codePostalRepresentant;
  }

  public void setCodePostalRepresentant(String codePostalRepresentant) {
    this.codePostalRepresentant = codePostalRepresentant;
  }

  public String getCommuneRepresentant() {
    return communeRepresentant;
  }

  public void setCommuneRepresentant(String communeRepresentant) {
    this.communeRepresentant = communeRepresentant;
  }

  public int getTelephoneRepresentant() {
    return telephoneRepresentant;
  }

  public void setTelephoneRepresentant(int telephoneRepresentant) {
    this.telephoneRepresentant = telephoneRepresentant;
  }

  public String getEmailRepresentant() {
    return emailRepresentant;
  }

  public void setEmailRepresentant(String emailRepresentant) {
    this.emailRepresentant = emailRepresentant;
  }

  public Date getDebutContrat() {
    return debutContrat;
  }

  public void setDebutContrat(Date debutContrat) {
    this.debutContrat = debutContrat;
  }

  public Date getFinContrat() {
    return finContrat;
  }

  public void setFinContrat(Date finContrat) {
    this.finContrat = finContrat;
  }

  public Integer getDureePeriodeEssai() {
    return dureePeriodeEssai;
  }

  public void setDureePeriodeEssai(Integer dureePeriodeEssai) {
    this.dureePeriodeEssai = dureePeriodeEssai;
  }

  public String getIntituleCertificationPro() {
    return intituleCertificationPro;
  }

  public void setIntituleCertificationPro(String intituleCertificationPro) {
    this.intituleCertificationPro = intituleCertificationPro;
  }

  public Integer getNiveauCertificationPro() {
    return niveauCertificationPro;
  }

  public void setNiveauCertificationPro(Integer niveauCertificationPro) {
    this.niveauCertificationPro = niveauCertificationPro;
  }

  public String getFormationAssuree() {
    return formationAssuree;
  }

  public void setFormationAssuree(String formationAssuree) {
    this.formationAssuree = formationAssuree;
  }

  public String getNumeroConventionTripartie() {
    return numeroConventionTripartie;
  }

  public void setNumeroConventionTripartie(String numeroConventionTripartie) {
    this.numeroConventionTripartie = numeroConventionTripartie;
  }

  public Date getDateConventionTripartie() {
    return dateConventionTripartie;
  }

  public void setDateConventionTripartie(Date dateConventionTripartie) {
    this.dateConventionTripartie = dateConventionTripartie;
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

  public Date getDateRupture() {
    return dateRupture;
  }

  public void setDateRupture(Date dateRupture) {
    this.dateRupture = dateRupture;
  }

  public String getMotifRupture() {
    return motifRupture;
  }

  public void setMotifRupture(String motifRupture) {
    this.motifRupture = motifRupture;
  }

  public Date getDateReceptionDecua() {
    return dateReceptionDecua;
  }

  public void setDateReceptionDecua(Date dateReceptionDecua) {
    this.dateReceptionDecua = dateReceptionDecua;
  }

  public Date getDateEnvoiRpDecua() {
    return dateEnvoiRpDecua;
  }

  public void setDateEnvoiRpDecua(Date dateEnvoiRpDecua) {
    this.dateEnvoiRpDecua = dateEnvoiRpDecua;
  }

  public Date getDateRetourRpDecua() {
    return dateRetourRpDecua;
  }

  public void setDateRetourRpDecua(Date dateRetourRpDecua) {
    this.dateRetourRpDecua = dateRetourRpDecua;
  }

  public Date getDateEnvoiEmailCuaConvention() {
    return dateEnvoiEmailCuaConvention;
  }

  public void setDateEnvoiEmailCuaConvention(Date dateEnvoiEmailCuaConvention) {
    this.dateEnvoiEmailCuaConvention = dateEnvoiEmailCuaConvention;
  }

  public Date getDateDepotAlfrescoCuaAnv1() {
    return dateDepotAlfrescoCuaAvn1;
  }

  public void setDateDepotAlfrescoCuaAnv1(Date dateDepotAlfrescoCuaAnv1) {
    this.dateDepotAlfrescoCuaAvn1 = dateDepotAlfrescoCuaAnv1;
  }

  public Date getDateRemiseOriginaux() {
    return dateRemiseOriginaux;
  }

  public void setDateRemiseOriginaux(Date dateRemiseOriginaux) {
    this.dateRemiseOriginaux = dateRemiseOriginaux;
  }

  public String getMotifAvn2() {
    return motifAvn2;
  }

  public void setMotifAvn2(String motifAvn2) {
    this.motifAvn2 = motifAvn2;
  }

  public Date getDateMailOuRdvSignatureCuaAvn2() {
    return dateMailOuRdvSignatureCuaAvn2;
  }

  public void setDateMailOuRdvSignatureCuaAvn2(Date dateMailOuRdvSignatureCuaAvn2) {
    this.dateMailOuRdvSignatureCuaAvn2 = dateMailOuRdvSignatureCuaAvn2;
  }

  public Date getDateDepotAlfrescoCuaAvn2() {
    return dateDepotAlfrescoCuaAvn2;
  }

  public void setDateDepotAlfrescoCuaAvn2(Date dateDepotAlfrescoCuaAvn2) {
    this.dateDepotAlfrescoCuaAvn2 = dateDepotAlfrescoCuaAvn2;
  }

  public Date getDateMailOuRdvSignatureConvAvn2() {
    return dateMailOuRdvSignatureConvAvn2;
  }

  public void setDateMailOuRdvSignatureConvAvn2(Date dateMailOuRdvSignatureConvAvn2) {
    this.dateMailOuRdvSignatureConvAvn2 = dateMailOuRdvSignatureConvAvn2;
  }

  public Date getDateDepotAlfrescoConvAvn2() {
    return dateDepotAlfrescoConvAvn2;
  }

  public void setDateDepotAlfrescoConvAvn2(Date dateDepotAlfrescoConvAvn2) {
    this.dateDepotAlfrescoConvAvn2 = dateDepotAlfrescoConvAvn2;
  }

  public Date getDateRemiseOriginauxAvn2() {
    return dateRemiseOriginauxAvn2;
  }

  public void setDateRemiseOriginauxAvn2(Date dateRemiseOriginauxAvn2) {
    this.dateRemiseOriginauxAvn2 = dateRemiseOriginauxAvn2;
  }

  public Date getFormationLea() {
    return formationLea;
  }

  public void setFormationLea(Date formationLea) {
    this.formationLea = formationLea;
  }

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getCodeContrat() {
    return codeContrat;
  }

  public void setCodeContrat(String codeContrat) {
    this.codeContrat = codeContrat;
  }

  public Date getDateDepotAlfrescoCuaConvSigne() {
    return dateDepotAlfrescoCuaConvSigne;
  }

  public void setDateDepotAlfrescoCuaConvSigne(Date dateDepotAlfrescoCuaConvSigne) {
    this.dateDepotAlfrescoCuaConvSigne = dateDepotAlfrescoCuaConvSigne;
  }

  public Date getDateReceptionOriginauxConvention() {
    return dateReceptionOriginauxConvention;
  }

  public void setDateReceptionOriginauxConvention(Date dateReceptionOriginauxConvention) {
    this.dateReceptionOriginauxConvention = dateReceptionOriginauxConvention;
  }

  public String getMotifAvn1() {
    return motifAvn1;
  }

  public void setMotifAvn1(String motifAvn1) {
    this.motifAvn1 = motifAvn1;
  }

  public Date getDateMailOuRdvSigantureConvAvn1() {
    return dateMailOuRdvSigantureConvAvn1;
  }

  public void setDateMailOuRdvSigantureConvAvn1(Date dateMailOuRdvSigantureConvAvn1) {
    this.dateMailOuRdvSigantureConvAvn1 = dateMailOuRdvSigantureConvAvn1;
  }

  public Date getDateMailOuRdvSignatureConvAvn1() {
    return dateMailOuRdvSignatureConvAvn1;
  }

  public void setDateMailOuRdvSignatureConvAvn1(Date dateMailOuRdvSignatureConvAvn1) {
    this.dateMailOuRdvSignatureConvAvn1 = dateMailOuRdvSignatureConvAvn1;
  }

  public Date getDateDepotAlfrescoConvAvn1() {
    return dateDepotAlfrescoConvAvn1;
  }

  public void setDateDepotAlfrescoConvAvn1(Date dateDepotAlfrescoConvAvn1) {
    this.dateDepotAlfrescoConvAvn1 = dateDepotAlfrescoConvAvn1;
  }

  public Date getDateRemiseOriginauxAvn1() {
    return dateRemiseOriginauxAvn1;
  }

  public void setDateRemiseOriginauxAvn1(Date dateRemiseOriginauxAvn1) {
    this.dateRemiseOriginauxAvn1 = dateRemiseOriginauxAvn1;
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


  // autres fonctions
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return "Contrat{" +
      "\n id=" + id +
      "\n etudiant=" + (etudiant != null ? etudiant.getPrenomEtudiant() + " " +etudiant.getNomEtudiant() : " ") +
      "\n tuteur=" + (tuteur != null ? tuteur.getPrenomTuteur() + " " +tuteur.getNomTuteur() : " ") +
      "\n entreprise=" + (entreprise != null ? entreprise.getEnseigne() : " ") +
      "\n formation=" + (formation != null ? formation.getLibelleFormation() : " ") +
      "\n codeContrat=" + codeContrat +
      "\n typeContrat=" + typeContrat +
      "\n nomRepresentantLegal=" + nomRepresentantLegal +
      "\n prenomRepresentantLegal=" + prenomRepresentantLegal +
      "\n relationAvecSalarie='" + relationAvecSalarie + '\'' +
      "\n adresseRepresentant=" + adresseRepresentant +
      "\n codePostalRepresentant='" + codePostalRepresentant + '\'' +
      "\n communeRepresentant='" + codePostalRepresentant + '\'' +
      "\n telephoneRepresentant=" + telephoneRepresentant +
      "\n emailRepresentant=" + emailRepresentant +
      "\n debutContrat=" + debutContrat +
      "\n finContrat=" + finContrat +
      "\n dureePeriodeEssai=" + dureePeriodeEssai +
      "\n intituleCertificationPro='" + intituleCertificationPro + '\'' +
      "\n niveauCertificationPro='" + niveauCertificationPro + '\'' +
      "\n formationAssuree='" + formationAssuree + '\'' +
      "\n numeroConventionTripartie='" + numeroConventionTripartie + '\'' +
      "\n dateConventionTripartie='" + dateConventionTripartie + '\'' +
      "\n semainesEntreprise='" + semainesEntreprise + '\'' +
      "\n heuresFormation='" + heuresFormation + '\'' +
      "\n semainesFormation='" + semainesFormation + '\'' +
      "\n lieuFormation='" + lieuFormation + '\'' +
      "\n dureeHebdomadaireTravail='" + dureeHebdomadaireTravail + '\'' +
      "\n dateRupture='" + dateRupture + '\'' +
      "\n motifRupture='" + motifRupture + '\'' +
      "\n dateReceptionDecua='" + dateReceptionDecua + '\'' +
      "\n dateEnvoirpDecua='" + dateEnvoiRpDecua + '\'' +
      "\n dateRetourRpDecua='" + dateRetourRpDecua + '\'' +
      "\n dateEnvoiEmailCuaConvention='" + dateEnvoiEmailCuaConvention + '\'' +
      "\n dateDepotAlfrescoCuaConvSigne='" + dateDepotAlfrescoCuaConvSigne + '\'' +
      "\n dateReceptionOriginauxConvention='" + dateReceptionOriginauxConvention + '\'' +
      "\n motifAvn1='" + motifAvn1 + '\'' +
      "\n dateMailOuRdvSignatureCuaAvn1='" + dateMailOuRdvSigantureConvAvn1 + '\'' +
      "\n dateDepotAlfrescoCuaAvn1='" + dateDepotAlfrescoCuaAvn1 + '\'' +
      "\n dateMailOuRdvSignatureConvAvn1='" + dateMailOuRdvSignatureConvAvn1 + '\'' +
      "\n dateDepotAlfrescoConvAvn1='" + dateDepotAlfrescoConvAvn1 + '\'' +
      "\n dateRemiseOriginauxAvn1='" + dateRemiseOriginauxAvn1 + '\'' +
      "\n motifAvn2='" + motifAvn2 + '\'' +
      "\n dateMailOuRdvSignatureCuaAvn2='" + dateMailOuRdvSignatureCuaAvn2 + '\'' +
      "\n dateDepotAlfrescoCuaAvn2='" + dateDepotAlfrescoCuaAvn2 + '\'' +
      "\n dateMailOuRdvSignatureConvAvn2='" + dateMailOuRdvSignatureConvAvn2 + '\'' +
      "\n dateDepotAlfrescoConvAvn2='" + dateDepotAlfrescoConvAvn2 + '\'' +
      "\n dateRemiseOriginauxAvn2='" + dateRemiseOriginauxAvn2 + '\'' +
      "\n formationLea='" + formationLea + '\'' +
      "\n observations='" + observations + '\'' +
      "\n createdAt='" + createdAt + '\'' +
      "\n updatedAt='" + updatedAt + '\'' +
    " }";
  }
}
