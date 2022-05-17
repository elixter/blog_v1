package elixter.blog.repository.userRepository;

import elixter.blog.domain.user.User;
import elixter.blog.repository.user.JdbcTemplateUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class JdbcTemplateUserRepositoryTest {
    @Autowired
    JdbcTemplateUserRepository repository;

    @Test
    public void findById() {
        User user = new User();
        System.out.println(user.getCreateAt());

        user.setLoginId("test");
        user.setLoginPw("123");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setProfileImage("test");

        User savedUser = repository.save(user);
        User result = repository.findById(savedUser.getId()).get();
        Assertions.assertThat(savedUser).isEqualTo(result);
    }

    @Test
    public void findByLoginId() {
        User user = new User();
        user.setLoginId("test");
        user.setLoginPw("123");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setProfileImage("test");

        User savedUser = repository.save(user);
        User result = repository.findByLoginId(user.getLoginId()).get();
        Assertions.assertThat(savedUser).isEqualTo(result);
    }

    @Test
    public void findByName() {
        User user = new User();
        user.setLoginId("test");
        user.setLoginPw("123");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setProfileImage("test");

        User savedUser = repository.save(user);
        List<User> result = repository.findByName(user.getName());
        Assertions.assertThat(result).contains(savedUser);
    }

    @Test
    public void update() {
        User user = new User();
        user.setLoginId("test");
        user.setLoginPw("123");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setProfileImage("test");

        User savedUser = repository.save(user);
        User updateUser = new User(user);
        updateUser.setProfileImage("update");

        repository.update(updateUser);
        User result = repository.findById(updateUser.getId()).get();

        Assertions.assertThat(updateUser).isEqualTo(result);
    };

    @Test
    public void findAll() {
        User user1 = new User();
        user1.setLoginId("test");
        user1.setLoginPw("123");
        user1.setName("test");
        user1.setEmail("test@test.com");
        user1.setProfileImage("test");
        repository.save(user1);

        User user2 = new User();
        user2.setLoginId("test2");
        user2.setLoginPw("123");
        user2.setName("test");
        user2.setEmail("test123123@test.com");
        user2.setProfileImage("test");
        repository.save(user2);

        List<User> userList = repository.findAll(0L, 50L);

        Assertions.assertThat(userList).contains(user1, user2);
    };

    @Test
    void delete() {
        User user = new User();
        user.setLoginId("test");
        user.setLoginPw("123");
        user.setEmail("test@test.com");
        user.setName("test");
        user.setProfileImage("test");
        repository.save(user);

        repository.delete(user.getId());

        Assertions.assertThat(repository.findById(user.getId())).isEmpty();
    };
}
