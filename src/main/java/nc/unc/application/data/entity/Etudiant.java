package nc.unc.application.data.entity;

import nc.unc.application.data.enums.Civilite;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "etudiant")
public class Etudiant implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  // le NotNull permet l'affichage du message du NotEmpty avec le binder
  // dans le formulaire (un peu bizarre, mais ok)
  @NotEmpty(message = "Le nom doit être renseigné")
  @NotNull(message = "Le nom ne peut pas être nul")
  @Column(name = "nom", nullable = false)
  private String nom;

  @NotEmpty(message = "Le prénom doit être renseigné")
  @NotNull(message = "Le prénom ne peut pas être nul")
  @Column(name = "prenom", nullable = false)
  private String prenom;

  @NotNull(message = "La civilité ne peut pas être nulle")
  @Enumerated(EnumType.STRING)
  @Column(name = "civilite", nullable = false)
  private Civilite civilite;

  @NotNull(message = "La date de naissance ne peut pas être nulle")
  @Past(message = "La date de naissance ne peut pas être aujourd'hui ou dans le futur")
  @Column(name = "date_naissance", nullable = false)
  private LocalDate dateNaissance;

  @Column(name = "situation_anne_precedente", nullable = false)
  private String situationAnneePrecedente;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDate getDateNaissance() {
    return dateNaissance;
  }

  public void setDateNaissance(LocalDate dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  public Civilite getCivilite() {
    return civilite;
  }

  public void setCivilite(Civilite civilite) {
    this.civilite = civilite;
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

  public boolean isNewEtudiant() {
    return getId() == null;
  }

  public String getSituationAnneePrecedente() {
    return situationAnneePrecedente;
  }

  public void setSituationAnneePrecedente(String situationAnneePrecedente) {
    this.situationAnneePrecedente = situationAnneePrecedente;
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Etudiant etudiant = (Etudiant) o;
    return Objects.equals(id, etudiant.id) && Objects.equals(nom, etudiant.nom) && Objects.equals(prenom, etudiant.prenom) && civilite == etudiant.civilite && Objects.equals(dateNaissance, etudiant.dateNaissance) && Objects.equals(createdAt, etudiant.createdAt) && Objects.equals(updatedAt, etudiant.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nom, prenom, civilite, dateNaissance, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "Etudiant{" +
            "id=" + id +
            ", nom='" + nom + '\'' +
            ", prenom='" + prenom + '\'' +
            ", civilite=" + civilite +
            ", dateNaissance=" + dateNaissance +
            '}';
  }
}
