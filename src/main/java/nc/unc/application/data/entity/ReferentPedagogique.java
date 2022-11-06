package nc.unc.application.data.entity;

import lombok.Getter;
import lombok.Setter;
import nc.unc.application.data.enums.Civilite;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "referent_pedagogique")
public class ReferentPedagogique implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_referent_pedagogique", nullable = false)
    private Long id;

    @NotEmpty(message = "Le nom doit être renseigné")
    @NotNull(message = "Le nom ne peut pas être nul")
    @Column(name = "nom", nullable = false)
    private String nomReferentPedago;

    @NotBlank(message = "Le prénom doit être renseigné")
    @NotNull(message = "Le prenom ne peut pas être nul")
    @Column(name = "prenom", nullable = false)
    private String prenomReferentPedago;

    @NotNull(message = "La civilité ne doit pas être nulle")
    @Enumerated(EnumType.STRING)
    @Column(name = "civilite" ,nullable = false)
    private Civilite civiliteReferentPedago;

    @Email
    @NotNull(message = "L'email ne peut pas être nul")
    @Column(name = "email", nullable = false)
    private String emailReferentPedago;

    @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
    @NotNull(message = "Le téléphone ne peut pas être nul")
    @Column(name = "telephone", nullable = false)
    private Integer telephoneReferentPedago;

    @OneToMany(mappedBy = "referentPedagogique", cascade = CascadeType.MERGE)
    private List<Formation> formations = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Retrait du référent pédagogique dans les formations avant suppression
     */
    @PreRemove
    private void preRemove() {
        for (Formation f : formations) {
            f.setReferentPedagogique(null);
        }
    }

    // Autres méthodes
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Référent Pédagogique : { " +
                "\n id=" + id +
                "\n nom='" + nomReferentPedago + '\'' +
                "\n prenom='" + prenomReferentPedago + '\'' +
                "\n email='" + emailReferentPedago + '\'' +
                "\n telephone=" + telephoneReferentPedago +
                "\n civilite=" + civiliteReferentPedago +
                " }";
    }


}
