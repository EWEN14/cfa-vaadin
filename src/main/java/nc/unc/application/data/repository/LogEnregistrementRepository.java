package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.LogEnregistrement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogEnregistrementRepository extends JpaRepository<LogEnregistrement, Long> {

  @Query("select l from LogEnregistrement l " +
          "where lower(l.description_log) like lower(concat('%', :searchTerm, '%')) ")
  List<LogEnregistrement> search(@Param("searchTerm") String searchTerm);
}
