package nc.unc.application.data.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tuteur_habilitation")
public class TuteurHabilitation implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_tuteur_habilitation", nullable = false)
  private Long id;

  @NotNull(message = "Le statut de la formation ne peut pas être nul")
  @Column(name = "statut_formation", nullable = false)
  private String statutFormation;

  @Column(name = "date_formation")
  private LocalDate dateFormation;

  @Column(name = "modalite_formation")
  private String modaliteFormation;

  @Column(name = "date_habilitation")
  private LocalDate dateHabilitation;

  @Column(name = "observations", length = 15000)
  private String observations;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Tuteur.class)
  @JoinColumn(name = "id_tuteur")
  private Tuteur tuteur;

  @NotNull(message = "La formation liée à l'habilation ne peut pas être nulle")
  @ManyToOne(cascade = CascadeType.MERGE, optional = false)
  @JoinColumn(name = "id_formation", nullable = false)
  private Formation formation;

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

  public String getStatutFormation() {
    return statutFormation;
  }

  public void setStatutFormation(String statutFormation) {
    this.statutFormation = statutFormation;
  }

  public LocalDate getDateFormation() {
    return dateFormation;
  }

  public void setDateFormation(LocalDate dateFormation) {
    this.dateFormation = dateFormation;
  }

  public String getModaliteFormation() {
    return modaliteFormation;
  }

  public void setModaliteFormation(String modaliteFormation) {
    this.modaliteFormation = modaliteFormation;
  }

  public LocalDate getDateHabilitation() {
    return dateHabilitation;
  }

  public void setDateHabilitation(LocalDate dateHabilitation) {
    this.dateHabilitation = dateHabilitation;
  }

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public Tuteur getTuteur() {
    return tuteur;
  }

  public void setTuteur(Tuteur tuteur) {
    this.tuteur = tuteur;
  }

  public Formation getFormation() {
    return formation;
  }

  public void setFormation(Formation formation) {
    this.formation = formation;
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

  // autres méthodes


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TuteurHabilitation that = (TuteurHabilitation) o;
    return id.equals(that.id) && statutFormation.equals(that.statutFormation) && Objects.equals(dateFormation, that.dateFormation) && Objects.equals(modaliteFormation, that.modaliteFormation) && Objects.equals(dateHabilitation, that.dateHabilitation) && tuteur.equals(that.tuteur) && formation.equals(that.formation) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, statutFormation, dateFormation, modaliteFormation, dateHabilitation, tuteur, formation, createdAt, updatedAt);
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return "TuteurHabilitation{" +
            "\n id=" + id +
            "\n statutFormation='" + statutFormation + '\'' +
            "\n dateFormation=" + dateFormation +
            "\n modaliteFormation='" + modaliteFormation + '\'' +
            "\n observations='" + observations + '\'' +
            "\n dateHabilitation=" + dateHabilitation +
            "\n tuteur=" + (tuteur != null ? tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() : "") +
            "\n formation=" + (formation != null ? formation.getLibelleFormation() : "") +
            '}';
  }
}
