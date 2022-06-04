package elixter.blog.service.user;

import elixter.blog.domain.user.User;
import elixter.blog.dto.user.UpdateUserRequestDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(UpdateUserRequestDto user);

    void deleteUser(Long id);

    List<User> findUser(String filterType, String filterKey);
}
