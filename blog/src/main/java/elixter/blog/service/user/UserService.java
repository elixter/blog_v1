package elixter.blog.service.user;

import elixter.blog.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Long createUser(User user);
    Long updateUser(User user);

    void deleteUser(Long id);

    List<User> findUser(String filterType, String filterKey);
}
