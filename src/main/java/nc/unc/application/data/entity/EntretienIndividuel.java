package nc.unc.application.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
  private String observations_entretien_individuel;

  @Column(name = "suivre_etudiant")
  private Boolean suivreEtudiant;

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

  public EntretienIndividuel() {}

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return "EntretienIndividuel{" +
            "\n id=" + id +
            ",\n date=" + date +
            ",\n observations_entretien_individuel='" + observations_entretien_individuel + '\'' +
            ",\n statutEtudiant=" + suivreEtudiant +
            ",\n created_at=" + created_at +
            ",\n updated_at=" + updated_at +
            ",\n etudiant=" + etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant() +
            ",\n referentCfa=" + referentCfa.getPrenomReferentCfa() + " " + referentCfa.getNomReferentCfa() +
            '}';
  }
}
