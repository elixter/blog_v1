package elixter.blog.service.user;

import elixter.blog.Constants;
import elixter.blog.domain.user.User;
import elixter.blog.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        String hashedPw = BCrypt.hashpw(user.getLoginPw(), BCrypt.gensalt());
        user.setLoginPw(hashedPw);
        user.setProfileImage(Constants.defaultProfileImage);
        repository.save(user);

        return user.getId();
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
                    result.add(repository.findById(Long.getLong(filterKey)).get());
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
}
