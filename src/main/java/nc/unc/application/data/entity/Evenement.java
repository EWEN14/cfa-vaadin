package nc.unc.application.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "evenement")
public class Evenement implements Cloneable {
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

  @NotNull(message = "l'événement doit être liée à au moins une formation")
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinTable(name = "evenement_formations",
          joinColumns = @JoinColumn(name = "evenement_id"),
          inverseJoinColumns = @JoinColumn(name = "formations_id"))
  private Set<Formation> formations;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime created_at;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updated_at;

  // Autres méthodes
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Evenement evenement = (Evenement) o;
    return id.equals(evenement.id) && Objects.equals(libelle, evenement.libelle) && Objects.equals(description, evenement.description) && dateDebut.equals(evenement.dateDebut) && dateFin.equals(evenement.dateFin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, libelle, description, dateDebut, dateFin);
  }
}
