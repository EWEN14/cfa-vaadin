package nc.unc.application.data.repository;

import nc.unc.application.data.entity.EntretienCollectif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntretienCollectifRepository extends JpaRepository<EntretienCollectif, Long> {

  @Query("select e from EntretienCollectif e " +
          "where lower(e.formation.libelleFormation) like lower(concat('%', :searchTerm, '%')) " +
          "order by e.date desc")
  List<EntretienCollectif> search(@Param("searchTerm") String searchTerm);

  List<EntretienCollectif> findAllByOrderByDateDesc();
}