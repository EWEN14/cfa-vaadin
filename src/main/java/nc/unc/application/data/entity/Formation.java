package nc.unc.application.data.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

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

  @NotNull(message = "Le code rome ne peut pas être nul")
  @Pattern(message = "Le code rome est composé d'une lettre majuscule puis de 4 chiffres. Ex : M1234", regexp = "/[A-Z][0-9]{4}/gm")
  @Column(name = "code_rome", length = 15)
  private String codeRome;

  @OneToOne(cascade = CascadeType.MERGE, targetEntity = ReferentPedagogique.class)
  @JoinColumn(name = "id_referent_pedagogique")
  private ReferentPedagogique referentPedagogique;

  @OneToOne(mappedBy = "formation", cascade = CascadeType.MERGE, targetEntity = TuteurHabilitation.class)
  private TuteurHabilitation tuteurHabilitation;

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

  public ReferentPedagogique getReferentPedagogique() {
    return referentPedagogique;
  }

  public void setReferentPedagogique(ReferentPedagogique referentPedagogique) {
    this.referentPedagogique = referentPedagogique;
  }

  public TuteurHabilitation getTuteurHabilitation() {
    return tuteurHabilitation;
  }

  public void setTuteurHabilitation(TuteurHabilitation tuteurHabilitation) {
    this.tuteurHabilitation = tuteurHabilitation;
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