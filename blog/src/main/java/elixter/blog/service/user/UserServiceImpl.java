package elixter.blog.service.user;

import elixter.blog.constants.RecordStatus;
import elixter.blog.constants.RecordErrorConstants;
import elixter.blog.domain.user.User;
import elixter.blog.exception.RestException;
import elixter.blog.exception.user.UserAlreadyExistException;
import elixter.blog.exception.user.UserNotFoundException;
import elixter.blog.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
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
    public void deleteUser(Long id) {
        repository.delete(id);
    }

    @Override
    public List<User> findUser(String filterType, String filterKey) {
        List<User> result = new ArrayList<>();

        switch (filterType) {
            case "id":
                result.add(repository.findById(Long.parseLong(filterKey)).orElse(User.getEmpty()));
                if (result.get(0).isEmpty()) {
                    log.debug("No such user id : {}", filterKey);
                }
                break;
            case "loginId":
                result.add(repository.findByLoginId(filterKey).orElse(User.getEmpty()));
                if (result.get(0).isEmpty()) {
                    log.debug("No such user login id : {}", filterKey);
                }
                break;
            case "userName":
                result = repository.findByName(filterKey);
                break;
            default:
                break;
        }

        return result;
    }

    @Override
    public User updateUser(User user) {
        String hashedPw = BCrypt.hashpw(user.getLoginPw(), BCrypt.gensalt());
        user.setLoginPw(hashedPw);

        return repository.update(user);
    }
}
