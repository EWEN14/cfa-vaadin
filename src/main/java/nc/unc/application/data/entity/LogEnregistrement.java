package nc.unc.application.data.entity;

import nc.unc.application.data.enums.TypeCrud;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_enregistrement")
public class LogEnregistrement {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_log_enregistrement", nullable = false)
  private Long id;

  @Column(name = "description_log", nullable = false, length = 15000)
  private String description_log;

  @Enumerated(EnumType.STRING)
  @Column(name = "type_crud", nullable = false)
  private TypeCrud typeCrud;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "executant")
  private String executant;

  public String getExecutant() {
    return executant;
  }

  public void setExecutant(String executant) {
    this.executant = executant;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription_log() {
    return description_log;
  }

  public void setDescription_log(String description_log) {
    this.description_log = description_log;
  }

  public TypeCrud getTypeCrud() {
    return typeCrud;
  }

  public void setTypeCrud(TypeCrud typeCrud) {
    this.typeCrud = typeCrud;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
