package nc.unc.application.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
  @Pattern(message = "Le numéro de ridet doit suivre le format suivant : 0 000 000.000", regexp = "\\d\\s\\d{3}\\s\\d{3}\\.\\d{3}$")
  @Column(name = "numero_ridet")
  private String numeroRidet;

  @Column(name = "forme_juridique")
  private String formeJuridique;

  @Range(message = "Le numéro de Cafat doit comporter 6 chiffres", min = 100000, max = 999999)
  @Column(name = "numero_cafat")
  private Integer numeroCafatEntreprise;

  @Column(name = "nombre_salarie")
  private Integer nombreSalarie;

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

  @Column(name = "telephone_entreprise")
  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  private Integer telephoneEntreprise;

  @Email
  @Column(name = "email_entreprise")
  private String emailEntreprise;

  @Column(name = "prenom_contact_cfa")
  private String prenomContactCfa;

  @Column(name = "nom_contact_cfa")
  private String nomContactCfa;

  @Column(name = "fonction_contact_cfa")
  private String fonctionContactCfa;

  @Column(name = "telephone_contact_cfa")
  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  private Integer telephoneContactCfa;

  @Email
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

  @Column(name = "observations", length = 15000)
  private String observations;

  @OneToMany(mappedBy = "entreprise", targetEntity = Etudiant.class)
  private List<Etudiant> etudiants = new ArrayList<>();

  @OneToMany(mappedBy = "entreprise", targetEntity = Tuteur.class)
  private List<Tuteur> tuteurs = new ArrayList<>();

  @OneToMany(mappedBy = "entreprise", targetEntity = Contrat.class)
  private List<Contrat> contrats = new ArrayList<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  /**
   * Avant la suppression d'une entreprise, on enlève ses références
   * dans les étudiants, tuteurs et contrats
   */
  @PreRemove
  private void preRemove() {
    for (Etudiant e : etudiants) {
      e.setEntreprise(null);
    }
    for (Tuteur t : tuteurs) {
      t.setEntreprise(null);
    }
    for (Contrat c: contrats) {
      c.setEntreprise(null);
    }
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
            "\n telephoneEntreprise='" + telephoneEntreprise + '\'' +
            "\n emailEntreprise='" + emailEntreprise + '\'' +
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
