package elixter.blog.service.auth;

import elixter.blog.constants.SessionConstants;
import elixter.blog.domain.user.SessionUser;
import elixter.blog.domain.user.User;
import elixter.blog.dto.auth.PostLoginRequestDto;
import elixter.blog.exception.RestException;
import elixter.blog.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User login(PostLoginRequestDto loginInfo) {
        User loginUser;

        try {
            loginUser = repository.findByLoginId(loginInfo.getId()).get();
            log.debug("User {} found.", loginUser.getLoginId());
        } catch (NoSuchElementException e) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Invalid user id or pw");
        }

        if (!BCrypt.checkpw(loginInfo.getPw(), loginUser.getLoginPw())) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Invalid user id or pw");
        }

        return loginUser;
    }

    @Override
    public SessionUser logout(HttpSession session) {
        SessionUser signoutUser = null;
        if (session != null) {
            signoutUser = (SessionUser) session.getAttribute(SessionConstants.AUTHENTICATION);
            session.setMaxInactiveInterval(SessionConstants.SESSION_REMOVE);
            session.removeAttribute(SessionConstants.AUTHENTICATION);
            session.invalidate();
        }

        return signoutUser;
    }

    @Override
    public User getSessionUser(HttpSession session) {
        SessionUser sessionUser = null;
        if (session == null) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        sessionUser = (SessionUser) session.getAttribute(SessionConstants.AUTHENTICATION);
        Long id = sessionUser.getUserId();

        User result;
        try {
            result = repository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new RestException(HttpStatus.NOT_FOUND, "No such user : " + sessionUser.getUserLoginId());
        }

        return result;
    }
}
