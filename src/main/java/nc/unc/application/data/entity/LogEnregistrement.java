package nc.unc.application.data.entity;

import lombok.Getter;
import lombok.Setter;
import nc.unc.application.data.enums.TypeCrud;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

  @Column(name = "executant")
  private String executant;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
