package nc.unc.application.data.repository;

import nc.unc.application.data.entity.EntretienIndividuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntretienIndividuelRepository extends JpaRepository<EntretienIndividuel, Long> {

  @Query("select e from EntretienIndividuel e " +
          "where lower(e.etudiant.nomEtudiant) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(e.etudiant.prenomEtudiant) like lower(concat('%', :searchTerm, '%')) " +
          "order by e.date desc")
  List<EntretienIndividuel> search(@Param("searchTerm") String searchTerm);

  List<EntretienIndividuel> findAllByOrderByDateDesc();
}