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
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "entreprise")
public class Entreprise {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_entreprise", nullable = false)
  private Long id;


  @NotNull(message = "Le statut actif de l'entreprise ne peut être nul")
  @Column(name = "statut_actif_entreprise", nullable = false)
  private String statutActifEntreprise;

  @NotEmpty(message = "L'enseigne de l'entreprise doit être renseigné")
  @Column(name = "enseigne", nullable = false)
  private String enseigne;

  @NotEmpty(message = "La raison sociale de l'entreprise doit être renseignée")
  @NotNull(message = "La raison sociale de l'entreprise ne peut pas être nulle")
  @Column(name = "raison_sociale", nullable = false)
  private String raisonSociale;

  @NotEmpty(message = "La forme juridique de l'entreprise doit être renseignée")
  @NotNull(message = "La forme juridique de l'entreprise ne peut pas être nulle")
  @Column(name = "forme_juridique", nullable = false)
  private String formeJuridique;

  @NotNull(message = "Le numéro de ridet ne peut pas être nul")
  @NotEmpty(message = "Le numéro de ridet doit être renseigné")
  @Pattern(message = "Le numéro de ridet doit suivre le format suivant : 0 000 000.000", regexp = "/\\d{3}\\s\\d{3}\\s\\d{3}\\.\\d{3}/gm")
  @Column(name = "numero_ridet")
  private String numeroRidet;

  @NotNull(message = "Le numéro cafat ne peut pas être nul")
  @Range(min = 100000, max = 999999)
  @Column(name = "numero_cafat")
  private Long numeroCafat;

  @NotNull(message = "Le nombre de salariés ne peut pas être nul")
  @Column(name = "nombre_salarie")
  private Long nombreSalarie;

  @Column(name = "code_naf")
  private String codeNaf;

  @Column(name = "activite_entreprise")
  private String activiteEntreprise;

  @Column(name = "convention_collective")
  private String conventionCollective;

  @Column(name = "boite_postale")
  private String boitePostale;

  @Column(name = "commune")
  private String commune;

  @Column(name = "nom_representant_employeur")
  private String nomRepresentantEmployeur;

  @Column(name = "prenom_representant_employeur")
  private String prenomRepresentantEmployeur;

  @Column(name = "fonction_representant_employeur")
  private String fonctionRepresentantEmployeur;

  @OneToMany(mappedBy = "entreprise", targetEntity = Etudiant.class)
  private List<Etudiant> etudiants = new java.util.ArrayList<>();

  @OneToMany(mappedBy = "entreprise", targetEntity = Tuteur.class)
  private List<Tuteur> tuteurs = new ArrayList<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

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

  public String getFormeJuridique() {
    return formeJuridique;
  }

  public void setFormeJuridique(String formeJuridique) {
    this.formeJuridique = formeJuridique;
  }

  public String getNumeroRidet() {
    return numeroRidet;
  }

  public void setNumeroRidet(String numeroRidet) {
    this.numeroRidet = numeroRidet;
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

  public String getBoitePostale() {
    return boitePostale;
  }

  public void setBoitePostale(String boitePostale) {
    this.boitePostale = boitePostale;
  }

  public String getCommune() {
    return commune;
  }

  public void setCommune(String commune) {
    this.commune = commune;
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

  public List<Tuteur> getTuteurs() {
    return tuteurs;
  }

  public void setTuteurs(List<Tuteur> tuteurs) {
    this.tuteurs = tuteurs;
  }
}
