package nc.unc.application.data.repository;

import nc.unc.application.data.entity.ReferentCfa;
import nc.unc.application.data.entity.ReferentPedagogique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferentCfaRepository extends JpaRepository<ReferentCfa, Long> {

  //Récupérer le(s) référent cfa selon le paramètre searchTerm
  @Query("select r from  ReferentCfa r "+
          "where lower(r.nomReferentCfa) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(r.prenomReferentCfa) like lower(concat('%', :searchTerm, '%'))")
  List<ReferentCfa> search(@Param("searchTerm") String searchTerm);
}