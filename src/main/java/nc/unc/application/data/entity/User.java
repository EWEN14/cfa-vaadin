package nc.unc.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import nc.unc.application.data.AbstractEntity;
import nc.unc.application.data.enums.Role;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "application_user")
public class User extends AbstractEntity implements Cloneable{

  @NotNull(message = "L'identifiant ne peut pas être nul")
  @Column(name = "username", nullable = false)
  private String username;

  @NotNull(message = "Le nom ne peut pas être nul")
  @Column(name = "nom", nullable = false)
  private String nomUser;

  @NotNull(message = "Le prénom ne peut pas être nul")
  @Column(name = "prenom", nullable = false)
  private String prenomUtilisateur;

  @NotNull(message = "Le mot de passe ne peut pas être vide")
  @Column(name = "hashed_password", nullable = false)
  @JsonIgnore
  private String hashedPasswordUser;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  private Set<Role> roles;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getNom() {
    return nomUser;
  }

  public void setNom(String nom) {
    this.nomUser = nom;
  }

  public String getPrenom() {
    return prenomUtilisateur;
  }

  public void setPrenom(String prenom) {
    this.prenomUtilisateur = prenom;
  }

  public String getHashedPassword() {
    return hashedPasswordUser;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPasswordUser = hashedPassword;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
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

  // autres fonctions
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return "User{" +
            "\n username='" + username + '\'' +
            "\n nomUser='" + nomUser + '\'' +
            "\n prenomUser='" + prenomUtilisateur + '\'' +
            "\n hashedPasswordUser='" + hashedPasswordUser + '\'' +
            "\n roles=" + roles +
            "\n createdAt=" + createdAt +
            "\n updatedAt=" + updatedAt +
            '}';
  }
}
