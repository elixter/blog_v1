package elixter.blog.service;

import elixter.blog.domain.user.User;
import elixter.blog.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    UserService service;

    @Test
    void createUser() {
        User user = new User("test", "test", "test", "test");
        user.setProfileImage("default");

        service.createUser(user);

        List<User> result = service.findUser("userName", user.getName());

        Assertions.assertThat(result).contains(user);
    }

    @Test
    void deleteUser() {
        User user = new User("test", "test", "test", "test");
        user.setProfileImage("default");

        service.createUser(user);

        service.deleteUser(user.getId());

        List<User> result = service.findUser("id", user.getId().toString());
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void updateUser() {
        User user = new User("test", "test", "test", "test");
        user.setProfileImage("default");

        service.createUser(user);

        user.setName("updated");
        Long id = service.updateUser(user);

        List<User> result = service.findUser("id", user.getId().toString());
        Assertions.assertThat(result).contains(user);
    }
}
