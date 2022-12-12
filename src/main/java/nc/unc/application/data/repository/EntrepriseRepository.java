package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

  @Query("select e from Entreprise e " +
          "where lower(e.enseigne) like lower(concat('%', :searchTerm, '%'))" +
          "order by e.enseigne asc")
  List<Entreprise> search(@Param("searchTerm") String searchTerm);

  List<Entreprise> findAllByOrderByEnseigneAsc();

  @Query("select count(e) from Entreprise e " +
          "where e.statutActifEntreprise = :statut")
  Integer CountBystatutActifEntreprise(@Param("statut") String statut);

  Entreprise findByNumeroRidet(String numeroRidet);

  @Modifying(clearAutomatically = true)
  @Query("update Entreprise e set e.statutActif = :enumStringiy, c.updatedAt = :now where c.id = :id")
  void updateStatusOfCompany(Long id, String enumStringify, LocalDateTime now);
}
