package nc.unc.application.data.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nc.unc.application.data.entity.User;
import nc.unc.application.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> get(UUID id) {
        return repository.findById(id);
    }

    public User save(User user){
        return repository.save(user);
    }

    public List<User> findAllUsers(String filterText){
        if(filterText != null){
            return repository.search(filterText);
        }
        return repository.findAll();
    }
    public User update(User entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
