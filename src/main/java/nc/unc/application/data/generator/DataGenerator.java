package nc.unc.application.data.generator;

import com.vaadin.flow.spring.annotation.SpringComponent;
import java.util.Collections;
import java.util.Set;
import nc.unc.application.data.enums.Role;
import nc.unc.application.data.entity.User;
import nc.unc.application.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 2 User entities...");
            User user = new User();
            user.setNom("RAMBO");
            user.setPrenom("John");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);

            User admin = new User();
            admin.setNom("CLÉMENT");
            admin.setPrenom("Ewen");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(Role.USER, Role.ADMIN));
            userRepository.save(admin);

            logger.info("Generated demo data");
        };
    }

}
