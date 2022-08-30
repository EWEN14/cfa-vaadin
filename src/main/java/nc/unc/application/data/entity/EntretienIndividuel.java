package nc.unc.application.data.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "entretien_individuelle")
public class EntretienIndividuel implements Cloneable{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull(message = "La date ne peut pas être nulle")
  @Column(name = "date")
  private LocalDate date;

  @Column(name = "observations", length = 15000)
  private String observations;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime created_at;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updated_at;

  @NotNull(message = "L'étudiant ne peut pas être null")
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_etudiant")
  private Etudiant etudiant;

  @NotNull(message = "Le référent cfa ne peut pas être null")
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "id_referent_cfa")
  private ReferentCfa referentCfa;

  public EntretienIndividuel(){

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

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
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

  public Etudiant getEtudiant() {
    return etudiant;
  }

  public void setEtudiant(Etudiant etudiant) {
    this.etudiant = etudiant;
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
}