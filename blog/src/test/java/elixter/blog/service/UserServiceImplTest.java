package elixter.blog.service;

import elixter.blog.constants.SessionConstants;
import elixter.blog.domain.user.SessionUser;
import elixter.blog.domain.user.User;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.dto.user.UpdateUserRequestDto;
import elixter.blog.service.user.UserSearchType;
import elixter.blog.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
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
        user.setEmailVerified(false);

        service.createUser(user);

        List<User> result = service.findUser(UserSearchType.USER_SEARCH_TYPE_USER_NAME, user.getName());

        Assertions.assertThat(result).contains(user);
    }

    @Test
    void deleteUser() {
        User user = new User("test", "test", "test", "test");
        user.setProfileImage("default");
        user.setEmailVerified(false);

        service.createUser(user);

        service.deleteUser(user.getId());

        List<User> result = service.findUser(UserSearchType.USER_SEARCH_TYPE_ID, user.getId().toString());
        Assertions.assertThat(result.get(0)).isEqualTo(User.getEmpty());
    }

    @Test
    void updateUser() {
        User user = new User("test", "test", "test", "test");
        user.setProfileImage("default");
        user.setEmailVerified(false);

        service.createUser(user);

        UpdateUserRequestDto dto = UpdateUserRequestDto.builder()
                .id(user.getId())
                .loginPw(user.getLoginPw())
                .email(user.getEmail())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .build();
        user.setName("updated");
        user = service.updateUser(dto);

        List<User> result = service.findUser(UserSearchType.USER_SEARCH_TYPE_ID, user.getId().toString());
        Assertions.assertThat(result).contains(user);
    }

    @Test
    @Transactional
    void findSessionUser() {
        User user = new User("test", "test", "test", "test");
        user.setProfileImage("default");
        user.setEmailVerified(false);

        service.createUser(user);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConstants.AUTHENTICATION, new SessionUser(user.getId(), user.getLoginId()));

        GetUserResponseDto sessionUser = service.findSessionUser(session);
        Assertions.assertThat(sessionUser).isEqualTo(
                GetUserResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .loginId(user.getLoginId())
                .profileImage(user.getProfileImage())
                .build()
        );
    }
}
