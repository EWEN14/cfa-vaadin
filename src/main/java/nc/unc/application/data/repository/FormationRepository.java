package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {

  // Récupérer le(s) formation selon le paramètre searchTerm
  @Query("select f from Formation f " +
          "where lower(f.libelleFormation) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(f.codeFormation) like lower(concat('%', :searchTerm, '%'))")
  List<Formation> search(@Param("searchTerm") String searchTerm);

  // récupère toutes les formations, sauf celles avec les id passés en paramètre.
  List<Formation> findAllByIdNotIn(List<Long> id);

  List<Formation> findAllByOrderByLibelleFormationAsc();
}
