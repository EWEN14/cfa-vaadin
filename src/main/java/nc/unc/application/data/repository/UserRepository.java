package nc.unc.application.data.repository;

import java.util.List;
import java.util.UUID;

import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    @Query("select e from User e " +
            "where lower(e.nomUser) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(e.prenomUtilisateur) like lower(concat('%', :searchTerm, '%'))")
    List<User> search(@Param("searchTerm") String searchTerm);
}
