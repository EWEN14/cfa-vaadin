package nc.unc.application.data.entity;

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

    @PreRemove
    private void preRemove() {
        for (Formation f : formations) {
            f.setReferentPedagogique(null);
        }
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomReferentPedago() {
        return nomReferentPedago;
    }

    public void setNomReferentPedago(String nom) {
        this.nomReferentPedago = nom;
    }

    public String getPrenomReferentPedago() {
        return prenomReferentPedago;
    }

    public void setPrenomReferentPedago(String prenom) {
        this.prenomReferentPedago = prenom;
    }

    public Civilite getCiviliteReferentPedago() {
        return civiliteReferentPedago;
    }

    public void setCiviliteReferentPedago(Civilite civilite) {
        this.civiliteReferentPedago = civilite;
    }

    public String getEmailReferentPedago() {
        return emailReferentPedago;
    }

    public void setEmailReferentPedago(String email) {
        this.emailReferentPedago = email;
    }

    public Integer getTelephoneReferentPedago() {
        return telephoneReferentPedago;
    }

    public void setTelephoneReferentPedago(Integer telephone) {
        this.telephoneReferentPedago = telephone;
    }

    public List<Formation> getFormations() {
        return formations;
    }

    public void setFormations(List<Formation> formations) {
        this.formations = formations;
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
                "\n nom='" + nomReferentPedago + '\'' +
                "\n prenom='" + prenomReferentPedago + '\'' +
                "\n email='" + emailReferentPedago + '\'' +
                "\n telephone=" + telephoneReferentPedago +
                "\n civilite=" + civiliteReferentPedago +
                " }";
    }


}
