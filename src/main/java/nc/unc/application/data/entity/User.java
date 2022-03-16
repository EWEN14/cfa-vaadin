package nc.unc.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;
import javax.persistence.*;

import nc.unc.application.data.AbstractEntity;
import nc.unc.application.data.enums.Role;

@Entity
@Table(name = "application_user")
public class User extends AbstractEntity {

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "nom", nullable = false)
  private String nom;

  @Column(name = "prenom", nullable = false)
  private String prenom;

  @JsonIgnore
  private String hashedPassword;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  private Set<Role> roles;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

}
