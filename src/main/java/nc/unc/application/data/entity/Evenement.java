package nc.unc.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "evenement")
public class Evenement implements Cloneable{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_evenement", nullable = false)
  private Long id;

  @NotNull(message = "La libelle de l'évenement ne peut pas être nulle")
  @Column(name = "libelle")
  private String libelle;

  @Column(name = "description")
  private String description;

  @NotNull(message = "La date de début ne peut pas être nulle")
  @Column(name = "date_debut", nullable = false)
  private LocalDate dateDebut;

  @NotNull(message = "La date de fin ne peut pas être nulle")
  @Column(name = "date_fin", nullable = false)
  private LocalDate dateFin;

  @ManyToMany(cascade = CascadeType.MERGE)
  @JoinTable(name = "evenement_formations",
          joinColumns = @JoinColumn(name = "evenement_id"),
          inverseJoinColumns = @JoinColumn(name = "formations_id"))
  private Set<Formation> formations = new LinkedHashSet<>();

  public Set<Formation> getFormations() {
    return formations;
  }

  public void setFormations(Set<Formation> formations) {
    this.formations = formations;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLibelle() {
    return libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getDateDebut() {
    return dateDebut;
  }

  public void setDateDebut(LocalDate dateDebut) {
    this.dateDebut = dateDebut;
  }

  public LocalDate getDateFin() {
    return dateFin;
  }

  public void setDateFin(LocalDate dateFin) {
    this.dateFin = dateFin;
  }

  // Autres méthodes
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}