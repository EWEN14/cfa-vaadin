package nc.unc.application.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "numero_convention_formation")
/**
 * Entité qui représentera une table composée d'une seule ligne représentant le numéro
 * à mettre sur une nouvelle convention de formation (entre 1 et 1000)
 */
public class NumConventionFormation {

  // Id
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_numero_convention_formation", nullable = false)
  private Long id;

  // Numéro de la convention
  @Range(message = "Le numéro de la convention doit être entre 1 et 1000", min = 1, max = 1000)
  @Column(name = "numero_convention", nullable = false)
  private int nomTuteur;
}
