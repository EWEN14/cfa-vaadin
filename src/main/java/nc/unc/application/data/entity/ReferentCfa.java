package nc.unc.application.data.entity;

import lombok.Getter;
import lombok.Setter;
import nc.unc.application.data.enums.Civilite;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "referent_cfa")
public class ReferentCfa implements Cloneable{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_referent_cfa", nullable = false)
  private Long id;

  @NotEmpty(message = "Le prénom doit être renseigné")
  @NotNull(message = "Le prénom ne peut pas être nul")
  @Column(name = "prenom")
  private String prenomReferentCfa;

  @NotEmpty(message = "Le nom doit être renseigné")
  @NotNull(message = "Le nom ne peut pas être nul")
  @Column(name = "nom")
  private String nomReferentCfa;

  @Email
  @NotNull(message = "L'email ne peut pas être nul")
  @Column(name = "email")
  private String emailReferentCfa;

  @NotNull(message = "La civilité ne doit pas être nulle")
  @Enumerated(EnumType.STRING)
  @Column(name = "civilite" ,nullable = false)
  private Civilite civiliteReferentCfa;

  @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
  @NotNull(message = "Le téléphone ne peut pas être nul")
  @Column(name = "telephone")
  private Integer telephoneReferentCfa;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime created_at;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updated_at;

  @OneToMany(mappedBy = "referentCfa", cascade = CascadeType.ALL)
  private List<EntretienIndividuel> entretienIndividuels = new ArrayList<>();

  @OneToMany(mappedBy = "referentCfa", cascade = CascadeType.ALL)
  private List<EntretienCollectif> entretienCollectif = new ArrayList<>();

  // Autres méthodes
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return "Référent Cfa : { " +
            "\n id=" + id +
            "\n nom='" + nomReferentCfa + '\'' +
            "\n prenom='" + prenomReferentCfa + '\'' +
            "\n email='" + emailReferentCfa + '\'' +
            "\n telephone=" + telephoneReferentCfa +
            "\n civilite=" + civiliteReferentCfa +
            " }";
  }
}
