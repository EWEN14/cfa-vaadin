package nc.unc.application.data.repository;

import nc.unc.application.data.entity.TuteurHabilitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuteurHabilitationRepository extends JpaRepository<TuteurHabilitation, Long> {
}
