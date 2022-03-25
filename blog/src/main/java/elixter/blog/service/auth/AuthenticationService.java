package elixter.blog.service.auth;

import elixter.blog.domain.user.User;
import elixter.blog.dto.auth.PostLoginRequestDto;

public interface AuthenticationService {
    User login(PostLoginRequestDto loginInfo);
}
