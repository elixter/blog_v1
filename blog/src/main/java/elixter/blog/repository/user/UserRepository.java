package elixter.blog.repository.user;

import elixter.blog.domain.user.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user) throws DataIntegrityViolationException;
    User update(User user);

    Optional<User> findById(Long id);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByEmail(String email);
    List<User> findByName(String name);

    List<User> findAll(Long offset, Long limit);

    void delete(Long id);
}
