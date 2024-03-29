package nc.unc.application.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "formation")
public class Formation implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_formation", nullable = false)
  private Long id;

  @NotNull(message = "Le libellé de la formation ne peut pas être nul")
  @NotEmpty(message = "Le libellé de la formation doit être renseigné")
  @Column(name = "libelle_formation", nullable = false)
  private String libelleFormation;

  @NotNull(message = "Le code de la formation ne peut pas être nul")
  @NotEmpty(message = "Le code de la formation doit être renseigné")
  @Column(name = "code_formation", nullable = false)
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

  @Column(name = "heures_projet_universitaire")
  private Integer heuresProjetUniversitaire;

  @Column(name = "semaines_formation")
  private Integer semainesFormation;

  @Column(name = "lieu_formation")
  private String lieuFormation;

  @Column(name = "statut_actif", length = 40)
  private String statutActif;

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

  @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL)
  private List<EntretienCollectif> entretienCollectifs = new ArrayList<>();

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

  // Autres méthodes
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Formation formation = (Formation) o;
    return id.equals(formation.id) && libelleFormation.equals(formation.libelleFormation) && codeFormation.equals(formation.codeFormation) && Objects.equals(codeRome, formation.codeRome) && Objects.equals(niveauCertificationProfessionnelle, formation.niveauCertificationProfessionnelle) && Objects.equals(typeEmploiExerce, formation.typeEmploiExerce) && Objects.equals(semainesEntreprise, formation.semainesEntreprise) && Objects.equals(dureeHebdomadaireTravail, formation.dureeHebdomadaireTravail) && Objects.equals(heuresFormation, formation.heuresFormation) && Objects.equals(heuresProjetUniversitaire, formation.heuresProjetUniversitaire) && Objects.equals(semainesFormation, formation.semainesFormation) && Objects.equals(lieuFormation, formation.lieuFormation) && Objects.equals(statutActif, formation.statutActif) && Objects.equals(observations, formation.observations) && Objects.equals(createdAt, formation.createdAt) && Objects.equals(updatedAt, formation.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, libelleFormation, codeFormation, codeRome, niveauCertificationProfessionnelle, typeEmploiExerce, semainesEntreprise, dureeHebdomadaireTravail, heuresFormation, heuresProjetUniversitaire, semainesFormation, lieuFormation, statutActif, observations, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "Formation{" +
            "\n id=" + id +
            "\n libelleFormation='" + libelleFormation + '\'' +
            "\n codeFormation='" + codeFormation + '\'' +
            "\n codeRome='" + codeRome + '\'' +
            "\n niveauCertificationProfessionnelle=" + niveauCertificationProfessionnelle +
            "\n typeEmploiExerce='" + typeEmploiExerce + '\'' +
            "\n semainesEntreprise=" + semainesEntreprise +
            "\n dureeHebdomadaireTravail=" + dureeHebdomadaireTravail +
            "\n heuresFormation=" + heuresFormation +
            "\n semainesFormation=" + semainesFormation +
            "\n lieuFormation='" + lieuFormation + '\'' +
            "\n statutActif='" + statutActif + '\'' +
            "\n observations='" + observations + '\'' +
            "\n referentPedagogique=" +
            (referentPedagogique != null ? referentPedagogique.getPrenomReferentPedago() + " " + referentPedagogique.getNomReferentPedago() : "") +
            '}';
  }
}
