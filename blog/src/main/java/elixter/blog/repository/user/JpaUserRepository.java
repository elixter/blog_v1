package elixter.blog.repository.user;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.user.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {

    @Override
    User save(User user);

    @Override
    @Modifying
    @Query("update User u set u.loginPw = :#{#user.loginPw}")
    User update(User user);

    @Override
    Optional<User> findById(Long id);

    @Override
    Optional<User> findByIdAndStatus(Long id, RecordStatus status);

    @Override
    Optional<User> findByLoginId(String loginId);

    @Override
    Optional<User> findByLoginIdAndStatus(String loginId, RecordStatus status);

    @Override
    Optional<User> findByEmail(String email);

    @Override
    Optional<User> findByEmailAndStatus(String email, RecordStatus status);

    @Override
    List<User> findByName(String name);

    @Override
    List<User> findByNameAndStatus(String name, RecordStatus status);

    @Override
    Page<User> findByName(String name, Pageable pageable);

    @Override
    Page<User> findByNameAndStatus(String name, RecordStatus status, Pageable pageable);

    @Override
    List<User> findAll(Long offset, Long limit);

    @Override
    Page<User> findAll(Pageable page);

    @Override
    @Modifying
    @Query("update User u set u.status = 0 where u.id = :id")
    void delete(Long id);
}
