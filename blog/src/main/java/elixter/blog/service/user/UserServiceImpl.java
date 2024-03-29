package elixter.blog.service.user;

import elixter.blog.domain.RecordStatus;
import elixter.blog.constants.SessionConstants;
import elixter.blog.domain.user.SessionUser;
import elixter.blog.domain.user.User;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.dto.user.UpdateUserRequestDto;
import elixter.blog.exception.user.UserAlreadyExistException;
import elixter.blog.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(@Qualifier("jpaUserRepository") UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(User user) {
        String hashedPw = BCrypt.hashpw(user.getLoginPw(), BCrypt.gensalt());
        user.setLoginPw(hashedPw);
        user.setProfileImage(User.defaultProfileImage);

        try {
            user = repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistException();
        }

        return user;
    }

    @Override
    public GetUserResponseDto findSessionUser(HttpSession session) {
        SessionUser sessionInfo = (SessionUser) session.getAttribute(SessionConstants.AUTHENTICATION);

        User user = repository.findByIdAndStatus(sessionInfo.getUserId(), RecordStatus.exist).orElse(User.getEmpty());
        if (user.isEmpty()) {
            return GetUserResponseDto.getEmpty();
        }

        return GetUserResponseDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .build();
    }

    @Override
    public void deleteUser(Long id) {
        repository.delete(id);
    }

    @Override
    public List<User> findUser(UserSearchType filterType, String filterKey) {
        List<User> result = new ArrayList<>();

        switch (filterType) {
            case USER_SEARCH_TYPE_ID:
                result.add(repository.findByIdAndStatus(Long.parseLong(filterKey), RecordStatus.exist).orElse(User.getEmpty()));
                if (result.get(0).isEmpty()) {
                    log.debug("No such user id : {}", filterKey);
                }
                break;
            case USER_SEARCH_TYPE_LOGIN_ID:
                result.add(repository.findByLoginIdAndStatus(filterKey, RecordStatus.exist).orElse(User.getEmpty()));
                if (result.get(0).isEmpty()) {
                    log.debug("No such user login id : {}", filterKey);
                }
                break;
            case USER_SEARCH_TYPE_USER_NAME:
                result = repository.findByNameAndStatus(filterKey, RecordStatus.exist);
                break;
            case USER_SEARCH_TYPE_EMAIL:
                result.add(repository.findByEmailAndStatus(filterKey, RecordStatus.exist).orElse(User.getEmpty()));
                if (result.get(0).isEmpty()) {
                    log.debug("No such user email: {}", filterKey);
                }
                break;
            default:
                break;
        }

        return result;
    }

    @Override
    @Transactional
    public User updateUser(UpdateUserRequestDto user) {
        User updateUser = repository.findByIdAndStatus(user.getId(), RecordStatus.exist).orElse(User.getEmpty());
        if (updateUser.isEmpty()) {
            return updateUser;
        }

        updateUser(user, updateUser);
        repository.update(updateUser);

        return updateUser;
    }

    private void updateUser(UpdateUserRequestDto updateDto, User updateUser) {
        String hashedPw = BCrypt.hashpw(updateDto.getLoginPw(), BCrypt.gensalt());
        updateUser.setLoginPw(hashedPw);
        updateUser.setEmail(updateDto.getEmail());
        updateUser.setName(updateDto.getName());
        updateUser.setProfileImage(updateDto.getProfileImage());
    }
}
