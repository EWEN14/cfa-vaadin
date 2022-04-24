package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratRepository extends JpaRepository<Contrat, Long> {
}