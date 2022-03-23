package nc.unc.application.data.repository;

import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TuteurRepository extends JpaRepository<Tuteur, UUID> {

    //Récupérer le(s) tuteurs selon le paramètre searchTerm
    @Query("select t from Tuteur t " +
            "where lower(t.nom) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(t.prenom) like lower(concat('%', :searchTerm, '%'))")
    List<Tuteur> search(@Param("searchTerm") String searchTerm);
}
