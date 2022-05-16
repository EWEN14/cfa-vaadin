package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

  @Query("select e from Entreprise e " +
          "where lower(e.enseigne) like lower(concat('%', :searchTerm, '%'))")
  List<Entreprise> search(@Param("searchTerm") String searchTerm);
}
