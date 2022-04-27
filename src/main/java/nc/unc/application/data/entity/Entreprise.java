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
  private Long numeroCafatEntreprise;

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
  private String nomContactCfa;

  @Column(name = "prenom_contact_cfa")
  private String prenomContactCfa;

  @Column(name = "fonction_contact_cfa")
  private String fonctionContactCfa;

  @Column(name = "telephone_contact_cfa")
  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  private Integer telephoneContactCfa;

  @Column(name = "email_contact_cfa")
  private String emailContactCfa;

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

  @OneToMany(mappedBy = "entreprise", cascade = CascadeType.MERGE)
  private List<Contrat> contrats = new ArrayList<>();

  public List<Contrat> getContrats() {
    return contrats;
  }

  public void setContrats(List<Contrat> contrats) {
    this.contrats = contrats;
  }

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

  public Long getNumeroCafatEntreprise() {
    return numeroCafatEntreprise;
  }

  public void setNumeroCafatEntreprise(Long numeroCafat) {
    this.numeroCafatEntreprise = numeroCafat;
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

  public String getEmailContactCfa() {
    return emailContactCfa;
  }

  public void setEmailContactCfa(String email_contact_cfa) {
    this.emailContactCfa = email_contact_cfa;
  }

  public @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999) Integer getTelephoneContactCfa() {
    return telephoneContactCfa;
  }

  public void setTelephoneContactCfa(@Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999) Integer telephone_contact_cfa) {
    this.telephoneContactCfa = telephone_contact_cfa;
  }

  public String getFonctionContactCfa() {
    return fonctionContactCfa;
  }

  public void setFonctionContactCfa(String fonction_contact_cfa) {
    this.fonctionContactCfa = fonction_contact_cfa;
  }

  public String getPrenomContactCfa() {
    return prenomContactCfa;
  }

  public void setPrenomContactCfa(String prenom_contact_cfa) {
    this.prenomContactCfa = prenom_contact_cfa;
  }

  public String getNomContactCfa() {
    return nomContactCfa;
  }

  public void setNomContactCfa(String nom_contact_cfa) {
    this.nomContactCfa = nom_contact_cfa;
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

  @Override
  public String toString() {
    return "Entreprise { " +
            "\n id=" + id +
            "\n statutActifEntreprise='" + statutActifEntreprise + '\'' +
            "\n enseigne='" + enseigne + '\'' +
            "\n raisonSociale='" + raisonSociale + '\'' +
            "\n numeroRidet='" + numeroRidet + '\'' +
            "\n formeJuridique='" + formeJuridique + '\'' +
            "\n numeroCafat=" + numeroCafatEntreprise +
            "\n nombreSalarie=" + nombreSalarie +
            "\n codeNaf='" + codeNaf + '\'' +
            "\n activiteEntreprise='" + activiteEntreprise + '\'' +
            "\n conventionCollective='" + conventionCollective + '\'' +
            "\n nomRepresentantEmployeur='" + nomRepresentantEmployeur + '\'' +
            "\n prenomRepresentantEmployeur='" + prenomRepresentantEmployeur + '\'' +
            "\n fonctionRepresentantEmployeur='" + fonctionRepresentantEmployeur + '\'' +
            "\n nomContactCfa='" + nomContactCfa + '\'' +
            "\n prenomContactCfa='" + prenomContactCfa + '\'' +
            "\n fonctionContactCfa='" + fonctionContactCfa + '\'' +
            "\n telephoneContactCfa='" + telephoneContactCfa + '\'' +
            "\n emailContactCfa='" + emailContactCfa + '\'' +
            "\n adressePhysiqueCommune='" + adressePhysiqueCommune + '\'' +
            "\n adressePhysiqueCodePostal=" + adressePhysiqueCodePostal +
            "\n adressePhysiqueRue='" + adressePhysiqueRue + '\'' +
            "\n adressePostaleCommune='" + adressePostaleCommune + '\'' +
            "\n adressePostaleCodePostal=" + adressePostaleCodePostal +
            "\n adressePostaleRueOuBp='" + adressePostaleRueOuBp + '\'' +
            "\n observations='" + observations + '\'' +
            " }";
  }
}
