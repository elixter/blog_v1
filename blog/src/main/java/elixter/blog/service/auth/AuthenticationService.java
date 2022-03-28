package elixter.blog.service.auth;

import elixter.blog.domain.user.User;
import elixter.blog.dto.auth.PostLoginRequestDto;

import javax.servlet.http.HttpSession;

public interface AuthenticationService {
    User login(PostLoginRequestDto loginInfo);
    void logout(HttpSession session);
    User getSessionUser(HttpSession session);
}
