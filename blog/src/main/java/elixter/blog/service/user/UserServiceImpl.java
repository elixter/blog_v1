package elixter.blog.service.user;

import elixter.blog.Constants;
import elixter.blog.domain.user.User;
import elixter.blog.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public Long createUser(User user) {
        Long result;
        String hashedPw = BCrypt.hashpw(user.getLoginPw(), BCrypt.gensalt());
        user.setLoginPw(hashedPw);
        user.setProfileImage(Constants.defaultProfileImage);

        try {
            repository.save(user);
            result = user.getId();
        } catch(DataIntegrityViolationException e) {
            String errCause = e.getCause().toString();
            if (errCause.contains("login_id")) {
                result = Constants.userLoginIdAlreadyExist;
            }
            else if (errCause.contains("email")) {
                result = Constants.userEmailAlreadyExist;
            } else {
                result = Constants.unknownError;
            }
        }

        return result;
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
                try {
                    result.add(repository.findById(Long.parseLong(filterKey)).get());
                } catch (NoSuchElementException e) {
                    log.debug("No such user id : {}", filterKey);
                }
                break;
            case "loginId":
                try {
                    result.add(repository.findByLoginId(filterKey).get());
                } catch (NoSuchElementException e) {
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
    public Long updateUser(User user) {
        Long result;

        String hashedPw = BCrypt.hashpw(user.getLoginPw(), BCrypt.gensalt());
        user.setLoginPw(hashedPw);

        User updateResult = repository.update(user);
        if (updateResult == null) {
            result = Constants.recordNotExist;
        } else {
            result = updateResult.getId();
        }

        return result;
    }
}
