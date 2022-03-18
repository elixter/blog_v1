package elixter.blog.service.user;

import elixter.blog.domain.user.User;
import elixter.blog.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public Long createUser(User user) {
        repository.save(user);
        return user.getId();
    }

    @Override
    public void deleteUser(Long id) {
        repository.delete(id);
    }

    @Override
    public List<User> findUser() {
        return null;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return  repository.findById(id);
    }

    @Override
    public Optional<User> findUserByLoginId(String loginId) {
        return Optional.empty();
    }

    @Override
    public List<User> findUserByName(String name) {
        return null;
    }
}
