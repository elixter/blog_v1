package elixter.blog.repository.user;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.user.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    void update(User user);

    Optional<User> findById(Long id);
    Optional<User> findByIdAndStatus(Long id, RecordStatus status);

    Optional<User> findByLoginId(String loginId);
    Optional<User> findByLoginIdAndStatus(String loginId, RecordStatus status);

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndStatus(String email, RecordStatus status);

    List<User> findByName(String name);
    List<User> findByNameAndStatus(String name, RecordStatus status);

    Page<User> findByName(String name, Pageable pageable);
    Page<User> findByNameAndStatus(String name, RecordStatus status, Pageable pageable);

    List<User> findAll(Long offset, Long limit);

    void delete(Long id);
}
