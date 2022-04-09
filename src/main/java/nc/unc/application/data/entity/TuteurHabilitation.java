package nc.unc.application.data.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tuteur_habilitation")
public class TuteurHabilitation {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_tuteur_habilitation", nullable = false)
  private Long id;

  @Column(name = "statut_formation", nullable = false)
  private String statutFormation;

  @Column(name = "date_formation")
  private LocalDate dateFormation;

  @OneToOne(cascade = CascadeType.MERGE, targetEntity = Formation.class)
  @JoinColumn(name = "id_formation")
  private Formation formation;

  @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Tuteur.class)
  @JoinColumn(name = "id_tuteur")
  private Tuteur tuteur;

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
}
