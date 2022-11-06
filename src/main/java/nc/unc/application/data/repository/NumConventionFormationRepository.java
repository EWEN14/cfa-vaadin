package nc.unc.application.data.repository;

import nc.unc.application.data.entity.NumConventionFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NumConventionFormationRepository extends JpaRepository<NumConventionFormation, Long> {

  Optional<NumConventionFormation> findAllById(Long id);
}
