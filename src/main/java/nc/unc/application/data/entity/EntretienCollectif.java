package nc.unc.application.data.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class EntretienCollectif implements Cloneable{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull(message = "La date ne peut pas être nulle")
  @Column(name = "date")
  private LocalDate date;

  @Column(name = "observations", length = 15000)
  private String observations_entretien_collectif;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime created_at;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updated_at;

  @NotNull(message = "La formation ne peut pas être null")
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_formation")
  private Formation formation;

  @NotNull(message = "Le référent cfa ne peut pas être null")
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_referent_cfa")
  private ReferentCfa referentCfa;

  public EntretienCollectif(){

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getObservations_entretien_collectif() {
    return observations_entretien_collectif;
  }

  public void setObservations_entretien_collectif(String observations_entretien_collectif) {
    this.observations_entretien_collectif = observations_entretien_collectif;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  public LocalDateTime getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(LocalDateTime updated_at) {
    this.updated_at = updated_at;
  }

  public Formation getFormation() {
    return formation;
  }

  public void setFormation(Formation formation) {
    this.formation = formation;
  }

  public ReferentCfa getReferentCfa() {
    return referentCfa;
  }

  public void setReferentCfa(ReferentCfa referentCfa) {
    this.referentCfa = referentCfa;
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EntretienCollectif that = (EntretienCollectif) o;
    return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(observations_entretien_collectif, that.observations_entretien_collectif) && Objects.equals(created_at, that.created_at) && Objects.equals(updated_at, that.updated_at) && Objects.equals(formation, that.formation) && Objects.equals(referentCfa, that.referentCfa);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, date, observations_entretien_collectif, created_at, updated_at, formation, referentCfa);
  }
}
