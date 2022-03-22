package nc.unc.application.data.repository;

import java.util.UUID;
import nc.unc.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}
