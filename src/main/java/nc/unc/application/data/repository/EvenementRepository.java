package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

  List<Evenement> findAllByFormationsIdOrderByDateDebutAsc(Long id);

  List<Evenement> findAllByOrderByDateDebutDesc();

  @Query("select e from Evenement e " +
          "where lower(e.description) like lower(concat('%', :searchTerm, '%')) " +
          "order by e.dateDebut desc")
  List<Evenement> search(@Param("searchTerm") String searchTerm);


}