package elixter.blog.repository.user;

import elixter.blog.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    void update(User user);

    Optional<User> findById(Long id);
    List<User> findAll(Long offset, Long limit);

    void delete(Long id);
}
