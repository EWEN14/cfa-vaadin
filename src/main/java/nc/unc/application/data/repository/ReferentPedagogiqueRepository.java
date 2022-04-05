package nc.unc.application.data.repository;

import nc.unc.application.data.entity.ReferentPedagogique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferentPedagogiqueRepository extends JpaRepository<ReferentPedagogique, Long> {

  //Récupérer le(s) référent pédagogique selon le paramètre searchTerm
  @Query("select r from  ReferentPedagogique r "+
          "where lower(r.nom) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(r.prenom) like lower(concat('%', :searchTerm, '%'))")
  List<ReferentPedagogique> search(@Param("searchTerm") String searchTerm);
}