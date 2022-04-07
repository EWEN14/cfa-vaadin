package nc.unc.application.data.entity;

import nc.unc.application.data.enums.Civilite;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "referent_pedagogique")
public class ReferentPedagogique implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Le nom ne peut pas être nul")
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull(message = "Le prenom ne peut pas être nul")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Email
    @NotNull(message = "L'email ne peut pas être nul")
    @Column(name = "email", nullable = false)
    private String email;

    @Max(999999)
    @Min(111111)
    @NotNull(message = "Le téléphone ne peut pas être nul")
    @Column(name = "telephone", nullable = false)
    private Integer telephone;

    @NotNull(message = "La civilité ne doit pas être nulle")
    @Enumerated(EnumType.STRING)
    @Column(name = "civilite" ,nullable = false)
    private Civilite civilite;

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Civilite getCivilite() {
        return civilite;
    }

    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

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
