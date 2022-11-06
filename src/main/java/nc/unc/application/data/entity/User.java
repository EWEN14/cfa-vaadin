package nc.unc.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import nc.unc.application.data.AbstractEntity;
import nc.unc.application.data.enums.Role;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Table(name = "application_user")
public class User extends AbstractEntity implements Cloneable{

  @NotNull(message = "L'identifiant ne peut pas être nul")
  @Column(name = "username", nullable = false)
  private String username;

  @NotNull(message = "Le nom ne peut pas être nul")
  @Column(name = "nom", nullable = false)
  private String nom;

  @NotNull(message = "Le prénom ne peut pas être nul")
  @Column(name = "prenom", nullable = false)
  private String prenom;

  @NotNull(message = "Le mot de passe ne peut pas être vide")
  @Column(name = "hashed_password", nullable = false)
  @JsonIgnore
  private String hashedPassword;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  private Set<Role> roles;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // autres fonctions
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return "User{" +
            "\n username='" + username + '\'' +
            "\n nomUser='" + nom + '\'' +
            "\n prenomUser='" + prenom + '\'' +
            "\n hashedPasswordUser='" + hashedPassword + '\'' +
            "\n roles=" + roles +
            "\n createdAt=" + createdAt +
            "\n updatedAt=" + updatedAt +
            '}';
  }
}
