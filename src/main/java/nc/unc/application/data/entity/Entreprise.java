package nc.unc.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "entreprise")
public class Entreprise {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotEmpty(message = "L'enseigne de l'entreprise doit être renseigné")
  @Column(name = "enseigne", nullable = false)
  private String enseigne;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEnseigne() {
    return enseigne;
  }

  public void setEnseigne(String enseigne) {
    this.enseigne = enseigne;
  }
}
