package nc.unc.application.data.entity;

import nc.unc.application.data.enums.Civilite;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referent_pedagogique")
public class ReferentPedagogique implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_referent_pedagogique", nullable = false)
    private Long id;

    @NotEmpty(message = "Le nom doit être renseigné")
    @NotNull(message = "Le nom ne peut pas être nul")
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom doit être renseigné")
    @NotNull(message = "Le prenom ne peut pas être nul")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull(message = "La civilité ne doit pas être nulle")
    @Enumerated(EnumType.STRING)
    @Column(name = "civilite" ,nullable = false)
    private Civilite civilite;

    @Email
    @NotNull(message = "L'email ne peut pas être nul")
    @Column(name = "email", nullable = false)
    private String email;

    @Range(message = "Le numéro de téléphone doit comporter 6 chiffres", min = 100000, max = 999999)
    @NotNull(message = "Le téléphone ne peut pas être nul")
    @Column(name = "telephone", nullable = false)
    private Integer telephone;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Civilite getCivilite() {
        return civilite;
    }

    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
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

    // Autres méthodes
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Tuteur { " +
                "\n id=" + id +
                "\n nom='" + nom + '\'' +
                "\n prenom='" + prenom + '\'' +
                "\n email='" + email + '\'' +
                "\n telephone=" + telephone +
                "\n civilite=" + civilite +
                " }";
    }


}
