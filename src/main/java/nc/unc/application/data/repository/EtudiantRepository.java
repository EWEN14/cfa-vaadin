package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

  @Query("select e from Etudiant e " +
          "where lower(e.nomEtudiant) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(e.prenomEtudiant) like lower(concat('%', :searchTerm, '%'))")
  List<Etudiant> search(@Param("searchTerm") String searchTerm);

  List<Etudiant> findAllByTuteurId(Long id);

  List<Etudiant> findAllByFormationId(Long id);

  List<Etudiant> findAllByEntrepriseId(Long id);
}
