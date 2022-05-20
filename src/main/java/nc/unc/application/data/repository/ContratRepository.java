package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {

  @Query("select c from Contrat c " +
          "where lower(c.codeContrat) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(c.etudiant.prenomEtudiant) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(c.etudiant.nomEtudiant) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(c.tuteur.prenomTuteur) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(c.tuteur.nomTuteur) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(c.entreprise.enseigne) like lower(concat('%', :searchTerm, '%')) " +
          "or lower(c.formation.codeFormation) like lower(concat('%', :searchTerm, '%')) ")
  List<Contrat> search(@Param("searchTerm") String searchTerm);

  List<Contrat> findAllByTuteurId(Long id);

  List<Contrat> findAllByEtudiantId(Long id);

  List<Contrat> findAllByEntrepriseId(Long id);
}
