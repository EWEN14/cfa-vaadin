package nc.unc.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nc.unc.application.data.enums.Decua;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "tuteur")
public class Tuteur implements Cloneable{

    //Id du tuteur
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_tuteur", nullable = false)
    private Long id;

    //Nom du tuteur
    @NotEmpty(message = "Le nom doit être renseigné")
    @NotNull(message = "Le nom ne peut pas être nul")
    @Column(name = "nom", nullable = false)
    private String nom;

    //Prénom du tuteur
    @NotEmpty(message = "Le prénom doit être renseigné")
    @NotNull(message = "Le prénom ne peut pas être nul")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    //Date de naissance du tuteur
    @NotNull(message = "La date de naissance ne peut pas être nulle")
    @Past(message = "La date de naissance ne peut pas être aujourd'hui ou dans le futur")
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    //Email du tuteur
    @Email
    @NotNull(message = "L'email ne peut pas être null")
    @Column(name = "email")
    private String email;

    //Casier judiciaire fourni du tuteur
    @NotNull(message = "La casier judiciaire fournit ne peut pas être null")
    @Column(name = "casier_judiciaire_fourni")
    private Boolean casierJudiciaireFourni;

    //Diplôme le plus élévé du tuteur
    @NotNull(message = "Le diplôme élevé obtenu ne peut pas être null")
    @Column(name = "diplome_eleve_obtenu")
    private String diplomeEleveObtenu;

    //Le poste occupé du tuteur
    @NotNull(message = "Le poste occupé ne peut pas être null")
    @Column(name = "poste_occupe")
    private String posteOccupe;

    //L'expérience professionnelle du tuteur
    @Column(name = "annee_experience_professionnelle")
    private String anneeExperienceProfessionnelle;

    //Niveau du diplôme du tuteur
    @NotNull(message = "Le niveau du diplome ne peut pas être null")
    @Column(name = "niveau_diplome", nullable = false)
    private Long niveauDiplome;

    //Première pièce jointe
    @NotNull(message = "La piece jointe ne doit pas être nulle")
    @Enumerated(EnumType.STRING)
    @Column(name = "pj1", nullable = false)
    private Decua pieceJointe1;

    //Deuxième pièce jointe
    @NotNull(message = "La piece jointe ne doit pas être nulle")
    @Enumerated(EnumType.STRING)
    @Column(name = "pj2", nullable = false)
    private Decua pieceJointe2;

    //Premier numéro de téléphone
    @Max(999999)
    @Min(111111)
    @NotNull(message = "Le numéro de téléphone ne doit pas être null")
    @Column(name = "telephone_1")
    private Integer telephone1;

    //Deuxième numéro de téléphone
    @Max(999999)
    @Min(111111)
    @NotNull(message = "La numéro de téléphone ne doit pas être nulle")
    @Column(name = "telephone_2")
    private Integer telephone2;

    //Entreprise du tuteur
    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Entreprise.class)
    @JoinColumn(name = "id_entreprise")
    @JsonIgnoreProperties({"tuteurs"})
    private Entreprise entreprise;

    //Getters et Setters
    public Decua getPieceJointe1() {
        return pieceJointe1;
    }

    public void setPieceJointe1(Decua pieceJointe1) {
        this.pieceJointe1 = pieceJointe1;
    }

    public Decua getPieceJointe2() {
        return pieceJointe2;
    }

    public void setPieceJointe2(Decua pieceJointe2) {
        this.pieceJointe2 = pieceJointe2;
    }

    public Long getNiveauDiplome() {
        return niveauDiplome;
    }

    public void setNiveauDiplome(Long niveauDiplome) {
        this.niveauDiplome = niveauDiplome;
    }


    public String getExperienceProfessionnelle() {
        return anneeExperienceProfessionnelle;
    }

    public void setExperienceProfessionnelle(String anneeExperienceProfessionnelle) {
        this.anneeExperienceProfessionnelle = anneeExperienceProfessionnelle;
    }

    public String getPosteOccupe() {
        return posteOccupe;
    }

    public void setPosteOccupe(String posteOccupe) {
        this.posteOccupe = posteOccupe;
    }

    public String getDiplomeEleveObtenu() {
        return diplomeEleveObtenu;
    }

    public void setDiplomeEleveObtenu(String diplomeEleveObtenu) {
        this.diplomeEleveObtenu = diplomeEleveObtenu;
    }

    public Boolean getCasierJudiciaireFourni() {
        return casierJudiciaireFourni;
    }

    public void setCasierJudiciaireFourni(Boolean casierJudiciaireFourni) {
        this.casierJudiciaireFourni = casierJudiciaireFourni;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public Integer getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(Integer telephone2) {
        this.telephone2 = telephone2;
    }

    public Integer getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(Integer telephone1) {
        this.telephone1 = telephone1;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
