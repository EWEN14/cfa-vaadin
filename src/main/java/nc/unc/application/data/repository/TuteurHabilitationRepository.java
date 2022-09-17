package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.entity.TuteurHabilitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuteurHabilitationRepository extends JpaRepository<TuteurHabilitation, Long> {

  // Récupérer les tuteurs sans habilitations
  @Query("select t.tuteur from TuteurHabilitation t " +
          "where t.statutFormation = 'NON FORMÉ' " +
          "and t.statutFormation = 'FORMÉ'")
  public List<Tuteur> findTuteursSansHabilitations();

}
