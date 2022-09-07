package nc.unc.application.data.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "formation")
public class Formation implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_formation", nullable = false)
  private Long id;

  @NotNull(message = "Le libellé de la formation ne peut pas être nul")
  @NotEmpty(message = "Le libellé de la formation doit être renseigné")
  @Column(name = "libelle_formation")
  private String libelleFormation;

  @NotNull(message = "Le code de la formation ne peut pas être nul")
  @NotEmpty(message = "Le code de la formation doit être renseigné")
  @Column(name = "code_formation")
  private String codeFormation;

  @Column(name = "code_rome", length = 15)
  private String codeRome;

  @Range(message = "Le niveau du diplôme doit être entre 3 et 8", min = 3, max = 8)
  @Column(name = "niveau_certification_pro")
  private Integer niveauCertificationProfessionnelle;

  @Column(name = "type_emploi_exerce")
  private String typeEmploiExerce;

  @Column(name = "semaines_entreprise")
  private Integer semainesEntreprise;

  @Column(name = "duree_hebdomadaire_travail")
  private Integer dureeHebdomadaireTravail;

  @Column(name = "heures_formation")
  private Integer heuresFormation;

  @Column(name = "semaines_formation")
  private Integer semainesFormation;

  @Column(name = "lieu_formation")
    private String lieuFormation;

  @Column(name = "observations", length = 15000)
  private String observations;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = ReferentPedagogique.class)
  @JoinColumn(name = "id_referent_pedagogique")
  private ReferentPedagogique referentPedagogique;

  @OneToMany(mappedBy = "formation", cascade = CascadeType.MERGE, targetEntity = TuteurHabilitation.class)
  private List<TuteurHabilitation> tuteurHabilitations = new ArrayList<>();

  @OneToMany(mappedBy = "formation", cascade = CascadeType.MERGE, targetEntity = Etudiant.class)
  private List<Etudiant> etudiants = new ArrayList<>();

  @OneToMany(mappedBy = "formation", cascade = CascadeType.MERGE, targetEntity = Contrat.class)
  private List<Contrat> contrats = new ArrayList<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToMany(mappedBy = "formations", cascade = CascadeType.MERGE)
  private Set<Evenement> evenements = new LinkedHashSet<>();

  @PreRemove
  private void preRemove() {
    for (TuteurHabilitation th : tuteurHabilitations) {
      th.setFormation(null);
    }
    for (Etudiant e : etudiants) {
      e.setFormation(null);
    }
    for (Contrat c : contrats) {
      c.setFormation(null);
    }
  }

  // Getters et Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLibelleFormation() {
    return libelleFormation;
  }

  public void setLibelleFormation(String libelleFormation) {
    this.libelleFormation = libelleFormation;
  }

  public String getCodeFormation() {
    return codeFormation;
  }

  public void setCodeFormation(String codeFormation) {
    this.codeFormation = codeFormation;
  }

  public String getCodeRome() {
    return codeRome;
  }

  public void setCodeRome(String codeRome) {
    this.codeRome = codeRome;
  }

  public Integer getNiveauCertificationProfessionnelle() {
    return niveauCertificationProfessionnelle;
  }

  public void setNiveauCertificationProfessionnelle(Integer niveauCertificationProfessionnelle) {
    this.niveauCertificationProfessionnelle = niveauCertificationProfessionnelle;
  }

  public String getTypeEmploiExerce() {
    return typeEmploiExerce;
  }

  public void setTypeEmploiExerce(String typeEmploiExerce) {
    this.typeEmploiExerce = typeEmploiExerce;
  }

  public Integer getSemainesEntreprise() {
    return semainesEntreprise;
  }

  public void setSemainesEntreprise(Integer semainesEntreprise) {
    this.semainesEntreprise = semainesEntreprise;
  }

  public Integer getDureeHebdomadaireTravail() {
    return dureeHebdomadaireTravail;
  }

  public void setDureeHebdomadaireTravail(Integer dureeHebdomadaireTravail) {
    this.dureeHebdomadaireTravail = dureeHebdomadaireTravail;
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

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public ReferentPedagogique getReferentPedagogique() {
    return referentPedagogique;
  }

  public void setReferentPedagogique(ReferentPedagogique referentPedagogique) {
    this.referentPedagogique = referentPedagogique;
  }

  public List<TuteurHabilitation> getTuteurHabilitations() {
    return tuteurHabilitations;
  }

  public void setTuteurHabilitations(List<TuteurHabilitation> tuteurHabilitations) {
    this.tuteurHabilitations = tuteurHabilitations;
  }

  public List<Contrat> getContrats() {
    return contrats;
  }

  public void setContrats(List<Contrat> contrats) {
    this.contrats = contrats;
  }

  public List<Etudiant> getEtudiants() {
    return etudiants;
  }

  public void setEtudiants(List<Etudiant> etudiants) {
    this.etudiants = etudiants;
  }

  public Set<Evenement> getEvenements() {
    return evenements;
  }

  public void setEvenements(Set<Evenement> evenements) {
    this.evenements = evenements;
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
    Formation formation = (Formation) o;
    return id.equals(formation.id) && Objects.equals(libelleFormation, formation.libelleFormation) && Objects.equals(codeFormation, formation.codeFormation) && Objects.equals(codeRome, formation.codeRome) && Objects.equals(niveauCertificationProfessionnelle, formation.niveauCertificationProfessionnelle) && Objects.equals(typeEmploiExerce, formation.typeEmploiExerce) && Objects.equals(semainesEntreprise, formation.semainesEntreprise) && Objects.equals(dureeHebdomadaireTravail, formation.dureeHebdomadaireTravail) && Objects.equals(heuresFormation, formation.heuresFormation) && Objects.equals(semainesFormation, formation.semainesFormation) && Objects.equals(lieuFormation, formation.lieuFormation) && Objects.equals(observations, formation.observations) && createdAt.equals(formation.createdAt) && updatedAt.equals(formation.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, libelleFormation, codeFormation, codeRome, niveauCertificationProfessionnelle, typeEmploiExerce, semainesEntreprise, dureeHebdomadaireTravail, heuresFormation, semainesFormation, lieuFormation, observations, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "Formation{" +
            "\n id=" + id +
            "\n , libelleFormation='" + libelleFormation + '\'' +
            "\n , codeFormation='" + codeFormation + '\'' +
            "\n , codeRome='" + codeRome + '\'' +
            "\n , niveauCertificationProfessionnelle=" + niveauCertificationProfessionnelle +
            "\n , typeEmploiExerce='" + typeEmploiExerce + '\'' +
            "\n , semainesEntreprise=" + semainesEntreprise +
            "\n , dureeHebdomadaireTravail=" + dureeHebdomadaireTravail +
            "\n , heuresFormation=" + heuresFormation +
            "\n , semainesFormation=" + semainesFormation +
            "\n , lieuFormation='" + lieuFormation + '\'' +
            "\n , observations='" + observations + '\'' +
            "\n , referentPedagogique=" +
            (referentPedagogique != null ? referentPedagogique.getPrenomReferentPedago() + " " + referentPedagogique.getNomReferentPedago() : "") +
            '}';
  }
}
