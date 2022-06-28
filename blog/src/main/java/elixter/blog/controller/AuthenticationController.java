package elixter.blog.controller;

import elixter.blog.constants.SessionConstants;
import elixter.blog.domain.user.User;
import elixter.blog.domain.user.SessionUser;
import elixter.blog.dto.auth.PostLoginRequestDto;
import elixter.blog.dto.user.GetUserResponseDto;
import elixter.blog.service.auth.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthenticationController {
    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public ResponseEntity<GetUserResponseDto> PostSigninHandler(
            @RequestBody PostLoginRequestDto requestBody,
            HttpServletRequest request
    ) {
        User loginUser = service.login(requestBody);

        SessionUser signinUser = new SessionUser(loginUser.getId(), loginUser.getLoginId());

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.AUTHENTICATION, signinUser);
        session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);

        log.debug("UserInfo : {} try to signin", signinUser);

        return ResponseEntity.ok(new GetUserResponseDto(loginUser));
    }

    @GetMapping("/signout")
    public ResponseEntity<Object> GetSignoutHandler(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        SessionUser signoutUser = service.logout(session);

        if (signoutUser != null) {
            log.info("user id : {}, login id : {} signed out", signoutUser.getUserId(), signoutUser.getUserLoginId());
            log.debug("{}", signoutUser);
        }

        return ResponseEntity.noContent().build();
    }
}
