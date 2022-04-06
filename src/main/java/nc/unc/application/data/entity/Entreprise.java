package nc.unc.application.data.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entreprise")
public class Entreprise implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_entreprise", nullable = false)
  private Long id;

  @NotNull(message = "Le statut qui définit l'entreprise ne peut être nul")
  @Column(name = "statut_actif_entreprise", nullable = false)
  private String statutActifEntreprise;

  @NotEmpty(message = "L'enseigne de l'entreprise doit être renseigné")
  @NotNull(message = "L'enseigne de l'entreprise ne peut pas être nulle")
  @Column(name = "enseigne", nullable = false)
  private String enseigne;

  @NotEmpty(message = "La raison sociale de l'entreprise doit être renseignée")
  @NotNull(message = "La raison sociale de l'entreprise ne peut pas être nulle")
  @Column(name = "raison_sociale")
  private String raisonSociale;

  @NotNull(message = "Le numéro de ridet ne peut pas être nul")
  @NotEmpty(message = "Le numéro de ridet doit être renseigné")
  @Pattern(message = "Le numéro de ridet doit suivre le format suivant : 0 000 000.000", regexp = "/\\d{3}\\s\\d{3}\\s\\d{3}\\.\\d{3}/gm")
  @Column(name = "numero_ridet")
  private String numeroRidet;

  @Column(name = "forme_juridique")
  private String formeJuridique;

  @Range(message = "Le numéro de Cafat doit comporter 6 chiffres", min = 100000, max = 999999)
  @Column(name = "numero_cafat")
  private Long numeroCafat;

  @Column(name = "nombre_salarie")
  private Long nombreSalarie;

  @Column(name = "code_naf")
  private String codeNaf;

  @Column(name = "activite_entreprise")
  private String activiteEntreprise;

  @Column(name = "convention_collective")
  private String conventionCollective;

  @Column(name = "nom_representant_employeur")
  private String nomRepresentantEmployeur;

  @Column(name = "prenom_representant_employeur")
  private String prenomRepresentantEmployeur;

  @Column(name = "fonction_representant_employeur")
  private String fonctionRepresentantEmployeur;

  @Column(name = "nom_contact_cfa")
  private String nom_contact_cfa;

  @Column(name = "prenom_contact_cfa")
  private String prenom_contact_cfa;

  @Column(name = "fonction_contact_cfa")
  private String fonction_contact_cfa;

  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @Column(name = "telephone_contact_cfa")
  private String telephone_contact_cfa;

  @Column(name = "email_contact_cfa")
  private String email_contact_cfa;

  @Column(name = "adr_phys_commune")
  private String adressePhysiqueCommune;

  @Range(message = "Le code postal doit correspondre à une commune en Nouvelle-Calédonie", min = 98000, max = 98999)
  @Column(name = "adr_phys_cp")
  private Integer adressePhysiqueCodePostal;

  @Column(name = "adr_phy_rue")
  private String adressePhysiqueRue;

  @Column(name = "adr_post_commune")
  private String adressePostaleCommune;

  @Range(message = "Le code postal doit correspondre à une commune en Nouvelle-Calédonie", min = 98000, max = 98999)
  @Column(name = "adr_postale_cp")
  private Integer adressePostaleCodePostal;

  @Column(name = "adr_post_rue_ou_bp")
  private String adressePostaleRueOuBp;

  @OneToMany(mappedBy = "entreprise", targetEntity = Etudiant.class)
  private List<Etudiant> etudiants = new ArrayList<>();

  @OneToMany(mappedBy = "entreprise", targetEntity = Tuteur.class)
  private List<Tuteur> tuteurs = new ArrayList<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "observations", length = 15000)
  private String observations;

  // Getters et Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEnseigne() {
    return enseigne;
  }

  public void setEnseigne(String enseigne) {
    this.enseigne = enseigne;
  }

  public String getStatutActifEntreprise() {
    return statutActifEntreprise;
  }

  public void setStatutActifEntreprise(String statutActifEntreprise) {
    this.statutActifEntreprise = statutActifEntreprise;
  }

  public String getRaisonSociale() {
    return raisonSociale;
  }

  public void setRaisonSociale(String raisonSociale) {
    this.raisonSociale = raisonSociale;
  }

  public String getNumeroRidet() {
    return numeroRidet;
  }

  public void setNumeroRidet(String numeroRidet) {
    this.numeroRidet = numeroRidet;
  }

  public String getFormeJuridique() {
    return formeJuridique;
  }

  public void setFormeJuridique(String formeJuridique) {
    this.formeJuridique = formeJuridique;
  }

  public Long getNumeroCafat() {
    return numeroCafat;
  }

  public void setNumeroCafat(Long numeroCafat) {
    this.numeroCafat = numeroCafat;
  }

  public Long getNombreSalarie() {
    return nombreSalarie;
  }

  public void setNombreSalarie(Long nombreSalarie) {
    this.nombreSalarie = nombreSalarie;
  }

  public String getCodeNaf() {
    return codeNaf;
  }

  public void setCodeNaf(String codeNaf) {
    this.codeNaf = codeNaf;
  }

  public String getActiviteEntreprise() {
    return activiteEntreprise;
  }

  public void setActiviteEntreprise(String activiteEntreprise) {
    this.activiteEntreprise = activiteEntreprise;
  }

  public String getConventionCollective() {
    return conventionCollective;
  }

  public void setConventionCollective(String conventionCollective) {
    this.conventionCollective = conventionCollective;
  }

  public String getNomRepresentantEmployeur() {
    return nomRepresentantEmployeur;
  }

  public void setNomRepresentantEmployeur(String nomRepresentantEmployeur) {
    this.nomRepresentantEmployeur = nomRepresentantEmployeur;
  }

  public String getPrenomRepresentantEmployeur() {
    return prenomRepresentantEmployeur;
  }

  public void setPrenomRepresentantEmployeur(String prenomRepresentantEmployeur) {
    this.prenomRepresentantEmployeur = prenomRepresentantEmployeur;
  }

  public String getFonctionRepresentantEmployeur() {
    return fonctionRepresentantEmployeur;
  }

  public void setFonctionRepresentantEmployeur(String fonctionRepresentantEmployeur) {
    this.fonctionRepresentantEmployeur = fonctionRepresentantEmployeur;
  }

  public String getEmail_contact_cfa() {
    return email_contact_cfa;
  }

  public void setEmail_contact_cfa(String email_contact_cfa) {
    this.email_contact_cfa = email_contact_cfa;
  }

  public String getTelephone_contact_cfa() {
    return telephone_contact_cfa;
  }

  public void setTelephone_contact_cfa(String telephone_contact_cfa) {
    this.telephone_contact_cfa = telephone_contact_cfa;
  }

  public String getFonction_contact_cfa() {
    return fonction_contact_cfa;
  }

  public void setFonction_contact_cfa(String fonction_contact_cfa) {
    this.fonction_contact_cfa = fonction_contact_cfa;
  }

  public String getPrenom_contact_cfa() {
    return prenom_contact_cfa;
  }

  public void setPrenom_contact_cfa(String prenom_contact_cfa) {
    this.prenom_contact_cfa = prenom_contact_cfa;
  }

  public String getNom_contact_cfa() {
    return nom_contact_cfa;
  }

  public void setNom_contact_cfa(String nom_contact_cfa) {
    this.nom_contact_cfa = nom_contact_cfa;
  }

  public String getAdressePhysiqueCommune() {
    return adressePhysiqueCommune;
  }

  public void setAdressePhysiqueCommune(String adressePhysiqueCommune) {
    this.adressePhysiqueCommune = adressePhysiqueCommune;
  }

  public Integer getAdressePhysiqueCodePostal() {
    return adressePhysiqueCodePostal;
  }

  public void setAdressePhysiqueCodePostal(Integer adressePhysiqueCodePostal) {
    this.adressePhysiqueCodePostal = adressePhysiqueCodePostal;
  }

  public String getAdressePhysiqueRue() {
    return adressePhysiqueRue;
  }

  public void setAdressePhysiqueRue(String adressePhysiqueRue) {
    this.adressePhysiqueRue = adressePhysiqueRue;
  }

  public String getAdressePostaleCommune() {
    return adressePostaleCommune;
  }

  public void setAdressePostaleCommune(String adressePostaleCommune) {
    this.adressePostaleCommune = adressePostaleCommune;
  }

  public Integer getAdressePostaleCodePostal() {
    return adressePostaleCodePostal;
  }

  public void setAdressePostaleCodePostal(Integer adressePostaleCodePostal) {
    this.adressePostaleCodePostal = adressePostaleCodePostal;
  }

  public String getAdressePostaleRueOuBp() {
    return adressePostaleRueOuBp;
  }

  public void setAdressePostaleRueOuBp(String adressePostaleRueOuBp) {
    this.adressePostaleRueOuBp = adressePostaleRueOuBp;
  }

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public List<Etudiant> getEtudiants() {
    return etudiants;
  }

  public void setEtudiants(List<Etudiant> etudiants) {
    this.etudiants = etudiants;
  }

  public List<Tuteur> getTuteurs() {
    return tuteurs;
  }

  public void setTuteurs(List<Tuteur> tuteurs) {
    this.tuteurs = tuteurs;
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
}
