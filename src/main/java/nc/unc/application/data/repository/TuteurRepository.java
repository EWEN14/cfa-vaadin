package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Tuteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TuteurRepository extends JpaRepository<Tuteur, UUID> {
    
    //Récupérer le(s) tuteurs selon le paramètre searchTerm
    @Query("select t from Tuteur t " +
            "where lower(t.nomTuteur) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(t.prenomTuteur) like lower(concat('%', :searchTerm, '%'))" +
            "order by t.nomTuteur asc")
    List<Tuteur> search(@Param("searchTerm") String searchTerm);

    List<Tuteur> findAllByOrderByNomTuteur();

    List<Tuteur> findAllByEntrepriseIdOrderByNomTuteur(Long id);

    List<Tuteur> findAllByTuteurHabilitationsIsNullOrderByNomTuteur();

    @Modifying(clearAutomatically = true)
    @Query("update Tuteur t set t.status = :actif where t.id_tuteur = :id")
    void updateStatusOfTuteur(Long id, String actif);

    @Query("select t from Tuteur t inner join contrat c on c.id_tuteur = t.id_tuteur inner join etudiant e on e.id_etudiant = c.id_etudiant where t.id_tuteur = :id")
    List<Tuteur> findAllByEtudiantId(Long id);
}
