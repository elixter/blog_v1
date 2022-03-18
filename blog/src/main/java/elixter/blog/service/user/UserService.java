package elixter.blog.service.user;

import elixter.blog.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Long createUser(User user);

    void deleteUser(Long id);

    List<User> findUser();
    Optional<User> findUserById(Long id);
    List<User> findUserByLoginId(String loginId);
    List<User> findUserByName(String name);
}
